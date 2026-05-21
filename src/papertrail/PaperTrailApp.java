package papertrail;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import papertrail.controller.BudgetManagerController;
import papertrail.controller.MainMenuController;
import papertrail.controller.ReceiptManagerController;
import papertrail.controller.SettingsController;
import papertrail.controller.TaskManagerController;
import papertrail.service.BudgetManager;
import papertrail.service.ReceiptManager;
import papertrail.service.TaskManager;
import papertrail.storage.BudgetStorage;
import papertrail.storage.ReceiptStorage;
import papertrail.storage.TaskStorage;

import java.io.IOException;

public class PaperTrailApp extends Application {

    private Scene mainMenuScene;
    private Scene taskManagerScene;
    private Scene budgetManagerScene;
    private Scene receiptManagerScene;
    private Scene settingsScene;

    private BudgetManagerController budgetController;
    private ReceiptManagerController receiptController;
    private TaskManagerController taskController;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("PaperTrail Dashboard");

        BudgetManager budgetManager = new BudgetManager();
        budgetManager.setBudgets(BudgetStorage.loadBudgets());

        TaskManager taskManager = new TaskManager();
        taskManager.setTasks(TaskStorage.loadTasks());

        ReceiptManager receiptManager = new ReceiptManager(budgetManager);
        receiptManager.setReceipts(ReceiptStorage.loadReceipts());

        try {
            loadMainMenu(primaryStage);
            loadTaskManager(primaryStage, taskManager, budgetManager);
            loadBudgetManager(primaryStage, budgetManager);
            loadReceiptManager(primaryStage, receiptManager, budgetManager);
            loadSettings(primaryStage, budgetManager, receiptManager, taskManager);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        primaryStage.setScene(mainMenuScene);
        primaryStage.show();
    }

    private void loadMainMenu(Stage primaryStage) throws IOException {
        FXMLLoader mainMenuLoader = new FXMLLoader(getClass().getResource("/resources/view/MainMenuView.fxml"));
        VBox mainMenuRoot = mainMenuLoader.load();
        MainMenuController mainMenuController = mainMenuLoader.getController();

        mainMenuController.setOnTaskPressed(() -> primaryStage.setScene(taskManagerScene));
        mainMenuController.setOnReceiptPressed(() -> primaryStage.setScene(receiptManagerScene));
        mainMenuController.setOnBudgetPressed(() -> {
            budgetController.refreshBudgets();
            primaryStage.setScene(budgetManagerScene);
        });
        mainMenuController.setOnSettingsPressed(() -> primaryStage.setScene(settingsScene));

        mainMenuScene = new Scene(mainMenuRoot, 800, 600);
    }

    private void loadTaskManager(Stage primaryStage, TaskManager taskManager, BudgetManager budgetManager) throws IOException {
        FXMLLoader taskLoader = new FXMLLoader(getClass().getResource("/resources/view/TaskManagerView.fxml"));
        VBox taskRoot = taskLoader.load();

        taskController = taskLoader.getController();
        taskController.setManagers(taskManager, budgetManager);
        taskController.setBackToMenuCallback(() -> primaryStage.setScene(mainMenuScene));

        taskManagerScene = new Scene(taskRoot, 800, 600);
    }

    private void loadBudgetManager(Stage primaryStage, BudgetManager budgetManager) throws IOException {
        FXMLLoader budgetLoader = new FXMLLoader(getClass().getResource("/resources/view/BudgetManagerView.fxml"));
        VBox budgetRoot = budgetLoader.load();

        budgetController = budgetLoader.getController();
        budgetController.setManagers(budgetManager);
        budgetController.setBackToMenuCallback(() -> primaryStage.setScene(mainMenuScene));

        budgetManagerScene = new Scene(budgetRoot, 800, 600);
    }

    private void loadReceiptManager(Stage primaryStage, ReceiptManager receiptManager, BudgetManager budgetManager) throws IOException {
        FXMLLoader receiptLoader = new FXMLLoader(getClass().getResource("/resources/view/ReceiptManagerView.fxml"));
        VBox receiptRoot = receiptLoader.load();

        receiptController = receiptLoader.getController();
        receiptController.setManagers(receiptManager, budgetManager);
        receiptController.setBackToMenuCallback(() -> primaryStage.setScene(mainMenuScene));
        receiptController.setBudgetSceneContext(budgetController, budgetManagerScene, primaryStage);

        receiptManagerScene = new Scene(receiptRoot, 800, 600);
    }

    private void loadSettings(
            Stage primaryStage,
            BudgetManager budgetManager,
            ReceiptManager receiptManager,
            TaskManager taskManager
    ) throws IOException {
        FXMLLoader settingsLoader = new FXMLLoader(getClass().getResource("/resources/view/SettingsView.fxml"));
        VBox settingsRoot = settingsLoader.load();
        SettingsController settingsController = settingsLoader.getController();

        settingsController.setManagers(budgetManager, receiptManager, taskManager);
        settingsController.setBackToMenuCallback(() -> primaryStage.setScene(mainMenuScene));
        settingsController.setDataClearedCallback(() -> {
            budgetController.refreshBudgets();
            receiptController.refreshReceipts();
            taskController.refreshTasks();
        });

        settingsScene = new Scene(settingsRoot, 800, 600);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
