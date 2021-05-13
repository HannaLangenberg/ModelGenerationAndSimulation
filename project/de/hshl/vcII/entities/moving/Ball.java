package project.de.hshl.vcII.entities.moving;

import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import project.de.hshl.vcII.utils.MyVector;

public class Ball extends Circle {
    private static final double RADIUS = 25, MASS = 20;

    private double mass;
    private MyVector posVec;
    private MyVector velVec;
    private MyVector accVec;
    private Color randomCol = Color.color(Math.random(), Math.random(), Math.random());
    private double randomSeed = Math.random();

    public Ball() {
        this.mass = MASS;
        this.setRadius(RADIUS);
        this.setFill(randomCol);
        this.setStroke(randomSeed > 0.5 ? randomCol.brighter() : randomCol.darker());
        this.setStrokeWidth(5);
    }

    public Ball(MyVector posVec, MyVector velVec, MyVector accVec) {
        this.posVec = posVec;
        this.velVec = velVec;
        this.accVec = accVec;
    }
    public void draw(AnchorPane aDrawingPane) {
        Platform.runLater(() -> {
            aDrawingPane.getChildren().remove(this);
            aDrawingPane.getChildren().add(this);
        });
    }

    //----Getter-&-Setter-----------------------------------------------------------------------------------------------
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
