package papertrail.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import papertrail.model.Category;
import papertrail.model.Receipt;
import papertrail.service.ReceiptManager;

import java.time.LocalDate;

public class ReceiptManagerView extends VBox {
    private final ReceiptManager receiptManager;
    private final HBox receiptContentHBox;
    private final VBox receiptListVBox = new VBox(5);

    public ReceiptManagerView(ReceiptManager receiptManager, Stage primaryStage, Scene mainMenuScene) {
        this.receiptManager = receiptManager;

        setSpacing(10);
        setPadding(new Insets(15));

        Label receiptTitle = new Label("Receipt Manager");
        receiptTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField storeNameField = new TextField();
        storeNameField.setPromptText("Store Name");

        ComboBox<Category> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll(Category.values());
        categoryComboBox.setPromptText("Category");

        DatePicker dayOfPurchase = new DatePicker();
        dayOfPurchase.setPromptText("Day of Purchase");
        dayOfPurchase.setValue(LocalDate.now());

        TextField spentField = new TextField();
        spentField.setPromptText("Amount Spent");

        Button addReceiptBtn = new Button("Add Receipt");

        receiptContentHBox = new HBox(30);
        receiptContentHBox.setPadding(new Insets(10));

        VBox mainReceiptSection = new VBox(10, receiptListVBox);
        receiptContentHBox.getChildren().add(mainReceiptSection);

        refreshReceipts();

        ScrollPane scrollPane = new ScrollPane(receiptContentHBox);
        scrollPane.setFitToWidth(true);

        Button backBtn = new Button("Back to Main Menu");
        backBtn.setOnAction(actionEvent -> primaryStage.setScene(mainMenuScene));

        addReceiptBtn.setOnAction(actionEvent -> {
            String store = storeNameField.getText().trim();
            Category cat = categoryComboBox.getValue();
            LocalDate day = dayOfPurchase.getValue();
            double spent;

            try {
                spent = Double.parseDouble(spentField.getText());
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid number for 'Amount Spent'.");
                alert.show();
                return;
            }

            Receipt newReceipt = new Receipt(store, cat, day, spent);
            receiptManager.addReceipt(newReceipt);

            storeNameField.clear();
            categoryComboBox.setValue(null);
            dayOfPurchase.setValue(null);
            spentField.clear();

            refreshReceipts();
        });

        getChildren().addAll(
                receiptTitle,
                new HBox(10, storeNameField, categoryComboBox),
                new HBox(10, dayOfPurchase, spentField),
                addReceiptBtn,
                new Separator(),
                scrollPane,
                backBtn
        );
    }

    public void refreshReceipts() {
        receiptListVBox.getChildren().clear();

        for (Receipt receipt : receiptManager.getAllReceipts()) {
            Label receiptLabel = new Label(
                    receipt.getStore() + " | " +
                    receipt.getCategory() + " | " +
                    receipt.getDayOfPurchase() + " | $" +
                    receipt.getAmountSpent() + " spent"
            );

            Button deleteBtn = new Button("Delete");
            deleteBtn.setOnAction(actionEvent -> {
                receiptManager.removeReceipt(receipt.getId());
                refreshReceipts();
            });

            HBox receiptRow = new HBox(10, receiptLabel, deleteBtn);
            receiptRow.setPadding(new Insets(5));
            receiptRow.setStyle("-fx-border-color: lightgray; -fx-border-width: 1;");

            receiptListVBox.getChildren().add(receiptRow);
        }
    }
}
