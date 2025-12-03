#!/usr/bin/env python3
"""
repo_progress.py

Scanner + Streamlit dashboard for a DSA repo.

Updates:
 - Aggregate pattern counts up the directory hierarchy so top-level pattern rows
   (e.g., `recursion`) show the sum of all child subpatterns (e.g., recursion/IBH).
 - Patterns view now shows subtree problems when a top-level pattern is selected.
 - Hide non-pattern path prefixes in the UI (e.g., 'src', 'src/main', 'src/main/java').
 - Keeps previous UX: page selector (no sidebar duplicate), notes loading, report view.
"""
from pathlib import Path
from datetime import datetime, timezone
import os
import re
import json
import argparse
import subprocess
from urllib.parse import quote, unquote

# -------------------------
# Config
# -------------------------
DEFAULT_EXCLUDE_DIRS = {'.git', '.venv', 'venv', '__pycache__', 'node_modules', 'target', '.idea', '.mvn', '.gradle'}
SOURCE_SUFFIXES = {'.java', '.py', '.cpp', '.c', '.js', '.ts'}

# Patterns to hide from UI (these are repo path prefixes, not "patterns" you study)
HIDE_DISPLAY_PATTERNS = {"src", "src/main", "src/main/java"}

# -------------------------
# Helpers
# -------------------------
def run_cmd(cmd, cwd=None):
    try:
        p = subprocess.run(cmd, capture_output=True, text=True, check=True, cwd=cwd)
        return p.stdout.strip()
    except subprocess.CalledProcessError:
        return ""
    except FileNotFoundError:
        return ""

def git_commits_for_file(path):
    try:
        out = run_cmd(["git", "log", "--format=%cI:::%s", "--reverse", "--", str(path)])
        if not out:
            return []
        lines = [ln for ln in out.splitlines() if ln.strip()]
        commits = []
        for ln in lines:
            parts = ln.split(":::", 1)
            date = parts[0].strip() if parts else ""
            message = parts[1].strip() if len(parts) > 1 else ""
            commits.append((date, message))
        return commits
    except Exception:
        return []

def iso_to_dt(iso):
    if not iso:
        return None
    try:
        return datetime.fromisoformat(iso)
    except Exception:
        try:
            return datetime.strptime(iso, "%Y-%m-%dT%H:%M:%S%z")
        except Exception:
            return None

def days_since(dt):
    if not dt:
        return None
    now = datetime.now(timezone.utc)
    if dt.tzinfo is None:
        dt = dt.replace(tzinfo=timezone.utc)
    delta = now - dt
    return delta.days

def is_source_file(path: Path):
    return path.suffix.lower() in SOURCE_SUFFIXES

# -------------------------
# Comment extraction and parsing
# -------------------------
def read_top_comment_block(path: Path):
    try:
        text = path.read_text(encoding="utf-8", errors="ignore")
    except Exception:
        return ""
    m = re.search(r"/\*([\s\S]*?)\*/", text)
    if m:
        block = m.group(1)
        cleaned_lines = []
        for ln in block.splitlines():
            cleaned_lines.append(re.sub(r'^\s*\*?\s?', '', ln))
        return "\n".join(cleaned_lines).strip()
    lines = text.splitlines()
    top = []
    for ln in lines[:120]:
        s = ln.strip()
        if s.startswith("//"):
            top.append(s[2:].strip())
        elif s == "":
            continue
        else:
            break
    return "\n".join(top).strip()

def parse_header_fields(block_text: str):
    result = {
        "title": None,
        "difficulty": None,
        "tags": None,
        "example": None,
        "complexity": None,
        "solved": None,
    }
    if not block_text:
        return result, ""

    lines = [ln.rstrip() for ln in block_text.splitlines()]
    remaining = []
    for ln in lines:
        m = re.match(r'^\s*([\w\s\-\#\u2600-\u26FF]+?)\s*[:\-]\s*(.+)$', ln)
        if m:
            key = m.group(1).strip().lower()
            val = m.group(2).strip()
            key_norm = re.sub(r'\s+', ' ', key).strip()
            if key_norm in ("problem", "title"):
                if not result["title"]:
                    result["title"] = val
                continue
            if key_norm == "difficulty":
                result["difficulty"] = val
                continue
            if key_norm == "tags":
                result["tags"] = [t.strip() for t in re.split(r'[,\|]+', val) if t.strip()]
                continue
            if key_norm == "example":
                result["example"] = val
                continue
            if key_norm == "complexity":
                result["complexity"] = val
                continue
            if key_norm == "solved":
                result["solved"] = val.lower() in ("true", "yes", "solved")
                continue
        remaining.append(ln)
    notes_md = "\n".join(remaining).strip()
    if not result["title"]:
        for ln in lines:
            if ln.strip() and not re.match(r'^\s*[\w\s\-\#]+?\s*[:\-]\s*', ln):
                result["title"] = ln.strip()
                break
    return result, notes_md

# -------------------------
# Repo scanning
# -------------------------
def scan_repo(root, exclude_dirs=None, only_paths=None):
    root = Path(root).resolve()
    exclude_dirs = set(exclude_dirs or DEFAULT_EXCLUDE_DIRS)
    only_paths_abs = []
    if only_paths:
        for p in only_paths:
            p = p.strip()
            if not p:
                continue
            allowed = root.joinpath(p).resolve()
            only_paths_abs.append(allowed)

    data = {
        "generated_at": datetime.utcnow().isoformat() + "Z",
        "repo_root": str(root),
        "patterns": {},
        "total_files_scanned": 0,
    }

    for dirpath, dirnames, filenames in os.walk(root):
        p = Path(dirpath)
        if any(part in exclude_dirs for part in p.parts):
            continue
        if only_paths_abs:
            under_allowed = False
            for allowed in only_paths_abs:
                try:
                    p.relative_to(allowed)
                    under_allowed = True
                    break
                except Exception:
                    continue
            if not under_allowed:
                continue

        has_src = any(is_source_file(Path(f)) for f in filenames)
        notes_exists = (p / "notes.md").exists()

        if not (has_src or notes_exists):
            continue

        try:
            rel = str(p.relative_to(root))
        except Exception:
            rel = str(p)
        pattern = rel or "."
        pe = data["patterns"].setdefault(pattern, {"count": 0, "problems": [], "notes": None, "notes_path": None})

        notes_md_path = p / "notes.md"
        if notes_md_path.exists():
            try:
                pe["notes"] = notes_md_path.read_text(encoding="utf-8", errors="ignore").strip()
                pe["notes_path"] = str(notes_md_path.relative_to(root))
            except Exception:
                pe["notes"] = None
                pe["notes_path"] = None

        for fname in filenames:
            fpath = p / fname
            if not is_source_file(fpath):
                continue
            data["total_files_scanned"] += 1

            header = read_top_comment_block(fpath)
            header_fields, notes_markdown = parse_header_fields(header)
            commits = git_commits_for_file(fpath)

            first_rev = commits[0][0] if len(commits) >= 1 else None
            second_rev = commits[1][0] if len(commits) >= 2 else None
            final_rev = commits[-1][0] if len(commits) >= 1 else None
            final_dt = iso_to_dt(final_rev)
            last_days = days_since(final_dt) if final_dt else None

            problem = {
                "file": str(fpath.relative_to(root)),
                "title": header_fields.get("title"),
                "difficulty": header_fields.get("difficulty"),
                "tags": header_fields.get("tags"),
                "example": header_fields.get("example"),
                "complexity": header_fields.get("complexity"),
                "solved": bool(header_fields.get("solved")) if header_fields.get("solved") is not None else None,
                "notes_markdown": notes_markdown,
                "commits": [{"date": d, "message": m} for d, m in commits],
                "first_revision_date": first_rev,
                "second_revision_date": second_rev,
                "final_revision_date": final_rev,
                "days_since_last": last_days,
            }
            pe["problems"].append(problem)
            pe["count"] += 1

    data["summary"] = {
        "patterns_count": len(data["patterns"]),
        "total_problems": sum(p["count"] for p in data["patterns"].values()),
    }
    return data

# -------------------------
# Streamlit UI
# -------------------------
def run_streamlit_ui(initial_summary=None, root=".", default_exclude=None, default_only=None):
    import streamlit as st
    import pandas as pd
    import os

    st.set_page_config(page_title="DSA Repo Progress", layout="wide")
    st.title("DSA Repo Progress — Revisions dashboard")

    # Sidebar controls
    st.sidebar.header("Controls")
    view_mode = st.sidebar.radio("View", options=["Report", "Patterns"], index=0)
    rev_threshold = st.sidebar.slider("Days since last revision to alert", min_value=1, max_value=365, value=30)
    show_unresolved_only = st.sidebar.checkbox("Show only unsolved", value=False)

    exclude_input = st.sidebar.text_input("Extra folders to exclude (space-separated)", value=(default_exclude or ".venv venv tools target .idea"))
    only_input = st.sidebar.text_input("Only scan paths (comma-separated)", value=(default_only or "src/main/java"))

    exclude_list = [x.strip() for x in re.split(r'\s+', exclude_input) if x.strip()]
    only_list = [x.strip() for x in only_input.split(",") if x.strip()]

    # Refresh button triggers rescan
    if st.sidebar.button("Refresh (scan repo)"):
        summary = scan_repo(root, exclude_dirs=set(exclude_list), only_paths=only_list)
        try:
            with open("summary.json", "w", encoding="utf-8") as f:
                json.dump(summary, f, indent=2, ensure_ascii=False)
        except Exception:
            pass
        st.experimental_rerun()
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
                "first_rev": pr.get("first_revision_date"),
                "second_rev": pr.get("second_revision_date"),
                "final_rev": pr.get("final_revision_date"),
                "days_since_last": pr.get("days_since_last"),
                "solved": bool(pr.get("solved")) if pr.get("solved") is not None else False,
            })
    df = pd.DataFrame(rows)

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

    # Compute difficulty_norm for summaries/plots (we will drop it before showing Problems table)
    if not df.empty:
        df['difficulty_norm'] = df['difficulty'].fillna('unknown').astype(str).str.lower().str.strip()
        df['difficulty_norm'] = df['difficulty_norm'].replace({'': 'unknown', 'med': 'medium', 'e': 'easy', 'm': 'medium', 'h': 'hard'})

    # -------------------------
    # Build aggregated pattern totals (hierarchical aggregation)
    # -------------------------
    agg_totals = {}
    for p in pattern_list:
        agg_totals[p] = {"total_problems": summary["patterns"].get(p, {}).get("count", 0), "solved_count": 0}
        solved_direct = sum(1 for pr in summary["patterns"].get(p, {}).get("problems", []) if pr.get("solved"))
        agg_totals[p]["solved_count"] = solved_direct

    for child in pattern_list:
        parts = child.split("/")
        for i in range(1, len(parts)):
            ancestor = "/".join(parts[:i])
            if ancestor not in agg_totals:
                agg_totals[ancestor] = {"total_problems": 0, "solved_count": 0}
            agg_totals[ancestor]["total_problems"] += agg_totals[child]["total_problems"]
            agg_totals[ancestor]["solved_count"] += agg_totals[child]["solved_count"]

    # -------------------------
    # REPORT VIEW
    # -------------------------
    if view_mode == "Report":
        st.subheader("Summary")
        total_problems = len(df)
        total_solved = int(df['solved'].fillna(False).astype(bool).sum())
        col1, col2, col3 = st.columns([1,1,2])
        col1.metric("Total problems", total_problems)
        col2.metric("Solved", total_solved)
        col3.metric("Unsolved", total_problems - total_solved)

        # Pattern summary table (use aggregated totals but hide technical path prefixes)
        pat_rows = []
        seen_keys = set()
        all_patterns_for_report = sorted(list(set(list(agg_totals.keys()) + pattern_list)))
        for full in all_patterns_for_report:
            # hide patterns that are in the HIDE_DISPLAY_PATTERNS exact set
            if full in HIDE_DISPLAY_PATTERNS:
                continue
            if full in seen_keys:
                continue
            seen_keys.add(full)
            total_problems_count = agg_totals.get(full, {}).get("total_problems", 0)
            solved_count = agg_totals.get(full, {}).get("solved_count", 0)
            trimmed = pattern_trim_map.get(full, full)
            pat_rows.append({
                "pattern_full": full,
                "pattern": trimmed,
                "total_problems": int(total_problems_count),
                "solved_count": int(solved_count),
            })
        pat_df = pd.DataFrame(pat_rows).sort_values("total_problems", ascending=False)
        if not pat_df.empty:
            pat_df["solved_pct"] = (pat_df["solved_count"] / pat_df["total_problems"].replace(0,1) * 100).round(1).astype(str) + '%'
            st.markdown("**Problems per pattern (total / solved)**")
            display_pat_df = pat_df[["pattern","total_problems","solved_count","solved_pct"]]
            st.dataframe(display_pat_df, height=250)

            st.markdown("**Top patterns by total problems**")
            topn = 20
            pat_chart = pat_df.set_index("pattern")[["total_problems","solved_count"]].head(topn)
            st.bar_chart(pat_chart)

        # Difficulty summary
        if not df.empty:
            diff_grp = df.groupby('difficulty_norm').agg(
                total=("file","count"),
                solved=("solved", lambda s: int(s.fillna(False).astype(bool).sum()))
            ).reset_index()
            if not diff_grp.empty:
                diff_grp['unsolved'] = diff_grp['total'] - diff_grp['solved']
                st.markdown("**Difficulty breakdown**")
                st.table(diff_grp[['difficulty_norm','total','solved','unsolved']].rename(columns={'difficulty_norm':'difficulty'}))
                diff_plot = diff_grp.set_index('difficulty_norm')[['solved','unsolved']]
                st.bar_chart(diff_plot)

        st.markdown("---")
        st.subheader("Problems table")
        if df.empty:
            st.info("No source files found.")
        else:
            display_df = df.copy()
            display_df["file"] = display_df["file"].astype(str)
            display_df["pattern"] = display_df["pattern"].astype(str) if "pattern" in display_df.columns else display_df["pattern_full"]
            drop_cols = [c for c in ["pattern_full","title","difficulty_norm"] if c in display_df.columns]
            display_df = display_df.drop(columns=drop_cols)
            if show_unresolved_only:
                display_df = display_df[display_df["solved"] == False]
            st.dataframe(display_df, height=600)

        # Quick Revision List (Due)
        st.subheader("Quick Revision List (Due)")
        if df.empty:
            st.info("No problems found.")
        else:
            def alert_flag(days):
                if days is None:
                    return "no-git"
                try:
                    if int(days) > rev_threshold:
                        return "due"
                    return "ok"
                except Exception:
                    return "no-git"
            df["alert"] = df["days_since_last"].apply(alert_flag)
            due = df[df["alert"] == "due"]
            if not due.empty:
                st.write(f"{len(due)} problems due for revision (>{rev_threshold} days since last edit):")
                st.table(due[["pattern","file","days_since_last","final_rev"]].head(100))
            else:
                st.success("No problems are currently due for revision.")

        # Inspector
        st.subheader("Inspect a problem")
        file_list = df["file"].tolist()
        if not file_list:
            st.info("No files to inspect.")
        else:
            sel = st.selectbox("Choose problem by file", file_list)
            if sel:
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

    # -------------------------
    # PATTERNS VIEW (focused) - selector on page, content BELOW (full width)
    # -------------------------
    else:  # view_mode == "Patterns"
        st.subheader("Patterns")
        st.markdown("Choose a pattern below to view notes and problems.")
        # exclude hidden display patterns from the selector
        visible_patterns = [p for p in pattern_list if p not in HIDE_DISPLAY_PATTERNS]
        pattern_choices = ["(none)"] + [pattern_trim_map.get(p, p) for p in visible_patterns]
        sel_pattern = st.selectbox("Pattern", pattern_choices, index=0)

        # Content loads below the selector (full width)
        if not pattern_list:
            st.info("No patterns found.")
        elif not sel_pattern or sel_pattern == "(none)":
            st.info("Select a pattern above to view its notes and problems.")
        else:
            trimmed_to_full = {pattern_trim_map.get(k, k): k for k in pattern_list}
            full_key = trimmed_to_full.get(sel_pattern)
            if not full_key:
                st.error("Pattern not found.")
            else:
                subtree_keys = [k for k in summary["patterns"].keys() if k == full_key or k.startswith(full_key + "/")]
                top_notes_path = summary["patterns"].get(full_key, {}).get("notes_path")
                top_notes_inline = summary["patterns"].get(full_key, {}).get("notes")
                st.markdown(f"### {sel_pattern}")
                if top_notes_path:
                    try:
                        md_content = Path(summary["repo_root"]).joinpath(top_notes_path).read_text(encoding="utf-8", errors="ignore")
                        st.markdown(md_content)
                        st.markdown(f"_Notes file: `{top_notes_path}`_")
                    except Exception:
                        st.markdown("_Failed to load notes file; showing inline notes if available._")
                        if top_notes_inline:
                            st.markdown(top_notes_inline)
                else:
                    combined = []
                    if top_notes_inline:
                        combined.append(top_notes_inline)
                    for k in subtree_keys:
                        if k == full_key:
                            continue
                        child_notes = summary["patterns"].get(k, {}).get("notes")
                        child_notes_path = summary["patterns"].get(k, {}).get("notes_path")
                        if child_notes_path and not top_notes_path:
                            combined.append(f"**Notes for {k}**\n\n" + (child_notes or f"_notes file: {child_notes_path}_"))
                        elif child_notes:
                            combined.append(f"**Notes for {k}**\n\n{child_notes}")
                    if combined:
                        st.markdown("\n\n---\n\n".join(combined))
                    else:
                        st.markdown("_No notes.md or inline notes found for this pattern or its subpatterns._")

                st.markdown("#### Problems in this pattern (including subpatterns)")
                all_problems = []
                for k in subtree_keys:
                    all_problems.extend(summary["patterns"].get(k, {}).get("problems", []))
                if not all_problems:
                    st.info("No source files found under this pattern or its subpatterns.")
                else:
                    rows_p = []
                    for p in all_problems:
                        rows_p.append({
                            "file": p.get("file"),
                            "title": p.get("title"),
                            "difficulty": p.get("difficulty"),
                            "tags": ", ".join(p.get("tags") or []),
                            "solved": bool(p.get("solved")) if p.get("solved") is not None else False,
                            "last_edit": p.get("final_revision_date"),
                            "days_since_last": p.get("days_since_last"),
                        })
                    pdp = pd.DataFrame(rows_p)
                    st.dataframe(pdp, height=350)

                    st.markdown("#### Inspect file")
                    files = pdp["file"].tolist()
                    sel_file = st.selectbox("Choose file", files)
                    if sel_file:
                        selobj = next((x for x in all_problems if x["file"] == sel_file), None)
                        if selobj:
                            st.markdown(f"**{selobj.get('title') or selobj.get('file')}**")
                            st.markdown(f"File: `{selobj['file']}`  \nDifficulty: {selobj.get('difficulty') or 'None'}  \nTags: {', '.join(selobj.get('tags') or [])}")
                            if selobj.get("notes_markdown"):
                                st.markdown("**Notes (inline):**")
                                st.markdown(selobj.get("notes_markdown"))
                            st.markdown("**Commit history (newest first):**")
                            if selobj["commits"]:
                                for c in list(selobj["commits"])[-20:][::-1]:
                                    st.write(f"- {c['date']} — {c['message']}")
                            else:
                                st.write("_No git commits found for this file._")

# -------------------------
# CLI
# -------------------------
def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--root", default=".", help="repo root folder to scan")
    parser.add_argument("--out", default="summary.json", help="output JSON file")
    parser.add_argument("--dashboard", action="store_true", help="run streamlit dashboard")
    parser.add_argument("--exclude", nargs="*", help="dir names to exclude (space separated)", default=None)
    parser.add_argument("--only", help="comma-separated relative paths to limit scanning (e.g. 'src/main/java')", default=None)
    args = parser.parse_args()

    only_list = []
    if args.only:
        only_list = [p.strip() for p in args.only.split(",") if p.strip()]

    exclude_set = set(args.exclude) if args.exclude else None

    summary = scan_repo(args.root, exclude_dirs=exclude_set, only_paths=only_list)
    try:
        with open(args.out, "w", encoding="utf-8") as f:
            json.dump(summary, f, indent=2, ensure_ascii=False)
        print("Wrote", args.out)
    except Exception as e:
        print("Warning: failed to write summary JSON:", e)

    if args.dashboard:
        try:
            run_streamlit_ui(summary, root=args.root, default_exclude=" ".join(list(exclude_set or DEFAULT_EXCLUDE_DIRS)), default_only=",".join(only_list) if only_list else "src/main/java")
        except Exception as e:
            print("Streamlit UI failed:", e)

if __name__ == "__main__":
    main()
