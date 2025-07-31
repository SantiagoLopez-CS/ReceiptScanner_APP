package papertrail.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainMenuController {

    @FXML private Button taskBtn;
    @FXML private Button receiptBtn;
    @FXML private Button budgetBtn;

    // Callbacks injected from main app
    private Runnable onTaskPressed;
    private Runnable onReceiptPressed;
    private Runnable onBudgetPressed;

    // Buttons for switching Windows
    public void initialize() {
        // Button Actions
        taskBtn.setOnAction(actionEvent -> {
            if (onTaskPressed != null) {
                onTaskPressed.run();
            }
        });
        receiptBtn.setOnAction(actionEvent -> {
            if (onReceiptPressed != null) {
                onReceiptPressed.run();
            }
        });
        budgetBtn.setOnAction(actionEvent -> {
            if (onBudgetPressed != null) {
                onBudgetPressed.run();
            }
        });
    }

    public void setOnTaskPressed(Runnable action) {
        this.onTaskPressed = action;
    }
    public void setOnReceiptPressed(Runnable action) {
        this.onReceiptPressed = action;
    }
    public void setOnBudgetPressed(Runnable action) {
        this.onBudgetPressed = action;
    }

}
