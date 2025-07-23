package papertrail.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import papertrail.model.Budget;
import papertrail.model.BudgetPeriod;
import papertrail.model.Category;
import papertrail.service.BudgetManager;

import java.util.List;


public class BudgetManagerView extends VBox {
    private final BudgetManager budgetManager;
    private final HBox budgetContentHBox;
    private final VBox mainBudgetListVBox = new VBox(5);
    private final TextField titleField = new TextField();
    private final ComboBox<Category> categoryComboBox = new ComboBox<>();
    private final TextField limitField = new TextField();
    private final ComboBox<BudgetPeriod> periodComboBox = new ComboBox<>();
    private Budget editingBudget = null;



    public BudgetManagerView(BudgetManager budgetManager, Stage primaryStage, Scene mainMenuScene){
        // Store backend reference
        this.budgetManager = budgetManager;

        setSpacing(10);
        setPadding(new Insets(15));

        Label budgetTitle = new Label("Budget Manager");
        budgetTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // addBudget fields
        titleField.setPromptText("Title");
        categoryComboBox.getItems().addAll(Category.values());
        categoryComboBox.setPromptText("Category");
        limitField.setPromptText("Limit");
        periodComboBox.getItems().addAll(BudgetPeriod.values());
        periodComboBox.setPromptText("Period");

        Button addBudgetBtn = new Button("Add Budget");

        budgetContentHBox = new HBox(30); // Holds task labels
        budgetContentHBox.setPadding(new Insets(10));

        VBox mainBudgetSection = new VBox(10,
                new Label("Budget's List"),
                mainBudgetListVBox
        );
        budgetContentHBox.getChildren().addAll(mainBudgetSection);

        refreshBudgets(); // Refresh list

        ScrollPane scrollPane = new ScrollPane(budgetContentHBox);
        scrollPane.setFitToWidth(true);

        javafx.scene.control.Button backBtn = new javafx.scene.control.Button("⬅ Back to Main Menu");
        backBtn.setOnAction(actionEvent -> primaryStage.setScene(mainMenuScene));

        addBudgetBtn.setOnAction(actionEvent -> {
                String title = titleField.getText();
                Category cat = categoryComboBox.getValue();
                double lim;
                try {
                    lim = Double.parseDouble(limitField.getText());
                } catch (NumberFormatException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid number for 'Limit'.");
                    alert.show();
                    return; // Stop execution
                }
                BudgetPeriod period = periodComboBox.getValue();
                if (period == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a time period.");
                    alert.show();
                    return;
                }

                if (editingBudget != null) {
                    // The budget exists, update values
                    editingBudget.setLimit(lim);
                    editingBudget.setPeriod(period);
                    budgetManager.addBudget(editingBudget);
                    editingBudget = null; // Reset edit mode
                } else {
                    // Creating a new budget
                    Budget newBudget = new Budget(title, cat, lim, period);

                    boolean exists = budgetManager.getAllBudgets().stream()
                            .anyMatch(b -> b.getTitle().equals(title) && b.getCategory() == cat);

                    if (exists) {
                        Alert duplicate = new Alert(Alert.AlertType.INFORMATION, "This budget already exists.");
                        duplicate.show(); // <- you also forgot to show it!
                        return;
                    } else {
                        budgetManager.addBudget(newBudget);
                    }
                }

                refreshBudgets();

                titleField.clear();
                categoryComboBox.setValue(null);
                limitField.clear();
                periodComboBox.setValue(null);
        });

        getChildren().addAll(
                budgetTitle,
                new HBox(10, titleField, categoryComboBox, limitField, periodComboBox),
                addBudgetBtn,
                new Separator(),
                scrollPane,
                backBtn
        );
    }
    // Separate method for creating a budget row
    private HBox createBudgetRow(Budget budget) {
        Label budgetLabel = new Label(
                budget.getTitle() + " | " +
                        budget.getCategory() + " | $" +
                        budget.getLimit() + " limit | $" +
                        budget.getSpent() + " spent | $" +
                        budget.getRemaining() + " left | " +
                        budget.getPeriod().name().toLowerCase().replace("_", " ") + " budget"
        );

        // Create and add Edit/Delete Buttons for each Budget
        Button editBtn = new Button("✏️");
        Button deleteBtn = new Button("🗑");

        deleteBtn.setOnAction(actionEvent -> {
            budgetManager.removeBudget(budget);
            refreshBudgets();
        });

        editBtn.setOnAction(actionEvent -> {
            // Populate input fields with the current Budget's values
            titleField.setText(budget.getTitle());
            categoryComboBox.setValue(budget.getCategory());
            limitField.setText(String.valueOf(budget.getLimit()));
            periodComboBox.setValue(budget.getPeriod());

            budgetManager.removeBudget(budget); // Remove it temporarily from Map, replace after editing
            editingBudget = budget;
        });

        HBox budgetRow = new HBox(10, budgetLabel, editBtn, deleteBtn);
        budgetRow.setPadding(new Insets(5));
        budgetRow.setStyle("-fx-border-color: lightgray; -fx-border-width: 1;");
        return budgetRow;
    }
    public void refreshBudgets() {
        budgetManager.resetAllBudgetsIfNeeded(); // Only reset once per view update

        mainBudgetListVBox.getChildren().clear();
        for (Budget budget : budgetManager.getAllBudgets()) {
            mainBudgetListVBox.getChildren().add(createBudgetRow(budget));
        }
    }
}
