package project.de.hshl.vcII.entities.stationary;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import project.de.hshl.vcII.mvc.MainWindowModel;
import project.de.hshl.vcII.utils.MyVector;

public class Scissors {
    private Rectangle rectangle;
    private Line leftLine;
    private Line rightLine;
    private final Group g;
    public Group getG() {
        return g;
    }
    private MyVector posVec; //upper left corner
    private MyVector centerPoint; //middle of rectangle
    private static MyVector upperLeft, lowerLeft, upperRight, lowerRight, crossingPoint, directionalVector;

    private double e_alpha;
    private double spin;
    private int orientation;

    public Scissors() {
        posVec = new MyVector(0,0);
        orientation = 2;
        leftLine = new Line();
        rightLine = new Line();
        rectangle = new Rectangle();
        setUnmarkedStroke();
        initializeLines();
        g = new Group();
        g.getChildren().addAll(rectangle, leftLine, rightLine);
    }
    private void initializeLines() {
        leftLine.setStroke(Color.GREEN);
        leftLine.setStrokeWidth(3);
        rightLine.setStroke(Color.GREEN);
        rightLine.setStrokeWidth(3);
        updateLines();
    }
    public void updateLines() {
        applyRotation();

        leftLine.setStartX(upperLeft.x);
        leftLine.setStartY(upperLeft.y);
        leftLine.setEndX(lowerLeft.x + 3.0/4 * (lowerRight.x - lowerLeft.x));
        leftLine.setEndY(lowerLeft.y + 3.0/4 * (lowerRight.y - lowerLeft.y));

        rightLine.setStartX(upperRight.x);
        rightLine.setStartY(upperRight.y);
        rightLine.setEndX(lowerLeft.x + 1.0/4 * (lowerRight.x - lowerLeft.x));
        rightLine.setEndY(lowerLeft.y + 1.0/4 * (lowerRight.y - lowerLeft.y));
    }

    public void setUnmarkedStroke() {
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.DARKGRAY);
        rectangle.getStrokeDashArray().addAll(3d, 10d);
    }
    public void setMarkedStroke() {
        rectangle.setStrokeType(StrokeType.OUTSIDE);
        rectangle.setStroke(new Color(0, 0.8, 0, 1));
        rectangle.setStrokeWidth(2);
    }

    public void applyRotation() {
        upperLeft  = new MyVector(
                rectangle.localToParent(rectangle.getX(), rectangle.getY()).getX(),
                rectangle.localToParent(rectangle.getX(), rectangle.getY()).getY());
        upperRight = new MyVector(
                rectangle.localToParent(rectangle.getX() + rectangle.getWidth(),     rectangle.getY()).getX(),
                rectangle.localToParent(rectangle.getX() + rectangle.getWidth(),     rectangle.getY()).getY());
        lowerLeft  = new MyVector(
                rectangle.localToParent(rectangle.getX(),                           rectangle.getY() + rectangle.getHeight()).getX(),
                rectangle.localToParent(rectangle.getX(),                           rectangle.getY() + rectangle.getHeight()).getY());
        lowerRight = new MyVector(
                rectangle.localToParent(rectangle.getX() + rectangle.getWidth(), rectangle.getY() + rectangle.getHeight()).getX(),
                rectangle.localToParent(rectangle.getX() + rectangle.getWidth(), rectangle.getY() + rectangle.getHeight()).getY());
    }

    public void calcCrossingPoint() {
        updateLines();
        double x = leftLine.getStartX() + 0.67 * (leftLine.getEndX() - leftLine.getStartX());
        double y = leftLine.getStartY() + 0.67 * (leftLine.getEndY() - leftLine.getStartY());
        crossingPoint = new MyVector(x,y);
    }
    public void calcCenterPoint() {
        if(rectangle.getRotate() != 0) {
            double x = rectangle.getX() + (rectangle.getWidth() * Math.cos(Math.toRadians(rectangle.getRotate()) + rectangle.getHeight() * Math.sin(Math.toRadians(rectangle.getRotate()))))/2;
            double y = rectangle.getY() + ( rectangle.getWidth() * Math.sin(Math.toRadians(rectangle.getRotate()) *-1 + rectangle.getHeight() * Math.cos(Math.toRadians(rectangle.getRotate()))))/2;

            setCenterPoint(new MyVector(x,y));
        }
        setCenterPoint(new MyVector(rectangle.getX() + rectangle.getWidth()/2, rectangle.getY() + rectangle.getHeight()/2));
    }

    public void calcDirectionalVector() {
        directionalVector = MyVector.norm(MyVector.subtract(lowerLeft, upperLeft));
    }

    public void draw(AnchorPane aDrawingPane) {
        Platform.runLater(() -> {
            aDrawingPane.getChildren().remove(g);
            aDrawingPane.getChildren().add(g);
        });
    }

    //_GETTER_&_SETTER__________________________________________________________________________________________________
    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setCenterPoint(MyVector centerPoint) {
        this.centerPoint = centerPoint;
    }
    public MyVector getCenterPoint() {
        return centerPoint;
    }

    public void setE_alpha(double e_alpha) {
        this.e_alpha = e_alpha;
    }
    public double getE_alpha() {
        return e_alpha;
    }

    public void setSpin(double spin) {
        this.spin = spin;
    }
    public double getSpin() {
        return spin;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }
    public int getOrientation() {
        return orientation;
    }

    public void setPosVec(MyVector posVec) {
        this.posVec = posVec;
        rectangle.setX(posVec.x);
        rectangle.setY(posVec.y);
        updateLines();
        calcCenterPoint();
    }
    public MyVector getPosVec() {
        return posVec;
    }

    public void setLeftLine(Line leftLine) {
        this.leftLine = leftLine;
    }
    public Line getLeftLine() {
        return leftLine;
    }

    public void setRightLine(Line rightLine) {
        this.rightLine = rightLine;
    }
    public Line getRightLine() {
        return rightLine;
    }
}
