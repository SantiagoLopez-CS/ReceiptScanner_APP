package papertrail;

import papertrail.model.Budget;
import papertrail.model.Category;
import papertrail.model.Task;
import papertrail.model.Receipt;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("PaperTrail backend is running...");
        // Eventually launch backend service or GUI here

        // Test(s)
        Task task = new Task(
                "Car Insurance",
                "Pay Car Insurance",
                Category.TRANSPORTATION,
                LocalDate.of(2025, 9, 7),
                280.00,
                false);
        System.out.println(task); // Print the Task

        Receipt receipt = new Receipt(
                "AutoZone",
                Category.TRANSPORTATION,
                LocalDate.of(2025, 3, 15),
                54.55);
        System.out.println(receipt);

        Budget grocerieBudget = new Budget(
                "Grocery Budget",
                Category.FOOD,
                200.00);
        System.out.println(grocerieBudget);
    }
}
