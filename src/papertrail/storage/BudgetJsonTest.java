package papertrail.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import papertrail.model.Budget;
import papertrail.model.Category;
import papertrail.model.BudgetPeriod;

import java.io.File;

public class BudgetJsonTest {
    public static void main(String[] args) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule()); // Needed for LocalDate

            // 1️⃣ Create a test budget
            Budget budget = new Budget("Groceries", Category.FOOD, 250.0, BudgetPeriod.MONTHLY);
            budget.addExpense(50.0);

            // 2️⃣ Serialize to JSON file
            File file = new File("budget_test.json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, budget);
            System.out.println("Serialized JSON saved to: " + file.getAbsolutePath());

            // 3️⃣ Read it back from JSON file
            Budget loadedBudget = mapper.readValue(file, Budget.class);

            // 4️⃣ Display both objects for comparison
            System.out.println("\n--- Original Budget ---");
            System.out.println(budget);

            System.out.println("\n--- Deserialized Budget ---");
            System.out.println(loadedBudget);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
