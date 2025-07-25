package papertrail;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import papertrail.model.*;
import papertrail.service.BudgetManager;
import papertrail.service.ReceiptManager;
import papertrail.service.TaskManager;
import papertrail.view.BudgetManagerView;
import papertrail.view.ReceiptManagerView;
import papertrail.view.TaskManagerView;

import java.time.LocalDate;

public class PaperTrailApp extends Application{

    // Declare scenes to allow switching
    private Scene mainMenuScene;
    private Scene taskManagerScene;
    private Scene budgetManagerScene;
    private Scene receiptManagerScene;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("📋 PaperTrail Dashboard"); // Set Title of Stage

        // Create TaskManager backend to pass into view
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
        mainMenuScene = new Scene(mainMenuLayout, 400, 300);

        // TaskManager Scene
        TaskManagerView taskManagerView = new TaskManagerView(taskManager, budgetManager, primaryStage, mainMenuScene);
        taskManagerScene = new Scene(taskManagerView, 600, 400);

        // BudgetManager Scene
        BudgetManagerView budgetManagerView = new BudgetManagerView(budgetManager, primaryStage, mainMenuScene);
        budgetManagerScene = new Scene(budgetManagerView, 600, 400);

        // ReceiptManager Scene
        ReceiptManagerView receiptManagerView = new ReceiptManagerView(receiptManager, budgetManager, primaryStage, mainMenuScene);
        receiptManagerView.setBudgetManagerView(budgetManagerView, budgetManagerScene);
        receiptManagerScene = new Scene(receiptManagerView, 600, 400);

        // Button Actions
        tasksBtn.setOnAction(actionEvent -> primaryStage.setScene(taskManagerScene));
        budgetsBtn.setOnAction(actionEvent -> {
            budgetManagerView.refreshBudgets();
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

