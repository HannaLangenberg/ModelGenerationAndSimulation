package project.de.hshl.vcII.entities.moving;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import project.de.hshl.vcII.mvc.MainModel;

/**
 * BallManager class for saving all balls on screen.
 * uses an ArrayList to keep track of the balls present on screen
 */
public class BallManager {
    private final ObservableList<Ball> balls = FXCollections.observableArrayList();
    private Ball b;

    public void removeArrows() {
        for (Ball b : balls) {
            MainModel.get().getADrawingPane().getChildren().remove(b.getArrow());
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

    public ObservableList<Ball> getBalls() {
        return balls;
    }
}

