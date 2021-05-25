package project.de.hshl.vcII.mvc;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.utils.MyVector;
import project.de.hshl.vcII.utils.Utils;

import java.io.IOException;

import static project.de.hshl.vcII.utils.Utils.isDouble;

public class SettingsController {
    private MainWindowModel mainWindowModel = MainWindowModel.get();

    private Slider sl_Radius, sl_Weight;
    private Label lCurrentRadius, lCurrentWeight;
    private TextField tf_Wind_Y, tf_Wind_X, tf_v0_Y, tf_v0_X;
    private VBox vb_displayCurrentParams;
    private AnchorPane cRootPane;
    private CurrentParamsController currentParamsController;

    private boolean v0_changed = false;
    private boolean wind_changed = false;
    private boolean currentParamsOpen = false;

    public void initialize(Slider sl_Radius, Label lCurrentRadius, Slider sl_Weight, Label lCurrentWeight,
                           TextField tf_Wind_X, TextField tf_Wind_Y, TextField tf_v0_X, TextField tf_v0_Y,
                           VBox vb_displayCurrentParams, AnchorPane cRootPane){
        this.sl_Radius = sl_Radius;
        this.lCurrentRadius = lCurrentRadius;
        this.sl_Weight = sl_Weight;
        this.lCurrentWeight = lCurrentWeight;
        this.tf_Wind_X = tf_Wind_X;
        this.tf_Wind_Y = tf_Wind_Y;
        this.tf_v0_X = tf_v0_X;
        this.tf_v0_Y = tf_v0_Y;
        this.vb_displayCurrentParams = vb_displayCurrentParams;
        this.cRootPane = cRootPane;

        initWindFields(tf_Wind_X);
        initWindFields(tf_Wind_Y);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/currentParams.fxml"));
            currentParamsController = loader.getController();
            this.cRootPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //-Parameter-Display------------------------------------------------------------------------------------------------
    public void showCurrentParams() {
        currentParamsController.update();
    }

    public void btn_showCurrentParams_OnAction() throws IOException {
        Stage stage = mainWindowModel.getStage();

        if(currentParamsOpen)
        {
            stage.setHeight(mainWindowModel.getSavedSceneHeight());
            vb_displayCurrentParams.getChildren().remove(cRootPane);
            currentParamsOpen = false;
        }
        else
        {
            mainWindowModel.setSavedSceneHeight(stage.getHeight());

            stage.setHeight(600);

            vb_displayCurrentParams.getChildren().add(cRootPane);
            currentParamsOpen = true;


            // check all TextFields for values
            fillVariables();
            showCurrentParams();
        }
    }


    //-Parameter-Setting------------------------------------------------------------------------------------------------
    public void sl_Radius_OnDragDetected(){
        if(mainWindowModel.getCurrentlySelected() instanceof Ball){
            ((Ball) mainWindowModel.getCurrentlySelected()).setRadius(sl_Radius.getValue());
            lCurrentRadius.setText("Aktueller Radius [px]: " + ((int)((Ball) mainWindowModel.getCurrentlySelected()).getRadius()));
        }
        System.out.println("-----\t" + sl_Radius.getValue() + "\t-----");
    }

    public void sl_Weight_OnDragDetected() {
        if(mainWindowModel.getCurrentlySelected() instanceof Ball){
            ((Ball) mainWindowModel.getCurrentlySelected()).setMass(sl_Weight.getValue());
            lCurrentWeight.setText("Aktuelles Gewicht [g]: " + ((int)((Ball) mainWindowModel.getCurrentlySelected()).getMass()));
        }
        System.out.println("-----\t" + sl_Weight.getValue() + "\t-----");
    }

    //------Helpers,-Getter-&-Setter------------------------------------------------------------------------------------
    public void fillVariables() {
        Utils.setWind(new MyVector(isDouble(tf_Wind_X), isDouble(tf_Wind_Y)));
        for(Ball b: mainWindowModel.getBallManager().getBalls()){
            b.setVelVec(new MyVector(isDouble(tf_v0_X), isDouble(tf_v0_Y)));
//            b.setAccVec(new MyVector(0,0));
        }
    }

    private void initWindFields(TextField tf) {
        tf.textProperty().addListener((observableValue, s, t1) -> {
            wind_changed = true;
            Utils.setWind(new MyVector(isDouble(tf_Wind_X), isDouble(tf_Wind_Y)));
        });
    }

    public CurrentParamsController getCurrentParamsController() {
        return currentParamsController;
    }
}
