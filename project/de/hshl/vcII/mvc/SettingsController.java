package project.de.hshl.vcII.mvc;

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

    private Slider sl_Radius, sl_Weight, sl_Elasticity, sl_ScissorsSpeed;
    private Label lCurrentRadius, lCurrentWeight, lCurrentElasticity;
    private TextField tf_Wind_Y, tf_Wind_X, tf_v0_Y, tf_v0_X;
    private VBox vb_displayCurrentParams;
    private AnchorPane cRootPane;
    private CurrentParamsController currentParamsController;

    private boolean currentParamsOpen = false;

    public void initialize(Slider sl_ScissorsSpeed, Slider sl_Radius, Label lCurrentRadius, Slider sl_Weight, Label lCurrentWeight,
                           Slider sl_Elasticity, Label lCurrentElasticity, TextField tf_Wind_X, TextField tf_Wind_Y,
                           TextField tf_v0_X, TextField tf_v0_Y, VBox vb_displayCurrentParams){
        this.sl_ScissorsSpeed = sl_ScissorsSpeed;
        this.sl_Radius = sl_Radius;
        this.lCurrentRadius = lCurrentRadius;
        this.sl_Weight = sl_Weight;
        this.lCurrentWeight = lCurrentWeight;
        this.sl_Elasticity = sl_Elasticity;
        this.lCurrentElasticity = lCurrentElasticity;
        this.tf_Wind_X = tf_Wind_X;
        this.tf_Wind_Y = tf_Wind_Y;
        this.tf_v0_X = tf_v0_X;
        this.tf_v0_Y = tf_v0_Y;
        this.vb_displayCurrentParams = vb_displayCurrentParams;
        this.cRootPane = new AnchorPane();

        lCurrentRadius.setText(lCurrentRadius.getText() + " " + sl_Radius.getValue());
        lCurrentWeight.setText(lCurrentWeight.getText() + " " + sl_Weight.getValue()); // TODO Gewicht in Kg
        lCurrentElasticity.setText(lCurrentElasticity.getText() + " " + sl_Elasticity.getValue()*100);

        initWindFields(tf_Wind_X);
        initWindFields(tf_Wind_Y);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/currentParams.fxml"));
            this.cRootPane = loader.load();
            currentParamsController = loader.getController();
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

            stage.setHeight(stage.getHeight() + cRootPane.getPrefHeight());

            vb_displayCurrentParams.getChildren().add(cRootPane);
            currentParamsOpen = true;

            // check all TextFields for values
            fillVariables();
            showCurrentParams();
        }
    }


    //-Parameter-Setting------------------------------------------------------------------------------------------------
    //_slider__
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

    public void sl_Elasticity_OnDragDetected() {
        if(mainWindowModel.getCurrentlySelected() instanceof Ball){
            ((Ball) mainWindowModel.getCurrentlySelected()).setElasticity(sl_Elasticity.getValue());
            lCurrentElasticity.setText("Aktuelle Elastizität [%]: " + (((Ball) mainWindowModel.getCurrentlySelected()).getElasticity())*100);
        }
        System.out.println("-----\t" + sl_Elasticity.getValue()*100 + "\t-----");
    }

    public void sl_ScissorsSpeed_OnDragDetected(){
        mainWindowModel.setScissorsSpeed(sl_ScissorsSpeed.getValue());
    }

    //------Helpers,-Getter-&-Setter------------------------------------------------------------------------------------
    public void fillVariables() {
        Utils.setWind(new MyVector(isDouble(tf_Wind_X), isDouble(tf_Wind_Y)));
    }

    private void initWindFields(TextField tf) {
        tf.textProperty().addListener((observableValue, s, t1) -> {
            Utils.setWind(new MyVector(isDouble(tf_Wind_X), isDouble(tf_Wind_Y)));
        });
    }

    public CurrentParamsController getCurrentParamsController() {
        return currentParamsController;
    }

    public void setV0() {
        for(Ball b: mainWindowModel.getBallManager().getBalls()){
            b.setVelVec(new MyVector( isDouble(tf_v0_X), isDouble(tf_v0_Y)));
        }
    }


}
