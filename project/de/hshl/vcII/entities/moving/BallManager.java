package project.de.hshl.vcII.entities.moving;

import javafx.scene.paint.Color;
import project.de.hshl.vcII.drawing.visuals.Arrow;
import project.de.hshl.vcII.mvc.MainWindowModel;
import project.de.hshl.vcII.utils.MyVector;

import java.util.ArrayList;
import java.util.List;

/**
 * BallManager class for saving all balls on screen.
 * uses an ArrayList to keep track of the balls present on screen
 */
public class BallManager {
    private final List<Ball> balls = new ArrayList<>();
    private static List<Arrow> arrows = new ArrayList<>();
    MainWindowModel mainWindowModel = MainWindowModel.get();
    private Ball b;

    public void drawArrows() {
        Arrow a;
        for (Ball b : balls) {
            a = b.drawArrow();
            a.toFront();
            arrows.add(a);
        }
        mainWindowModel.getADrawingPane().getChildren().addAll(arrows);
    }

    public void removeArrows() {
        MainWindowModel.get().getADrawingPane().getChildren().removeAll(arrows);
        arrows.clear();
    }

    //----Getter-&-Setter-----------------------------------------------------------------------------------------------
    public void setB(Ball b){
        this.b = b;
    }
    public Ball getB(){
        return b;
    }

    public void addBall(Ball ball) {
        balls.add(ball);
    }
    public List<Ball> getBalls() {
        return balls;
    }

    public List<Arrow> getArrows() {
        return arrows;
    }
}
