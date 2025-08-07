package papertrail.controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import papertrail.model.Budget;
import papertrail.model.BudgetPeriod;
import papertrail.model.Category;
import papertrail.service.BudgetManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BudgetManagerController {
    @FXML private TextField titleField;
    @FXML private ComboBox<Category> categoryComboBox;
    @FXML private TextField limitField;
    @FXML private ComboBox<BudgetPeriod> periodComboBox;

    @FXML private VBox mainBudgetListVBox;
    @FXML private HBox budgetContentHBox;

    @FXML private TextField searchTitleField;
    @FXML private ComboBox<Category> filterCategoryBox;
    @FXML private Button clearFiltersBtn;
    // Fields for error labels and addButton
    @FXML private Label titleErrorLabel;
    @FXML private Label categoryErrorLabel;
    @FXML private Label limitErrorLabel;
    @FXML private Label periodErrorLabel;
    @FXML private Button addBudgetButton;

    @FXML private Label emptyBudgetLabel;
    // Sorting Box
    @FXML private ComboBox<String> sortComboBox;
    private BudgetManager budgetManager;
    private Runnable backToMenuCallback;
    private Budget editingBudget = null;

    // Call by PaperTrailApp after loading FXML
    public void setManagers(BudgetManager budgetManager) {
        this.budgetManager = budgetManager;

        // Populate combo boxes with enums
        categoryComboBox.getItems().addAll(Category.values());
        periodComboBox.getItems().addAll(BudgetPeriod.values());

        refreshBudgets(); // Initial Load
        initializeFilters();
    }

    /*
     * Sets a callback to return to the main menu.
     * This avoids tight coupling to the primary stage.
     */
    public void setBackToMenuCallback(Runnable callback) {
        this.backToMenuCallback = callback;
    }

    @FXML
    private void handleBackToMenu() {
        if (backToMenuCallback != null) {
            backToMenuCallback.run();
        }
    }

    @FXML
    private void handleAddBudget() {
        titleErrorLabel.setVisible(false);
        categoryErrorLabel.setVisible(false);
        limitErrorLabel.setVisible(false);
        periodErrorLabel.setVisible(false);

        titleField.setStyle(null);
        categoryComboBox.setStyle(null);
        limitField.setStyle(null);
        periodComboBox.setStyle(null);

        String title = titleField.getText();
        Category cat = categoryComboBox.getValue();
        String limitStr = limitField.getText().trim();
        BudgetPeriod period = periodComboBox.getValue();

        boolean valid = true;

        // Action if fields are empty when add button is clicked
        if (title.isEmpty()) {
            titleErrorLabel.setVisible(true);
            valid = false;
        }
        if (cat == null) {
            categoryErrorLabel.setVisible(true);
            valid = false;
        }
        double lim = 0.00;
        try {
            lim = Double.parseDouble(limitStr);
            if (lim < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException nfe) {
            limitErrorLabel.setVisible(true);
            valid = false;
        }
        if (period == null) {
            periodErrorLabel.setVisible(true);
            valid = false;
        }

        if (!valid) return;


        // ✅ If valid, proceed with creation or update
        if (editingBudget != null) {
            // The budget exists, update values
            editingBudget.setTitle(title);
            editingBudget.setLimit(lim);
            editingBudget.setPeriod(period);
            budgetManager.addBudget(editingBudget);
            editingBudget = null; // Reset edit mode
        } else {
            // Creating a new budget
            Budget newBudget = new Budget(title, cat, lim, period);
            budgetManager.addBudget(newBudget);
        }

        refreshBudgets();
        titleField.clear();
        categoryComboBox.setValue(null);
        limitField.clear();
        periodComboBox.setValue(null);
    }
    // Refresh the visible list of budget rows
    public void refreshBudgets() {
        budgetManager.resetAllBudgetsIfNeeded(); // Only reset once per resources.view update
        mainBudgetListVBox.getChildren().clear();
        emptyBudgetLabel.setVisible(false);

        String titleFilter = searchTitleField.getText() != null
                ? searchTitleField.getText().toLowerCase().trim() : "";
        Category categoryFilter = filterCategoryBox.getValue();

        boolean anyMatch = false;

        List<Budget> sorted = new ArrayList<>(budgetManager.getAllBudgets());

        String sortOption = sortComboBox.getValue();
        if ("Title (A-Z)".equals(sortOption)) {
            sorted.sort(Comparator.comparing(budget -> budget.getTitle().toLowerCase()));
        } else if ("Limit (Lowest First)".equals(sortOption)) {
            sorted.sort(Comparator.comparingDouble(Budget::getLimit));
        } else if ("Limit (Highest First)".equals(sortOption)) {
            sorted.sort(Comparator.comparingDouble(Budget::getLimit).reversed());
        } else if ("Reset Period (A-Z)".equals(sortOption)) {
            sorted.sort(Comparator.comparing(budget -> budget.getPeriod().name()));
        }

        for (Budget budget : sorted) {
            boolean matchesTitle = budget.getTitle().toLowerCase().contains(titleFilter);
            boolean matchesCategory = (categoryFilter == null || budget.getCategory() == categoryFilter);

            if (matchesTitle && matchesCategory) {
                mainBudgetListVBox.getChildren().add(createBudgetRow(budget));
                anyMatch = true;
            }
        }
        // Show Label if no budgets are created
        if (!anyMatch) {
            emptyBudgetLabel.setVisible(true);
        }
    }

    // Add highlight to budget after receipt is deleted
    public void highlightBudget(String budgetId) {
        for (javafx.scene.Node node : mainBudgetListVBox.getChildren()) {
            if (node instanceof HBox row && budgetId.equals(row.getId())) {
                String originalStyle = row.getStyle();
                row.setStyle(originalStyle + "; -fx-background-color: yellow;");

                // Revert after a delay
                PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(1.5));
                pause.setOnFinished(e -> row.setStyle(originalStyle));
                pause.play();
                break;
            }
        }
    }

    private void showBudgetDetailsDialog(Budget budget) {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Budget Details");
        dialog.setHeaderText(budget.getTitle());

        String content = String.format(
                "Category: %s%nlimit: $%.2f%nSpent: $%.2f%nRemaining: $%.2f%nPeriod: %s%nLast Reset: %s",
                budget.getCategory(),
                budget.getLimit(),
                budget.getSpent(),
                budget.getRemaining(),
                budget.getPeriod().name(),
                budget.getLastResetDate()
        );

        dialog.setContentText(content);
        dialog.showAndWait();
    }

    // Separate method for creating a budget row
    private HBox createBudgetRow(Budget budget) {
        // Status label: "left" or "OVERSPENT"
        Label budgetLabel = getBudgetLabel(budget);

        Label resetLabel = new Label("Last Reset: " + budget.getLastResetDate());
        resetLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: gray;");

        // Create and add Edit/Delete Buttons for each Budget
        Button editBtn = new Button("✏️");
        Tooltip editTooltip = new Tooltip("Edit this budget's details");
        Tooltip.install(editBtn, editTooltip);
        editBtn.setTooltip(editTooltip);
        editBtn.getStyleClass().add("edit-button");

        Button deleteBtn = new Button("🗑");
        Tooltip deleteTooltip = new Tooltip("Remove this budget permanently");
        Tooltip.install(deleteBtn, deleteTooltip);
        deleteBtn.setTooltip(deleteTooltip);
        deleteBtn.getStyleClass().add("delete-button");

        deleteBtn.setOnAction(actionEvent -> {
            budgetManager.removeBudget(budget);
            refreshBudgets();
        });

        editBtn.setOnAction(actionEvent -> {
            // Populate input fields with the current Budget's values
            titleField.setText(budget.getTitle());
            categoryComboBox.setValue(budget.getCategory());
            limitField.setText(String.valueOf(budget.getLimit()));
            periodComboBox.setValue(budget.getPeriod());

            budgetManager.removeBudget(budget);
            editingBudget = budget;
        });

        VBox budgetInfoBox = new VBox(2, budgetLabel, resetLabel);
        HBox budgetRow = new HBox(10, budgetInfoBox, editBtn, deleteBtn);
        budgetRow.setOnMouseClicked(event -> {
            showBudgetDetailsDialog(budget);
        });
        budgetRow.setPadding(new Insets(5));
        budgetRow.getStyleClass().add("budget-row");
        budgetRow.setId(budget.getId());
        return budgetRow;
    }

    // Extracted Method for better readability
    private Label getBudgetLabel(Budget budget) {
        String status = budget.isOverspent() ? "OVERSPENT" : "left";
        String remainingStr = String.format("%.2f", budget.getRemaining());

        Label budgetLabel = new Label(
                budget.getTitle() + " | " +
                        budget.getCategory() + " | $" +
                        String.format("%.2f", budget.getLimit()) + " limit | $" +
                        String.format("%.2f", budget.getSpent()) + " spent | $" +
                        remainingStr + " " + status + " | " +
                        budget.getPeriod().name().toLowerCase().replace("_", " ") + " budget"
        );
        // Red color if overspent, going to change to progress bars in the future
        if (budget.getRemaining() < 0) {
            budgetLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        }
        return budgetLabel;
    }

    /*
    * Method for filters initialization
    */
    private void initializeFilters() {
        filterCategoryBox.getItems().add(null); // allow "no filter"
        filterCategoryBox.getItems().addAll(Category.values());

        searchTitleField.textProperty().addListener((obs, oldVal, newVal) -> refreshBudgets());
        filterCategoryBox.valueProperty().addListener((obs, oldVal, newVal) -> refreshBudgets());

        // Initialize Sorting
        sortComboBox.getItems().clear(); // Prevent duplicates
        sortComboBox.getItems().addAll(
                "Title (A-Z)",
                "Limit (Lowest First)",
                "Limit (Highest First)",
                "Reset Period (A-Z)"
        );
        sortComboBox.valueProperty().addListener((obs, oldVal, newVal) -> refreshBudgets());
    }

    /*
    * Handle clear filters method
    */
    @FXML
    private void handleClearFilters() {
        searchTitleField.clear();
        filterCategoryBox.setValue(null);
    }
}
