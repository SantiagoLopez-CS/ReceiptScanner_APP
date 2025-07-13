package papertrail.service;

import papertrail.model.Receipt;
import papertrail.model.Category;

import java.util.ArrayList;
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
        receipts.add(receipt);
        // Update the budgets new spent amount
        budgetManager.addExpense(receipt.getCategory(), receipt.getAmountSpent());
    }
    // Remove receipt from the list by ID
    public boolean removeReceipt(String id) {
        return receipts.removeIf(r -> r.getId().equals(id));
    }
    // Print all available receipts
    // Make COPY of receipt list
    public List<Receipt> getAllReceipts() {
        return new ArrayList<>(receipts);
    }
}
