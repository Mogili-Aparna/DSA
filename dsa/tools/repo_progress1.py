#!/usr/bin/env python3

from pathlib import Path
from datetime import datetime, timezone, timedelta
import os
import re
import json
import argparse
import subprocess

# -------------------------
# Config
# -------------------------
DEFAULT_EXCLUDE_DIRS = {'.git', '.venv', 'venv', '__pycache__', 'node_modules', 'target', '.idea', '.mvn', '.gradle'}
SOURCE_SUFFIXES = {'.java', '.py', '.cpp', '.c', '.js', '.ts'}
REV_LOG_DIR = Path(".dsa_progress")
REV_LOG = REV_LOG_DIR / "revisions.json"

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
        out = run_cmd(["git", "log", "--follow", "--format=%aI:::%s", "--reverse", "--", str(path)])
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
        dt = datetime.fromisoformat(iso)
        if dt.tzinfo is None:
            dt = dt.replace(tzinfo=timezone.utc)
        return dt
    except Exception:
        try:
            return datetime.strptime(iso, "%Y-%m-%dT%H:%M:%S%z")
        except Exception:
            return None

def dt_to_iso(dt):
    if not dt:
        return None
    if dt.tzinfo is None:
        dt = dt.replace(tzinfo=timezone.utc)
    return dt.astimezone(timezone.utc).isoformat()

def add_days(dt, days):
    if dt is None:
        return None
    return dt + timedelta(days=days)

def is_source_file(path: Path):
    return path.suffix.lower() in SOURCE_SUFFIXES

def sanitize_key(s: str) -> str:
    return re.sub(r'[^0-9a-zA-Z_\-]', '_', s)

# -------------------------
# Revision storage helpers (.dsa_progress/revisions.json)
# -------------------------
def load_revisions():
    try:
        if not REV_LOG.exists():
            return {}
        j = json.loads(REV_LOG.read_text(encoding="utf-8"))
        files = j.get("files", {})
        for k, v in files.items():
            files[k] = sorted(v)
        return files
    except Exception:
        return {}

def save_revisions(rev_map):
    try:
        REV_LOG_DIR.mkdir(parents=True, exist_ok=True)
        content = {"files": rev_map}
        REV_LOG.write_text(json.dumps(content, indent=2, ensure_ascii=False), encoding="utf-8")
        return True
    except Exception:
        return False

# -------------------------
# Revision schedule computation
# -------------------------
def normalize_difficulty(d):
    if not d:
        return "medium"
    dn = str(d).strip().lower()
    if dn in ("e", "easy"):
        return "easy"
    if dn in ("m", "med", "medium"):
        return "medium"
    if dn in ("h", "hard"):
        return "hard"
    return "medium"

def compute_revision_schedule(commit_iso, difficulty, manual_revs):
    """
    Adaptive revision schedule:

    - If no manual revisions yet:
        R1..R4 are based on commit date (same as before).
    - If there is at least one manual revision:
        R1 = first manual revision date
        R2..R4 are computed from R1 using difficulty rules.

    manual_revs: list of ISO strings (when you clicked "Mark revision completed now").
    """
    diff = normalize_difficulty(difficulty)
    commit_dt = iso_to_dt(commit_iso) if commit_iso else None

    # Parse & sort manual revisions
    manual_list = manual_revs or []
    manual_dts = [iso_to_dt(x) for x in manual_list if iso_to_dt(x) is not None]
    manual_dts.sort()
    manual_count = len(manual_dts)

    # -------------------------
    # Compute R1..R4 (plan)
    # -------------------------
    r1 = r2 = r3 = r4 = None

    if manual_count == 0:
        # 🔹 No manual revisions yet → keep old behavior (commit-based plan)
        anchor = commit_dt
        if anchor:
            # First planned revision = commit + 3 days
            r1 = add_days(anchor, 3)

            if diff == "easy":
                r2 = add_days(r1, 15)
                r3 = add_days(r2, 15) if r2 else None
            elif diff == "medium":
                r2 = add_days(r1, 7)
                r3 = add_days(r2, 15) if r2 else None
                r4 = add_days(r3, 15) if r3 else None
            else:  # hard
                r2 = add_days(r1, 3)
                r3 = add_days(r2, 7) if r2 else None
                r4 = add_days(r3, 7) if r3 else None
    else:
        # 🔹 At least one manual revision → adapt schedule based on actual study
        anchor = manual_dts[0]   # first time you revised this problem
        r1 = anchor              # show R1 as the actual first revision date

        if diff == "easy":
            r2 = add_days(r1, 15)
            r3 = add_days(r2, 15) if r2 else None
            # no R4 for easy by your original plan
        elif diff == "medium":
            r2 = add_days(r1, 7)
            r3 = add_days(r2, 15) if r2 else None
            r4 = add_days(r3, 15) if r3 else None
        else:  # hard
            r2 = add_days(r1, 3)
            r3 = add_days(r2, 7) if r2 else None
            r4 = add_days(r3, 7) if r3 else None

    # -------------------------
    # Next due calculation (unchanged)
    # -------------------------
    from datetime import datetime, timezone
    now = datetime.now(timezone.utc)

    next_due = None
    next_step = None
    days_until_next = None

    if manual_count == 0:
        # You haven't revised even once yet:
        # Next due = R1 (if it exists)
        if r1:
            next_due = r1
            next_step = "First revision"
        else:
            next_due = None
            next_step = "First revision (manual)"
    elif manual_count == 1:
        # One revision done → schedule second based on last manual revision
        last = manual_dts[-1]
        if diff == "easy":
            nd = add_days(last, 15)
        elif diff == "medium":
            nd = add_days(last, 7)
        else:  # hard
            nd = add_days(last, 3)
        next_due = nd
        next_step = "Second revision"
    else:
        # 2 or more revisions done → repeated "final" revisions
        last = manual_dts[-1]
        if diff == "easy":
            # stop after ~3 revisions total (you can tweak if you want)
            next_due = add_days(last, 15) if manual_count < 3 else None
            next_step = "Final revision" if next_due else None
        elif diff == "medium":
            # repeat every 15 days from last revision, stop after 5 total
            if manual_count < 5:
                next_due = add_days(last, 15)
                next_step = "Final revision (repeat)"
            else:
                next_due = None
                next_step = None
        else:  # hard
            # repeat every 7 days from last revision, stop after 5 total
            if manual_count < 5:
                next_due = add_days(last, 7)
                next_step = "Final revision (repeat)"
            else:
                next_due = None
                next_step = None

    if next_due:
        days_until_next = (next_due - now).days
    else:
        days_until_next = None

    return {
        "commit_date": dt_to_iso(commit_dt) if commit_dt else None,
        "r1": dt_to_iso(r1) if r1 else None,
        "r2": dt_to_iso(r2) if r2 else None,
        "r3": dt_to_iso(r3) if r3 else None,
        "r4": dt_to_iso(r4) if r4 else None,
        "revisions": [dt_to_iso(x) for x in manual_dts],
        "next_due": dt_to_iso(next_due) if next_due else None,
        "next_step": next_step,
        "days_until_next": days_until_next,
    }


# -------------------------
# Comment extraction & header parsing
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

    revisions_map = load_revisions()

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

            final_rev = commits[-1][0] if len(commits) >= 1 else None

            relpath = str(fpath.relative_to(root))
            manual_revs = revisions_map.get(relpath, [])

            schedule = compute_revision_schedule(final_rev, header_fields.get("difficulty"), manual_revs)

            problem = {
                "file": relpath,
                "title": header_fields.get("title"),
                "difficulty": header_fields.get("difficulty"),
                "tags": header_fields.get("tags"),
                "example": header_fields.get("example"),
                "complexity": header_fields.get("complexity"),
                "solved": bool(header_fields.get("solved")) if header_fields.get("solved") is not None else None,
                "notes_markdown": notes_markdown,
                "commits": [{"date": d, "message": m} for d, m in commits],
                "commit_date": schedule.get("commit_date"),
                "r1": schedule.get("r1"),
                "r2": schedule.get("r2"),
                "r3": schedule.get("r3"),
                "r4": schedule.get("r4"),
                "revisions": schedule.get("revisions"),
                "next_due": schedule.get("next_due"),
                "next_step": schedule.get("next_step"),
                "days_until_next": schedule.get("days_until_next"),
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
                "commit_date": pr.get("commit_date"),
                "r1": pr.get("r1"),
                "r2": pr.get("r2"),
                "r3": pr.get("r3"),
                "r4": pr.get("r4"),
                "revisions": ";".join(pr.get("revisions") or []),
                "next_due": pr.get("next_due"),
                "next_step": pr.get("next_step"),
                "days_until_next": pr.get("days_until_next"),
                "solved": bool(pr.get("solved")) if pr.get("solved") is not None else False,
            })
    df = pd.DataFrame(rows)

    # Build list of patterns including those with only notes and zero problems
    raw_pattern_list = list(summary["patterns"].keys())

    def is_generic_src_entry(p):
        return p in ("src", "src/main", "src/main/java")

    pattern_list = [p for p in raw_pattern_list if not is_generic_src_entry(p)]
    if not pattern_list:
        pattern_list = raw_pattern_list

    def compute_trim_map(patterns):
        if not patterns:
            return {}
        common_pref = os.path.commonprefix(patterns)
        if "/" in common_pref:
            cut = common_pref.rfind("/") + 1
            if cut <= 0:
                cut = 0
        else:
            cut = 0
        trim_map = {}
        for p in patterns:
            if cut > 0 and p.startswith(common_pref[:cut]):
                trim_map[p] = p[cut:]
            else:
                trim_map[p] = p
        return trim_map

    pattern_trim_map = compute_trim_map(pattern_list)

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

        # Pattern summary (aggregate over subpatterns)
        pat_rows = []
        for full in pattern_list:
            total_problems_count = 0
            solved_count = 0
            for k, pinfo in summary["patterns"].items():
                if is_generic_src_entry(k):
                    continue
                if k == full or k.startswith(full + "/"):
                    total_problems_count += int(pinfo.get("count", 0))
                    for pr in pinfo.get("problems", []):
                        if pr.get("solved"):
                            solved_count += 1

            trimmed = pattern_trim_map.get(full, full)
            pat_rows.append({
                "pattern_full": full,
                "pattern": trimmed,
                "total_problems": int(total_problems_count),
                "solved_count": int(solved_count),
            })
        pat_df = pd.DataFrame(pat_rows)
        if not pat_df.empty:
            pat_df = pat_df.sort_values("total_problems", ascending=False)
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
            df['difficulty_norm'] = df['difficulty'].fillna('unknown').astype(str).str.lower().str.strip()
            df['difficulty_norm'] = df['difficulty_norm'].replace({'': 'unknown', 'med': 'medium', 'e': 'easy', 'm': 'medium', 'h': 'hard'})
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

        # Problems table
        st.subheader("Problems table")
        if df.empty:
            st.info("No source files found.")
        else:
            display_df = df.copy()
            display_df["file"] = display_df["file"].astype(str)
            display_df["pattern"] = display_df["pattern"].astype(str) if "pattern" in display_df.columns else display_df["pattern_full"]
            drop_cols = [c for c in ["pattern_full","title"] if c in display_df.columns]
            display_df = display_df.drop(columns=drop_cols)
            st.dataframe(display_df, height=600)

        # Quick Revision List (Due)
        st.subheader("Quick Revision List (Due)")
        if df.empty:
            st.info("No problems found.")
        else:
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
                def rev_count_str(s):
                    if not s:
                        return "0"
                    return str(len([x for x in s.split(";") if x.strip()]))
                due_display = due_df.copy()
                due_display["revisions_count"] = due_display["revisions"].apply(rev_count_str)
                show_cols = ["pattern","file","difficulty","revisions_count","next_step","next_due"]
                st.write(f"{len(due_display)} problems due for revision (per schedule):")
                st.table(due_display[show_cols].head(200))

                st.markdown("### Mark revisions as completed")
                for _, row in due_display.head(200).iterrows():
                    cols = st.columns([3,1,1])
                    cols[0].markdown(f"**{row['pattern']} — `{row['file']}`**  \n**Next:** {row['next_step']} — {row['next_due']}")
                    key_mark = f"mark_due_{sanitize_key(row['file'])}"
                    key_undo = f"undo_due_{sanitize_key(row['file'])}"
                    if cols[1].button("Mark", key=key_mark):
                        revs = load_revisions()
                        key = row['file']
                        now_iso = datetime.utcnow().replace(tzinfo=timezone.utc).isoformat()
                        cur = revs.get(key, [])
                        cur.append(now_iso)
                        revs[key] = sorted(cur)
                        save_revisions(revs)
                        if auto_commit_revisions:
                            run_cmd(["git", "add", str(REV_LOG)])
                            run_cmd(["git", "commit", "-m", f"Revision: {key} @ {now_iso}"])
                        st.rerun()
                    if cols[2].button("Undo", key=key_undo):
                        revs = load_revisions()
                        key = row['file']
                        cur = revs.get(key, [])
                        if cur:
                            cur = cur[:-1]
                            if cur:
                                revs[key] = cur
                            else:
                                revs.pop(key, None)
                            save_revisions(revs)
                            if auto_commit_revisions:
                                run_cmd(["git", "add", str(REV_LOG)])
                                run_cmd(["git", "commit", "-m", f"Undo revision: {key}"])
                        st.rerun()

    # Patterns view
    if view_mode == "Patterns":
        st.header("Patterns")
        st.subheader("Choose pattern")
        pattern_choices = ["(none)"] + [pattern_trim_map.get(p, p) for p in pattern_list]
        trimmed_to_full = {}
        for p in pattern_list:
            trimmed = pattern_trim_map.get(p, p)
            trimmed_to_full[trimmed] = p

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
                problems = []
                for k, pinfo in summary["patterns"].items():
                    if k in ("src", "src/main", "src/main/java"):
                        continue
                    if k == full or k.startswith(full + "/"):
                        problems.extend(pinfo.get("problems", []))

                # build DataFrame for the pattern's problems to display
                import pandas as _pd
                if not problems:
                    st.info("No source files found under this pattern.")
                else:
                    proto_rows = []
                    for pr in problems:
                        proto_rows.append({
                            "file": pr.get("file"),
                            "difficulty": pr.get("difficulty"),
                            "tags": ", ".join(pr.get("tags") or []),
                            "commit_date": pr.get("commit_date"),
                            "r1": pr.get("r1"),
                            "r2": pr.get("r2"),
                            "r3": pr.get("r3"),
                            "r4": pr.get("r4"),
                            "revisions": ";".join(pr.get("revisions") or []),
                            "next_due": pr.get("next_due"),
                            "next_step": pr.get("next_step"),
                            "days_until_next": pr.get("days_until_next"),
                            "solved": bool(pr.get("solved")) if pr.get("solved") is not None else False,
                        })
                    pattern_df = _pd.DataFrame(proto_rows).sort_values("file")
                    # display same style as report's problems table
                    st.dataframe(pattern_df, height=360)

                    # inspector limited to these files (pattern-specific)
                    st.subheader("Inspect a problem in this pattern")
                    files_for_pattern = pattern_df["file"].tolist()
                    if files_for_pattern:
                        sel_file = st.selectbox("Choose file", ["(none)"] + files_for_pattern, index=0)
                        if sel_file and sel_file != "(none)":
                            # find problem object
                            found = None
                            for pr in problems:
                                if pr.get("file") == sel_file:
                                    found = pr
                                    break
                            if found:
                                st.markdown(f"### {found.get('title') or sel_file}")
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

                                st.markdown("**Revision schedule & actions**")
                                st.write(f"Commit date (git): `{found.get('commit_date')}`")
                                st.write(f"R1: {found.get('r1')}  |  R2: {found.get('r2')}  |  R3: {found.get('r3')}  |  R4: {found.get('r4')}")
                                revs_list = found.get('revisions') or []
                                st.write(f"Manual revisions ({len(revs_list)}): {revs_list}")
                                st.write(f"Next: {found.get('next_step')} — {found.get('next_due')}  (days_until_next: {found.get('days_until_next')})")

                                col1, col2 = st.columns([1,1])
                                if col1.button("Mark revision completed now", key=f"pat_ins_mark_{sanitize_key(found['file'])}"):
                                    revs = load_revisions()
                                    key = found["file"]
                                    now_iso = datetime.utcnow().replace(tzinfo=timezone.utc).isoformat()
                                    cur = revs.get(key, [])
                                    cur.append(now_iso)
                                    revs[key] = sorted(cur)
                                    save_revisions(revs)
                                    if auto_commit_revisions:
                                        run_cmd(["git", "add", str(REV_LOG)])
                                        run_cmd(["git", "commit", "-m", f"Revision: {key} @ {now_iso}"])
                                    st.rerun()
                                if col2.button("Undo last revision", key=f"pat_ins_undo_{sanitize_key(found['file'])}"):
                                    revs = load_revisions()
                                    key = found['file']
                                    cur = revs.get(key, [])
                                    if cur:
                                        cur = cur[:-1]
                                        if cur:
                                            revs[key] = cur
                                        else:
                                            revs.pop(key, None)
                                        save_revisions(revs)
                                        if auto_commit_revisions:
                                            run_cmd(["git", "add", str(REV_LOG)])
                                            run_cmd(["git", "commit", "-m", f"Undo revision: {key}"])
                                    st.rerun()

    # Inspector in Report view (kept for convenience)
    if view_mode == "Report":
        st.subheader("Inspect a problem")
        file_list = df["file"].tolist()
        if not file_list:
            st.info("No files to inspect.")
            return
        sel = st.selectbox("Choose problem by file (for inspection)", ["(none)"] + file_list, index=0)
        if sel and sel != "(none)":
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

                st.markdown("**Revision schedule & actions**")
                st.write(f"Commit date (git): `{found.get('commit_date')}`")
                st.write(f"R1: {found.get('r1')}  |  R2: {found.get('r2')}  |  R3: {found.get('r3')}  |  R4: {found.get('r4')}")
                revs_list = found.get('revisions') or []
                st.write(f"Manual revisions ({len(revs_list)}): {revs_list}")
                st.write(f"Next: {found.get('next_step')} — {found.get('next_due')}  (days_until_next: {found.get('days_until_next')})")

                col1, col2 = st.columns([1,1])
                if col1.button("Mark revision completed now", key=f"ins_mark_{sanitize_key(found['file'])}"):
                    revs = load_revisions()
                    key = found["file"]
                    now_iso = datetime.utcnow().replace(tzinfo=timezone.utc).isoformat()
                    cur = revs.get(key, [])
                    cur.append(now_iso)
                    revs[key] = sorted(cur)
                    save_revisions(revs)
                    if auto_commit_revisions:
                        run_cmd(["git", "add", str(REV_LOG)])
                        run_cmd(["git", "commit", "-m", f"Revision: {key} @ {now_iso}"])
                    st.rerun()
                if col2.button("Undo last revision", key=f"ins_undo_{sanitize_key(found['file'])}"):
                    revs = load_revisions()
                    key = found["file"]
                    cur = revs.get(key, [])
                    if cur:
                        cur = cur[:-1]
                        if cur:
                            revs[key] = cur
                        else:
                            revs.pop(key, None)
                        save_revisions(revs)
                        if auto_commit_revisions:
                            run_cmd(["git", "add", str(REV_LOG)])
                            run_cmd(["git", "commit", "-m", f"Undo revision: {key}"])
                    st.rerun()

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
