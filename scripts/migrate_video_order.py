import firebase_admin
from firebase_admin import credentials, firestore
from pathlib import Path

# ============================================================
# CONFIGURATION
# ============================================================

BASE_DIR = Path(__file__).resolve().parent
SERVICE_ACCOUNT_FILE = BASE_DIR / "firebase-key.json"

# True  = only show what would be changed
# False = actually update Firestore
DRY_RUN = False


CATEGORIES = {
    "sermon": "BjopYNQj8Q6wT004zfs2",
    "music": "PxoNBQSohemDXFgKbEOT",
    "podcast": "o6sYl4zXTyTHqAwsDgC7",
    "video": "sza0zbcnK44A4LkHa3NY",
}


# ============================================================
# FIREBASE INITIALIZATION
# ============================================================

cred = credentials.Certificate(str(SERVICE_ACCOUNT_FILE))

firebase_admin.initialize_app(cred)

db = firestore.client()


# ============================================================
# LOAD CATEGORY REFERENCES
# ============================================================

category_refs = {}

for category_name, document_id in CATEGORIES.items():
    category_refs[category_name] = (
        db.collection("widgetCategory")
        .document(document_id)
    )


# ============================================================
# READ VIDEO DOCUMENTS
# ============================================================

print("\nReading video collection...\n")

documents = list(
    db.collection("video").stream()
)

print(f"Found {len(documents)} video documents.\n")


# ============================================================
# GROUP DOCUMENTS BY CATEGORY
# ============================================================

grouped = {
    category_name: []
    for category_name in CATEGORIES
}

unknown_documents = []


for document in documents:
    data = document.to_dict()

    category_ref = data.get("category")

    if category_ref is None:
        unknown_documents.append(
            (document.id, "missing category")
        )
        continue

    matched_category = None

    for category_name, expected_ref in category_refs.items():
        if category_ref.path == expected_ref.path:
            matched_category = category_name
            break

    if matched_category is None:
        unknown_documents.append(
            (
                document.id,
                f"unknown category: {category_ref.path}"
            )
        )
        continue

    grouped[matched_category].append(document)


# ============================================================
# SORT DOCUMENTS
# ============================================================
#
# Firestore does not guarantee the order in which .stream()
# returns documents.
#
# Therefore we use the document ID to get a deterministic order.
#
# If you have another preferred ordering, we can change this.
# ============================================================

for category_name in grouped:
    grouped[category_name].sort(
        key=lambda document: document.id
    )


# ============================================================
# SHOW PLAN
# ============================================================

print("=" * 70)
print("MIGRATION PLAN")
print("=" * 70)

total_updates = 0

for category_name, category_documents in grouped.items():

    print(
        f"\n{category_name.upper()} "
        f"({len(category_documents)} documents)"
    )

    print("-" * 70)

    for order, document in enumerate(category_documents, start=1):

        data = document.to_dict()

        video_id = data.get("videoID", "<missing>")

        current_order = data.get("order")

        print(
            f"{order:3} | "
            f"{document.id} | "
            f"videoID={video_id} | "
            f"current order={current_order}"
        )

        if current_order != order:
            total_updates += 1


# ============================================================
# UNKNOWN DOCUMENTS
# ============================================================

if unknown_documents:

    print("\n" + "=" * 70)
    print("DOCUMENTS THAT WILL NOT BE MODIFIED")
    print("=" * 70)

    for document_id, reason in unknown_documents:
        print(
            f"{document_id} -> {reason}"
        )


# ============================================================
# DRY RUN
# ============================================================

print("\n" + "=" * 70)

if DRY_RUN:

    print("DRY RUN")
    print("=" * 70)

    print(
        f"\nNo changes were made to Firestore."
    )

    print(
        f"{total_updates} documents would receive an "
        f"order update."
    )

    print(
        "\nIf everything looks correct, change:"
    )

    print(
        "    DRY_RUN = False"
    )

    print(
        "\nand run the script again."
    )

    exit(0)


# ============================================================
# APPLY CHANGES
# ============================================================

print("APPLYING CHANGES")
print("=" * 70)

batch = db.batch()

operation_count = 0

for category_name, category_documents in grouped.items():

    for order, document in enumerate(
            category_documents,
            start=1
    ):

        current_order = document.to_dict().get("order")

        if current_order == order:
            continue

        batch.update(
            document.reference,
            {
                "order": order
            }
        )

        operation_count += 1

        # Firestore batches have a limit of 500 operations.
        if operation_count == 500:
            batch.commit()

            print(
                "Committed batch of 500 updates."
            )

            batch = db.batch()
            operation_count = 0


# Commit remaining operations

if operation_count > 0:
    batch.commit()

    print(
        f"Committed final batch of "
        f"{operation_count} updates."
    )


# ============================================================
# DONE
# ============================================================

print("\n" + "=" * 70)
print("MIGRATION COMPLETE")
print("=" * 70)

print(
    f"\nUpdated documents: {total_updates}"
)

print(
    "\nvideoID and category were not modified."
)