package papertrail;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import papertrail.model.Budget;
import papertrail.model.BudgetPeriod;
import papertrail.model.Category;
import papertrail.service.BudgetManager;
import papertrail.service.TaskManager;
import papertrail.view.BudgetManagerView;
import papertrail.view.TaskManagerView;

public class PaperTrailApp extends Application{

    // Declare scenes to allow switching
    private Scene mainMenuScene;
    private Scene taskManagerScene;
    private Scene budgetManagerScene;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("📋 PaperTrail Dashboard"); // Set Title of Stage

        // Create TaskManager backend to pass into view
        TaskManager taskManager = new TaskManager();
        BudgetManager budgetManager = new BudgetManager();
        // BudgetUI Test
        budgetManager.addBudget(new Budget("Starter Budget", Category.FOOD, 100.00, BudgetPeriod.WEEKLY));

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

        // Button Actions
        tasksBtn.setOnAction(actionEvent -> primaryStage.setScene(taskManagerScene));
        budgetsBtn.setOnAction(actionEvent -> {
            budgetManagerView.refreshBudgets();
            primaryStage.setScene(budgetManagerScene);
        });
        // TODO: Add actions to open different views later
        receiptsBtn.setOnAction(e -> System.out.println("Budgets View - Coming Soon"));

        // Set the Scene and show(make visible)
        primaryStage.setScene(mainMenuScene);
        primaryStage.show();
    }
    // Launch the App
    public static void main(String[] args) {
        launch(args);
    }
}

