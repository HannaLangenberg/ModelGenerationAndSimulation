package project.de.hshl.vcII.drawing.calculations;

import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.utils.MyVector;
import project.de.hshl.vcII.utils.Utils;

public class CollisionHandling {
    static MyVector normedCenterLine, vOrthogonal, vParallel;

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

    public static void stopBouncing(Ball b) {
        if (Math.abs(vParallel.y) < Utils.CONSTANT_OF_GRAVITATION / 3) {
            b.setVelVec(MyVector.add(vOrthogonal, MyVector.multiply(vParallel, 0)));
            b.setColliding_Orthogonal_F(true);
            b.setAccVec(MyVector.add(b.getAccVec(), new MyVector(0, -Utils.CONSTANT_OF_GRAVITATION)));
        }
    }

    public static void reset() {
        normedCenterLine = vParallel = vOrthogonal = new MyVector(0,0);
    }
}
