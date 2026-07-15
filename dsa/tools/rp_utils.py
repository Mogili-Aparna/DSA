# tools/rp_utils.py
from pathlib import Path
from datetime import datetime, timezone, timedelta
import subprocess
import json
import re

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
    """
    Return list of (iso_date, message) for the file, oldest-first.
    Uses author date (%aI) and --follow to track renames.
    """
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

def days_since(dt):
    if not dt:
        return None
    now = datetime.now(timezone.utc)
    if dt.tzinfo is None:
        dt = dt.replace(tzinfo=timezone.utc)
    delta = now - dt
    return delta.days

def add_days(dt, days):
    if dt is None:
        return None
    return dt + timedelta(days=days)

def is_source_file(path: Path):
    return path.suffix.lower() in SOURCE_SUFFIXES

# -------------------------
# Revision storage helpers (.dsa_progress/revisions.json)
# -------------------------
def load_revisions():
    """
    returns dict: { "relative/path/File.java": ["2025-12-04T...Z", ...], ... }
    'revisions' is the list of ISO timestamps (strings). We use len(revisions) when we need the count.
    """
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
# Comment extraction & header parsing
# -------------------------
def read_top_comment_block(path):
    try:
        text = Path(path).read_text(encoding="utf-8", errors="ignore")
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

def mark_revision_for_file(file_path: str, auto_commit_revisions: bool):
    revs = load_revisions()
    now_iso = datetime.utcnow().replace(tzinfo=timezone.utc).isoformat()
    cur = revs.get(file_path, [])
    cur.append(now_iso)
    revs[file_path] = sorted(cur)
    save_revisions(revs)
    if auto_commit_revisions:
        run_cmd(["git", "add", str(REV_LOG)])
        run_cmd(["git", "commit", "-m", f"Revision: {file_path} @ {now_iso}"])

def undo_revision_for_file(file_path: str, auto_commit_revisions: bool):
    revs = load_revisions()
    cur = revs.get(file_path, [])
    if cur:
        cur = cur[:-1]
        if cur:
            revs[file_path] = cur
        else:
            revs.pop(file_path, None)
        save_revisions(revs)
        if auto_commit_revisions:
            run_cmd(["git", "add", str(REV_LOG)])
            run_cmd(["git", "commit", "-m", f"Undo revision: {file_path}"])
