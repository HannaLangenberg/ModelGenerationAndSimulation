package project.de.hshl.vcII.drawing.calculations;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import project.de.hshl.vcII.drawing.visuals.Arrow;
import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.entities.stationary.Wall;
import project.de.hshl.vcII.mvc.MainWindowModel;
import project.de.hshl.vcII.utils.MyVector;
import project.de.hshl.vcII.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Movement {
    private static List<Arrow> arrows = new ArrayList<>();
    private static List<Ball> balls = MainWindowModel.get().getBallManager().getBalls();
    public static void calcFriction(Ball b) {
        // Get all walls.
        List<Wall> walls = MainWindowModel.get().getWallManager().getWalls();

        // Iterate through the balls and walls and check for collisions
        for (Wall w : walls)
            Calculator.initializeForces(w, b);
    }

    public static void calcAcceleration(Ball b) {
        // add every single ACCELERATION to get accVec
        b.setAccVec(MyVector.add(Utils.getWind(), Utils.GRAVITY));
    }

    public static void calcVelocity(Ball b) {
        // v = v_0 + a * t
        b.setVelVec(MyVector.add(b.getVelVec(), MyVector.multiply(b.getAccVec(), Utils.DELTA_T)));
    }

    public static void correctVelocity(Ball b, double epsilon) {
        // decide bewteen ballOnWall and ballOnScreen
        // First off: checkPosition
        Collision.checkPosition(b, epsilon);

        // Get all walls.
        List<Wall> walls = MainWindowModel.get().getWallManager().getWalls();

        // Iterate through the balls and walls and check for collisions
        for (Wall w : walls)
            Collision.checkWalls(w,b,epsilon);

    }

    public static void calcPosition(Ball b) {
        // s = s_0 + v_0 * dt
        //                   + 0.5 * a * dt^2
        b.setPosVec(MyVector.add(b.getPosVec(), MyVector.multiply(b.getVelVec(), Utils.DELTA_T)));
        b.setPosVec(MyVector.add(b.getPosVec(), MyVector.multiply(MyVector.multiply(b.getAccVec(), Math.pow(Utils.DELTA_T, 2)), 0.5)));

    }

    public static void drawArrows() {
        Arrow arrow;
        for (Ball b : balls) {
            arrow = new Arrow();
            arrow.toFront();
            MyVector direction = MyVector.add(b.getPosVec(), MyVector.multiply(b.getVelVec(), 10));
            Stop[] stops = new Stop[]{new Stop(0, javafx.scene.paint.Color.RED), new Stop(1, Color.BLUE)};
            arrow.getLine().setStroke(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops));
            arrow.setStartX(b.getPosVec().x);
            arrow.setStartY(b.getPosVec().y);
            arrow.setEndX(direction.x);
            arrow.setEndY(direction.y);
            arrow.toFront();
            arrows.add(arrow);
        }
        MainWindowModel.get().getADrawingPane().getChildren().addAll(arrows);

    }

    public static void removeArrows() {
        MainWindowModel.get().getADrawingPane().getChildren().removeAll(arrows);
        arrows.clear();
    }

}