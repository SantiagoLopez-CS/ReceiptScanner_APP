package papertrail.service;

import papertrail.model.Budget;
import papertrail.model.Category;
import papertrail.model.Receipt;

import java.util.ArrayList;
import java.util.List;

public class BudgetManager {
    // Create list of budget's
    private final List<Budget> budgets = new ArrayList<>();
    // Add budget to the list
    public void addBudget(Budget budget) {
        budgets.add(budget);
    }
    // Show all the current Budget's with a COPY of the real list
    public List<Budget> getAllBudgets() {
        return new ArrayList<>(budgets);
    }
    // Separate method for resetting budgets
    public void resetAllBudgetsIfNeeded() {
        for (Budget b : budgets) {
            b.resetIfNeeded();
        }
    }

    // Function to add an expense to a particular budget with the receipt price
    public boolean addExpense(Category category, double amount) {
        for (Budget budget : budgets) { // For each Budget object in the budgets list
            budget.resetIfNeeded(); // Ensure the list is updated before adding
            if (budget.getCategory() == category) {
                budget.addExpense(amount);
                return true;
            }
        }
        return false;
    }

    // (Optional) Get a specific budget by category
    public Budget getBudgetByCategory(Category category) {
        for (Budget budget : budgets) {
            budget.resetIfNeeded(); // Keep fresh
            if (budget.getCategory() == category) {
                return budget;
            }
        }
        return null;
    }
}
