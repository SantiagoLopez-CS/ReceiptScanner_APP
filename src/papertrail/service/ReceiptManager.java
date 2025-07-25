package papertrail.service;

import papertrail.model.Budget;
import papertrail.model.Receipt;
import papertrail.model.Category;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReceiptManager {
    // Make a list of all receipt objects
    private final List<Receipt> receipts = new ArrayList<>();
    private final BudgetManager budgetManager;

    // Constructor that takes a BudgetManager
    public ReceiptManager(BudgetManager budgetManager) {
        this.budgetManager = budgetManager;
    }
    // Add receipt to the list
    public void addReceipt(Receipt receipt) {
        // Find a matching budget
        for (Budget budget : budgetManager.getAllBudgets()) {
            if (budget.getCategory() == receipt.getCategory()) {
                // Assign the budget ID to the receipt
                receipt.setBudgetId(budget.getId());
                budget.addExpense(receipt.getAmountSpent());
                break;
            }
        }
        receipts.add(receipt); // Store receipt
        //save(); TODO: Add save method
    }
    // Remove receipt from the list by ID
    public boolean removeReceipt(String id) {
        Iterator<Receipt> iterator = receipts.iterator();
        while (iterator.hasNext()) {
            Receipt r = iterator.next();
            if (r.getId().equals(id)) {
                iterator.remove();
                budgetManager.removeExpense(r.getBudgetId(), r.getAmountSpent());
                return true;
            }
        }
        return false;
    }
    // Print all available receipts
    // Make COPY of receipt list
    public List<Receipt> getAllReceipts() {
        return new ArrayList<>(receipts);
    }
}
