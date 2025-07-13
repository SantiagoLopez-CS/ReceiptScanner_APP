package papertrail;

import papertrail.model.Budget;
import papertrail.model.Category;
import papertrail.model.Task;
import papertrail.model.Receipt;
import papertrail.service.BudgetManager;
import papertrail.service.ReceiptManager;
import papertrail.service.TaskManager;

import java.time.LocalDate;

public class BackendTests {
    public static void main(String[] args) {
        System.out.println("PaperTrail backend is running...");
        System.out.println("-----------------------------");

        // Task Test
        Task task = new Task(
                "Car Insurance",
                "Pay Car Insurance",
                Category.TRANSPORTATION,
                LocalDate.of(2025, 9, 7),
                280.00,
                false);

        // Receipt Test
        Receipt autoZoneReceipt = new Receipt(
                "AutoZone",
                Category.TRANSPORTATION,
                LocalDate.of(2025, 3, 15),
                54.55);

        // Budget Test
        Budget grocerieBudget = new Budget(
                "Grocery Budget",
                Category.FOOD,
                200.00);


        // -----------------------
        // ✅ TaskService Tests
        System.out.println("\n=== TaskService Tests ===");

        TaskManager taskManager = new TaskManager();

        // Test 1: Add a task
        taskManager.addTask(task);
        System.out.println("\n[Add Task] Task List:");
        // For every task in the list, print it
        taskManager.getAllTasks().forEach(System.out::println);

        // Test 2: Complete the task
        boolean completed = taskManager.completeTask(task.getId());
        System.out.println("\n[Complete Task] Marked as complete: " + completed);
        System.out.println(taskManager.getTaskById(task.getId()));

        // Test 3: Get pending tasks
        System.out.println("\n[Pending Tasks] Should be empty:");
        taskManager.getPendingTasks().forEach(System.out::println);

        // Test 4: Remove the task
        boolean removed = taskManager.removeTask(task.getId());
        System.out.println("\n[Remove Task] Removed: " + removed);
        System.out.println("[Remaining Tasks]: " + taskManager.getAllTasks().size());

        // BudgetManager & ReceiptManager Tests;
        BudgetManager budgetManager = new BudgetManager();
        ReceiptManager receiptManager = new ReceiptManager(budgetManager);

        // Test 1: Add Budget first, so expenses have somewhere to go
        Budget transportBudget = new Budget("Car Stuff", Category.TRANSPORTATION, 280);
        budgetManager.addBudget(transportBudget);
        // Test 2: Add a receipt (this should automatically update the budget)
        receiptManager.addReceipt(autoZoneReceipt);
        // Test 3: View updated budgets
        System.out.println("\n=== Updated Budgets ===");
        budgetManager.getAllBudgets().forEach(System.out::println);

    }
}
