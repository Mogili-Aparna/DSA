# tools/rp_scan.py
from pathlib import Path
import os
from rp_utils import DEFAULT_EXCLUDE_DIRS, is_source_file, read_top_comment_block, parse_header_fields, git_commits_for_file, load_revisions
from rp_revision import compute_revision_schedule
from datetime import datetime

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
                # schedule fields
                "commit_date": schedule.get("commit_date"),
                "r1": schedule.get("r1"),
                "r2": schedule.get("r2"),
                "r3": schedule.get("r3"),
                "r4": schedule.get("r4"),
                # 'revisions' is a list of ISO strings (manual timestamps). Use len(revisions) if you need the count.
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
