package papertrail.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import papertrail.model.Budget;
import papertrail.model.Task;
import papertrail.model.Category;
import papertrail.service.BudgetManager;
import papertrail.service.TaskManager;

import java.time.LocalDate;

/*
 * Class for building UI for managing tasks.
 * Extends VBox(vertical layout container) and contains input fields.
 * Also contains buttons and a scrollable list of tasks.
 */
public class TaskManagerView  extends VBox {
    private final TaskManager taskManager;
    private final BudgetManager budgetManager;
    private final HBox taskListHBox; // Holds and displays list in the UI
    // Two VBox for separate lists('To Do', and 'Done')
    private final VBox incompleteTasksVBOX = new VBox(5);
    private final VBox completedTasksVBox = new VBox(5); // 5px apart for each element
    // Initialize backend references and UI layout
    public TaskManagerView(TaskManager taskManager, BudgetManager budgetManager, Stage primaryStage, Scene mainMenuScene) {
        this.taskManager = taskManager; // Store the backend reference
        this.budgetManager = budgetManager;

        // Set layout padding and spacing
        setSpacing(10);
        setPadding(new Insets(15));

        // Title Label
        Label titleLabel = new Label("Task Manager");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Input Fields for Adding a Task
        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        TextField descrField = new TextField();
        descrField.setPromptText("Description");

        ComboBox<Category> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll(Category.values()); // Populate dropdown with categories

        DatePicker dueDatePicker = new DatePicker();
        dueDatePicker.setPromptText("Due Date");

        TextField amountField = new TextField();
        amountField.setPromptText("Expected Amount");

        Button addButton = new Button("Add Task");

        // Task List Container
        taskListHBox = new HBox(30); // Holds task labels
        taskListHBox.setPadding(new Insets(10));

        // Wrap each list in its own VBox with a label
        VBox incompleteSection = new VBox(10,
                new Label("Incomplete Tasks"),
                incompleteTasksVBOX
        );
        VBox completeSection = new VBox(10,
                new Label("✅ Completed Tasks"),
                completedTasksVBox
        );
        // Add the two lists to the list container
        taskListHBox.getChildren().addAll(incompleteSection, completeSection);

        ScrollPane scrollPane = new ScrollPane(taskListHBox);
        scrollPane.setFitToWidth(true);

        // Back to Main Menu button
        Button backBtn = new Button("⬅ Back to Main Menu");
        backBtn.setOnAction(actionEvent -> primaryStage.setScene(mainMenuScene)); // On action, go back to the main menu scene

        // Button Action: Add Task
        addButton.setOnAction(e -> {
            // Get input from form
            String title = titleField.getText();
            String descr = descrField.getText();
            Category cat = categoryBox.getValue();
            LocalDate due = dueDatePicker.getValue();
            double amt;
            try {
                amt = Double.parseDouble(amountField.getText());
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid number for 'Amount'.");
                alert.show();
                return; // Stop execution
            }

            // Create and add the Task
            Task newTask = new Task(title, descr, cat, due, amt, false);
            taskManager.addTask(newTask);

            // Check if a budget for this category already exists
            boolean budgetExists = budgetManager.getAllBudgets().stream()
                    .anyMatch(budget -> budget.getCategory() == newTask.getCategory());
            // If not, create a new Budget
            if (!budgetExists) {
                budgetManager.addBudget(new Budget(
                        newTask.getTitle() + " Budget",
                        newTask.getCategory(),
                        newTask.getExpectedAmount()
                ));
                System.out.println("Auto-created budget for task: " + newTask.getTitle());
            }
            // Refresh the UI task List
            refreshTasks();

            // Clear input fields
            titleField.clear();
            descrField.clear();
            categoryBox.setValue(null);
            dueDatePicker.setValue(null);
            amountField.clear();
        });

        // Add Everything to the VBox
        getChildren().addAll(
                titleLabel,
                new HBox(10, titleField, descrField), // Title + Description input
                new HBox(10, categoryBox, dueDatePicker, amountField), // Category, DueDate, Amount
                addButton,
                new Separator(),
                scrollPane, // Task List
                backBtn // 🔙 Add back button at bottom
        );
        refreshTasks(); // Load existing tasks if any
    }

    // Refresh the list of tasks in the UI
    private void refreshTasks() {
        incompleteTasksVBOX.getChildren().clear(); // Remove old list
        completedTasksVBox.getChildren().clear();

        for (Task task : taskManager.getAllTasks()) {

            // Checkbox to toggle completion
            CheckBox checkBox = new CheckBox();
            checkBox.setSelected(task.isCompleted()); // When selected run isCompleted function
            // Task info label
            Label taskLabel = new Label(task.getTitle() + " - Due: " + task.getDueDate());
            // Delete Button
            Button deleteBtn = new Button("🗑");
            deleteBtn.setOnAction(e -> {
                taskManager.removeTask(task.getId());
                refreshTasks(); // Refresh list after deletion
            });

            // Handle Checkbox toggle
            checkBox.setOnAction(e -> {
                if (checkBox.isSelected()) {
                    taskManager.completeTask(task.getId());
                } else {
                    task.setCompleted(false); // Manually unmark
                }
                refreshTasks();
            });

            // Arrange in a row (HBox)
            HBox taskRow = new HBox(10, checkBox, taskLabel, deleteBtn);
            taskRow.setPadding(new Insets(5));
            taskRow.setStyle(" -fx-border-color: lightgray; -fx-border-width: 1;");

            if (task.isCompleted()) {
                completedTasksVBox.getChildren().add(taskRow);
            } else {
                incompleteTasksVBOX.getChildren().add(taskRow);
            }
        }
    }
}
