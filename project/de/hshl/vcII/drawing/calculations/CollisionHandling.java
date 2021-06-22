package project.de.hshl.vcII.drawing.calculations;

import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.utils.MyVector;
import project.de.hshl.vcII.utils.Utils;

public class CollisionHandling {
    static MyVector normedCenterLine, vOrthogonal, vParallel;

    /**
     * Splits the velocity vector of b to it's orthogonal and parallel components, and checks if b moves to the wall or away from it.
     * @param b     Ball, the Ball-Object currently handled
     * @param side  int, the direction the ball goes
     * @param dp    MyVector, the dropped perpendicular of the Ball (b) to the according wall
     */
    public static void bounceVelocity(Ball b, int side, MyVector dp) {
        // Calculate values
        // The directional vector between the ball's position and its dropped perpendicular.
        // It has to be normed:
        normedCenterLine = MyVector.norm(MyVector.subtract(b.getPosVec(), dp));

        // Find the orthogonal and the parallel velocity vectors of b
        vOrthogonal = MyVector.subtract(MyVector.orthogonalProjection(b.getVelVec(), normedCenterLine), b.getVelVec());
        vParallel = MyVector.orthogonalProjection(b.getVelVec(), normedCenterLine);


        if ((side == 1 & vParallel.x < 0)
         || (side == 3 & vParallel.x > 0)
         || (side == 2 & vParallel.y < 0)
         || (side == 0 & vParallel.y > 0)) {
            b.setVelVec(MyVector.add(vOrthogonal, MyVector.multiply(vParallel, -b.getElasticity())));
        }
        b.setColliding_Parallel_B(false);
    }

    /**
     * Stops the Ball b from bouncing if the value of the parallel vector (in y direction) in less than 9.81 [m/s^2].
     * @param b Ball, this specifies which ball should stop bouncing
     */
    public static void stopBouncing(Ball b) {
        if (Math.abs(vParallel.y) < Utils.CONSTANT_OF_GRAVITATION / 3) {
            b.setVelVec(MyVector.add(vOrthogonal, MyVector.multiply(vParallel, 0)));
            b.setColliding_Orthogonal_F(true);
        }
    }

    public static void reset() {
        normedCenterLine = vParallel = vOrthogonal = new MyVector(0,0);
    }
}
