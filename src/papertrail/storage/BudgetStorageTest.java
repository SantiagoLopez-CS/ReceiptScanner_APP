package papertrail.storage;

import papertrail.model.Budget;
import papertrail.model.BudgetPeriod;
import papertrail.model.Category;

import java.util.ArrayList;
import java.util.List;

public class BudgetStorageTest {
    public static void main(String[] args) {
        // 1. Create some budgets
        List<Budget> budgets = new ArrayList<>();
        budgets.add(new Budget("Groceries", Category.FOOD, 250.00, BudgetPeriod.MONTHLY));
        budgets.add(new Budget("Utilities", Category.HOUSING, 120.00, BudgetPeriod.MONTHLY));

        // 2. Save budgets to JSON file
        BudgetStorage.saveBudgets(budgets);
        System.out.println("Budgets saved to file.");

        // 3. Load budgets back from file
        List<Budget> loadedBudgets = BudgetStorage.loadBudgets();
        System.out.println("Loaded Budgets");
        for (Budget b : loadedBudgets) {
            System.out.println("- " + b.getCategory() + ": " + b.getLimit());
        }

        // 4. Confirm data consistency
        System.out.println("Match: " + (budgets.size() == loadedBudgets.size()));
    }
}
