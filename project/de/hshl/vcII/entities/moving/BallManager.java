package project.de.hshl.vcII.entities.moving;

import project.de.hshl.vcII.drawing.visuals.Arrow;
import project.de.hshl.vcII.mvc.MainWindowModel;

import java.util.ArrayList;
import java.util.List;

/**
 * BallManager class for saving all balls on screen.
 * uses an ArrayList to keep track of the balls present on screen
 */
public class BallManager {
    private final List<Ball> balls = new ArrayList<>();
    private Ball b;

    public void removeArrows() {
        for (Ball b : balls) {
            MainWindowModel.get().getADrawingPane().getChildren().remove(b.getArrow());
        }
    }

    //----Getter-&-Setter-----------------------------------------------------------------------------------------------
    public void setB(Ball b) {
        this.b = b;
    }

    public Ball getB() {
        return b;
    }

    public void addBall(Ball ball) {
        balls.add(ball);
    }

    public List<Ball> getBalls() {
        return balls;
    }
}

