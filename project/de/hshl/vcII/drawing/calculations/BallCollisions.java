package project.de.hshl.vcII.drawing.calculations;

import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.mvc.MainModel;
import project.de.hshl.vcII.utils.MyVector;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for detecting and handling all collisions regarding scenarios between balls.
 *
 * The declaration who wrote which part can be found in the method comments.
 * */
public class BallCollisions {

    /**
     * Check the current ball against every other ball by iterating through the balls list.
     * For each pairing call {@link BallCollisions#ballOnBall(Ball, Ball, double)}.
     * @param b       current ball
     * @param epsilon safety distance for checking for collisions.
     */
    public static void checkBalls(Ball b, double epsilon){
        List<Ball> balls = new ArrayList<>(MainModel.get().getBallManager().getBalls()); // deep copy of ball list
        balls.remove(b);                                               // Don't copy the Ball given as a parameter

        for (Ball b2 : balls)                                          // Iterate though every ball except the one given
            ballOnBall(b, b2, epsilon);                                // in the parameters, and check their collision.
    }

    /**
     * Check current ball vs iterated ball. If the distance between their center points is smaller than the
     * both individual radii and epsilon combined, call {@link BallCollisions#angledShock(Ball, Ball)}.
     * @param b1      current ball
     * @param b2      iterated ball from list
     * @param epsilon safety distance for checking for collisions.
     */
    private static void ballOnBall(Ball b1, Ball b2, double epsilon) {
        double r_b1 = b1.getRadius(),                                  // finds the radii of b1, and b2
               r_b2 = b2.getRadius();

        MyVector pos_b1 = b1.getPosVec(),                              // finds the center-points of b1, and b2
                 pos_b2 = b2.getPosVec();                              // pos vector gives the center of the ball

        boolean collision = MyVector.distance(pos_b1, pos_b2) <= r_b1+r_b2+epsilon;

        if(collision)                                                  //-If-they-collide
            angledShock(b1, b2);
    }

    /**
     * This method splits the balls' velocity vectors into their parallel and their orthogonal components.
     * The theory of the angledShock is to extract the orthogonal and parallel components of the balls' velocity
     * vectors and calculate the new velocity vectors by adding the components "crossed".
     *      e.g. b1) velVec_before = v1_O + v1_P
     *               velVec_after  = v1_O + v2_P
     *
     * The first step is to calculate a normed vector from b1's center to the b2's center (nCL).
     * Afterwards v1Orthogonal (orthogonal component of b1's velVec) and v2Parallel (parallel component of b2's VelVec)
     * are calculated by projecting the VelVecs onto the normedCenterLine. The next step is to flip the direction of
     * normedCenterLine and calculate the remaining components.
     * This way we do not have to switch the parallel component's direction manually before adding them back up, as they
     * directly are pointed in the correct direction.
     *
     * During setting of the new velVecs {@link Collisions#centerShock(MyVector, double, MyVector, double)} is called.
     * This method takes the balls' masses into account and returnes the updated parallel components.
     *
     * @param b1      current ball
     * @param b2      iterated ball from list
     */
    private static void angledShock(Ball b1, Ball b2){
        // Norm the line from the center of b1 to center of b2
        MyVector nCL = MyVector.norm(MyVector.subtract(b1.getPosVec(), b2.getPosVec()));
        // Find the orthogonal velocity vector of b1 and the parallel velocity vector of b2
        MyVector v1Orthogonal = Calculator.calc_O_component(nCL, b1);
        MyVector v2Orthogonal = Calculator.calc_O_component(nCL, b2);
        MyVector v1Parallel   = Calculator.calc_P_component(nCL, b1);
        MyVector v2Parallel   = Calculator.calc_P_component(nCL, b2);

        MyVector contactPoint = MyVector.insertScalingFactorIntoEquation(b1.getPosVec(), MyVector.subtract(b1.getPosVec(), b2.getPosVec()), 1.0/2);

        //-------Set-values---------------------------------------------------------------------------------------------
        if (MyVector.insertPintoEquation(b1.getPosVec(), MyVector.norm(v1Parallel), contactPoint) >= 0 || MyVector.insertPintoEquation( b2.getPosVec(), MyVector.norm(v2Parallel), contactPoint) >= 0 ) {
            b1.setVelVec(MyVector.add(v1Orthogonal, MyVector.multiply(Collisions.centerShock(v1Parallel, b1.getMass(), v2Parallel, b2.getMass()), b1.getElasticity())));
            b2.setVelVec(MyVector.add(v2Orthogonal, MyVector.multiply(Collisions.centerShock(v2Parallel, b2.getMass(), v1Parallel, b1.getMass()), b2.getElasticity())));
        }
    }
}
