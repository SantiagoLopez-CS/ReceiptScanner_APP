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


public class BudgetManagerView extends VBox {
    private final BudgetManager budgetManager;
    private final HBox budgetContentHBox;
    private final VBox mainBudgetListVBox = new VBox(5);

    public BudgetManagerView(BudgetManager budgetManager, Stage primaryStage, Scene mainMenuScene){
        // Store backend reference
        this.budgetManager = budgetManager;

        setSpacing(10);
        setPadding(new Insets(15));

        Label budgetTitle = new Label("Budget Manager");
        budgetTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // addBudget fields
        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        ComboBox<Category> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll(Category.values());
        categoryComboBox.setPromptText("Category");
        TextField limitField = new TextField();
        limitField.setPromptText("Limit");
        ComboBox<BudgetPeriod> periodComboBox = new ComboBox<>();
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

                Budget newBudget = new Budget(title, cat, lim, period);

                boolean exists = budgetManager.getAllBudgets().stream()
                        .anyMatch(b -> b.getTitle().equals(title) && b.getCategory() == cat);

                if (exists) {
                    Alert duplicate = new Alert(Alert.AlertType.INFORMATION, "This budget already exists.");
                    duplicate.show(); // <- you also forgot to show it!
                    return;
                }

                    refreshBudgets();

                    titleField.clear();
                    categoryComboBox.setValue(null);
                    limitField.clear();
        });

        getChildren().addAll(
                budgetTitle,
                new HBox(10, titleField, categoryComboBox, limitField),
                addBudgetBtn,
                new Separator(),
                scrollPane,
                backBtn
        );
    }
    public void refreshBudgets() {
        mainBudgetListVBox.getChildren().clear();
        for (var budget : budgetManager.getAllBudgets()) {
            Label budgetLabel = new Label(
                    budget.getTitle() + " | " +
                    budget.getCategory() + " | $" +
                    budget.getLimit() + " limit | $" +
                    budget.getSpent() + " spent | $" +
                    budget.getRemaining() + " left"
            );
            mainBudgetListVBox.getChildren().add(budgetLabel);
        }
    }
}
