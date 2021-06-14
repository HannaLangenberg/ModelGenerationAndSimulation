package project.de.hshl.vcII.drawing.calculations;

import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.mvc.MainWindowModel;
import project.de.hshl.vcII.utils.MyVector;

import java.util.ArrayList;
import java.util.List;

public class BallCollisions {
    //_Balls_Collision__________________________________________________________________________________________________
    public static void checkBalls(Ball b, double epsilon){
        //-Collision-test-----------------------------------------------------------------------------------------------
        // Make a deep copy of the balls list
        List<Ball> balls = new ArrayList<>(MainWindowModel.get().getBallManager().getBalls());
        // Don't copy the Ball given as a parameter
        balls.remove(b);

        // Iterate though every ball except the one given in the parameters, and check their collision.
        for (Ball b2 : balls)
            ballOnBall(b, b2, epsilon);
    }

    private static void ballOnBall(Ball b1, Ball b2, double epsilon) {
        //-Do-two-balls-collide-----------------------------------------------------------------------------------------

        // finds the radii of b1, and b2
        double r_b1 = b1.getRadius(),
               r_b2 = b2.getRadius();

        // finds the center-points of b1, and b2
        // pos vector gives the center of the ball
        MyVector pos_b1 = b1.getPosVec(),
                 pos_b2 = b2.getPosVec();

        boolean collision = MyVector.distance(pos_b1, pos_b2) <= r_b1+r_b2+epsilon;

        //-If-they-collide----------------------------------------------------------------------------------------------

        if(collision)
            angledShock(b1, b2);
    }

    private static void angledShock(Ball b1, Ball b2){
        //-------Calculate-values---------------------------------------------------------------------------------------

        // Norm the line between the two centers of b1 and b2.
        MyVector normedCenterLine = MyVector.norm(MyVector.subtract(b1.getPosVec(), b2.getPosVec()));
        // Find the orthogonal velocity vector of b1 and the parallel velocity vector of b2
        MyVector v1Orthogonal = MyVector.subtract(MyVector.orthogonalProjection(b1.getVelVec(), normedCenterLine), b1.getVelVec());
        MyVector v2Parallel = MyVector.orthogonalProjection(b2.getVelVec(), normedCenterLine);

        // Switch normedCenterLine's orientation
        normedCenterLine = MyVector.multiply(normedCenterLine, -1);
        // Find the orthogonal velocity vector of b2 and the parallel velocity vector of b1
        MyVector v2Orthogonal = MyVector.subtract(MyVector.orthogonalProjection(b2.getVelVec(), normedCenterLine), b2.getVelVec());
        MyVector v1Parallel = MyVector.orthogonalProjection(b1.getVelVec(), normedCenterLine);

        //-------Set-values---------------------------------------------------------------------------------------------

        b1.setVelVec(MyVector.add(v1Orthogonal, Collision.centerShock(v1Parallel, b1.getMass(), v2Parallel, b2.getMass())));
        b2.setVelVec(MyVector.add(v2Orthogonal, Collision.centerShock(v2Parallel, b2.getMass(), v1Parallel, b1.getMass())));
    }

    //_Out_Of_Order_method_for_reference_tho
    private static void centerShock(Ball b1, Ball b2){
        //-------Calculate-values---------------------------------------------------------------------------------------

        // The first three steps are the same for both balls
        MyVector step1 = MyVector.add(MyVector.multiply(b1.getVelVec(), b1.getMass()), MyVector.multiply(b2.getVelVec(), b2.getMass()));
        MyVector step2 = MyVector.divide(step1, b1.getMass() + b2.getMass());
        MyVector step3 = MyVector.multiply(step2, 2);

        MyVector step4_b1 = MyVector.subtract(b1.getVelVec(), step3);
        MyVector step4_b2 = MyVector.subtract(b2.getVelVec(), step3);

        //-------Set-values---------------------------------------------------------------------------------------------

        b1.setVelVec(step4_b1);
        b2.setVelVec(step4_b2);
    }

}
