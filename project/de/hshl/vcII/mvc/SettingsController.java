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

import java.util.ArrayList;
import java.util.List;

import static project.de.hshl.vcII.utils.Utils.isDouble;

public class SettingsController {
    private MainModel mainWindowModel = MainModel.get();

    private Slider sl_Radius, sl_Weight, sl_Elasticity, sl_ScissorsSpeed;
    private Label lCurrentRadius, lCurrentWeight, lCurrentElasticity;
    private TextField tf_Wind_Y, tf_Wind_X, tf_v0_Y, tf_v0_X;
    private VBox vb_displayCurrentParams;
    private AnchorPane cRootPane;

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

        initVelocityFields(tf_v0_X);
        initVelocityFields(tf_v0_Y);
    }


    //_Parameter_Display________________________________________________________________________________________________
    public void updateParams() {
        if(tf_Wind_X != null & tf_Wind_Y != null) {
            tf_Wind_X.setText(String.valueOf(Math.round(Utils.getWind().x)));
            tf_Wind_Y.setText(String.valueOf(Math.round(Utils.getWind().y)));
        }
        mainWindowModel.getCurrentParamsController().update();
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
            if(tf.equals(tf_Wind_X)) {
                Utils.setWind(new MyVector(isDouble(tf_Wind_X), Utils.getWind().y));
                MainModel.get().getCurrentParamsController().getlWind().setText("(" + Utils.getWind().x + "/" + Utils.getWind().y + ")");
            }
            else if(tf.equals(tf_Wind_Y)) {
                Utils.setWind(new MyVector(Utils.getWind().x, isDouble(tf_Wind_Y)));
                MainModel.get().getCurrentParamsController().getlWind().setText("(" + Utils.getWind().x + "/" + Utils.getWind().y + ")");
            }
        });
    }
    private void initVelocityFields(TextField tf) {
        List<Ball> changedBalls = new ArrayList<>();
        tf.textProperty().addListener((observableValue, s, t1) -> {
            if(mainWindowModel.isChoiceMade() & mainWindowModel.getCurrentlySelected() instanceof Ball) {
                if (tf.equals(tf_v0_X)) {
                    ((Ball) mainWindowModel.getCurrentlySelected()).setVelVec(new MyVector(isDouble(tf_v0_X), ((Ball) mainWindowModel.getCurrentlySelected()).getVelVec().y));
                    ((Ball) mainWindowModel.getCurrentlySelected()).setVel0Vec(new MyVector(isDouble(tf_v0_X), ((Ball) mainWindowModel.getCurrentlySelected()).getVel0Vec().y));
                }
                else if(tf.equals(tf_v0_Y)) {
                    ((Ball) mainWindowModel.getCurrentlySelected()).setVelVec(new MyVector(((Ball) mainWindowModel.getCurrentlySelected()).getVelVec().x, isDouble(tf_v0_Y)));
                    ((Ball) mainWindowModel.getCurrentlySelected()).setVel0Vec(new MyVector(((Ball) mainWindowModel.getCurrentlySelected()).getVel0Vec().x, isDouble(tf_v0_Y)));
                }
                    MainModel.get().getCurrentParamsController().update();
                if (changedBalls.contains((Ball) mainWindowModel.getCurrentlySelected())) {
                    changedBalls.add((Ball) mainWindowModel.getCurrentlySelected());
                }
            }
            if(!mainWindowModel.isChoiceMade()) {
                for(Ball b : mainWindowModel.getBallManager().getBalls()) {
                    if (!changedBalls.contains(b)) {
                        if (tf.equals(tf_v0_X)) {
                            b.setVelVec(new MyVector(isDouble(tf_v0_X),  b.getVelVec().y));
                            b.setVel0Vec(new MyVector(isDouble(tf_v0_X), b.getVel0Vec().y));
                        }
                        else if(tf.equals(tf_v0_Y)) {
                            b.setVelVec(new MyVector( b.getVelVec().x, isDouble(tf_v0_Y)));
                            b.setVel0Vec(new MyVector(b.getVel0Vec().x, isDouble(tf_v0_Y)));
                        }
                            MainModel.get().getCurrentParamsController().update();
                    }
                }
            }
        });
    }

    public void reset(){
        tf_Wind_Y.setText("0");
        tf_Wind_X.setText("0");
        tf_v0_Y.setText("0");
        tf_v0_X.setText("0");
    }

    public void setV0() {
        if(mainWindowModel.isChoiceMade() & mainWindowModel.getCurrentlySelected() instanceof Ball) {
            ((Ball) mainWindowModel.getCurrentlySelected()).setVelVec(new MyVector(isDouble(tf_v0_X), isDouble(tf_v0_Y)));
        }

        if(!mainWindowModel.isChoiceMade()) {
            for(Ball b : mainWindowModel.getBallManager().getBalls()) {
                if(MyVector.length(b.getVelVec()) == 0) {
                    b.setVelVec(new MyVector(isDouble(tf_v0_X), isDouble(tf_v0_Y)));
                }
            }
        }

        updateParams();
    }
}
