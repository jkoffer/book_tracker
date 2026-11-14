# NavigationBar Implementation Plan

Implement a `NavigationBar` in the `StartingPoint` composable to allow navigation between Home and Settings screens. The `NavigationBar` will only be visible on these two screens.

## Proposed Changes

### [app]

#### [MODIFY] [StartingPoint.kt](file:///C:/Users/Kuba/Desktop/bbb/Android/book_tracker2/app/src/main/java/com/example/book_tracker2/StartingPoint.kt)
- Wrap `NavHost` in a `Scaffold`.
- Add a `bottomBar` to the `Scaffold` using `NavigationBar`.
- Implement visibility logic for the `NavigationBar` based on the current destination.
- Add the `settings` destination to the `NavHost`.

#### [MODIFY] [SettingsScreen.kt](file:///C:/Users/Kuba/Desktop/bbb/Android/book_tracker2/app/src/main/java/com/example/book_tracker2/features/settings/SettingsScreen.kt)
- Add a simple placeholder UI to `SettingsScreen` so it's distinguishable.

## Verification Plan

### Automated Tests
- No automated tests planned as this is primarily a UI layout change.

### Manual Verification
1. Deploy the app.
2. Verify that the `NavigationBar` is visible on the Home screen.
3. Tap on the "Settings" icon in the `NavigationBar` and verify navigation to the Settings screen.
4. Verify that the `NavigationBar` remains visible on the Settings screen.
5. Navigate to other screens (e.g., Book Details, Add Book) and verify that the `NavigationBar` is hidden.
