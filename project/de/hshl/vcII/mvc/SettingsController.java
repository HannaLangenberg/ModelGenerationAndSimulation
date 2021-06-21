package project.de.hshl.vcII.mvc;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.utils.MyVector;
import project.de.hshl.vcII.utils.Utils;

import static project.de.hshl.vcII.utils.Utils.isDouble;

public class SettingsController {
    private MainModel mainWindowModel = MainModel.get();

    private Slider sl_Radius, sl_Weight, sl_Elasticity, sl_ScissorsSpeed;
    private Label lCurrentRadius, lCurrentWeight, lCurrentElasticity;
    private TextField tf_Wind_Y, tf_Wind_X, tf_v0_Y, tf_v0_X;
    private VBox vb_displayCurrentParams;
    private AnchorPane cRootPane;
    private CurrentParamsController currentParamsController;

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
        lCurrentWeight.setText(lCurrentWeight.getText() + " " + sl_Weight.getValue());
        lCurrentElasticity.setText(lCurrentElasticity.getText() + " " + sl_Elasticity.getValue()*100);

        initWindFields(tf_Wind_X);
        initWindFields(tf_Wind_Y);
    }


    //_Parameter_Display________________________________________________________________________________________________
    public void updateParams() {
        MainModel.get().getCurrentParamsController().update();
    }


    //_Parameter_Setting________________________________________________________________________________________________
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
            lCurrentWeight.setText("Aktuelles Gewicht [Kg]: " + ((int)((Ball) mainWindowModel.getCurrentlySelected()).getMass()));
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

    //_Helpers,_Getter_&_Setter_________________________________________________________________________________________

    private void initWindFields(TextField tf) {
        tf.textProperty().addListener((observableValue, s, t1) -> {
            Utils.setWind(new MyVector(isDouble(tf_Wind_X), isDouble(tf_Wind_Y)));
            MainModel.get().getCurrentParamsController().getlWind().setText("(" + tf_Wind_X.getText() + "/" + tf_Wind_Y.getText() + ")");
        });
    }

    public void reset(){
        tf_Wind_Y.setText("0");
        tf_Wind_X.setText("0");
        tf_v0_Y.setText("0");
        tf_v0_X.setText("0");
    }

    public void setV0() {
        for(Ball b: mainWindowModel.getBallManager().getBalls()){
            b.setVelVec(new MyVector( isDouble(tf_v0_X), isDouble(tf_v0_Y)));
        }
    }
}
