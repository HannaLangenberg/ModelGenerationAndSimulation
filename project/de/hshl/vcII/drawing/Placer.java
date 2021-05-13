package project.de.hshl.vcII.drawing;

import javafx.scene.paint.Color;
import project.de.hshl.vcII.entities.Ball;
import project.de.hshl.vcII.mvc.MainWindowModel;
import project.de.hshl.vcII.entities.Entity;
import project.de.hshl.vcII.entities.stationary.Block;
import project.de.hshl.vcII.entities.moving.Kugel;
import javafx.scene.input.MouseEvent;
import project.de.hshl.vcII.utils.MyVector;

/**
 * Placer is for placing the bocks, the sphere, and all Entities added.
 */
public class Placer {
    private Entity entity;
    private Ball ball;
    private MainWindowModel mainWindowModel;

    private double x, y;

    public void init() {
        this.mainWindowModel = MainWindowModel.get();
    }

    public void place(MouseEvent e){
        ball = mainWindowModel.getBallManager().getB();
        x = e.getX();
        y = e.getY();

        if (!mainWindowModel.getADrawingPane().getChildren().contains(ball)) {
            ball.setCenterX(x);
            ball.setCenterY(y);
            ball.setPosVec(new MyVector(x, y));
            mainWindowModel.getBallManager().addBall(ball);
            mainWindowModel.getADrawingPane().getChildren().add(ball);
        } else {
            ball.setCenterX(x);
            ball.setCenterY(y);
            ball.setPosVec(new MyVector(x, y));
            mainWindowModel.getADrawingPane().getChildren().remove(ball);
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
