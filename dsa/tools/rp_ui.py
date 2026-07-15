# tools/rp_ui.py
import os
import re
import json
from datetime import datetime, timezone
from pathlib import Path

import streamlit as st
import pandas as pd

from rp_utils import load_revisions, save_revisions, REV_LOG, REV_LOG_DIR, iso_to_dt, dt_to_iso
from rp_scan import scan_repo
from rp_utils import run_cmd, REV_LOG, mark_revision_for_file,undo_revision_for_file

def logical_top_pattern(pattern_full: str) -> str:
    """
    Convert a full pattern path like:
      'src/main/java/recursion/IBH'
    into a logical top pattern like:
      'recursion'

    Also hides src/src/main/src/main/java prefixes.
    """
    if not pattern_full:
        return pattern_full
    p = pattern_full.replace("\\", "/")
    for pref in ("src/main/java/", "src/main/", "src/"):
        if p.startswith(pref):
            p = p[len(pref):]
            break
    return p.split("/", 1)[0] if p else p

def run_streamlit_ui(initial_summary=None, root=".", default_exclude=None, default_only=None):
    st.set_page_config(page_title="DSA Repo Progress", layout="wide")
    st.title("DSA Repo Progress — Revisions dashboard")

    # Sidebar
    st.sidebar.header("Controls")
    view_mode = st.sidebar.radio("View", ["Report", "Patterns"], index=0)
    rev_threshold = st.sidebar.slider("Days since last revision to alert", min_value=1, max_value=365, value=30)
    show_unresolved_only = st.sidebar.checkbox("Show only unsolved", value=False)
    auto_commit_revisions = st.sidebar.checkbox("Auto-commit revision file (.dsa_progress/revisions.json)", value=False)

    exclude_input = st.sidebar.text_input("Extra folders to exclude (space-separated)", value=(default_exclude or ".venv venv tools target .idea"))
    only_input = st.sidebar.text_input("Only scan paths (comma-separated)", value=(default_only or "src/main/java"))

    exclude_list = [x.strip() for x in re.split(r'\s+', exclude_input) if x.strip()]
    only_list = [x.strip() for x in only_input.split(",") if x.strip()]

    if st.sidebar.button("Refresh (scan repo)"):
        summary = scan_repo(root, exclude_dirs=set(exclude_list), only_paths=only_list)
        try:
            with open("summary.json", "w", encoding="utf-8") as f:
                json.dump(summary, f, indent=2, ensure_ascii=False)
        except Exception:
            pass
        st.rerun()
    else:
        summary = initial_summary or scan_repo(root, exclude_dirs=set(exclude_list), only_paths=only_list)

    st.write("Repo root:", summary.get("repo_root"))
    st.write("Generated:", summary.get("generated_at"))

    # Flatten to DataFrame
    rows = []
    for pattern, pdata in summary["patterns"].items():
        for pr in pdata["problems"]:
            rows.append({
                "pattern_full": pattern,
                "file": pr["file"],
                "title": pr.get("title"),
                "difficulty": pr.get("difficulty"),
                "tags": ", ".join(pr.get("tags") or []),
                # schedule fields
                "commit_date": pr.get("commit_date"),
                "r1": pr.get("r1"),
                "r2": pr.get("r2"),
                "r3": pr.get("r3"),
                "r4": pr.get("r4"),
                # revisions is a semicolon-joined string for table display; original list lives in summary JSON
                "revisions": ";".join(pr.get("revisions") or []),
                "next_due": pr.get("next_due"),
                "next_step": pr.get("next_step"),
                "days_until_next": pr.get("days_until_next"),
                "solved": bool(pr.get("solved")) if pr.get("solved") is not None else False,
            })
    df = pd.DataFrame(rows)
    if not df.empty:
        df["top_pattern"] = df["pattern_full"].fillna("").apply(logical_top_pattern)

    # Build list of patterns including those with only notes and zero problems
    pattern_list = list(summary["patterns"].keys())
    if pattern_list:
        common_pref = os.path.commonprefix(pattern_list)
        if "/" in common_pref:
            cut = common_pref.rfind("/") + 1
            if cut > 0:
                pattern_trim_map = {p: (p[cut:] if p.startswith(common_pref) else p) for p in pattern_list}
            else:
                pattern_trim_map = {p: p for p in pattern_list}
        else:
            pattern_trim_map = {p: p for p in pattern_list}
    else:
        pattern_trim_map = {}

    if not df.empty:
        df["pattern"] = df["pattern_full"].apply(lambda x: pattern_trim_map.get(x, x))

    # --- Summary block ---
    if view_mode == "Report":
        st.subheader("Summary")
        total_problems = len(df)
        total_solved = int(df['solved'].fillna(False).astype(bool).sum())
        col1, col2, col3 = st.columns([1,1,2])
        col1.metric("Total problems", total_problems)
        col2.metric("Solved", total_solved)
        col3.metric("Unsolved", total_problems - total_solved)

        # Pattern summary:
        # 1) show each concrete pattern (e.g. recursion/IBH, recursion/recursiveTree, bigo)
        # 2) add an extra aggregated row per top_pattern (e.g. recursion) that sums all its subpatterns

        pat_df = pd.DataFrame()

        if not df.empty:
            # 1) base rows: one per concrete pattern_full (trimmed name in "pattern")
            base = (
                df.groupby("pattern_full")
                .agg(
                    total_problems=("file", "count"),
                    solved_count=("solved", lambda s: int(s.fillna(False).astype(bool).sum())),
                    top_pattern=("top_pattern", "first"),
                )
                .reset_index()
            )

            # apply trimmed display name (what you already use elsewhere)
            if pattern_trim_map:
                base["pattern"] = base["pattern_full"].apply(lambda x: pattern_trim_map.get(x, x))
            else:
                base["pattern"] = base["pattern_full"]

            # 2) aggregated rows per top_pattern (e.g. recursion)
            agg_rows = []
            top_groups = base.groupby("top_pattern")
            for tp, grp in top_groups:
                # total across all subpatterns of this top pattern
                total = int(grp["total_problems"].sum())
                solved = int(grp["solved_count"].sum())

                # Only create an aggregated row if:
                # - there is more than one concrete pattern under this top_pattern
                #   OR the pattern names are not exactly equal to the top_pattern.
                # This avoids creating a duplicate row for "bigo" if there's only
                # a single 'bigo' folder.
                unique_patterns = grp["pattern"].unique().tolist()
                should_add_agg = len(unique_patterns) > 1 or tp not in unique_patterns

                if should_add_agg:
                    agg_rows.append({
                        "pattern_full": f"__agg__/{tp}",  # synthetic key, not a real folder
                        "pattern": tp,
                        "top_pattern": tp,
                        "total_problems": total,
                        "solved_count": solved,
                    })

            # Combine base + aggregated rows
            pat_df = pd.concat(
                [base[["pattern_full", "pattern", "top_pattern", "total_problems", "solved_count"]],
                 pd.DataFrame(agg_rows)],
                ignore_index=True
            )

            # solved percentage
            pat_df["solved_pct"] = (
                                           pat_df["solved_count"] /
                                           pat_df["total_problems"].replace(0, 1) * 100
                                   ).round(1).astype(str) + "%"

            # sort: group by top_pattern, then by "depth", and put aggregated row last within each group
            def pattern_depth(pf: str) -> int:
                if pf.startswith("__agg__/"):
                    return 999  # ensure aggregated row comes last
                # approximate depth by counting '/' in the trimmed pattern name
                return pf.count("/")

            pat_df["depth"] = pat_df["pattern_full"].apply(pattern_depth)
            pat_df["is_agg"] = pat_df["pattern_full"].str.startswith("__agg__/").astype(int)

            pat_df = pat_df.sort_values(
                by=["top_pattern", "depth", "is_agg", "pattern"],
                ascending=[True, True, True, True]
            )

            st.markdown("**Problems per pattern (total / solved)**")
            display_pat_df = pat_df[["pattern", "total_problems", "solved_count", "solved_pct"]]
            st.dataframe(display_pat_df, height=300)
            print(pat_df)
            #st.markdown("** patterns by total problems**")

            #chart_data = pat_df.set_index("pattern")[["total_problems", "solved_count"]]
            #st.bar_chart(chart_data)

            import altair as alt

            st.markdown("**Top patterns by total problems**")

            chart_df = pat_df.copy()

            bar = alt.Chart(chart_df).mark_bar().encode(
                y=alt.Y("pattern:N", sort="-x", title="Pattern"),
                x=alt.X("total_problems:Q", title="Total Problems"),
                color=alt.value("#1f77b4")  # same blue as before
            )

            solved = alt.Chart(chart_df).mark_bar().encode(
                y=alt.Y("pattern:N", sort="-x"),
                x=alt.X("solved_count:Q"),
                color=alt.value("#9bd7ff")  # solved overlay color
            )

            st.altair_chart((bar + solved), use_container_width=True)


            # Difficulty summary
            # if not df.empty:
            #     df['difficulty_norm'] = df['difficulty'].fillna('unknown').astype(str).str.lower().str.strip()
            #     df['difficulty_norm'] = df['difficulty_norm'].replace({'': 'unknown', 'med': 'medium', 'e': 'easy', 'm': 'medium', 'h': 'hard'})
            #     diff_grp = df.groupby('difficulty_norm').agg(
            #         total=("file","count"),
            #         solved=("solved", lambda s: int(s.fillna(False).astype(bool).sum()))
            #     ).reset_index()
            #     if not diff_grp.empty:
            #         diff_grp['unsolved'] = diff_grp['total'] - diff_grp['solved']
            #         st.markdown("**Difficulty breakdown**")
            #         st.table(diff_grp[['difficulty_norm','total','solved','unsolved']].rename(columns={'difficulty_norm':'difficulty'}))
            #         diff_plot = diff_grp.set_index('difficulty_norm')[['solved','unsolved']]
            #         st.bar_chart(diff_plot)

            if not df.empty:
                df['difficulty_norm'] = df['difficulty'].fillna('unknown').astype(str).str.lower().str.strip()
                df['difficulty_norm'] = df['difficulty_norm'].replace({
                    '': 'unknown',
                    'med': 'medium',
                    'e': 'easy',
                    'm': 'medium',
                    'h': 'hard'
                })

                diff_grp = df.groupby('difficulty_norm').agg(
                    total=("file","count"),
                    solved=("solved", lambda s: int(s.fillna(False).astype(bool).sum()))
                ).reset_index()

                if not diff_grp.empty:
                    diff_grp['unsolved'] = diff_grp['total'] - diff_grp['solved']

                    st.markdown("**Difficulty breakdown**")
                    st.table(
                        diff_grp[['difficulty_norm','total','solved','unsolved']]
                        .rename(columns={'difficulty_norm':'difficulty'})
                    )

                    # ✅ PIE / DONUT CHART (REPLACES st.bar_chart)
                    pie = alt.Chart(diff_grp).mark_arc(innerRadius=45).encode(
                        theta=alt.Theta(field="total", type="quantitative"),
                        color=alt.Color(
                            field="difficulty_norm",
                            type="nominal",
                            legend=alt.Legend(title="Difficulty")
                        ),
                        tooltip=["difficulty_norm", "total", "solved", "unsolved"]
                    ).properties(
                        title="Difficulty Distribution"
                    )

                    st.altair_chart(pie, use_container_width=True)

            st.markdown("---")

            # Problems table (display trimmed pattern). Removed first_rev/second_rev/final_rev columns per request.
            st.subheader("Problems table")
            if df.empty:
                st.info("No source files found.")
            else:
                display_df = df.copy()
                display_df["file"] = display_df["file"].astype(str)
                display_df["pattern"] = display_df["pattern"].astype(str) if "pattern" in display_df.columns else display_df["pattern_full"]
                # removed columns: first_rev, second_rev, final_rev, revisions_count
                drop_cols = [c for c in ["pattern_full","title"] if c in display_df.columns]
                display_df = display_df.drop(columns=drop_cols)
                st.dataframe(display_df, height=600)

            # Quick Revision List (Due)
            st.subheader("Quick Revision List (Due)")
            if df.empty:
                st.info("No problems found.")
            else:
                # compute due items: next_due <= today (or days_until_next <= 0)
                due_df = df.copy()
                def due_flag(row):
                    try:
                        nd = row.get("next_due")
                        if not nd:
                            return False
                        ndt = iso_to_dt(nd)
                        if not ndt:
                            return False
                        now = datetime.now(timezone.utc)
                        return ndt <= now
                    except Exception:
                        return False
                due_df = due_df[due_df.apply(due_flag, axis=1)]
                if due_df.empty:
                    st.success("No problems are currently due for revision.")
                else:
                    # show table with key info and allow marking as completed
                    # We removed revisions_count column; compute count from 'revisions' field
                    def rev_count_str(s):
                        if not s:
                            return "0"
                        return str(len([x for x in s.split(";") if x.strip()]))
                    due_display = due_df.copy()
                    due_display["revisions_count"] = due_display["revisions"].apply(rev_count_str)
                    show_cols = ["pattern","file","difficulty","revisions_count","next_step","next_due"]
                    st.write(f"{len(due_display)} problems due for revision (per schedule):")
                    st.table(due_display[show_cols].head(200))

                    # interactive mark/completion UI per item
                    st.markdown("### Mark revisions as completed")
                    for _, row in due_display.head(200).iterrows():
                        cols = st.columns([3,1,1])
                        cols[0].markdown(f"**{row['pattern']} — `{row['file']}`**  \n**Next:** {row['next_step']} — {row['next_due']}")
                        if cols[1].button(f"Mark revision completed now ({row['file']})", key=f"mark_{row['file']}"):
                            mark_revision_for_file(row['file'], auto_commit_revisions)
                            st.rerun()
                        if cols[2].button(f"Undo last ({row['file']})", key=f"undo_{row['file']}"):
                            undo_revision_for_file(row['file'], auto_commit_revisions)
                            st.rerun()

    # Patterns view
    if view_mode == "Patterns":
        st.header("Patterns")
        st.subheader("Choose pattern")
        pattern_choices = ["(none)"] + [pattern_trim_map.get(p, p) for p in pattern_list]
        trimmed_to_full = {pattern_trim_map.get(p, p): p for p in pattern_list}
        sel = st.selectbox("Pattern", pattern_choices, index=0)
        if sel and sel != "(none)":
            full = trimmed_to_full.get(sel)
            if full:
                info = summary["patterns"].get(full, {})
                notes_md = info.get("notes")
                notes_path = info.get("notes_path")
                st.markdown(f"## {sel}")
                if notes_path:
                    try:
                        md_content = Path(summary["repo_root"]).joinpath(notes_path).read_text(encoding="utf-8", errors="ignore")
                        st.markdown(md_content)
                        st.markdown(f"_Notes file: `{notes_path}`_")
                    except Exception:
                        if notes_md:
                            st.markdown(notes_md)
                        else:
                            st.markdown("_No notes found._")
                else:
                    if notes_md:
                        st.markdown(notes_md)
                    else:
                        st.info("No notes.md or inline notes found for this pattern.")
                st.markdown("### Problems in this pattern")
                # --- NEW: aggregate problems from this pattern + all its sub-patterns ---
                aggregated_problems = []
                for pat_name, pdata in summary["patterns"].items():
                    # exact match OR sub-pattern like "recursion/IBH"
                    if pat_name == full or pat_name.startswith(full + "/"):
                        aggregated_problems.extend(pdata.get("problems", []))

                if not aggregated_problems:
                    st.info("No source files found under this pattern.")
                else:
                    # build a small table, similar style to Problems table in Report
                    table_rows = []
                    for pr in aggregated_problems:
                        table_rows.append({
                            "file": pr.get("file"),
                            "difficulty": pr.get("difficulty") or "None",
                            "next_step": pr.get("next_step"),
                            "next_due": pr.get("next_due"),
                            "revisions_count": len(pr.get("revisions") or []),
                        })

                    pat_problems_df = pd.DataFrame(table_rows).sort_values("file")
                    st.dataframe(pat_problems_df, height=400)
                # --- Pattern-scoped inspector ---
                st.markdown("### Inspect a problem in this pattern")

                # Re-use the same aggregated_problems list we just built
                if aggregated_problems:
                    files_in_pattern = [pr["file"] for pr in aggregated_problems]
                    # ✅ Add "(none)" as default
                    file_choices = ["(none)"] + files_in_pattern

                    sel_file = st.selectbox(
                        "Choose problem by file (for inspection, this pattern only)",
                        file_choices,
                        key="pattern_inspect_file",index =0# ✅ ensures "(none)" on load
                    )

                    if sel_file!=None:
                        # Find the matching problem object
                        found = None
                        for pr in aggregated_problems:
                            if pr["file"] == sel_file:
                                found = pr
                                break

                        if found:
                            # reuse your existing inspector UI pieces
                            st.markdown(f"#### {found.get('title') or sel_file}")
                            st.markdown(
                                f"**File**: `{found['file']}`  \n"
                                f"**Difficulty**: {found.get('difficulty') or 'None'}"
                            )

                            st.markdown("**Notes:**")
                            if found.get("notes_markdown"):
                                st.markdown(found.get("notes_markdown"))
                            else:
                                st.markdown("_No inline notes found._")

                            st.markdown("**Commit history (newest first):**")
                            if found.get("commits"):
                                for c in list(found["commits"])[-20:][::-1]:
                                    st.write(f"- {c['date']} — {c['message']}")
                            else:
                                st.write("_No git commits found for this file._")

                            # revision info + buttons – call your shared helpers here
                            st.markdown("**Revision schedule & actions**")
                            st.write(f"Commit date (git): `{found.get('commit_date')}`")
                            st.write(f"R1: {found.get('r1')}  |  R2: {found.get('r2')}  |  R3: {found.get('r3')}  |  R4: {found.get('r4')}")
                            revs_list = found.get('revisions') or []
                            st.write(f"Manual revisions ({len(revs_list)}): {revs_list}")
                            st.write(f"Next: {found.get('next_step')} — {found.get('next_due')}  (days_until_next: {found.get('days_until_next')})")

                            col1, col2 = st.columns([1, 1])

                            # Use UNIQUE keys here
                            if col1.button("Mark revision completed now",
                                           key=f"pat_inspect_mark_{found['file']}"):
                                mark_revision_for_file(found["file"], auto_commit_revisions)
                                st.rerun()

                            if col2.button("Undo last revision",
                                           key=f"pat_inspect_undo_{found['file']}"):
                                undo_revision_for_file(found["file"], auto_commit_revisions)
                                st.rerun()
                        else:
                            st.info("No problems in this pattern to inspect.")


    if view_mode == "Report":
    # Inspector
        st.subheader("Inspect a problem")
        file_list = df["file"].tolist()
        if not file_list:
            st.info("No files to inspect.")
            return
        # ✅ Add "(none)" as default
        file_choices = ["(none)"] + file_list
        sel = st.selectbox("Choose problem by file (for inspection)",file_choices, index=0)   # ✅ ensures "(none)" on load
        if sel != "(none)":
            found = None
            for pattern, pdata in summary["patterns"].items():
                for pr in pdata["problems"]:
                    if pr["file"] == sel:
                        found = pr
                        break
                if found:
                    break
            if found:
                st.markdown(f"### {found.get('title') or sel}")
                st.markdown(f"**File**: `{found['file']}`  \n**Tags**: {', '.join(found.get('tags') or [])}  \n**Difficulty**: {found.get('difficulty') or 'None'}")
                st.markdown("**Notes:**")
                if found.get("notes_markdown"):
                    st.markdown(found.get("notes_markdown"))
                else:
                    st.markdown("_No inline notes found._")
                st.markdown("**Commit history (newest first):**")
                if found["commits"]:
                    for c in list(found["commits"])[-20:][::-1]:
                        st.write(f"- {c['date']} — {c['message']}")
                else:
                    st.write("_No git commits found for this file._")

                # revision panel (uses 'revisions' list; compute count on the fly)
                st.markdown("**Revision schedule & actions**")
                st.write(f"Commit date (git): `{found.get('commit_date')}`")
                st.write(f"R1: {found.get('r1')}  |  R2: {found.get('r2')}  |  R3: {found.get('r3')}  |  R4: {found.get('r4')}")
                revs_list = found.get('revisions') or []
                st.write(f"Manual revisions ({len(revs_list)}): {revs_list}")
                st.write(f"Next: {found.get('next_step')} — {found.get('next_due')}  (days_until_next: {found.get('days_until_next')})")

                col1, col2 = st.columns([1,1])
                print("key = found[\"file\"] : ",found["file"])
                if col1.button("Mark revision completed now", key=f"report_mark_inspect_{found['file']}"):
                    mark_revision_for_file(found["file"], auto_commit_revisions)
                    st.rerun()
                if col2.button("Undo last revision", key=f"report_undo_inspect_{found['file']}"):
                    undo_revision_for_file(found["file"], auto_commit_revisions)
                    st.rerun()
