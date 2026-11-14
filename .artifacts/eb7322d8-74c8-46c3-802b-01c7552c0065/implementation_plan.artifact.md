# Implementation Plan - Hero Animation for Book Covers

This plan outlines the steps to implement a shared element transition (hero animation) for book covers between the Home screen (list/grid view) and the Book Details screen.

## Proposed Changes

We will leverage the Jetpack Compose `SharedTransitionLayout` to coordinate animations between different screens in the `NavHost`.

### [Navigation & Scopes]

#### [MODIFY] [StartingPoint.kt](file:///C:/Users/Kuba/Desktop/bbb/Android/book_tracker2/app/src/main/java/com/example/book_tracker2/StartingPoint.kt)
- Pass `this` (which is `AnimatedVisibilityScope` inside `composable` blocks) to `HomeScreen` and `BookDetailsScreen`.

### [Home Screen]

#### [MODIFY] [HomeScreen.kt](file:///C:/Users/Kuba/Desktop/bbb/Android/book_tracker2/app/src/main/java/com/example/book_tracker2/features/home/HomeScreen.kt)
- Update `HomeScreen` to pass `animatedVisibilityScope` to `BookList`.

#### [MODIFY] [BookList.kt](file:///C:/Users/Kuba/Desktop/bbb/Android/book_tracker2/app/src/main/java/com/example/book_tracker2/features/home/BookList.kt)
- Update `BookList` to be an extension of `SharedTransitionScope` and take `AnimatedVisibilityScope`.
- Pass these scopes down to `BookTile` and `BookGridTile`.

#### [MODIFY] [BookTile.kt](file:///C:/Users/Kuba/Desktop/bbb/Android/book_tracker2/app/src/main/java/com/example/book_tracker2/features/home/BookTile.kt)
- Update `BookTile` to take `AnimatedVisibilityScope` as a parameter.
- Update the `sharedElement` modifier to use a unique key per book: `"cover-${book.id}"`.

#### [MODIFY] [BookGridTile.kt](file:///C:/Users/Kuba/Desktop/bbb/Android/book_tracker2/app/src/main/java/com/example/book_tracker2/features/home/BookGridTile.kt)
- Update `BookGridTile` to be an extension of `SharedTransitionScope` and take `AnimatedVisibilityScope`.
- Add the `sharedElement` modifier to the `AsyncImage` using the same unique key: `"cover-${book.id}"`.

### [Details Screen]

#### [MODIFY] [BookDetailsScreen.kt](file:///C:/Users/Kuba/Desktop/bbb/Android/book_tracker2/app/src/main/java/com/example/book_tracker2/features/details/BookDetailsScreen.kt)
- Update `BookDetailsScreen` to take `AnimatedVisibilityScope` as a parameter.
- Pass the scopes down to `BookDetailsContent`.
- Update `BookDetailsContent` to be an extension of `SharedTransitionScope` and take `AnimatedVisibilityScope`.
- Add the `sharedElement` modifier to the `AsyncImage` in the details view using the same unique key: `"cover-${book.id}"`.

## Verification Plan

### Automated Tests
- Build the project to ensure no compilation errors.

### Manual Verification
1. Run the app on an emulator or device.
2. Tap on a book in the list or grid view on the Home screen.
3. Observe the book cover animating smoothly to its position on the Details screen.
4. Go back and observe the animation returning to the list/grid.
