package papertrail.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import papertrail.model.Budget;
import papertrail.model.BudgetPeriod;
import papertrail.model.Task;
import papertrail.model.Category;
import papertrail.service.BudgetManager;
import papertrail.service.TaskManager;

import java.time.LocalDate;

/*
 * Controller for the Task Manager resources.view.
 * Handles logic for adding, displaying, and managing tasks.
 * Linked to TaskManager.fxml which defines the layout.
 */
public class TaskManagerController {
    private TaskManager taskManager;
    private BudgetManager budgetManager;
    private Runnable backToMenuCallback; // Set externally by app entry point

    // UI elements injected from FXML
    @FXML private TextField titleField;
    @FXML private TextField descrField;
    @FXML private ComboBox<Category> categoryBox;
    @FXML private DatePicker dueDatePicker;
    @FXML private TextField amountField;
    // Separate List elements
    @FXML private VBox incompleteTasksVBOX;
    @FXML private VBox completedTasksVBOX;
    // Elements for searching/filtering tasks
    @FXML private TextField searchField;
    @FXML private ComboBox<Category> filterCategoryBox;

    /*
     * Called automatically after the FXML is loaded.
     * Used to initialize dropdowns, bindings, etc.
     */
    @FXML
    public void initialize() {
        categoryBox.getItems().setAll(Category.values()); // Populate category dropdown
        filterCategoryBox.getItems().setAll(Category.values()); // For filtering

        // Listeners to trigger filtering
        searchField.textProperty().addListener((obs, old, newVal) -> refreshTasks());
        filterCategoryBox.valueProperty().addListener((obs, old, newVal) -> refreshTasks());
    }

    /*
     * Called by the app to inject backend managers.
     */
    public void setManagers(TaskManager taskManager, BudgetManager budgetManager) {
        this.taskManager = taskManager;
        this.budgetManager = budgetManager;
        refreshTasks(); // Initial load of tasks
    }

    /*
     * Sets a callback to return to the main menu.
     * This avoids tight coupling to the primary stage.
     */
    public void setBackToMenuCallback(Runnable callback) {
        this.backToMenuCallback = callback;
    }

    @FXML
    private void handleClearFilter() {
        searchField.clear();
        filterCategoryBox.setValue(null);
        refreshTasks();
    }


    /*
     * Triggered when user clicks "Add Task".
     * Reads input fields, validates data, and creates a new task.
     */
    @FXML
    private void handleAddTask() {
        // Get input from form
        String title = titleField.getText();
        String descr = descrField.getText();
        Category cat = categoryBox.getValue();
        LocalDate due = dueDatePicker.getValue();
        double amt;

        if (title.isEmpty() || descr.isEmpty() || cat == null || due == null || amountField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill in all fields.");
            alert.show();
            return;
        }

        try {
            amt = Double.parseDouble(amountField.getText().trim());
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
                    newTask.getExpectedAmount(),
                    BudgetPeriod.MONTHLY
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

    /*
     * Refreshes the list of tasks in the UI.
     * Called after adding, deleting, or updating a task.
     */
    private void refreshTasks() {
        incompleteTasksVBOX.getChildren().clear(); // Remove old list
        completedTasksVBOX.getChildren().clear();

        String keyword = searchField.getText().toLowerCase().trim();
        Category selectedCategory = filterCategoryBox.getValue();

        for (Task task : taskManager.getAllTasks()) {
            if (task == null) continue; // Guard against null entries (unlikely but safe)

            // === Filtering Logic ===
            boolean matchesKeyword = keyword.isEmpty() || task.getTitle().toLowerCase().contains(keyword);
            boolean matchesCategory = selectedCategory == null || task.getCategory() == selectedCategory;

            if (!(matchesKeyword && matchesCategory)) continue;

            // Checkbox to toggle completion
            CheckBox checkBox = new CheckBox();
            checkBox.setSelected(task.isCompleted()); // When selected run isCompleted function

            // Task info label
            Label taskLabel = new Label(task.getTitle() + " - Due: " + task.getDueDate());
            // Make the Label clickable to view whole Task: Title, descr, cat, dueDate, amount
            taskLabel.setOnMouseClicked(event -> showTaskDetails(task));

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
                    task.setCompleted(false); // Manually unmarked
                }
                refreshTasks();
            });

            // Arrange in a row (HBox)
            HBox taskRow = new HBox(10, checkBox, taskLabel, deleteBtn);
            taskRow.setPadding(new Insets(5));
            taskRow.setStyle(" -fx-border-color: lightgray; -fx-border-width: 1;");

            if (task.isCompleted()) {
                completedTasksVBOX.getChildren().add(taskRow);
            } else {
                incompleteTasksVBOX.getChildren().add(taskRow);
            }
        }
    }
    /**
     * Opens a popup dialog showing all details of the selected Task.
     * Triggered when the user clicks on a task's label in the list.
     */
    private void showTaskDetails(Task task) {
        // Create a new dialog(modal popup)
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Task Details");

        // VBox to hold all the detail labels
        VBox content = new VBox(10); // Spacing of 10px between elements
        content.setPadding(new Insets(15)); // Padding around edges

        // Create individual labels for each task attribute
        Label titleLabel = new Label("Title: " + task.getTitle());
        Label descrLabel = new Label("Description: " + task.getDescription());
        Label categoryLabel = new Label("Category: " + task.getCategory());
        Label dueLabel = new Label("Due Date: " + task.getDueDate());
        Label amountLabel = new Label(String.format("Expected Amount: $%.2f", task.getExpectedAmount()));
        Label statusLabel = new Label("Completed: " + (task.isCompleted() ? "✅ Yes" : "❌ No"));

        // Add all labels to the dialog's content
        content.getChildren().addAll(
                titleLabel,
                descrLabel,
                categoryLabel,
                dueLabel,
                amountLabel,
                statusLabel
        );

        // Set the dialog content and add a "Close" button
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        // Show the dialog and wait until it's closed
        dialog.showAndWait();
    }
}