package papertrail.service;

import papertrail.model.Budget;
import papertrail.model.Category;
import papertrail.model.Receipt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BudgetManager {
    // Map budgets by category for fast lookup
    private final Map<Category, Budget> budgets = new HashMap<>();
    // Add budget to the map(keyed by category)
    public void addBudget(Budget budget) {
        budgets.put(budget.getCategory(), budget);
    }
    public void removeBudget(Budget budget) {
        budgets.remove(budget);
    }
    // Show all the current Budget's with a COPY of the real list
    public List<Budget> getAllBudgets() {
        resetAllBudgetsIfNeeded();
        return new ArrayList<>(budgets.values());
    }
    // Separate method for resetting budgets
    public void resetAllBudgetsIfNeeded() {
        for (Budget b : budgets.values()) {
            b.resetIfNeeded();
        }
    }

    // Function to add an expense to a particular budget with the receipt price
    public boolean addExpense(Category category, double amount) {
        Budget budget = budgets.get(category);
        if (budget != null) {
            budget.resetIfNeeded(); // Reset if needed before adding expense
            budget.addExpense(amount);
            return true;
        }
        return false;
    }

    // (Optional) Get a specific budget by category
    public Budget getBudgetByCategory(Category category) {
        Budget budget = budgets.get(category);
        if (budget != null) {
            budget.resetIfNeeded();
        }
        return budget;
    }
}
