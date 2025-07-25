package papertrail.model;

import java.time.LocalDate;
import java.util.UUID;

public class Receipt {
    private final String id;
    private String store;
    private Category category;
    private LocalDate dayOfPurchase;
    private double amountSpent;
    private String budgetId;

    public Receipt(String store, Category category, LocalDate dayOfPurchase, double amountSpent, String budgetId) {
        this.id = UUID.randomUUID().toString(); // auto-generate random ID
        this.store = store;
        this.category = category;
        this.dayOfPurchase = dayOfPurchase;
        this.amountSpent = amountSpent;
        this.budgetId = budgetId;
    }

    // Getters and Setters
    public String getBudgetId() {
        return budgetId;
    }
    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }

    public String getId() { return id; }

    public String getStore() { return store; }
    public void setStore(String store) { this.store = store; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public LocalDate getDayOfPurchase() { return dayOfPurchase; }
    public void setDayOfPurchase(LocalDate dayOfPurchase) { this.dayOfPurchase = dayOfPurchase; }

    public double getAmountSpent() { return amountSpent; }
    public void setAmountSpent(double amountSpent) { this.amountSpent = amountSpent; }

    // Display Receipt
    @Override
    public String toString() {
        return "Store: " + store + " (" + category + ")" +
                "\nDate: " + dayOfPurchase +
                "\nAmount Spent: $" + amountSpent;
    }

}
