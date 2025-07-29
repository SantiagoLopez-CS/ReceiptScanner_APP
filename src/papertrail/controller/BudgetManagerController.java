package papertrail.controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import papertrail.model.Budget;
import papertrail.model.BudgetPeriod;
import papertrail.model.Category;
import papertrail.service.BudgetManager;

public class BudgetManagerController {
    @FXML private TextField titleField;
    @FXML private ComboBox<Category> categoryComboBox;
    @FXML private TextField limitField;
    @FXML private ComboBox<BudgetPeriod> periodComboBox;

    @FXML private VBox mainBudgetListVBox;
    @FXML private HBox budgetContentHBox;

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
        String title = titleField.getText();
        Category cat = categoryComboBox.getValue();
        double lim;
        try {
            lim = Double.parseDouble(limitField.getText());
        } catch (NumberFormatException ex) {
            showError("Please enter a valid number for 'Limit'.");
            return; // Stop execution
        }
        BudgetPeriod period = periodComboBox.getValue();
        if (period == null) {
            showError("Please select a time period.");
            return;
        }

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

        for (Budget budget : budgetManager.getAllBudgets()) {
            mainBudgetListVBox.getChildren().add(createBudgetRow(budget));
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

    // Separate method for creating a budget row
    private HBox createBudgetRow(Budget budget) {
        // Status label: "left" or "OVERSPENT"
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

        Label resetLabel = new Label("Last Reset: " + budget.getLastResetDate());
        resetLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: gray;");

        // Create and add Edit/Delete Buttons for each Budget
        Button editBtn = new Button("✏️");
        Button deleteBtn = new Button("🗑");

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
        budgetRow.setPadding(new Insets(5));
        budgetRow.setStyle("-fx-border-color: lightgray; -fx-border-width: 1;");
        budgetRow.setId(budget.getId());
        return budgetRow;
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.show();
    }
}
