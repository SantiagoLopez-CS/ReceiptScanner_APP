# 📍 PaperTrail Development Roadmap

This file outlines the planned versions and feature milestones for the PaperTrail app from alpha to stable release.

---

## ✅ CURRENT VERSION
- **v0.12.0-alpha**: Full UX polish across all views with tooltips, validation, filtering, empty list labels, and CSS styles

---

## 🔜 UPCOMING ALPHA RELEASES

### 🔹 v0.13.0-alpha — Search & Sorting
- Add ability to sort tasks by due date or category
- Add ability to sort receipts by date, amount, or category
- Add ability to sort budgets by title or remaining budget
- Add a settings menu (stub) for future sort/display preferences

### 🔹 v0.14.0-alpha — Data Persistence: Budget & Task
- Implement file-based storage and loading of `Budget` objects
- Implement file-based storage and loading of `Task` objects
- Ensure UUIDs, limits, dates, and categories persist correctly

### 🔹 v0.15.0-alpha — Data Persistence: Receipt
- Add persistent storage and loading for `Receipt` objects
- Update budget references on receipt load (using `budgetId`)
- Ensure data integrity across budget → receipt linkage

### 🔹 v0.16.0-alpha — Refactor Storage Logic
- Create `storage/` package for `BudgetStorage`, `ReceiptStorage`, `TaskStorage`
- Modularize save/load logic
- Make storage format pluggable (JSON, CSV, etc.)

### 🔹 v0.17.0-alpha — OCR Integration
- Add basic support for uploading receipt images
- Integrate with OCR API (e.g., Google Vision, Tesseract)
- Extract amount, date, and category hints from receipt image

### 🔹 v0.18.0-alpha — UI Styling Pass (CSS)
- Create global stylesheet and apply to all views
- Style buttons, error labels, and list items
- Add dark mode toggle in settings (optional)

### 🔹 v0.19.0-alpha — Summary & Analytics View
- Add new Dashboard/Summary page:
  - Show total monthly spending
  - Show budget progress (spent vs limit)
  - Show pie chart of categories (optional)
- Export summary data to file

---

## 🚧 BETA PHASE (Feature-Frozen, Testing + Polish)

### 🔹 v0.20.0-beta — Integration Testing & Error Handling
- Fix edge cases in data parsing/loading
- Add alerts for corrupted/missing files
- Improve UX during first-time setup

### 🔹 v0.21.0-beta — UX Polish
- Smooth animations and transitions
- Add undo/redo for task and receipt deletion
- Review and standardize color palette and font sizes

### 🔹 v0.22.0-beta — Final QA
- Freeze feature set
- Fix all known issues and prepare documentation
- Final data format export check

---

## ✅ v1.0.0 — First Stable Release
- All major features implemented
- Polished, styled, and fully functional
- Ready for public distribution

---

## 🧭 Post-1.0 Versions (Planned Features)

### 🔹 v1.1.0 — Post-1.0 UX Expansion
- Add custom display labels to `Category` enum (e.g., "HOUSING" → "Housing")
- Allow user-defined custom `Category` entries
- Support `BudgetPeriod` expansion (e.g., "Biweekly", "Quarterly")
- Enable category-based sorting (e.g., essential categories shown first)
- Assign default colors to each `Category` for future chart visualizations

### 🔹 v1.2.0 — Advanced Budget Features
- Track historical budget performance across past reset periods
- Add optional notes/description field for each budget
- Allow users to manually reset budgets or customize reset intervals (e.g., custom day of week/month)

---

*This roadmap will evolve with user feedback and development needs.*
