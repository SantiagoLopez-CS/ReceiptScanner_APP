package papertrail;

import papertrail.model.Budget;
import papertrail.model.Category;
import papertrail.model.Task;
import papertrail.model.Receipt;
import papertrail.service.TaskManager;

import java.time.LocalDate;

public class Main {
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
        System.out.println("Initial Task:\n" + task);

        // Receipt Test
        Receipt receipt = new Receipt(
                "AutoZone",
                Category.TRANSPORTATION,
                LocalDate.of(2025, 3, 15),
                54.55);
        System.out.println("\nReceipt:\n" + receipt);

        // Budget Test
        Budget grocerieBudget = new Budget(
                "Grocery Budget",
                Category.FOOD,
                200.00);
        System.out.println("\nBudget:\n" + grocerieBudget);

        // -----------------------
        // ✅ TaskService Tests
        System.out.println("\n=== TaskService Tests ===");

        TaskManager service = new TaskManager();

        // Test 1: Add a task
        service.addTask(task);
        System.out.println("\n[Add Task] Task List:");
        service.getAllTasks().forEach(System.out::println);

        // Test 2: Complete the task
        boolean completed = service.completeTask(task.getId());
        System.out.println("\n[Complete Task] Marked as complete: " + completed);
        System.out.println(service.getTaskById(task.getId()));

        // Test 3: Get pending tasks
        System.out.println("\n[Pending Tasks] Should be empty:");
        service.getPendingTasks().forEach(System.out::println);

        // Test 4: Remove the task
        boolean removed = service.removeTask(task.getId());
        System.out.println("\n[Remove Task] Removed: " + removed);
        System.out.println("[Remaining Tasks]: " + service.getAllTasks().size());
    }
}
