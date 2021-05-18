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
            ballOnWall(w, b, epsilon);
    }

    private static void  ballOnWall(Wall w, Ball b, double epsilon){
        //-Ball-and-Wall-Collide----------------------------------------------------------------------------------------

        /*// Radius of the ball.
        double r_b = b.getRadius();

        // Rotation of the Wall
        double alpha = w.getSpin();

        // Positions for the wall
        double rX = w.getCollision().getX();
        double rY = w.getCollision().getY();

        // Positions for the ball
        double bX = b.getPosVec().x;
        double bY = b.getPosVec().y;

        // Parts of the calc so it doesn't get too long
        double v1 = (rX - bX) * Math.cos(Math.toRadians(alpha));
        double v2 = (rY - bY) * Math.sin(Math.toRadians(alpha));
        double v = v1 + v2;

        // Top left of the wall
        double x = rX + v * (-1) * Math.cos(Math.toRadians(alpha));
        double y = rY + v * (-1) * Math.sin(Math.toRadians(alpha));

        double distBallWall = MyVector.distance(b.getPosVec(), new MyVector(x,y));

        boolean collision = false;
        boolean onWall = false;

        double lambda = calculateLambda(w, b, 0);

        if(0 <= lambda && lambda <= 1)
            onWall = true;

        if(distBallWall <= r_b + 5 && onWall)
            collision = true;

        // bottom

        *//*
        // coordinates of the projected point
        // x = (vec1 * vec2)/|vec1|^2

        // Vector parallel to the wall top
        MyVector vec1 = new MyVector(x, y);

        // Vector with Length w.getCollision().getHeight();
        // and starting at pos_w
        MyVector vec2 = new MyVector(y, x);
*//*

        //-If-they-collide----------------------------------------------------------------------------------------------

        if(collision)
            b.setVelVec(new MyVector(b.getVelVec().x, -b.getVelVec().y));*/


        for(int i = 0; i < 2; i++) {
            calculateLambda(w, b, i, epsilon);
        }
    }

    private static void calculateDroppedPerpendicular(Ball b, MyVector r, double x_y_stretch, double alpha, double epsilon) {
        // Positions for the wall
        double rX = r.x;
        double rY = r.y;

        // (X/Y) coordinates of the dropped perpendicular
        double x = rX + x_y_stretch * (-1) * Math.cos(Math.toRadians(alpha));
        double y = rY + x_y_stretch * (-1) * Math.sin(Math.toRadians(alpha));

        MyVector dp = new MyVector(x,y);
        
        if(MyVector.distance(b.getPosVec(), dp) <= b.getRadius() + epsilon)
            correctVelocity(b, dp);

    }

    private static void correctVelocity(Ball b, MyVector dp) {
        // Calculate values
        // Norm the line between the two centers of b1 and b2.
        MyVector normedCenterLine = MyVector.norm(MyVector.subtract(b.getPosVec(), dp));

        // Find the orthogonal and the parallel velocity vectors of b

        MyVector vOrthogonal = MyVector.subtract(MyVector.orthogonalProjection(b.getVelVec(), normedCenterLine), b.getVelVec());
        MyVector vParallel = MyVector.orthogonalProjection(b.getVelVec(), normedCenterLine);

        b.setVelVec(MyVector.add(vOrthogonal, MyVector.multiply(vParallel, -1)));
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

        MyVector step4_b1 = MyVector.subtract(b1.getVelVec(), step3);
        MyVector step4_b2 = MyVector.subtract(b2.getVelVec(), step3);

        //-------Set-values---------------------------------------------------------------------------------------------

        b1.setVelVec(step4_b1);
        b2.setVelVec(step4_b2);
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

        b1.setVelVec(MyVector.add(v1Orthogonal, v2Parallel));
        b2.setVelVec(MyVector.add(v2Orthogonal, v1Parallel));
    }

    private static void calculateLambda(Wall w, Ball b, int decision, double epsilon){
        /* decision variable:
        * case 0 = top of wall
        * case 1 = bottom of wall
        */
        double alpha = w.getSpin();

        // Positions for the wall
        double rW = w.getCollision().getWidth();
        double rX = w.getCollision().getX();
        double rY = 0;
        switch (decision) {
            case 0: rY = w.getCollision().getY();
                break;
            case 1: rY = w.getCollision().getY() + w.getCollision().getHeight();
                break;
            default:
                assert false: "Unknown Case";
        }

        // Positions for the ball
        double bX = b.getPosVec().x;
        double bY = b.getPosVec().y;

        // Parts of the calc so it doesn't get too long
        double x_stretch = (rX - bX) * Math.cos(Math.toRadians(alpha));
        double y_stretch = (rY - bY) * Math.sin(Math.toRadians(alpha));
        double x_y_stretch = x_stretch + y_stretch;

        if(0 <= x_y_stretch / rW * -1 && x_y_stretch / rW * -1 <= 1)
            calculateDroppedPerpendicular(b, new MyVector(rX, rY),  x_y_stretch, alpha, epsilon);
    }
}

