package project.de.hshl.vcII.drawing.calculations;

import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.entities.stationary.Wall;
import project.de.hshl.vcII.mvc.MainWindowModel;
import project.de.hshl.vcII.utils.MyVector;
import project.de.hshl.vcII.utils.Utils;

import java.util.List;

public class Movement {

    public static void calcAcceleration(Ball b) {
        // add every single ACCELERATION to get accVec
        b.setAccVec(MyVector.addMultiple(b.getAccVec(), Utils.getWind(), Utils.GRAVITY));
    }

    public static void calcVelocity(Ball b) {
        // v = v_0 + a * t
        b.setVel0Vec(b.getVelVec());
        b.setVelVec(MyVector.add(b.getVelVec(), MyVector.multiply(b.getAccVec(), Utils.DELTA_T)));
    }

    public static void checkCollisions(Ball b, double epsilon) {
        // Check if ball hits screen bounds
        Collision.checkScreen(b, epsilon);

        // Check if ball hits wall
        List<Wall> walls = MainWindowModel.get().getWallManager().getWalls();

        // Iterate through the balls and walls and check for collisions
        for (Wall w : walls)
            WallCollision.checkWalls(w,b,epsilon);
    }

    public static void calcPosition(Ball b) {
        // s = s_0 + v_0 * dt
        //                   + 0.5 * a * dt^2
        b.setPosVec(MyVector.add(b.getPosVec(), MyVector.multiply(b.getVel0Vec(), Utils.DELTA_T)));
        b.setPosVec(MyVector.add(b.getPosVec(), MyVector.multiply(MyVector.multiply(b.getAccVec(), Math.pow(Utils.DELTA_T, 2)), 0.5)));

        b.setAccVec(MyVector.multiply(b.getAccVec(),0));

    }
}