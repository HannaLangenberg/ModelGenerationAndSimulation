package project.de.hshl.vcII.entities.moving;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import project.de.hshl.vcII.drawing.visuals.Arrow;
import project.de.hshl.vcII.mvc.MainWindowModel;
import project.de.hshl.vcII.utils.MyVector;

public class Ball extends Ellipse {
    private static final double RADIUS = 25, MASS = 2.75, DEFAULT_ELASTICITY = 0.5;

    private DoubleProperty mass;
    private DoubleProperty radius;
    private DoubleProperty totE;
    private DoubleProperty potE;
    private DoubleProperty kinE;
    private DoubleProperty lostE;
    private DoubleProperty elasticity;
    private MyVector posVec;
    private MyVector velVec;
    private MyVector vel0Vec;
    private MyVector accVec;
    private MyVector frcVec;
    private MyVector rolVec;
    private Arrow arrow;
    private boolean isColliding;
    private Arrow a;

    private Color randomCol = Color.color(Math.random(), Math.random(), Math.random());
    private int number;

    private double randomSeed = pickColor();
    private double pickColor() {
        double r = Math.random();

        while(r < 0.5 && MainWindowModel.get().getMode().isMode())
            r = Math.random();
        while(r > 0.5 && !MainWindowModel.get().getMode().isMode())
            r = Math.random();

        return r;
    }
    private Color strokeColor = randomSeed > 0.5 ? randomCol.brighter() : randomCol.darker();

    public Ball() {
        this.mass = new SimpleDoubleProperty(MASS);
        this.radius = new SimpleDoubleProperty(RADIUS);
        this.elasticity = new SimpleDoubleProperty(DEFAULT_ELASTICITY);
        this.totE = new SimpleDoubleProperty();
        this.potE = new SimpleDoubleProperty();
        this.kinE = new SimpleDoubleProperty();
        this.lostE = new SimpleDoubleProperty();
        this.setFill(randomCol);
        this.setStroke(strokeColor);
        this.setStrokeWidth(5);
        this.isColliding = false;
        this.toBack();
    }

    public Ball(int number, MyVector posVec, MyVector velVec, MyVector vel0Vec, MyVector accVec, MyVector frcVec, double radius, double mass, double elasticity, double totE, double potE, double kinE, double lostE) {
        this.number = number;
        this.posVec = posVec;
        this.velVec = velVec;
        this.vel0Vec = vel0Vec;
        this.accVec = accVec;
        this.frcVec = frcVec;
        // Haft- und Gleitreibung für Stein auf Holz (i.d.R.)
        // Der maximale Böschungswinkel wäre 41,98°, daher haben wir es skaliert mit 3.75, Bewegung bei ca 13.49°
        setFrcVec(new MyVector(0.24, 0.18));
        // Rollreibung für Fahrradreifen auf Straße zwei Komponenten, da ein Breich angeben war. 0,002 - 0,004
        setRolVec(new MyVector(0.002, 0.004));
        this.mass = new SimpleDoubleProperty(mass);
        this.radius = new SimpleDoubleProperty(radius);
        this.elasticity = new SimpleDoubleProperty(elasticity);
        this.totE = new SimpleDoubleProperty(totE);
        this.potE = new SimpleDoubleProperty(potE);
        this.kinE = new SimpleDoubleProperty(kinE);
        this.lostE = new SimpleDoubleProperty(lostE);
        this.setRadius(radius);
        this.setStrokeWidth(5);
        this.setFill(randomCol);
        this.setStroke(strokeColor);
        this.isColliding = false;
        this.toBack();
    }

    public void draw(AnchorPane aDrawingPane) {
        Platform.runLater(() -> {
            aDrawingPane.getChildren().remove(this);
            aDrawingPane.getChildren().add(this);
            aDrawingPane.getChildren().remove(a);
            if (MainWindowModel.get().isArrowsActive())
            {
                a = this.drawArrow();
                aDrawingPane.getChildren().add(a);
            }
        });
    }

    public Arrow drawArrow() {
        arrow = new Arrow();
        MyVector direction = MyVector.add(posVec, velVec);
        //Stop[] stops = new Stop[]{new Stop(0, Color.RED), new Stop(1, Color.BLUE)};
        arrow.getLine().setStroke(Color.RED);
        arrow.getLine().setStrokeWidth(2);
        arrow.setStartX(posVec.x);
        arrow.setStartY(posVec.y);
        arrow.setEndX(direction.x);
        arrow.setEndY(direction.y);
        return arrow;
    }

    //_GETTERS_&_SETTERS________________________________________________________________________________________________
    //int
    public void setNumber(int number) {
        this.number = number;
    }
    public int getNumber() {
        return number;
    }

    //Arrow
    public Arrow getArrow() { return arrow; }

    //boolean
    public void setColliding(boolean colliding) {
        isColliding = colliding;
    }
    public boolean isColliding() {
        return isColliding;
    }

    //Color
    public Color getStrokeColor() {
        return strokeColor;
    }

    //MyVector
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

    public void setVel0Vec(MyVector vel0Vec) {
        this.vel0Vec = vel0Vec;
    }
    public MyVector getVel0Vec() {
        return vel0Vec;
    }

    public void setAccVec(MyVector accVec) {
        this.accVec = accVec;
    }
    public MyVector getAccVec() {
        return accVec;
    }

    public void setFrcVec(MyVector frcVec) {
        this.frcVec = frcVec;
    }
    public MyVector getFrcVec() {
        return frcVec;
    }

    public void setRolVec(MyVector rolVec) {
        this.rolVec = rolVec;
    }
    public MyVector getRolVec() {
        return rolVec;
    }

    //DoubleProperty
    public void setMass(double mass) {
        this.mass.set(mass);
    }
    public double getMass() {
        return mass.get();
    }
    public DoubleProperty massProperty() {
        return mass;
    }

    public void setRadius(double radius) {
        this.radius.set(radius);
        this.setRadiusX(radius);
        this.setRadiusY(radius);
    }
    public double getRadius() {
        return radius.get();
    }
    public DoubleProperty radiusProperty() {
        return radius;
    }

    public void setPotE(double potE) {
        this.potE.set(potE);
    }
    public double getPotE() {
        return potE.get();
    }
    public DoubleProperty potEProperty() {
        return potE;
    }

    public void setKinE(double kinE) {
        this.kinE.set(kinE);
    }
    public double getKinE() {
        return kinE.get();
    }
    public DoubleProperty kinEProperty() {
        return kinE;
    }

    public void setLostE(double lostE) {
        this.lostE.set(lostE);
    }
    public double getLostE() {
        return lostE.get();
    }
    public DoubleProperty lostEProperty() {
        return lostE;
    }

    public void setTotE(double totE) {
        this.totE.set(totE);
    }
    public double getTotE() {
        return totE.get();
    }
    public DoubleProperty totEProperty() {
        return totE;
    }

    public void setElasticity(double elasticity) {
        this.elasticity.set(elasticity);
    }
    public double getElasticity() {
        return elasticity.get();
    }
    public DoubleProperty elasticityProperty() {
        return elasticity;
    }

    //_toString()_______________________________________________________________________________________________________
    @Override
    public String toString() {
        return "No: " + this.number + ". -- Pos: (" + Math.round(this.posVec.x)+"/"+ Math.round(this.posVec.y)
                + ") -- v: (" + Math.round(this.velVec.x) +"/"+ Math.round(this.velVec.y)
                + ") -- a: (" + Math.round(this.accVec.x) +"/"+ accVec.y
                + ") -- Masse: " + this.mass + " -- Radius: " + this.getRadius();
    }

}
