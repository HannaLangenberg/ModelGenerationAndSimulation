package project.de.hshl.vcII.entities.moving;

import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import project.de.hshl.vcII.utils.MyVector;

import java.util.Arrays;

public class Ball extends Circle {
    private static final double RADIUS = 25, MASS = 20;

    private double mass;
    private MyVector posVec;
    private MyVector velVec;
    private MyVector accVec;
    private Color randomCol = Color.color(Math.random(), Math.random(), Math.random());
    private double randomSeed = Math.random();
    private int number;

    public Ball() {
        this.mass = MASS;
        this.setRadius(RADIUS);
        this.setFill(randomCol);
        this.setStroke(randomSeed > 0.5 ? randomCol.brighter() : randomCol.darker());
        this.setStrokeWidth(5);
    }

    public Ball(int number, MyVector posVec, MyVector velVec, MyVector accVec, double radius, double mass) {
        this.number = number;
        this.posVec = posVec;
        this.velVec = velVec;
        this.accVec = accVec;
        setRadius(radius);
        this.mass = mass;

        this.setFill(randomCol);
        this.setStroke(randomSeed > 0.5 ? randomCol.brighter() : randomCol.darker());
        this.setStrokeWidth(5);
    }

    public void draw(AnchorPane aDrawingPane) {
        Platform.runLater(() -> {
            aDrawingPane.getChildren().remove(this);
            aDrawingPane.getChildren().add(this);
        });
    }
    @Override
    public String toString() {
        return "No: " + this.number + ". -- Pos: (" + Math.round(this.posVec.x)+"/"+ Math.round(this.posVec.y)
                + ") -- v: (" + Math.round(this.velVec.x) +"/"+ Math.round(this.velVec.y)
                + ") -- a: (" + Math.round(this.accVec.x) +"/"+ Math.round(this.accVec.y)
                + ") -- Masse: " + this.mass + " -- Radius: " + this.getRadius();
    }

    //----Getter-&-Setter-----------------------------------------------------------------------------------------------
    public void setNumber(int number) {
        this.number = number;
    }

    public void setPosVec(MyVector posVec) {
        this.posVec = posVec;
        this.setCenterX(posVec.x);
        this.setCenterY(posVec.y);
    }
    public MyVector getPosVec() {
        return posVec;
    }

    public void setVelVec(MyVector velVec) {
        this.velVec = velVec;
    }
    public MyVector getVelVec() {
        return velVec;
    }

    public void setAccVec(MyVector accVec) {
        this.accVec = accVec;
    }
    public MyVector getAccVec() {
        return accVec;
    }

    public double getMass() {
        return mass;
    }
    public void setMass(double mass) {
        this.mass = mass;
    }
}
