package papertrail.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.util.UUID;

// Represents a budget with a category, spending limit, and reset schedule.
public class Budget {
    private String title;
    private Category category;
    private double limit; // How much the user plans to spend
    private double spent; // How much has been spent so far
    private BudgetPeriod period;
    private LocalDate lastResetDate;
    private String id = UUID.randomUUID().toString();

    public Budget() {}

    public Budget(String title, Category category, double limit, BudgetPeriod period) {
        this.title = title;
        this.category = category;
        this.limit = limit;
        this.period = period;
        this.spent = 0.00;
        this.lastResetDate = LocalDate.now();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public double getLimit() { return  limit; }
    public void setLimit(double limit) { this.limit = limit; }

    public double getSpent() { return spent; }
    public void subtractExpense(double amount) {
        this.spent = Math.max(0.0, this.spent - amount);
    }
    @JsonIgnore
    public boolean isOverspent() {
        return spent > limit;
    }

    public LocalDate getLastResetDate() {
        return lastResetDate;
    }

    public BudgetPeriod getPeriod() {
        return period;
    }
    public void setPeriod(BudgetPeriod period) {this.period = period;}

    // Add amount spent for a particular budget
    public void addExpense(double amount) {
        this.spent += amount;
    }

    @JsonIgnore
    public double getRemaining() {
        return limit - spent;
    }

    public void resetIfNeeded() {
        LocalDate now = LocalDate.now();
        boolean shouldReset = switch (period) {
            case WEEKLY -> lastResetDate.plusWeeks(1).isBefore(now) || lastResetDate.plusWeeks(1).isEqual(now);
            case MONTHLY -> lastResetDate.plusMonths(1).isBefore(now) || lastResetDate.plusMonths(1).isEqual(now);
            case YEARLY -> lastResetDate.plusYears(1).isBefore(now) || lastResetDate.plusYears(1).isEqual(now);
        };
        if (shouldReset) {
            this.spent = 0.0;
            this.lastResetDate = now;
        }
    }

    @Override
    public String toString() {
        return "Title: " + title +
                "\nCategory: " + category +
                "\nLimit: $" + limit +
                "\nSpent: $" + spent +
                "\nRemaining: $" + getRemaining();
    }
}
