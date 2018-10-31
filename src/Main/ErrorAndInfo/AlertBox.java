package Main.ErrorAndInfo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class AlertBox {
    public AlertBox(Stage primaryStage, FXMLLoader fxmlLoader, boolean alertType, String msg) {
        try {
            Parent root = (Parent) fxmlLoader.load();
            Scene scene = new Scene(root);
            Label alertText = (Label) scene.lookup("#alert_text");
            alertText.setText(msg);
             System.out.println("");
            if (alertType == true)
                alertText.setStyle("-fx-text-fill: chartreuse");
            else
                alertText.setStyle("-fx-text-fill: red");
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (Exception e) {
        }
    }
}
