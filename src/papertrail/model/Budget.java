package papertrail.model;

// Track how much money is allocated and spent in a specific category
public class Budget {
    private String title;
    private Category category;
    private double limit; // How much the user plans to spend
    private double spent; // How much has been spent so far

    public Budget(String title, Category category, double limit) {
        this.title = title;
        this.category = category;
        this.limit = limit;
        this.spent = 0.00;
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public double getLimit() { return  limit; }
    public void setLimit(double limit) { this.limit = limit; }

    public double getSpent() { return spent; }
    public void setSpent(double spent) { this.spent = spent; }

    // Add amount spent for a particular budget
    public void addExpense(double amount) {
        this.spent += amount;
    }

    public double getRemaining() {
        return limit - spent;
    }

    @Override
    public String toString() {
        return "Title: " + title +
                "\nCategory: " + category +
                "\nLimit: $" + limit +
                "\nSpent: $" + spent +
                "\nRemaining: $" + (limit - spent);
    }
}
