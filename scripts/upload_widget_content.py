import re
from urllib.parse import urlparse, parse_qs

import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
from pathlib import Path


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
# MAIN
# ============================================================

print("Firestore YouTube Uploader")
print("--------------------------")

while True:
    category = input("Category (video/music/sermon/podcast): ").strip().lower()

    if category in CATEGORY_IDS:
        break

    print("Invalid category.\n")

category_ref = db.collection("widgetCategory").document(
    CATEGORY_IDS[category]
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

    if video_id in seen:
        duplicates += 1
        continue

    seen.add(video_id)

    doc = db.collection("video").document(video_id)

    if doc.get().exists:
        already_exists += 1
        print(f"⏭ Already exists: {video_id}")
        continue

    doc.set({
        "videoID": video_id,
        "category": category_ref
    })

    uploaded += 1
    print(f"✅ Uploaded: {video_id}")

print("\n==============================")
print("Finished")
print("==============================")
print(f"Category: {category}")
print(f"Uploaded: {uploaded}")
print(f"Already existed: {already_exists}")
print(f"Duplicates pasted: {duplicates}")
print(f"Invalid URLs: {invalid}")