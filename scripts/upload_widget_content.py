import re
from urllib.parse import urlparse, parse_qs
from pathlib import Path

import firebase_admin
from firebase_admin import credentials, firestore


# ============================================================
# FIREBASE SETUP
# ============================================================

BASE_DIR = Path(__file__).resolve().parent
KEY_PATH = BASE_DIR / "firebase-key.json"

cred = credentials.Certificate(str(KEY_PATH))
firebase_admin.initialize_app(cred)

db = firestore.client()

CATEGORY_IDS = {
    "video": "sza0zbcnK44A4LkHa3NY",
    "sermon": "BjopYNQj8Q6wT004zfs2",
    "music": "PxoNBQSohemDXFgKbEOT",
    "podcast": "o6sYl4zXTyTHqAwsDgC7",
}


# ============================================================
# YOUTUBE ID EXTRACTION
# ============================================================

def extract_video_id(url: str):
    url = url.strip()

    if not url:
        return None

    # Thumbnail URLs
    match = re.search(r"/vi/([A-Za-z0-9_-]{11})/", url)

    if match:
        return match.group(1)

    try:
        parsed = urlparse(url)

        # youtube.com/watch?v=
        if "youtube.com" in parsed.netloc:
            qs = parse_qs(parsed.query)

            if "v" in qs:
                return qs["v"][0]

            parts = parsed.path.strip("/").split("/")

            if len(parts) >= 2 and parts[0] == "shorts":
                return parts[1]

            if len(parts) >= 2 and parts[0] == "live":
                return parts[1]

        # youtu.be/VIDEO_ID
        if "youtu.be" in parsed.netloc:
            return parsed.path.strip("/")

    except Exception:
        pass

    return None


# ============================================================
# GET NEXT ORDER
# ============================================================

def get_next_order(category_ref):
    documents = (
        db.collection("video")
        .where("category", "==", category_ref)
        .stream()
    )

    max_order = 0

    for document in documents:
        data = document.to_dict()
        current_order = data.get("order")

        if isinstance(current_order, int):
            max_order = max(max_order, current_order)

    return max_order + 1


# ============================================================
# MAIN
# ============================================================

print("Firestore YouTube Uploader")
print("--------------------------")

while True:
    category = input(
        "Category (video/music/sermon/podcast): "
    ).strip().lower()

    if category in CATEGORY_IDS:
        break

    print("Invalid category.\n")


category_ref = (
    db.collection("widgetCategory")
    .document(CATEGORY_IDS[category])
)


print()
print("Paste YouTube links (one per line).")
print("Press ENTER on an empty line when finished.\n")


urls = []

while True:
    line = input().strip()

    if line == "":
        break

    urls.append(line)


# ============================================================
# DETERMINE STARTING ORDER
# ============================================================

next_order = get_next_order(category_ref)

print(
    f"\nNext order for '{category}': {next_order}"
)


# ============================================================
# UPLOAD
# ============================================================

seen = set()

uploaded = 0
already_exists = 0
duplicates = 0
invalid = 0


print("\nUploading...\n")


for url in urls:

    video_id = extract_video_id(url)

    if video_id is None:
        invalid += 1

        print(f"❌ Invalid URL: {url}")

        continue


    # --------------------------------------------------------
    # DUPLICATE URL IN CURRENT INPUT
    # --------------------------------------------------------

    if video_id in seen:
        duplicates += 1

        print(
            f"⏭ Duplicate in input: {video_id}"
        )

        continue

    seen.add(video_id)


    # --------------------------------------------------------
    # CHECK IF VIDEO ALREADY EXISTS
    # --------------------------------------------------------

    doc = db.collection("video").document(video_id)

    if doc.get().exists:

        already_exists += 1

        print(
            f"⏭ Already exists: {video_id}"
        )

        continue


    # --------------------------------------------------------
    # CREATE DOCUMENT
    # --------------------------------------------------------

    doc.set({
        "videoID": video_id,
        "category": category_ref,
        "order": next_order
    })


    uploaded += 1

    print(
        f"✅ Uploaded: {video_id} "
        f"(order={next_order})"
    )


    next_order += 1


# ============================================================
# SUMMARY
# ============================================================

print("\n==============================")
print("Finished")
print("==============================")

print(f"Category: {category}")
print(f"Uploaded: {uploaded}")
print(f"Already existed: {already_exists}")
print(f"Duplicates pasted: {duplicates}")
print(f"Invalid URLs: {invalid}")