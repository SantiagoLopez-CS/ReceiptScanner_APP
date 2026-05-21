package papertrail;

import papertrail.model.Budget;
import papertrail.model.BudgetPeriod;
import papertrail.model.Category;
import papertrail.model.Receipt;
import papertrail.model.Task;
import papertrail.service.BudgetManager;
import papertrail.service.ReceiptManager;
import papertrail.service.TaskManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

public class BackendTests {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) throws IOException {
        Path receiptsFile = Paths.get("data", "receipts.json");
        boolean receiptsFileExists = Files.exists(receiptsFile);
        byte[] originalReceiptsFile = receiptsFileExists ? Files.readAllBytes(receiptsFile) : null;

        try {
            System.out.println("Running PaperTrail backend tests...");
            testTaskManager();
            testBudgetAndReceiptManagers();
        } finally {
            restoreFile(receiptsFile, receiptsFileExists, originalReceiptsFile);
        }

        System.out.println();
        System.out.println("Tests passed: " + passed);
        System.out.println("Tests failed: " + failed);

        if (failed > 0) {
            throw new AssertionError("Backend tests failed.");
        }
    }

    private static void testTaskManager() {
        TaskManager taskManager = new TaskManager();
        Task task = new Task(
                "Car Insurance",
                "Pay car insurance",
                Category.TRANSPORTATION,
                LocalDate.of(2026, 9, 7),
                280.00,
                false
        );

        taskManager.addTask(task);
        assertEquals("Task is added", 1, taskManager.getAllTasks().size());
        assertEquals("Task starts pending", 1, taskManager.getPendingTasks().size());

        assertTrue("Task can be completed", taskManager.completeTask(task.getId()));
        assertTrue("Completed task is marked complete", taskManager.getTaskById(task.getId()).isCompleted());
        assertEquals("Completed task is no longer pending", 0, taskManager.getPendingTasks().size());

        assertTrue("Task can be removed", taskManager.removeTask(task.getId()));
        assertEquals("Task list is empty after removal", 0, taskManager.getAllTasks().size());
    }

    private static void testBudgetAndReceiptManagers() {
        BudgetManager budgetManager = new BudgetManager();
        ReceiptManager receiptManager = new ReceiptManager(budgetManager);

        Budget transportBudget = new Budget(
                "Car Stuff",
                Category.TRANSPORTATION,
                280.00,
                BudgetPeriod.MONTHLY
        );
        budgetManager.addBudget(transportBudget);

        assertEquals("Budget is added", 1, budgetManager.getAllBudgets().size());
        assertSame("Budget can be found by id", transportBudget, budgetManager.getBudgetById(transportBudget.getId()));

        Receipt autoZoneReceipt = new Receipt(
                "AutoZone",
                Category.TRANSPORTATION,
                LocalDate.of(2026, 3, 15),
                54.55,
                null
        );

        receiptManager.addReceipt(autoZoneReceipt);
        assertEquals("Receipt is added", 1, receiptManager.getAllReceipts().size());
        assertEquals("Receipt links to matching budget", transportBudget.getId(), autoZoneReceipt.getBudgetId());
        assertDoubleEquals("Receipt amount is added to budget spent", 54.55, transportBudget.getSpent());

        assertTrue("Receipt can be removed", receiptManager.removeReceipt(autoZoneReceipt.getId()));
        assertEquals("Receipt list is empty after removal", 0, receiptManager.getAllReceipts().size());
        assertDoubleEquals("Receipt removal subtracts from budget spent", 0.00, transportBudget.getSpent());
    }

    private static void restoreFile(Path path, boolean existed, byte[] originalContent) throws IOException {
        if (existed) {
            Files.createDirectories(path.getParent());
            Files.write(path, originalContent);
        } else {
            Files.deleteIfExists(path);
        }
    }

    private static void assertTrue(String testName, boolean condition) {
        if (condition) {
            pass(testName);
        } else {
            fail(testName, "Expected true but was false.");
        }
    }

    private static void assertSame(String testName, Object expected, Object actual) {
        if (expected == actual) {
            pass(testName);
        } else {
            fail(testName, "Expected same object reference.");
        }
    }

    private static void assertEquals(String testName, int expected, int actual) {
        if (expected == actual) {
            pass(testName);
        } else {
            fail(testName, "Expected " + expected + " but was " + actual + ".");
        }
    }

    private static void assertEquals(String testName, String expected, String actual) {
        if (expected == null ? actual == null : expected.equals(actual)) {
            pass(testName);
        } else {
            fail(testName, "Expected " + expected + " but was " + actual + ".");
        }
    }

    private static void assertDoubleEquals(String testName, double expected, double actual) {
        if (Math.abs(expected - actual) < 0.001) {
            pass(testName);
        } else {
            fail(testName, "Expected " + expected + " but was " + actual + ".");
        }
    }

    private static void pass(String testName) {
        passed++;
        System.out.println("[PASS] " + testName);
    }

    private static void fail(String testName, String message) {
        failed++;
        System.out.println("[FAIL] " + testName + " - " + message);
    }
}
