package papertrail.model;

import java.time.LocalDate;
import java.util.UUID;

public class Task {
    private String id;
    private String title;
    private String description;
    private Category category;
    private LocalDate dueDate;
    private double expectedAmount;
    private boolean isCompleted;

    public Task(String title, String description, Category category, LocalDate dueDate, double expectedAmount, boolean isCompleted) {
        this.id = UUID.randomUUID().toString(); // Auto-generate unique ID
        this.title = title;
        this.description = description;
        this.category = category;
        this.dueDate = dueDate;
        this.expectedAmount = expectedAmount;
        this.isCompleted = isCompleted;
    }

    // Getters and Setters
    public String getId() { return  id; }
    public String getTitle() { return  title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public double getExpectedAmount() { return expectedAmount; }
    public void setExpectedAmount(double expectedAmount) { this.expectedAmount = expectedAmount; }

    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }

    // Display Task object
    @Override
    public String toString() {
        return "Title: " + title +
                "\nDescription: " + description +
                "\nDue Date: " + dueDate +
                "\nCategory: " + category +
                "\nExpected Amount: $" + expectedAmount +
                "\nStatus: " + (isCompleted ? "Completed [✓]" : "Pending [✗]");
    }
}
