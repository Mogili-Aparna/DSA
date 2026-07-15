# tools/rp_revision.py
from rp_utils import iso_to_dt, dt_to_iso, add_days

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

