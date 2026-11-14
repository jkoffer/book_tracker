# Refactor Image Saving Logic

This plan addresses the relocation of `saveImageToInternalStorage` from the UI state to a service layer and coordinates its execution within the `ViewModel`.

## User Review Required

> [!IMPORTANT]
> The image saving logic currently uses a hardcoded filename `image.jpg`. I will update it to use a unique filename (e.g., based on the book's UUID) to prevent multiple books from overwriting each other's covers.

## Proposed Changes

### Data Layer

#### [NEW] [ImageManager.kt](file:///C:/Users/Kuba/Desktop/bbb/Android/book_tracker2/app/src/main/java/com/example/book_tracker2/data/local/storage/ImageManager.kt)
Create a service to handle saving images from various sources (Local URI, Remote URL) to the app's internal storage.

#### [NEW] [StorageModule.kt](file:///C:/Users/Kuba/Desktop/bbb/Android/book_tracker2/app/src/main/java/com/example/book_tracker2/di/StorageModule.kt)
A Hilt module to provide the `ImageManager` implementation.

---

### UI Layer / Feature Layer

#### [MODIFY] [AddBookFormState.kt](file:///C:/Users/Kuba/Desktop/bbb/Android/book_tracker2/app/src/main/java/com/example/book_tracker2/features/add_book/AddBookFormState.kt)
- Remove `saveImageToInternalStorage` function.
- Update `toBookWithTags()` to accept an optional `coverPath: String?` to override the `image` field when mapping to the entity.

#### [MODIFY] [AddBookFormViewModel.kt](file:///C:/Users/Kuba/Desktop/bbb/Android/book_tracker2/app/src/main/java/com/example/book_tracker2/features/add_book/AddBookFormViewModel.kt)
- Inject `ImageManager`.
- Update `submitForm` to:
    1. Check if `state.image` is a local URI or remote URL.
    2. Save the image using `ImageManager` if necessary.
    3. Pass the resulting path to `state.toBookWithTags(coverPath)`.
    4. Save the book to the repository.

## Verification Plan

### Automated Tests
- I will create a unit test for `ImageManager` (if possible given `Context` dependency, otherwise manual verification).
- Verify that `AddBookFormViewModel` correctly calls `ImageManager`.

### Manual Verification
1. Open the "Add Book" screen.
2. Select an image from the gallery (Local URI).
3. Enter book details and submit.
4. Verify the book is saved and the image is displayed (meaning it was successfully saved to internal storage and the path was stored in the DB).
5. Repeat with an ISBN lookup that fetches a remote image URL.
