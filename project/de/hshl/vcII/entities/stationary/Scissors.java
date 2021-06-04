package project.de.hshl.vcII.entities.stationary;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import project.de.hshl.vcII.utils.MyVector;

public class Scissors extends Rectangle {
    private Line leftLine;
    private Line rightLine;
    private MyVector posVec; //upper left corner
    private MyVector centerPoint; //middle of rectangle
    private double e_alpha;
    private double spin;
    private int orientation;

    public Scissors() {
        this.posVec = new MyVector(0,0);
        this.orientation = 2;
        this.setFill(Color.TRANSPARENT);
        this.setStroke(Color.DARKGRAY);
        this.getStrokeDashArray().addAll(3d, 10d);
        leftLine = new Line();
        rightLine = new Line();
        initializeLines();
    }
    private void initializeLines() {
        leftLine.setStroke(Color.GREEN);
        leftLine.setStrokeWidth(3);

        rightLine.setStroke(Color.GREEN);
        rightLine.setStrokeWidth(3);
        updateLines();
    }

    public void calcCenterPoint() {
        if(this.getRotate() != 0) {
            double x = this.getX() + (this.getWidth() * Math.cos(Math.toRadians(this.getRotate()) + this.getHeight() * Math.sin(Math.toRadians(this.getRotate()))))/2;
            double y = this.getY() + ( this.getWidth() * Math.sin(Math.toRadians(this.getRotate()) *-1 + this.getHeight() * Math.cos(Math.toRadians(this.getRotate()))))/2;

            setCenterPoint(new MyVector(x,y));
        }
        setCenterPoint(new MyVector(this.getX() + this.getWidth()/2, this.getY() +this.getHeight()/2));
    }

    public MyVector getPosVec() {
        return posVec;
    }

    public void setPosVec(MyVector posVec) {
        this.posVec = posVec;
        this.setX(posVec.x);
        this.setY(posVec.y);
        updateLines();
        calcCenterPoint();
    }

    private void updateLines() {
        leftLine.setStartX(getX());
        leftLine.setStartY(getY());
        leftLine.setEndX(getX() + 3.0/4 * getWidth());
        leftLine.setEndY(getY() + getHeight());

        rightLine.setStartX(getX() + getWidth());
        rightLine.setStartY(getY());
        rightLine.setEndX(getX() + 1.0/4 * getWidth());
        rightLine.setEndY(getY() + getHeight());
    }

    public MyVector getCenterPoint() {
        return centerPoint;
    }

    public void setCenterPoint(MyVector centerPoint) {
        this.centerPoint = centerPoint;
    }

    public double getE_alpha() {
        return e_alpha;
    }

    public void setE_alpha(double e_alpha) {
        this.e_alpha = e_alpha;
    }

    public double getSpin() {
        return spin;
    }

    public void setSpin(double spin) {
        this.spin = spin;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

}
