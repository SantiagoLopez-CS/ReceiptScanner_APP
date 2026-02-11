package papertrail.service;

import papertrail.model.Budget;
import papertrail.model.Receipt;
import papertrail.storage.ReceiptStorage;

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

    public void setReceipts(List<Receipt> loadedReceipts) {
        receipts.clear();
        receipts.addAll(loadedReceipts);

        // Re-link receipts to budgets
        for (Receipt receipt : receipts) {
            Budget linkedBudget = null;

            // 1️⃣ Try linking by budgetId
            if (receipt.getBudgetId() != null) {
                for (Budget budget : budgetManager.getAllBudgets()) {
                    if (budget.getId().equals(receipt.getBudgetId())) {
                        linkedBudget = budget;
                        break;
                    }
                }
            }

            // 2️⃣ Fallback: link by category if budgetId not found
            if (linkedBudget == null) {
                for (Budget b : budgetManager.getAllBudgets()) {
                    if (b.getCategory() == receipt.getCategory()) {
                        linkedBudget = b;
                        receipt.setBudgetId(b.getId());
                        break;
                    }
                }
            }

            // 3️⃣ If linked, re-add expense
            if (linkedBudget != null) {
                linkedBudget.addExpense(receipt.getAmountSpent());
            }
        }
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
        save();
    }
    // Remove receipt from the list by ID
    public boolean removeReceipt(String id) {
        Iterator<Receipt> iterator = receipts.iterator();
        while (iterator.hasNext()) {
            Receipt r = iterator.next();
            if (r.getId().equals(id)) {
                iterator.remove();
                budgetManager.removeExpense(r.getBudgetId(), r.getAmountSpent());
                save();
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

    // Manual save method
    public void save() {
        ReceiptStorage.saveReceipts(receipts);
    }
}
