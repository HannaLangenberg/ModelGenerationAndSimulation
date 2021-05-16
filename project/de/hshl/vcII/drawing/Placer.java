package project.de.hshl.vcII.drawing;

import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.entities.stationary.Wall;
import project.de.hshl.vcII.mvc.MainWindowModel;
import javafx.scene.input.MouseEvent;
import project.de.hshl.vcII.utils.MyVector;

/**
 * Placer is for placing the bocks, the sphere, and all Entities added.
 */
public class Placer {
    private Wall wall;
    private Ball ball;
    private MainWindowModel mainWindowModel;

    private double x, y;

    public void init() {
        this.mainWindowModel = MainWindowModel.get();
    }

    public void place(MouseEvent e){
        ball = mainWindowModel.getBallManager().getB();
        wall = mainWindowModel.getWallManager().getW();
        x = e.getX();
        y = e.getY();

        if(ball != null) {
            if (!mainWindowModel.getADrawingPane().getChildren().contains(ball)) {
                ball.setCenterX(x);
                ball.setCenterY(y);
                ball.setPosVec(new MyVector(x, y));
                ball.setNumber(mainWindowModel.getBallManager().getBalls().size() + 1);
                mainWindowModel.getBallManager().addBall(ball);
                mainWindowModel.getADrawingPane().getChildren().add(ball);
            } else {
                ball.setCenterX(x);
                ball.setCenterY(y);
                ball.setPosVec(new MyVector(x, y));
            }
        } else if (wall != null){
            if (!mainWindowModel.getADrawingPane().getChildren().contains(wall)) {
                ball.setCenterX(x);
                ball.setCenterY(y);
                ball.setPosVec(new MyVector(x, y));
                ball.setNumber(mainWindowModel.getBallManager().getBalls().size() + 1);
                mainWindowModel.getBallManager().addBall(ball);
                mainWindowModel.getADrawingPane().getChildren().add(ball);
            } else {
                ball.setCenterX(x);
                ball.setCenterY(y);
                ball.setPosVec(new MyVector(x, y));
            }
        }
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
