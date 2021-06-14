package project.de.hshl.vcII.entities.stationary;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.transform.Rotate;
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
    private MyVector centerPoint, crossingPoint, llStart, llEnd, rlStart, rlEnd; //middle of rectangle
    private static MyVector upperLeft, lowerLeft, upperRight, lowerRight, directionalVector, llVector, rlVector;
    private static double angle;

    private double e_alpha;
    private double spin;
    private int orientation;
    private boolean closing = false;

    public Scissors() {
        rectangle = new Rectangle();
        leftLine = new Line();
        rightLine = new Line();
        g = new Group();
        posVec = new MyVector(0,0);
        orientation = 2;
        setUnmarkedStroke();
        initializeLines();
        calcCrossingPoint();
        g.getChildren().addAll(rectangle, leftLine, rightLine);
        closing = false;
    }
    private void initializeLines() {
        leftLine.setStroke(Color.NAVY);
        leftLine.setStrokeWidth(3);
        rightLine.setStroke(Color.GREEN);
        rightLine.setStrokeWidth(3);
        updateLines();
    }
    public void updateLines() {
        applyRotation(0);

        leftLine.setStartX(llStart.x);
        leftLine.setStartY(llStart.y);
        leftLine.setEndX(lowerLeft.x + 3.0/4 * (lowerRight.x - lowerLeft.x));
        leftLine.setEndY(lowerLeft.y + 3.0/4 * (lowerRight.y - lowerLeft.y));

        rightLine.setStartX(rlStart.x);
        rightLine.setStartY(rlStart.y);
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

    public void applyRotation(int decision) {
        switch (decision) {
            case 0: // abhängig von Gruppenrotation
                upperLeft = llStart  = new MyVector(
                        g.localToParent(rectangle.getX(), rectangle.getY()).getX(),
                        g.localToParent(rectangle.getX(), rectangle.getY()).getY());
                upperRight = rlStart = new MyVector(
                        g.localToParent(rectangle.getX() + rectangle.getWidth(),     rectangle.getY()).getX(),
                        g.localToParent(rectangle.getX() + rectangle.getWidth(),     rectangle.getY()).getY());
                lowerLeft  = new MyVector(
                        g.localToParent(rectangle.getX(),                           rectangle.getY() + rectangle.getHeight()).getX(),
                        g.localToParent(rectangle.getX(),                           rectangle.getY() + rectangle.getHeight()).getY());
                lowerRight = new MyVector(
                        g.localToParent(rectangle.getX() + rectangle.getWidth(), rectangle.getY() + rectangle.getHeight()).getX(),
                        g.localToParent(rectangle.getX() + rectangle.getWidth(), rectangle.getY() + rectangle.getHeight()).getY());
                llEnd = new MyVector(leftLine.localToParent(leftLine.getEndX(), leftLine.getEndY()).getX(), leftLine.localToParent(leftLine.getEndX(), leftLine.getEndY()).getY());
                rlEnd = new MyVector(rightLine.localToParent(rightLine.getEndX(), rightLine.getEndY()).getX(), rightLine.localToParent(rightLine.getEndX(), rightLine.getEndY()).getY());
                break;
            case 1: // abhängig von einzelne Linienrotation
                llStart = new MyVector(leftLine.localToParent(leftLine.getStartX(), leftLine.getStartY()).getX(), leftLine.localToParent(leftLine.getStartX(), leftLine.getStartY()).getY());
                rlStart = new MyVector(rightLine.localToParent(rightLine.getStartX(), rightLine.getStartY()).getX(), rightLine.localToParent(rightLine.getStartX(), rightLine.getStartY()).getY());
                break;
        }
    }

    public void calcCrossingPoint() {
        double x = llStart.x + 2.0/3 * (llEnd.x - llStart.x);
        double y = llStart.y + 2.0/3 * (llEnd.y - llStart.y);
        crossingPoint = new MyVector(x,y);
//        System.out.println(crossingPoint);
//        System.out.println(leftLine.toString());
    }
    public void calcCenterPoint() {
        if(rectangle.getRotate() != 0) {
            double x = rectangle.getX() + (rectangle.getWidth() * Math.cos(Math.toRadians(rectangle.getRotate()) + rectangle.getHeight() * Math.sin(Math.toRadians(rectangle.getRotate()))))/2;
            double y = rectangle.getY() + (rectangle.getWidth() * Math.sin(Math.toRadians(rectangle.getRotate()) *-1 + rectangle.getHeight() * Math.cos(Math.toRadians(rectangle.getRotate()))))/2;

            setCenterPoint(new MyVector(x,y));
        }
        setCenterPoint(new MyVector(rectangle.getX() + rectangle.getWidth()/2, rectangle.getY() + rectangle.getHeight()/2));
    }

    public void calcDirectionalVector() {
        directionalVector = MyVector.multiply(MyVector.norm(MyVector.subtract(lowerLeft, upperLeft)), MainWindowModel.get().getScissorsSpeed());
//        directionalVector = MyVector.subtract(lowerLeft, upperLeft);
    }

    public void animate(AnchorPane aDrawingPane) {
        applyRotation(1);
        calcCrossingPoint();
        llVector = MyVector.subtract(crossingPoint, llStart);
        rlVector = MyVector.subtract(crossingPoint, rlStart);
        angle = MyVector.angle(llVector, rlVector);
//        System.out.println(angle);
        if(angle > 1) {
            leftLine.getTransforms().add(new Rotate(MainWindowModel.get().getScissorsSpeed(), crossingPoint.x, crossingPoint.y));
            rightLine.getTransforms().add(new Rotate(-MainWindowModel.get().getScissorsSpeed(), crossingPoint.x, crossingPoint.y));
        }
        else {
            setClosing(false);
        }
        draw(aDrawingPane);
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

    public void setClosing(boolean closing) {
        this.closing = closing;
    }

    public static void setDirectionalVector(MyVector directionalVector) {
        Scissors.directionalVector = directionalVector;
    }
    public static MyVector getDirectionalVector() {
        return directionalVector;
    }

    public void setCrossingPoint(MyVector crossingPoint) {
        this.crossingPoint = crossingPoint;
    }
    public MyVector getCrossingPoint() {
        return crossingPoint;
    }

    public boolean isClosing() {
        return closing;
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
        calcCrossingPoint();
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

    public MyVector getLlStart() {
        return llStart;
    }

    public void setLlStart(MyVector llStart) {
        this.llStart = llStart;
    }

    public MyVector getRlStart() {
        return rlStart;
    }

    public void setRlStart(MyVector rlStart) {
        this.rlStart = rlStart;
    }
}
