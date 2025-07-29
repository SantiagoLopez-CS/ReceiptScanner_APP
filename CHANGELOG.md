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

