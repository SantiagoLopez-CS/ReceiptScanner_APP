# Changelog

## [v0.9.0-alpha] - 2025-07-28

### Added
- Task filtering by title and category
- Clear filter button in TaskManager UI
- Auto-assignment of today’s date for receipts
- Dialog popup to view full task details
- Navigation callback system for all views

### Changed
- Refactored all hardcoded JavaFX views to FXML + controller structure
- Improved budget auto-creation logic based on task category
- Receipt deletion now highlights corresponding budget

### Removed
- Obsolete TaskManagerView.java, ReceiptManagerView.java, and BudgetManagerView.java

### Known Issues
- No CSS/styling applied yet
- No save/load/export feature
- No persistent storage (runtime-only data)

## [v0.10.0-alpha] - 2025-07-28

### Added
- Search field for filtering receipts by store name
- Category filter ComboBox in ReceiptManager view
- Date filter using DatePicker
- Clear Filter button to reset receipt filters

### Changed
- Receipt list now updates live based on active filters
- Improved user feedback and validation in receipt input fields

### Known Issues
- BudgetManager still lacks filtering/search options
- No CSS/styling applied yet
- No save/load/export feature

## [v0.11.0-alpha] - 2025-07-28

### Added
- Search field to filter budgets by title
- Category ComboBox to filter budgets by category
- Clear Filter button in BudgetManager UI to reset all filters

### Changed
- Budget list now dynamically updates based on active filters
- Improved input validation and edit mode handling for budgets

### Known Issues
- Still no CSS/styling applied
- No save/load/export feature
- No persistent storage (runtime-only data)

## [v0.11.1-alpha] - 2025-07-29

### Fixed
- Corrected duplicate `fx:id` attributes in `BudgetManagerView.fxml`
- Restored Add Budget button functionality
- Cleaned up validation bindings for budget form fields

## [v0.11.2-alpha] - 2025-07-29

### Added
- Error label + validation logic for all views (Task, Receipt, Budget)
- Invalid fields now show red borders and error messages after clicking "Add"

### Fixed
- Receipt form now properly validates and adds receipts
- Budget form no longer shows errors prematurely (only after Add is clicked)

### Consistency
- All forms now use the same UX pattern for input validation and feedback

## [v0.11.3-alpha] - 2025-07-29

### Added
- Tooltips for all input fields, buttons, filters, and controls in all views (Budget, Receipt, & Task)
- Tooltip support for dynamically created Edit and Delete buttons in all controller classes (Budget, Receipt, and Task)

### UX Improvements
- Users now receive helpful guidance when hovering over form fields and controls across all views

### Consistency
- All three manager views (Budget, Receipt, Task) now follow a unified tooltip standard

### Known Issues
- No CSS/styling applied yet
- No save/load/export feature
- No persistent storage (runtime-only data)

## [v0.12.0-alpha] - 2025-07-31

### Added
- TaskManagerView: 'Show Completed Tasks' toggle with filter logic
- TaskManagerView: Tooltip descriptions for all form fields and buttons
- TaskManagerView: Search bar + category filter with live updates
- TaskManagerView: Validation logic and error labels for all required fields
- TaskManagerView: "No tasks to show" label when list is empty or filtered out
- TaskManagerView: CSS styling via `task-manager.css` for layout, buttons, errors

- ReceiptManagerView: Tooltips for all form fields and buttons
- ReceiptManagerView: Search bar + category filter with live updates
- ReceiptManagerView: Validation logic and error labels for all required fields
- ReceiptManagerView: "No receipts to show" label when list is empty or filtered out
- ReceiptManagerView: CSS styling via `receipt-manager.css`
- ReceiptManagerController: Refreshes BudgetView after deletion

- BudgetManagerView: Tooltips for all form fields and buttons
- BudgetManagerView: Search bar + category filter with live updates
- BudgetManagerView: Validation logic and error labels for all required fields
- BudgetManagerView: "No budgets to show" label when list is empty or filtered out
- BudgetManagerView: CSS styling via `budget-manager.css`

- MainMenuView: CSS button styling via `main-menu.css`, with custom styles per button

### Changed
- PaperTrailApp.java: Switched all manager views to use FXML layout and controllers
- PaperTrailApp.java: Injects test data for Task, Receipt, and Budget for UX verification

