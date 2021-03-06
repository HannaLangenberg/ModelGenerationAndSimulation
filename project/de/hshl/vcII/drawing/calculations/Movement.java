package project.de.hshl.vcII.drawing.calculations;

import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.entities.stationary.Wall;
import project.de.hshl.vcII.mvc.MainModel;
import project.de.hshl.vcII.utils.MyVector;
import project.de.hshl.vcII.utils.Utils;

import java.util.List;

public class Movement {

    /**
     * Add up every acceleration
     * @param b Ball, the specified Ball-Object
     */
    public static void calcAcceleration(Ball b) {
        // add every single ACCELERATION to get accVec
        b.setAccVec(MyVector.addMultiple(b.getAccVec(), Utils.getWind(), Utils.GRAVITY));
    }

    /**
     * Calculate the velocity, using the formula v = v_0 + a * t
     * @param b Ball, the specified Ball-Object
     */
    public static void calcVelocity(Ball b) {
        // v = v_0 + a * t
        b.setVel0Vec(b.getVelVec());
        b.setVelVec(MyVector.add(b.getVelVec(), MyVector.multiply(b.getAccVec(), Utils.DELTA_T)));
    }

    /**
     * Calculate the position using the formula s = s_0 + v_0 * dt + 0.5 * a * dt^2
     * @param b Ball, the specified Ball-Object
     */
    public static void calcPosition(Ball b) {
        // s = s_0 + v_0 * dt
        //                   + 0.5 * a * dt^2
        b.setPosVec(MyVector.add(b.getPosVec(), MyVector.multiply(b.getVel0Vec(), Utils.DELTA_T)));
        b.setPosVec(MyVector.add(b.getPosVec(), MyVector.multiply(MyVector.multiply(b.getAccVec(), Math.pow(Utils.DELTA_T, 2)), 0.5)));

        b.setAccVec(MyVector.multiply(b.getAccVec(),0));
    }

    /**
     * Only chick for ball-on-screen and ball-on-wall collisions
     * @param b         Ball, the specified Ball-Object
     * @param epsilon   double, safety distance for checking for collisions.
     */
    public static void checkCollisions(Ball b, double epsilon) {
        // Check if ball hits screen bounds
        Collisions.checkScreen(b, epsilon);

        // Check if ball hits wall
        List<Wall> walls = MainModel.get().getWallManager().getWalls();

        // Iterate through the walls and check for collisions
        for (Wall w : walls)
            WallCollisions.checkWalls(w,b,epsilon);
    }
}