package papertrail;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import papertrail.controller.BudgetManagerController;
import papertrail.controller.ReceiptManagerController;
import papertrail.controller.TaskManagerController;
import papertrail.model.*;
import papertrail.service.BudgetManager;
import papertrail.service.ReceiptManager;
import papertrail.service.TaskManager;

import java.io.IOException;
import java.time.LocalDate;

public class PaperTrailApp extends Application{

    // Declare scenes to allow switching
    private Scene mainMenuScene;
    private Scene taskManagerScene;
    private Scene budgetManagerScene;
    private Scene receiptManagerScene;
    // Declare Controller for BudgetManager
    private BudgetManagerController budgetController;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("📋 PaperTrail Dashboard"); // Set Title of Stage

        // Create TaskManager backend to pass into resources.view
        TaskManager taskManager = new TaskManager();
        BudgetManager budgetManager = new BudgetManager();
        ReceiptManager receiptManager = new ReceiptManager(budgetManager);
        //TaskUI Tests
        taskManager.addTask(new Task("Finish budget report", "Complete the Q3 budget summary and send to manager.", Category.PERSONAL, LocalDate.now().plusDays(2), 0.00, false));
        taskManager.addTask(new Task("Upload receipt photos", "Scan and upload all grocery and gas receipts for July.", Category.FOOD, LocalDate.now().plusDays(1), 120.50, false));
        taskManager.addTask(new Task("Pay rent", "Transfer payment to landlord's account.", Category.HOUSING, LocalDate.now().plusDays(5), 850.00, false));
        taskManager.addTask(new Task("Review subscriptions", "Cancel unused entertainment subscriptions.", Category.ENTERTAINMENT, LocalDate.now().plusDays(3), 15.99, false));
        // BudgetUI Test
        budgetManager.addBudget(new Budget("Starter Budget", Category.FOOD, 100.00, BudgetPeriod.WEEKLY));
        budgetManager.addBudget(new Budget("Gas Budget", Category.TRANSPORTATION, 50.00, BudgetPeriod.MONTHLY));
        budgetManager.addBudget(new Budget("Fun Budget", Category.ENTERTAINMENT, 75.00, BudgetPeriod.MONTHLY));
        // ReceiptUI Tests
        receiptManager.addReceipt(new Receipt("Walmart", Category.FOOD, LocalDate.now().minusDays(1), 24.50, budgetManager.getAllBudgets().get(0).getId()));
        receiptManager.addReceipt(new Receipt("Shell", Category.TRANSPORTATION, LocalDate.now().minusDays(2), 35.00, budgetManager.getAllBudgets().get(1).getId()));
        receiptManager.addReceipt(new Receipt("Netflix", Category.ENTERTAINMENT, LocalDate.now().minusDays(3), 12.99, budgetManager.getAllBudgets().get(2).getId()));
        receiptManager.addReceipt(new Receipt("Clash", Category.ENTERTAINMENT, LocalDate.now().minusDays(5), 5.99, budgetManager.getAllBudgets().get(2).getId()));


        // MAIN MENU LAYOUT
        VBox mainMenuLayout = new VBox(15); // 15px space between elements
        mainMenuLayout.setStyle("-fx-padding: 20; -fx-alignment: center;"); // Styling for VBox elements

        // Buttons for each feature
        Button tasksBtn = new Button("Task Manager");
        Button receiptsBtn = new Button("Receipt Manager ");
        Button budgetsBtn = new Button("Budget Manager");
        // Add buttons to Main scene
        mainMenuLayout.getChildren().addAll(tasksBtn, receiptsBtn, budgetsBtn);

        // MAIN MENU SCENE
        mainMenuScene = new Scene(mainMenuLayout, 800, 600);
        final Scene finalMainMenuScene = mainMenuScene;

        // Load FXML-based TaskManager scene
        try {
            FXMLLoader taskLoader = new FXMLLoader(getClass().getResource("/resources/view/TaskManagerView.fxml"));
            VBox taskRoot = taskLoader.load(); // The root element of your FXML is VBox
            // Get controller and inject logic
            TaskManagerController taskController = taskLoader.getController();
            taskController.setManagers(taskManager, budgetManager);
            taskController.setBackToMenuCallback(() -> primaryStage.setScene(finalMainMenuScene));
            // Create scene
            taskManagerScene = new Scene(taskRoot, 800, 600);
        } catch (IOException e) {
            e.printStackTrace();
            return; // Or show an alert
        }


        // BudgetManager Scene
        // BudgetManager Scene (FXML-based)
        FXMLLoader budgetLoader = new FXMLLoader(getClass().getResource("/resources/view/BudgetManagerView.fxml"));
        VBox budgetRoot = null;

        try {
            budgetRoot = budgetLoader.load();
            budgetController = budgetLoader.getController();
            budgetController.setManagers(budgetManager);
            budgetController.setBackToMenuCallback(() -> primaryStage.setScene(finalMainMenuScene));
            budgetManagerScene = new Scene(budgetRoot, 800, 600);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }


        // Receipt Manager Page
        try {
            FXMLLoader receiptLoader = new FXMLLoader(getClass().getResource("/resources/view/ReceiptManagerView.fxml"));
            VBox receiptRoot = receiptLoader.load();
            ReceiptManagerController receiptController = receiptLoader.getController();

            // Inject logic
            receiptController.setManagers(receiptManager, budgetManager);
            receiptController.setBackToMenuCallback(() -> primaryStage.setScene(finalMainMenuScene));
            receiptController.setBudgetSceneContext(budgetController, budgetManagerScene, primaryStage);

            receiptManagerScene = new Scene(receiptRoot, 800, 600);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }


        // Button Actions
        tasksBtn.setOnAction(actionEvent -> primaryStage.setScene(taskManagerScene));
        budgetsBtn.setOnAction(actionEvent -> {
            budgetController.refreshBudgets();
            primaryStage.setScene(budgetManagerScene);
        });
        receiptsBtn.setOnAction(actionEvent -> primaryStage.setScene(receiptManagerScene));

        // Set the Scene and show(make visible)
        primaryStage.setScene(mainMenuScene);
        primaryStage.show();
    }
    // Launch the App
    public static void main(String[] args) {
        launch(args);
    }
}

