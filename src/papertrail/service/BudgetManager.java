package papertrail.service;

import papertrail.model.Budget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BudgetManager {
    // Map budgets by category for fast lookup
    private final Map<String, Budget> budgets = new HashMap<>();
    // Add budget to the map(keyed by category)
    public void addBudget(Budget budget) {
        if (budget != null) {
            budgets.put(budget.getId(), budget);
        }
    }
    public void removeBudget(Budget budget) {
        budgets.remove(budget.getId());
    }
    // Show all the current Budget's with a COPY of the real list
    public List<Budget> getAllBudgets() {
        resetAllBudgetsIfNeeded();
        return new ArrayList<>(budgets.values());
    }

    // Function to add an expense to a particular budget with the receipt price
    public boolean addExpense(String budgetId, double amount) {
        Budget budget = budgets.get(budgetId);
        if (budget != null) {
            budget.resetIfNeeded(); // Reset if needed before adding expense
            budget.addExpense(amount);
            return true;
        }
        return false;
    }
    // Function to remove the receipt expense when a receipt is deleted
    public void removeExpense(String budgetId, double amount) {
        Budget budget = budgets.get(budgetId);
        if (budget != null) {
            budget.subtractExpense(amount);
        }
    }

    // (Optional) Get a specific budget by category
    public Budget getBudgetById(String id) {
        Budget budget = budgets.get(id);
        if (budget != null) {
            budget.resetIfNeeded();
        }
        return budget;
    }

    // Separate method for resetting budgets
    public void resetAllBudgetsIfNeeded() {
        for (Budget b : budgets.values()) {
            b.resetIfNeeded();
        }
    }

    // Repopulate map for JSON serialization
    public void setBudgets(List<Budget> budgetList) {
        budgets.clear();
        if (budgetList != null) {
            for (Budget b : budgetList) {
                budgets.put(b.getId(), b);
            }
        }
    }

    public void relinkReceiptsToBudgets() {}
}
