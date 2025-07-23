package papertrail.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import papertrail.model.Budget;
import papertrail.service.BudgetManager;


public class BudgetManagerView extends VBox {
    private final BudgetManager budgetManager;
    private final HBox budgetListsHBox;
    private final VBox mainBudgetListVBox = new VBox(5);

    public BudgetManagerView(BudgetManager budgetManager, Stage primaryStage, Scene mainMenuScene){
        // Store backend reference
        this.budgetManager = budgetManager;

        setSpacing(10);
        setPadding(new Insets(15));

        Label budgetTitle = new Label("Budget Manager");
        budgetTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        budgetListsHBox = new HBox(30); // Holds task labels
        budgetListsHBox.setPadding(new Insets(10));

        VBox mainBudgetSection = new VBox(10,
                new Label("Budget's List"),
                mainBudgetListVBox
        );
        budgetListsHBox.getChildren().addAll(mainBudgetSection);

        for (var budget : budgetManager.getAllBudgets()) {
            Label budgetLabel = new Label(
                    budget.getTitle() + " | " +
                            budget.getCategory() + " | $" +
                            budget.getLimit()
            );
            mainBudgetListVBox.getChildren().add(budgetLabel);
        }

        ScrollPane scrollPane = new ScrollPane(budgetListsHBox);
        scrollPane.setFitToWidth(true);

        javafx.scene.control.Button backBtn = new javafx.scene.control.Button("⬅ Back to Main Menu");
        backBtn.setOnAction(actionEvent -> primaryStage.setScene(mainMenuScene));

        getChildren().addAll(
                budgetTitle,
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
