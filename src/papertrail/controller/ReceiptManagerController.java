package papertrail.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import papertrail.model.Budget;
import papertrail.model.Category;
import papertrail.model.Receipt;
import papertrail.service.BudgetManager;
import papertrail.service.ReceiptManager;

import java.time.LocalDate;

public class ReceiptManagerController {
    private ReceiptManager receiptManager;
    private BudgetManager budgetManager;
    private Scene budgetScene;
    private Stage primaryStage;
    private Runnable backToMenuCallback;

    private BudgetManagerController budgetManagerController;

    // UI elements injected from FXML
    @FXML private TextField storeNameField;
    @FXML private ComboBox<Category> categoryComboBox;
    @FXML private DatePicker dayOfPurchase;
    @FXML private TextField spentField;
    @FXML private VBox receiptsListVBox;
    // Filter fields
    @FXML TextField filterStoreField;
    @FXML ComboBox<Category> filterCategoryBox;
    @FXML DatePicker filterDatePicker;

    @FXML
    public void initialize() {
        categoryComboBox.getItems().setAll(Category.values()); // Populate category dropdown
        filterCategoryBox.getItems().setAll(Category.values()); // Populate category filter dropdown
        // Add listeners to trigger filtering when inputs change
        filterStoreField.textProperty().addListener((observableValue, oldValue, newValue) -> refreshReceipts());
        filterCategoryBox.valueProperty().addListener((observableValue, oldValue, newValue) -> refreshReceipts());
        filterDatePicker.valueProperty().addListener((observableValue, oldValue, newValue) -> refreshReceipts());
    }

    /*
     * Called by the app to inject backend managers.
     */
    public void setManagers(ReceiptManager receiptManager, BudgetManager budgetManager) {
        this.receiptManager = receiptManager;
        this.budgetManager = budgetManager;

        refreshReceipts(); // Initial load of tasks

        // 👇 Set default date to today
        dayOfPurchase.setValue(LocalDate.now());
    }

    /*
     * Sets a callback to return to the main menu.
     * This avoids tight coupling to the primary stage.
     */
    public void setBackToMenuCallback(Runnable callback) {
        this.backToMenuCallback = callback;
    }

    public void setBudgetSceneContext(BudgetManagerController budgetManagerController, Scene scene, Stage stage) {
        this.budgetManagerController = budgetManagerController;
        this.budgetScene = scene;
        this.primaryStage = stage;
    }

    @FXML
    private void handleAddReceipt() {
        String store = storeNameField.getText().trim();
        Category cat = categoryComboBox.getValue();
        LocalDate day = dayOfPurchase.getValue();
        // Validation
        if (store.isEmpty() || cat == null || day == null) {
            showAlert(Alert.AlertType.ERROR, "Please complete all fields.");
            return;
        }

        double spent;
        try {
            spent = Double.parseDouble(spentField.getText());
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Please enter a valid number for 'Amount Spent'.");
            return; // Stop execution
        }
        // Create and Store
        String budgetId = findMatchingBudgetId(cat);
        if (budgetId == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No matching budget found for selected category.");
            alert.show();
            return;
        }
        Receipt newReceipt = new Receipt(store, cat, day, spent, budgetId);
        receiptManager.addReceipt(newReceipt);

        storeNameField.clear();
        categoryComboBox.setValue(null);
        dayOfPurchase.setValue(null);
        spentField.clear();

        refreshReceipts();
    }
    /*
     * Triggered when user clicks "Back to Main Menu".
     */
    @FXML
    private void handleBackToMenu() {
        if (backToMenuCallback != null) {
            backToMenuCallback.run();
        }
    }

    public void refreshReceipts() {
        receiptsListVBox.getChildren().clear();

        // Filter Fields
        String storeFilter = filterStoreField.getText() != null ? filterStoreField.getText().trim().toLowerCase() : "";
        Category categoryFilter = filterCategoryBox.getValue();
        LocalDate dateFilter = filterDatePicker.getValue();

        for (Receipt receipt : receiptManager.getAllReceipts()) {
            if (receipt == null) continue; // Safeguard

            // Apply store name filter
            if (!storeFilter.isEmpty() && !receipt.getStore().toLowerCase().contains(storeFilter)) {
                continue;
            }
            // Apply category filter
            if (categoryFilter != null && receipt.getCategory() != categoryFilter) {
                continue;
            }
            // Apply date filter
            if (dateFilter != null && !receipt.getDayOfPurchase().equals(dateFilter)) {
                continue;
            }

            // Format and display matching receipt
            String formattedAmount = String.format("$%.2f", receipt.getAmountSpent());
            Label receiptLabel = new Label(
                    receipt.getStore() + " | " +
                            receipt.getCategory() + " | " +
                            receipt.getDayOfPurchase() + " | " +
                            formattedAmount + " spent"
            );
            HBox receiptRow = getHBox(receipt, receiptLabel);
            receiptsListVBox.getChildren().add(receiptRow);
        }
    }

    private HBox getHBox(Receipt receipt, Label receiptLabel) {
        Button deleteBtn = new Button("🗑");
        deleteBtn.setOnAction(actionEvent -> {
            // Implement removeReceipt boolean return
            boolean removed = receiptManager.removeReceipt(receipt.getId());
            if (!removed) {
                showAlert(Alert.AlertType.ERROR, "Failed to delete receipt.");
            } else {
                refreshReceipts();

                // Highlight the updated budget row
                if (budgetManagerController != null && receipt.getBudgetId() != null) {
                    budgetManagerController.refreshBudgets(); // Ensure resources.view is up-to-date
                    budgetManagerController.highlightBudget(receipt.getBudgetId());
                    if (budgetScene != null) {
                        primaryStage.setScene(budgetScene);
                    }
                }
            }
        });

        HBox receiptRow = new HBox(10, receiptLabel, deleteBtn);
        receiptRow.setPadding(new Insets(5));
        receiptRow.setStyle("-fx-border-color: lightgray; -fx-border-width: 1;");
        return receiptRow;
    }
    private String findMatchingBudgetId(Category category) {
        for (Budget budget : budgetManager.getAllBudgets()) {
            if (budget.getCategory() == category) {
                return budget.getId();
            }
        }
        return null;
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.show();
    }

    @FXML
    private void handleClearFilters() {
        filterStoreField.clear();
        filterCategoryBox.setValue(null);
        filterDatePicker.setValue(null);
        refreshReceipts();
    }
}
