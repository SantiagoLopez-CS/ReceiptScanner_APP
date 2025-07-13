package papertrail;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PaperTrailApp extends Application{
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("📋 PaperTrail Dashboard"); // Set Title of Stage

        // Basic Layout (vertical)
        VBox root = new VBox();
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Buttons for each feature
        Button tasksBtn = new Button("Task Manager");
        Button receiptsBtn = new Button("Receipt Manager ");
        Button budgetsBtn = new Button("Budget Manager");

        // TODO: Add actions to open different views later
        tasksBtn.setOnAction(e -> System.out.println("Tasks View - Coming Soon"));
        receiptsBtn.setOnAction(e -> System.out.println("Receipts View - Coming Soon"));
        budgetsBtn.setOnAction(e -> System.out.println("Budgets View - Coming Soon"));

        // Add to layout
        root.getChildren().addAll(tasksBtn, receiptsBtn, budgetsBtn);

        // Set the Scene and show(make visible)
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    // Launch the GUI
    public static void main(String[] args) {
        launch(args);
    }
}

