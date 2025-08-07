package papertrail.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class MainMenuController {

    @FXML private Button taskBtn;
    @FXML private Button receiptBtn;
    @FXML private Button budgetBtn;
    @FXML private Button settingsBtn;

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
        settingsBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Settings (Coming Soon)");
            alert.setHeaderText("This feature is not available yet.");
            alert.setContentText("In a future update, you'll be able to customize sorting, themes, and display preferences.");
            alert.showAndWait();
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
