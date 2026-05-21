package papertrail.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import papertrail.service.BudgetManager;
import papertrail.service.ReceiptManager;
import papertrail.service.TaskManager;
import papertrail.storage.BudgetStorage;
import papertrail.storage.ReceiptStorage;
import papertrail.storage.TaskStorage;

import java.util.ArrayList;
import java.util.Optional;

public class SettingsController {
    private BudgetManager budgetManager;
    private ReceiptManager receiptManager;
    private TaskManager taskManager;
    private Runnable backToMenuCallback;
    private Runnable dataClearedCallback;

    public void setManagers(BudgetManager budgetManager, ReceiptManager receiptManager, TaskManager taskManager) {
        this.budgetManager = budgetManager;
        this.receiptManager = receiptManager;
        this.taskManager = taskManager;
    }

    public void setBackToMenuCallback(Runnable callback) {
        this.backToMenuCallback = callback;
    }

    public void setDataClearedCallback(Runnable callback) {
        this.dataClearedCallback = callback;
    }

    @FXML
    private void handleClearData() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Clear Local Data");
        confirmation.setHeaderText("Clear all saved budgets, receipts, and tasks?");
        confirmation.setContentText("This resets the local JSON data files used by PaperTrail.");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (!result.isPresent() || result.get() != ButtonType.OK) {
            return;
        }

        budgetManager.setBudgets(new ArrayList<>());
        receiptManager.setReceipts(new ArrayList<>());
        taskManager.setTasks(new ArrayList<>());

        BudgetStorage.saveBudgets(budgetManager.getAllBudgets());
        ReceiptStorage.saveReceipts(receiptManager.getAllReceipts());
        TaskStorage.saveTasks(taskManager.getAllTasks());

        if (dataClearedCallback != null) {
            dataClearedCallback.run();
        }

        Alert success = new Alert(Alert.AlertType.INFORMATION);
        success.setTitle("Data Cleared");
        success.setHeaderText("Local data has been cleared.");
        success.setContentText("Budgets, receipts, and tasks are now empty.");
        success.showAndWait();
    }

    @FXML
    private void handleBackToMenu() {
        if (backToMenuCallback != null) {
            backToMenuCallback.run();
        }
    }
}
