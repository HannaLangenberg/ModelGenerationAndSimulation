package project.de.hshl.vcII.drawing.calculations;

import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.entities.stationary.Wall;
import project.de.hshl.vcII.mvc.MainWindowModel;
import project.de.hshl.vcII.utils.MyVector;

import java.util.ArrayList;
import java.util.List;

public class Collision {
    private static double s, t;
    private static boolean collision_onEdge;
    private static boolean collision_onCorner;
    private static MyVector s_t_Parameters;
    private static MyVector possibleCorner;


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

    //_Ball_And_Wall_Collision__________________________________________________________________________________________
    public static void checkWalls(Wall w, Ball b, double e) {
        s_t_Parameters = Calculator.calc_s_t_Parameters(w,b);
        checkSides(w, b, e);
        checkCorners(w, b, e);
        if (collision_onEdge || collision_onCorner)
        {
            Calculator.bounceVelocity(b);
            collision_onEdge = false;
            collision_onCorner = false;
        }
    }

    private static void checkCorners(Wall w, Ball b, double e) {
        boolean s_onPosCorner = s >= -1-b.getRadius()-e  &&  s <= -1+e;
        boolean s_onNegCorner = s >= 1-e                 &&  s <= 1+b.getRadius()+e;
        boolean t_onPosCorner = t >= 1-e                 &&  t <= 1+b.getRadius()+e;
        boolean t_onNegCorner = t >= -1-b.getRadius()-e  &&  t <= -1+e;

        if(s_onNegCorner && t_onNegCorner) {
            possibleCorner = w.getPosVec();
        }
        else if(s_onPosCorner && t_onNegCorner) {
            possibleCorner = new MyVector(w.getPosVec().x + w.getCollision().getWidth(),    w.getPosVec().y);
        }
        else if(s_onNegCorner && t_onPosCorner) {
            possibleCorner = new MyVector(w.getPosVec().x,                                  w.getPosVec().y + w.getCollision().getHeight());
        }
        else if(s_onPosCorner && t_onPosCorner) {
            possibleCorner = new MyVector(w.getPosVec().x + w.getCollision().getWidth(), w.getPosVec().y + w.getCollision().getHeight());
        }
        collision_onCorner = Calculator.checkDistance(b, possibleCorner,e);

    }
    private static void checkSides(Wall w, Ball b, double e) {
        /*
         * Es gibt verschiedene Bereiche, die unser s und t kombiniert abdecken.
         * Je nach vorhandener Kombination müssen wir auf Kollision prüfen oder eine weitere Methode, die gesondert
         * die Ecken überprüft aufrufen. Bereiche:
         *
         *        ----------------------------------------------------- → x
         *      |
         *      |                            0
         *      |            4 ----------------------------- 5
         *      |             |                            |
         *      |           2 |             X              | 3
         *      |             |                            |
         *      |           6 ----------------------------- 7
         *      ↓ y                         1
         *   e = epsilon
         * SEITEN: ggf e durch Eckprüfung abfangen
         *   0) Oberseite:       -1 <= s <= 1      &          t = -1
         *   1) Unterseite:      -1 <= s <= 1      &          t =  1
         *   2) Links:                 s = -1      &    -1 <= t <= 1
         *   3) Rechts:                s =  1      &    -1 <= t <= 1
         *
         * ECKEN:
         *   4) OL:              -1-r-e <= s <= -1      &    -1-r-e <= t <= -1
         *   5) OR:                   1 <= s <= 1+r+e   &    -1-r-e <= t <= -1
         *   6) UL:              -1-r-e <= s <= -1      &         1 <= t <= 1+r+e
         *   7) UR:                   1 <= s <= 1+r+e   &         1 <= t <= 1+r+e
         *
         * */
        s = s_t_Parameters.x;
        t = s_t_Parameters.y;
        boolean s_onEdge = s >= -1 && s <= 1;
        boolean t_onEdge = t >= -1 && t <= 1;

        if (s_onEdge) {
            Calculator.calcSideCollisions(w, s_t_Parameters, 0);
        }
        if (t_onEdge) {
            Calculator.calcSideCollisions(w, s_t_Parameters, 1);
        }
        collision_onEdge = Calculator.checkDistance(b, e);
    }

    public static void checkPosition(Ball b, double e) {
        if(b.getPosVec().x > MainWindowModel.get().getADrawingPane().getWidth() - b.getRadius() - e || b.getPosVec().x < b.getRadius() + e) {
            b.setVelVec(new MyVector(-b.getVelVec().x, b.getVelVec().y));
        }

        if(b.getPosVec().y > MainWindowModel.get().getADrawingPane().getHeight() - b.getRadius() - e || b.getPosVec().y < b.getRadius() + e) {
            b.setVelVec(new MyVector(b.getVelVec().x, -b.getVelVec().y));
        }
    }
}

