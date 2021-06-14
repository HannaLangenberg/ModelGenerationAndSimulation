package project.de.hshl.vcII.drawing.calculations;

import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.entities.stationary.Scissors;
import project.de.hshl.vcII.entities.stationary.Wall;
import project.de.hshl.vcII.mvc.MainWindowModel;
import project.de.hshl.vcII.utils.MyVector;

import java.util.ArrayList;
import java.util.List;

public class Collision {
    private static double s, t;
    private static boolean collision_onEdge, collision_onCorner, s_onPosCorner, s_onNegCorner, t_onPosCorner, t_onNegCorner;
    private static boolean lambda_onBlade, rho_onBlade, collision_LambdaOnBlade, collision_RhoOnBlade;
    private static MyVector lambda_dp, lambda_hc, rho_dp, rho_hc, lambda_rho_Parameters;
    private static MyVector s_t_Parameters;
    private static MyVector possibleCorner = new MyVector(0,0);


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
    private static MyVector centerShock(MyVector v1, double m1, MyVector v2, double m2){
        //-------Calculate-values---------------------------------------------------------------------------------------

        MyVector step1 = MyVector.add(MyVector.multiply(v1, m1), MyVector.multiply(v2, m2));
        MyVector step2 = MyVector.divide(step1, m1 + m2);
        MyVector step3 = MyVector.multiply(step2, 2);

        MyVector step4 = MyVector.subtract(v1, step3);

        //-------Set-values---------------------------------------------------------------------------------------------
        return  step4;
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

        b1.setVelVec(MyVector.add(v1Orthogonal, centerShock(v1Parallel, b1.getMass(), v2Parallel, b2.getMass())));
        b2.setVelVec(MyVector.add(v2Orthogonal, centerShock(v2Parallel, b2.getMass(), v1Parallel, b1.getMass())));
    }

    //_Ball_And_Wall_Collision__________________________________________________________________________________________
    public static void checkWalls(Wall w, Ball b, double e) {
        s_t_Parameters = Calculator.calc_s_t_Parameters(w, b);
        checkSides(w, b, e);
        checkCorners(w, b, e);
        if (collision_onEdge || collision_onCorner)
        {
            if(collision_onEdge)
                Calculator.bounceVelocity(b);
            else bounceVelocity(b);

            if (w.getOrientation() == 0) {                            // Rotation nach LINKS
                Calculator.initializeForces(w, b, 0);
            }
            else if (w.getOrientation() == 1) {                       // Rotation nach RECHTS
                Calculator.initializeForces(w,b,1);
            }

            reset();
        }
    }
    public static void bounceVelocity(Ball b, int decision) {
        MyVector normedCenterLine, vOrthogonal, vParallel;
        switch (decision) {
            case 0:
                // Calculate values
                // The directional vector between the ball's position and its dropped perpendicular.
                // It has to be normed:
                normedCenterLine = MyVector.norm(MyVector.subtract(b.getPosVec(), lambda_dp));

                // Find the orthogonal and the parallel velocity vectors of b
                vOrthogonal = MyVector.subtract(MyVector.orthogonalProjection(b.getVelVec(), normedCenterLine), b.getVelVec());
                vParallel = MyVector.orthogonalProjection(b.getVelVec(), normedCenterLine);

                b.setVelVec(MyVector.add(vOrthogonal, MyVector.multiply(vParallel, -1)));
                lambda_dp = new MyVector(0,0);
                break;
            case 1:
                // Calculate values
                // The directional vector between the ball's position and its dropped perpendicular.
                // It has to be normed:
                normedCenterLine = MyVector.norm(MyVector.subtract(b.getPosVec(), rho_dp));

                // Find the orthogonal and the parallel velocity vectors of b
                vOrthogonal = MyVector.subtract(MyVector.orthogonalProjection(b.getVelVec(), normedCenterLine), b.getVelVec());
                vParallel = MyVector.orthogonalProjection(b.getVelVec(), normedCenterLine);

                b.setVelVec(MyVector.add(vOrthogonal, MyVector.multiply(vParallel, -1)));
                rho_dp = new MyVector(0,0);
                break;
        }
    }

    private static void reset() {
        collision_onCorner = false;
        collision_onEdge = false;
        s_onPosCorner = false;
        s_onNegCorner = false;
        t_onPosCorner = false;
        t_onNegCorner = false;
        s_t_Parameters = new MyVector(0,0);
        possibleCorner = new MyVector(0,0);
        s = -5;
        t = -5;
    }

    //_Ball_And_Wall_Collision__________________________________________________________________________________________
    public static void checkScissors(Ball b, double e) {
        if(MainWindowModel.get().getScissorsManager().getS() != null) {
            Scissors s = MainWindowModel.get().getScissorsManager().getS();
            s.calcCrossingPoint();
            lambda_rho_Parameters = Calculator.calcBlade(s, b, s.getLlStart(), s.getRlStart());
            lambda_onBlade = lambda_rho_Parameters.x >= 0 & lambda_rho_Parameters.x <= 1;
            rho_onBlade    = lambda_rho_Parameters.y >= 0 & lambda_rho_Parameters.y <= 1;
            lambda_hc = Calculator.calcMissingXCoordinate(s, b, s.getLlStart());
            rho_hc    = Calculator.calcMissingXCoordinate(s, b, s.getRlStart());
            int decision = checkClosest(b); // 0 = left blade 1 = right blade

            switch (decision) {
                case 0: // left blade
                    if(lambda_onBlade) {
                        lambda_dp = Calculator.calcCoord_onBlade(s, lambda_rho_Parameters.x, s.getLlStart());
                        collision_LambdaOnBlade = Calculator.checkDistance(b, lambda_dp, e);
                        /*if(collision_LambdaOnBlade)
                            deflect(b,0);*/
                        if (collision_LambdaOnBlade & !s.isClosing()) {
                            deflect(b, 0);
                        }
                        else if(collision_LambdaOnBlade & s.isClosing()) {
                            shoot(b, s);
                        }
                    }

                    break;
                case 1: // right blade
                    if(rho_onBlade) {
                        rho_dp = Calculator.calcCoord_onBlade(s, lambda_rho_Parameters.y, s.getRlStart());
                        collision_RhoOnBlade = Calculator.checkDistance(b, rho_dp, e);
                        /*if (collision_RhoOnBlade)
                            deflect(b, 1);*/
                        if (collision_RhoOnBlade & !s.isClosing()) {
                            deflect(b,1);
                        }
                        else if (collision_RhoOnBlade & s.isClosing()) {
                            shoot(b, s);
                        }
                    }

                    break;
            }


        }
    }
    private static int checkClosest(Ball b) {
        double lambdaDistance = Math.abs(b.getPosVec().x - lambda_hc.x);
        double    rhoDistance = Math.abs(b.getPosVec().x -    rho_hc.x);

        if(lambdaDistance < rhoDistance)
            return 0; // Collision on left blade
        else return 1; // Collision on right blade
    }

    public static void shoot(Ball b, Scissors s) {
        s.calcDirectionalVector();
        MyVector vOrthogonal = MyVector.subtract(MyVector.orthogonalProjection(b.getVelVec(), Scissors.getDirectionalVector()), b.getVelVec());
        MyVector vParallel = MyVector.orthogonalProjection(b.getVelVec(), Scissors.getDirectionalVector());

        b.setVelVec(MyVector.add(b.getVelVec(), Scissors.getDirectionalVector()));
//        b.setVelVec(MyVector.add(vOrthogonal, MyVector.add(vParallel, Scissors.getDirectionalVector())));

    }
    public static void deflect(Ball b, int decision) {
        MyVector normedCenterLine, vOrthogonal, vParallel;
        switch (decision) {
            case 0: // lambda
                normedCenterLine = MyVector.norm(MyVector.subtract(b.getPosVec(), lambda_dp));

                vOrthogonal = MyVector.subtract(MyVector.orthogonalProjection(b.getVelVec(), normedCenterLine), b.getVelVec());
                vParallel = MyVector.orthogonalProjection(b.getVelVec(), normedCenterLine);
                lambda_dp = new MyVector(0,0);
                break;
            case 1: // rho
                normedCenterLine = MyVector.norm(MyVector.subtract(b.getPosVec(), rho_dp));

                vOrthogonal = MyVector.subtract(MyVector.orthogonalProjection(b.getVelVec(), normedCenterLine), b.getVelVec());
                vParallel = MyVector.orthogonalProjection(b.getVelVec(), normedCenterLine);
                rho_dp = new MyVector(0,0);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + decision);
        }
        b.setVelVec(MyVector.add(vOrthogonal, MyVector.multiply(vParallel, -1)));
    }

    //_Split_and_rearrange_velocity_vector_using_orthogonal_projection__________________________________________________
    private static void bounceVelocity(Ball b) {
        // Calculate values
        // The directional vector between the ball's position and its dropped perpendicular.
        // It has to be normed:
        MyVector normedCenterLine = MyVector.norm(MyVector.subtract(b.getPosVec(), possibleCorner));

        // Find the orthogonal and the parallel velocity vectors of b
        MyVector vOrthogonal = MyVector.subtract(MyVector.orthogonalProjection(b.getVelVec(), normedCenterLine), b.getVelVec());
        MyVector vParallel = MyVector.orthogonalProjection(b.getVelVec(), normedCenterLine);

        b.setVelVec(MyVector.add(vOrthogonal, MyVector.multiply(vParallel, -1)));

    }

    private static void checkCorners(Wall w, Ball b, double e) {
        s_onPosCorner = (s >=  1 + (-b.getRadius()-e)/75)  &  (s <=  1 + (b.getRadius()+e)/75);
        s_onNegCorner = (s >= -1 + (-b.getRadius()-e)/75)  &  (s <= -1 + (b.getRadius()+e)/75);
        t_onPosCorner = (t >=  1 + (-b.getRadius()-e)/75)  &  (t <=  1 + (b.getRadius()+e)/75);
        t_onNegCorner = (t >= -1 + (-b.getRadius()-e)/75)  &  (t <= -1 + (b.getRadius()+e)/75);

        if(s_onPosCorner & t_onPosCorner) {
            possibleCorner = Calculator.calcCoord_onEdge(w, new MyVector(1, 1));
            System.out.println("1");
        }
        else if(s_onPosCorner & t_onNegCorner) {
            possibleCorner = Calculator.calcCoord_onEdge(w, new MyVector(1,-1));
            System.out.println("2");
        }
        else if(s_onNegCorner & t_onNegCorner) {
            possibleCorner = Calculator.calcCoord_onEdge(w, new MyVector(-1,-1));
            System.out.println("3");
        }
        else if(s_onNegCorner & t_onPosCorner) {
            possibleCorner = Calculator.calcCoord_onEdge(w, new MyVector(-1,1));
            System.out.println("4");
        }
        collision_onCorner = Calculator.checkDistance(b, possibleCorner, e);

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
        collision_onEdge = Calculator.checkDistance(b, e, s_onEdge||t_onEdge);
    }
    public static boolean checkBallPlacement(Wall w, Ball b, MyVector s_t_Parameters, double e) {
        s = s_t_Parameters.x;
        t = s_t_Parameters.y;
        boolean s_onEdge = s >= -1 && s <= 1;
        boolean t_onEdge = t >= -1 && t <= 1;

        if (s_onEdge) {
            Calculator.setDroppedPerpendicular(Calculator.calcCoord_onEdge(w, new MyVector(s, -1)));
        }
        return Calculator.checkDistance(b, e+15, s_onEdge||t_onEdge);
    }
    public static boolean setBallPlacement(Wall w, Ball b, MyVector s_t_Parameters, double e) {
        s = s_t_Parameters.x;
        t = s_t_Parameters.y;
        boolean s_onEdge = s >= -1 && s <= 1;
        boolean t_onEdge = t >= -1 && t <= 1;

        if (s_onEdge) {
            Calculator.calcSideCollisions(w, s_t_Parameters, 0);
        }
        return Calculator.checkDistance(b, e, s_onEdge||t_onEdge);
    }

    public static void checkScreen(Ball b, double e) {
        if(b.getPosVec().y > MainWindowModel.get().getADrawingPane().getHeight() - b.getRadius() - e || b.getPosVec().y < b.getRadius() + e) {
            b.setVel0Vec(new MyVector(b.getVel0Vec().x, -b.getVel0Vec().y));
            b.setVelVec(new MyVector(b.getVelVec().x, -b.getVelVec().y));
        }

        if(b.getPosVec().x > MainWindowModel.get().getADrawingPane().getWidth() - b.getRadius() - e || b.getPosVec().x < b.getRadius() + e) {
            b.setVel0Vec(new MyVector(-b.getVel0Vec().x, b.getVel0Vec().y));
            b.setVelVec(new MyVector(-b.getVelVec().x, b.getVelVec().y));
        }
    }
}

