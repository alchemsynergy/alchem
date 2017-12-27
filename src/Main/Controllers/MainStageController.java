package Main.Controllers;

import Main.Controllers.Retailers.ViewSaleController;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainStageController implements Initializable {


    private static double drawableStageWidth;
    private static double drawableStageHeight;
    private int f0,f1,f2,f3,f4;
    AnchorPane homePane = null;
    AnchorPane allFeaturesPane = null;
    AnchorPane dashboardPane = null;
    AnchorPane userDrawerPane = null;
    AnchorPane settingsDrawerPane = null;
    TranslateTransition openNavigation = null;
    TranslateTransition closeNavigation = null;

    @FXML
    private Button pane_button1, pane_button2, pane_button21, pane_button3, pane_button4;
    @FXML
    private Pane pane1, pane2, pane21, pane3, pane4;
    @FXML
    private StackPane switcher_pane;
    @FXML
    private StackPane drawer_parent;

    public MainStageController() throws IOException {
        homePane = FXMLLoader.load(getClass().getResource("../../Resources/Layouts/main_home_scene.fxml"));
        allFeaturesPane = FXMLLoader.load(getClass().getResource("../../Resources/Layouts/main_features_tab_scene.fxml"));
        dashboardPane = FXMLLoader.load(getClass().getResource("../../Resources/Layouts/main_dashboard_scene.fxml"));
        userDrawerPane = FXMLLoader.load(getClass().getResource("../../Resources/Layouts/NavigationDrawer/user.fxml"));
        settingsDrawerPane = FXMLLoader.load(getClass().getResource("../../Resources/Layouts/NavigationDrawer/settings.fxml"));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        /*Set private static variables for Main Stage width and Height*/

        drawableStageWidth = switcher_pane.getWidth();
        drawableStageHeight = switcher_pane.getHeight();

        /*Assign Buttons and Panes to their array references apply respective CSS styles*/
        Button[] buttons = new Button[5];
        buttons[0] = pane_button1;
        buttons[1] = pane_button2;
        buttons[2] = pane_button3;
        buttons[3] = pane_button4;
        buttons[4] = pane_button21;

        Pane[] panes = new Pane[5];
        panes[0] = pane1;
        panes[1] = pane2;
        panes[2] = pane3;
        panes[3] = pane4;
        panes[4] = pane21;


        f0 = 0;
        f1 = 0;
        f2 = 0;
        f3 = 0;
        f4 = 0;

        for (int i = 0; i < 5; i++) {
            /*Make 'i' effectively final*/
            int finalI = i;
            buttons[i].setOnMouseEntered((event) -> {
                /*Set CSS styling for buttons and panes 'onMouseEntered'*/
                buttons[finalI].setStyle("-fx-background-color: grey");
                panes[finalI].setStyle("-fx-background-color: aqua ");

            });

            buttons[i].setOnMouseExited((event) -> {
                /*Set CSS styling for buttons and panes 'onMouseEntered'*/
                int flag;
                int i1=finalI;
                if(i1==0)
                    flag = f0;
                else if(i1==1)
                    flag = f1;
                else if(i1==2)
                    flag = f2;
                else if(i1==3)
                    flag = f3;
                else
                    flag = f4;
                if(flag==0) {
                    buttons[finalI].setStyle("-fx-background-color: #200020");
                    panes[finalI].setStyle("-fx-background-color: #200020");
                }
            });

            buttons[i].setOnMouseClicked((event) -> {
                /*Set CSS styling for buttons and panes 'onMouseEntered'*/

                int i1=finalI;
                if(i1==0)
                    f0 = 1;
                else if(i1==1)
                    f1 = 1;
                else if(i1==2)
                    f2 = 1;
                else if(i1==3)
                    f3 = 1;
                else
                    f4 = 1;
                for(int j=0;j<5;j++){

                    if(j!=finalI){
                        buttons[j].setStyle("-fx-background-color: #200020");
                        panes[j].setStyle("-fx-background-color: #200020");
                    }

                }

            });

        }

        /*Create Tranisiton Animations for opening and closing of navigation drawers*/
        openNavigation = new TranslateTransition(new Duration(350), drawer_parent);
        openNavigation.setToX(0);

        closeNavigation = new TranslateTransition(new Duration(350), drawer_parent);
    }

    public void viewHomeScene() {
        switcher_pane.getChildren().clear();
        homePane.setPrefWidth(drawableStageWidth);
        homePane.setPrefHeight(drawableStageHeight);
        switcher_pane.getChildren().addAll(homePane);
    }


    public void viewDashboardScene() {
        switcher_pane.getChildren().clear();
        dashboardPane.setPrefWidth(drawableStageWidth);
        dashboardPane.setPrefHeight(drawableStageHeight);
        switcher_pane.getChildren().addAll(dashboardPane);
    }

        public void viewAllFeaturesScene() {
        switcher_pane.getChildren().clear();
        allFeaturesPane.setPrefWidth(drawableStageWidth);
        InventoryController.setDrawableWidth(drawableStageWidth);
        ViewSaleController.setDrawableWidth(drawableStageWidth);
        AddSaleController.setBillDrawableWidth(drawableStageWidth);
        allFeaturesPane.setPrefHeight(drawableStageHeight);
        switcher_pane.getChildren().addAll(allFeaturesPane);
    }

    public void viewUserDrawer() {
        /*Draw user.fxml into Parent Drawer Pane (StackPane) in main_stage.fxml */
        drawer_parent.getChildren().clear();
        drawer_parent.setPrefHeight(drawableStageHeight);
        drawer_parent.getChildren().addAll(userDrawerPane);

        /*Play transition set by Initialise()*/
        if (drawer_parent.getTranslateX() != 0) {
            openNavigation.play();
        } else {
            closeNavigation.setToX(-(drawer_parent.getWidth()));
            closeNavigation.play();
        }

    }

    public void viewSettingsDrawer() {
         /*Draw settings.fxml into Parent Drawer Pane (StackPane) in main_stage.fxml */
        drawer_parent.getChildren().clear();
        drawer_parent.setPrefHeight(drawableStageHeight);
        drawer_parent.getChildren().addAll(settingsDrawerPane);

        /*Play transition set by Initialise()*/
        if (drawer_parent.getTranslateX() != 0) {
            openNavigation.play();
        } else {
            closeNavigation.setToX(-(drawer_parent.getWidth()));
            closeNavigation.play();
        }
    }
}

