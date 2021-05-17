package project.de.hshl.vcII.drawing.calculations;

import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.entities.stationary.Wall;
import project.de.hshl.vcII.mvc.MainWindowModel;
import project.de.hshl.vcII.utils.MyVector;

import java.util.ArrayList;
import java.util.List;

public class Collision {
    public static void collide(Ball b, double epsilon){
        //-Collision-test-----------------------------------------------------------------------------------------------
        // Make a deep copy of the balls list
        List<Ball> balls = new ArrayList<>(MainWindowModel.get().getBallManager().getBalls());
        // Don't copy the Ball given as a parameter
        balls.remove(b);

        // Get all walls.
        List<Wall> walls = MainWindowModel.get().getWallManager().getWalls();

        // Iterate though every ball except the one given in the parameters, and check their collision.
        for (Ball b2 : balls)
            ballOnBall(b, b2, epsilon);

        for (Wall w : walls)
            ballOnWall(w, b);
    }

    private static void  ballOnWall(Wall w, Ball b){
        //-Ball-and-Wall-Collide----------------------------------------------------------------------------------------

        // position vector of the wall
        // pos vector gives the center of the wall
        MyVector pos_w = new MyVector(w.getCollision().getX(), w.getCollision().getY());

        // position vector of the ball
        // pos vector gives the center of the ball
        MyVector pos_b = b.getPosVec();

        // Radius of the ball.
        double r_b = b.getRadius();

        // Rotation of the Wall
        double alpha = w.getSpin();

        // IDK
        double lambda = 0;
/*
        // location vector of the wall
        MyVector location = new MyVector(Math.sin(Math.toRadians(alpha)), Math.cos(Math.toRadians(alpha)));

        // line of wall that is solid
        MyVector line = MyVector.add(pos_w, MyVector.multiply(MyVector.multiply(location, w.getCollision().getWidth()), lambda));

        double rX = w.getCollision().getX();
        double bX = b.getPosVec().x;
        double rW = w.getCollision().getWidth();

        double rY = w.getCollision().getY();
        double bY = b.getPosVec().y;

        double x = rX + ((rX - bX) * Math.pow((rW*Math.sin(alpha)), 2) + (rY - bY) * (rW*Math.cos(alpha))*(rW*Math.sin(alpha))) / -(Math.pow((rW*Math.cos(alpha)), 2) - Math.pow((rW*Math.sin(alpha)), 2));
        double y = rY + ((rX - bX) * (rW*Math.sin(alpha))*(rW*Math.cos(alpha)) + (rY - bY) * Math.pow((rW*Math.cos(alpha)), 2)) / -(Math.pow((rW*Math.cos(alpha)), 2) - Math.pow((rW*Math.sin(alpha)), 2));
*/
        double distBallWall = MyVector.distance(pos_b, new MyVector(b.getPosVec().x, w.getCollision().getY()));

        boolean collision = distBallWall <= r_b + 5;

        //-If-they-collide----------------------------------------------------------------------------------------------

        if(collision)
            b.setVelVec(new MyVector(b.getVelVec().x, -b.getVelVec().y));
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

    private static void centerShock(Ball b1, Ball b2){
        //-------Calculate-values---------------------------------------------------------------------------------------

        // The first three steps are the same for both balls
        MyVector step1 = MyVector.add(MyVector.multiply(b1.getVelVec(), b1.getMass()), MyVector.multiply(b2.getVelVec(), b2.getMass()));
        MyVector step2 = MyVector.divide(step1, b1.getMass() + b2.getMass());
        MyVector step3 = MyVector.multiply(step2, 2);

        MyVector step4_b1 = MyVector.subtract(step3, b1.getVelVec());
        MyVector step4_b2 = MyVector.subtract(step3, b2.getVelVec());

        //-------Set-values---------------------------------------------------------------------------------------------

        b1.setVelVec(step4_b1);
        b2.setVelVec(step4_b2);
    }

    private static void angledShock(Ball b1, Ball b2){
        //-------Calculate-values---------------------------------------------------------------------------------------

        // Norm the line between the two centers of b1 and b2.
        MyVector normedCenterLine = MyVector.norm(MyVector.subtract(b2.getPosVec(), b1.getPosVec()));
        // Find the orthogonal velocity vector of b1 and the parallel velocity vector of b2
        MyVector v1Orthogonal = MyVector.subtract(b1.getVelVec(), MyVector.orthogonalProjection(b1.getVelVec(), normedCenterLine));
        MyVector v2Parallel = MyVector.orthogonalProjection(b2.getVelVec(), normedCenterLine);

        // Switch normedCenterLine's orientation
        normedCenterLine = MyVector.multiply(normedCenterLine, -1);
        // Find the orthogonal velocity vector of b2 and the parallel velocity vector of b1
        MyVector v2Orthogonal = MyVector.subtract(b2.getVelVec(), MyVector.orthogonalProjection(b2.getVelVec(), normedCenterLine));
        MyVector v1Parallel = MyVector.orthogonalProjection(b1.getVelVec(), normedCenterLine);

        //-------Set-values---------------------------------------------------------------------------------------------

        b1.setVelVec(MyVector.add(v1Orthogonal, v2Parallel));
        b2.setVelVec(MyVector.add(v2Orthogonal, v1Parallel));
    }
}

