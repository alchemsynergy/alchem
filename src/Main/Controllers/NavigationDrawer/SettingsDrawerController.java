package Main.Controllers.NavigationDrawer;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsDrawerController implements Initializable {

    @FXML
    private Button pane_button;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        /*Assigning button CSS style*/
        Button buttons = new Button();
        buttons = pane_button;

        Button finalButtons = buttons;
        buttons.setOnMouseEntered((MouseEvent event) -> {
            /*Set CSS styling for button 'onMouseEntered'*/
            finalButtons.setStyle("-fx-background-color: #200020");

        });


        buttons.setOnMouseExited((MouseEvent event) -> {
            /*Set CSS styling for button 'onMouseExit'*/
            finalButtons.setStyle("-fx-background-color: grey");

        });

        buttons.setOnMouseClicked((MouseEvent event) -> {
            try {
                /*Opening login page*/
                FXMLLoader fxmlMainStage = new FXMLLoader(getClass().getResource("../../../Resources/Layouts/login_stage.fxml"));
                Parent root1 = (Parent) fxmlMainStage.load();
                Stage mainStage = new Stage();
                mainStage.setScene(new Scene(root1));
                mainStage.setResizable(false);
                mainStage.initStyle(StageStyle.UNDECORATED);
                mainStage.show();

                /*Closing the present window*/
                ((Stage) pane_button.getScene().getWindow()).close();
            }
            catch(Exception e){
                System.out.println(e.toString());
            }

        });

        }

}


