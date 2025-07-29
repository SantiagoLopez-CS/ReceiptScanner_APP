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
