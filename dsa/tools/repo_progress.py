#!/usr/bin/env python3
# tools/repo_progress.py
import argparse
import json
from rp_scan import scan_repo
from rp_ui import run_streamlit_ui

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
            run_streamlit_ui(summary, root=args.root, default_exclude=" ".join(list(exclude_set or [])), default_only=",".join(only_list) if only_list else "src/main/java")
        except Exception as e:
            print("Streamlit UI failed:", e)

if __name__ == "__main__":
    main()
