package papertrail.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import papertrail.model.Task;
import papertrail.model.Category;
import papertrail.service.TaskManager;

import java.time.LocalDate;

/*
 * Class for building UI for managing tasks.
 * Extends VBox(vertical layout container) and contains input fields.
 * Also contains buttons and a scrollable list of tasks.
 */
public class TaskManagerView  extends VBox {
    private final TaskManager taskManager; // Reference to backend logic
    private final VBox taskListVBox; // Holds and displays list in the UI

    // Initialize backend references and UI layout
    public TaskManagerView(TaskManager taskManager, Stage primaryStage, Scene mainMenuScene) {
        this.taskManager = taskManager; // Store the backend reference

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
        taskListVBox = new VBox(5); // Holds task labels
        ScrollPane scrollPane = new ScrollPane(taskListVBox); // Makes it scrollable
        scrollPane.setFitToWidth(true); // Match width of parent

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
            double amt = Double.parseDouble(amountField.getText());

            // Create and add the Task
            Task newTask = new Task(title, descr, cat, due, amt, false);
            taskManager.addTask(newTask);
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
        taskListVBox.getChildren().clear(); // Remove old list
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

            // Add to the task list container
            taskListVBox.getChildren().add(taskRow);
        }
    }
}
