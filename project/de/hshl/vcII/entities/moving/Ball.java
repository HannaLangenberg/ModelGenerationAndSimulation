package project.de.hshl.vcII.entities.moving;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.util.Duration;
import project.de.hshl.vcII.drawing.visuals.Arrow;
import project.de.hshl.vcII.mvc.MainModel;
import project.de.hshl.vcII.utils.MyVector;

public class Ball extends Ellipse {
    private static final double RADIUS = 25, MASS = 2.75, DEFAULT_ELASTICITY = 0.5;
    private Tooltip tooltip = new Tooltip();

    private DoubleProperty mass;
    private DoubleProperty radius;
    private DoubleProperty totE;
    private DoubleProperty potE;
    private DoubleProperty kinE;
    private DoubleProperty lostE;
    private DoubleProperty elasticity;
    private double initialTotE_c, totE_c, potE_c, kinE_c, lostE_c;
    private MyVector posVec;
    private MyVector velVec;
    private MyVector vel0Vec;
    private MyVector accVec;
    private MyVector frcVec;
    private MyVector rolVec;
    private Arrow arrow = new Arrow();
    private boolean colliding_Orthogonal_F, colliding_Parallel_B;
    private Arrow a = new Arrow();

    private Color randomCol = Color.color(Math.random(), Math.random(), Math.random());
    private int number;

    private double randomSeed = pickColor();
    private double pickColor() {
        double r = Math.random();

        while(r < 0.5 && MainModel.get().getMode().isMode())
            r = Math.random();
        while(r > 0.5 && !MainModel.get().getMode().isMode())
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
        this.colliding_Orthogonal_F = false;
        this.toBack();
        addMouseMoved();
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
        this.colliding_Orthogonal_F = false;
        this.toBack();
        addMouseMoved();
    }

    /**
     * Used by the load function
     * @param initialTotE parsed to double
     * @param totE parsed to double
     * @param potE parsed to double
     * @param kinE parsed to double
     * @param lostE parsed to double
     * @param posVec parsed to MYVector
     * @param velVec parsed to MYVector
     * @param vel0Vec parsed to MYVector
     * @param accVec parsed to MYVector
     * @param frcVec parsed to MYVector
     * @param rolVec parsed to MYVector
     * @param arrow DELETE THIS
     * @param colliding_Orthogonal_F parsed to boolean
     * @param colliding_Parallel_B parsed to boolean
     * @param a DELETE THIS
     * @param randomCol Color.valueOf
     * @param randomSeed  parsed to double
     * @param strokeColor Color.valueOf
     */
    public Ball(String initialTotE, String totE, String potE, String kinE, String lostE, String posVec,
                String velVec, String vel0Vec, String accVec, String frcVec, String rolVec, String arrow,
                String colliding_Orthogonal_F, String colliding_Parallel_B, String a, String randomCol, String randomSeed,
                String strokeColor, String radius, String mass, String elasticity){
        this.totE = new SimpleDoubleProperty(Double.parseDouble(totE));
        this.potE = new SimpleDoubleProperty(Double.parseDouble(potE));
        this.kinE = new SimpleDoubleProperty(Double.parseDouble(kinE));
        this.lostE = new SimpleDoubleProperty(Double.parseDouble(lostE));
        this.initialTotE_c = Double.parseDouble(initialTotE);
        this.totE_c = Double.parseDouble(totE);
        this.potE_c = Double.parseDouble(potE);
        this.kinE_c = Double.parseDouble(kinE);
        this.lostE_c = Double.parseDouble(lostE);
        this.posVec = new MyVector(posVec);
        this.vel0Vec = new MyVector(vel0Vec);
        this.velVec = new MyVector(velVec);
        this.accVec = new MyVector(accVec);
        this.frcVec = new MyVector(frcVec);
        this.rolVec = new MyVector(rolVec);
        //this.arrow = new Arrow(arrow);
        this.colliding_Orthogonal_F = Boolean.parseBoolean(colliding_Orthogonal_F);
        this.colliding_Parallel_B = Boolean.parseBoolean(colliding_Parallel_B);
        //this.a = new Arrow(a);
        this.randomCol = Color.valueOf(randomCol);
        this.randomSeed = Double.parseDouble(randomSeed);
        this.strokeColor = Color.valueOf(strokeColor);
        this.mass = new SimpleDoubleProperty(MASS);
        setMass(Double.parseDouble(mass));
        this.radius = new SimpleDoubleProperty(RADIUS);
        setRadius(Double.parseDouble(radius));
        this.elasticity = new SimpleDoubleProperty(DEFAULT_ELASTICITY);
        setElasticity(Double.parseDouble(elasticity));
        this.setFill(this.randomCol);
        this.setStroke(this.strokeColor);
        this.setStrokeWidth(5);
        this.colliding_Orthogonal_F = false;
        this.toBack();
    }

    /**
     * Is called any time but only shows the Tooltip if the simulation is not running and 'W' was not pressed.
     */
    private void addMouseMoved(){
        this.setOnMouseMoved((e) -> {
            if (MainModel.get().getSimulator().isRunning()) {
                Tooltip.uninstall(this, tooltip);
                return;
            }
            String sTooltip = this.toString();
            //sTooltip = sTooltip.replace(".", "");
            sTooltip = sTooltip.replace(" -- ", "\n");
            tooltip.setText(sTooltip);
            tooltip.setShowDelay(Duration.ZERO);
            tooltip.setHideDelay(Duration.ZERO);
            Tooltip.install(this, tooltip);
        });
    }

    /**
     * Draws the ball on the specified AnchorPane. Plattform.runLater is called to unify the thread the simulation
     * is currently running on and the drawing thread of javafx.
     * @param aDrawingPane specifies the Pane where the Ball should be drawn to
     */
    public void draw(AnchorPane aDrawingPane) {
        Platform.runLater(() -> {
            aDrawingPane.getChildren().remove(this);
            aDrawingPane.getChildren().add(this);
            aDrawingPane.getChildren().remove(a);
            if (MainModel.get().isArrowsActive())
            {
                a = this.giveArrow();
                aDrawingPane.getChildren().add(a);
            }
        });
    }

    /**
     * The Arrow class was taken fro stackoverflow.com: ___LINK___
     * but modified ;)
     * Sets the position of the arrow according to the direction, which is calculated by adding the Position Vector to the Velocity Vector
     * @return an Arrow which can be drawn to any JavaFx Pane
     */
    public Arrow giveArrow() {
        arrow = new Arrow();
        MyVector direction = MyVector.add(posVec, velVec);
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
    public void setColliding_Orthogonal_F(boolean colliding_Orthogonal_F) {
        this.colliding_Orthogonal_F = colliding_Orthogonal_F;
    }
    public boolean isColliding_Orthogonal_F() {
        return colliding_Orthogonal_F;
    }

    public void setColliding_Parallel_B(boolean colliding_Parallel_B) {
        this.colliding_Parallel_B = colliding_Parallel_B;
    }
    public boolean isColliding_Parallel_B() {
        return colliding_Parallel_B;
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

    //_double__
    public void setInitialTotE_c(double initialTotE_c) {
        this.initialTotE_c = initialTotE_c;
    }
    public double getInitialTotE_c() {
        return initialTotE_c;
    }

    public void setTotE_c(double totE_c) {
        this.totE_c = totE_c;
    }
    public double getTotE_c() {
        return totE_c;
    }

    public void setPotE_c(double potE_c) {
        this.potE_c = potE_c;
    }
    public double getPotE_c() {
        return potE_c;
    }

    public void setKinE_c(double kinE_c) {
        this.kinE_c = kinE_c;
    }
    public double getKinE_c() {
        return kinE_c;
    }

    public void setLostE_c(double lostE_c) {
        this.lostE_c = lostE_c;
    }
    public double getLostE_c() {
        return lostE_c;
    }

    //_toString()_______________________________________________________________________________________________________
    @Override
    public String toString() {
        return "Ball No: " + this.number + ". -- Pos: " + posVec
                + " -- v: " + velVec
                + " -- Masse: " + this.mass.getValue() + " -- Radius: " + this.getRadius()
                + " -- ges Energie: " + this.totE.getValue()
                + " -- pot Energie: " + this.potE.getValue()
                + " -- kin Energie: " + this.kinE.getValue()
                + " -- Verlust: " +     this.lostE.getValue();
    }

    //_SAVE_____________________________________________________________________________________________________________
    public String save(){
        return ";initialTotE_c: " + initialTotE_c
                 + ",,totE: " + totE.getValue()
                 + ",,potE: " + potE.getValue()
                 + ",,kinE: " + kinE.getValue()
                 + ",,lostE: " + lostE.getValue()
                 + ",,posVec: " + posVec.toString()
                 + ",,velVec: " + velVec.toString()
                 + ",,vel0Vec: " + vel0Vec.toString()
                 + ",,accVec: " + accVec.toString()
                 + ",,frcVec: " + frcVec.toString()
                 + ",,rolVec: " + rolVec.toString()
                 + ",,arrow: " + arrow.toString()
                 + ",,colliding_Orthogonal_F: " + colliding_Orthogonal_F
                 + ",,colliding_Parallel_B: " + colliding_Parallel_B
                 + ",,a: " + a.toString()
                 + ",,randomCol: " + randomCol.toString()
                 + ",,randomSeed: " + randomSeed
                 + ",,strokeColor: " + strokeColor.toString()
                 + ",,radius: " + radius.getValue()
                 + ",,mass: " + mass.getValue()
                 + ",,elasticity: " + elasticity.getValue()
                 + ",,ball :" + number;
    }
}
