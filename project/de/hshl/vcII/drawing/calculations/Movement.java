package project.de.hshl.vcII.drawing.calculations;

import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.entities.stationary.Wall;
import project.de.hshl.vcII.mvc.MainWindowModel;
import project.de.hshl.vcII.utils.MyVector;
import project.de.hshl.vcII.utils.Utils;

import java.util.List;

public class Movement {
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

}