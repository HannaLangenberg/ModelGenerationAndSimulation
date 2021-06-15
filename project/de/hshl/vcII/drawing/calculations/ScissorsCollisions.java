package project.de.hshl.vcII.drawing.calculations;

import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.entities.stationary.Scissors;
import project.de.hshl.vcII.mvc.MainWindowModel;
import project.de.hshl.vcII.utils.MyVector;

public class ScissorsCollisions {
    static double lambda, rho;
    private static boolean lambda_onBlade;
    private static boolean rho_onBlade;
    private static boolean collision_LambdaOnBlade;
    private static boolean collision_RhoOnBlade;
    static MyVector lambda_dp = new MyVector(0,0);
    static MyVector rho_dp = new MyVector(0,0);
    private static MyVector lambda_hc;
    private static MyVector rho_hc;
    private static MyVector lambda_rho_Parameters;
    private static Scissors s;

    public static void checkScissors(Ball b, double e) {
        if(MainWindowModel.get().getScissorsManager().getS() != null) {
            s = MainWindowModel.get().getScissorsManager().getS();
            lambda_rho_Parameters = ScissorsCalculations.calc_lambda_rho_Parameters(s, b, s.getLlStart(), s.getRlStart());

            checkBlades(b, e);
        }
    }

    private static void checkBlades(Ball b, double e) {
        lambda = lambda_rho_Parameters.x; // Wo befinden wir uns auf dem von der Klinge aufgespannten Vektor
        rho    = lambda_rho_Parameters.y;

        lambda_onBlade = lambda_rho_Parameters.x >= 0 & lambda_rho_Parameters.x <= 1; // sind wir auf der Klinge
        rho_onBlade    = lambda_rho_Parameters.y >= 0 & lambda_rho_Parameters.y <= 1;

        lambda_hc = ScissorsCalculations.calcMissingXCoordinate(s, b, s.getLlStart());
        rho_hc    = ScissorsCalculations.calcMissingXCoordinate(s, b, s.getRlStart());

        if(lambda_onBlade) // Parametermäßig auf der linken Klinge
        {
            lambda_dp = ScissorsCalculations.calcDroppedPerpendicular(s, lambda, s.getLlStart());
            collision_LambdaOnBlade = Calculator.checkDistance(b, lambda_dp, e); // Kollidieren mit der linken Klinge
        }
        if(rho_onBlade) // Parametermäßig auf der rechten Klinge
        {
            rho_dp = ScissorsCalculations.calcDroppedPerpendicular(s, rho, s.getRlStart());
            collision_RhoOnBlade = Calculator.checkDistance(b, rho_dp, e); // Kollidieren mit der rechten Klinge
        }

        /*
        * durch das epsilon werden an engen Stellen beide Kollisions-booleans auf true gesetzt.
        * Daher prüfen welche die nähere Klinge ist
        * */
        int decision = checkClosest(b); // 0 = left blade 1 = right blade 2 = both blades
        switch (decision) {
            case 0: // left blade
                if (collision_LambdaOnBlade) {
                    if(s.isClosing() & (lambda_hc.x < b.getPosVec().x & b.getPosVec().x < rho_hc.x))
                        deflect_Dynamic(b, s, 0);
                    else
                        deflect_Static(b, 0);
                }
                break;

            case 1: // right blade
                if (collision_RhoOnBlade) {
                    if(s.isClosing() & (lambda_hc.x < b.getPosVec().x & b.getPosVec().x < rho_hc.x))
                        deflect_Dynamic(b, s,1);
                    else
                        deflect_Static(b, 1);
                }
                break;

            case 2: // both blades
                if (collision_LambdaOnBlade & collision_RhoOnBlade & s.isClosing())
                    shoot(b, s);
                break;
        }
    }

    private static int checkClosest(Ball b) {
        double lambdaDistance = Math.abs(b.getPosVec().x - lambda_hc.x);
        double    rhoDistance = Math.abs(b.getPosVec().x -    rho_hc.x);

        if(lambdaDistance == rhoDistance)
            return 2; // Collision on both blades
        else if(lambdaDistance < rhoDistance)
            return 0; // Collision on left blade
        else
            return 1;// Collision on right blade
    }

    public static void shoot(Ball b, Scissors s) {
        s.calcDirectionalVector();
        ScissorsCalculations.calcAverageDeflectVelocity(s, lambda_dp, rho_dp);
//        MyVector vOrthogonal = MyVector.subtract(MyVector.orthogonalProjection(b.getVelVec(), s.getDirectionalVector()), b.getVelVec());
//        MyVector vParallel = MyVector.orthogonalProjection(b.getVelVec(), s.getDirectionalVector());

        b.setVelVec(MyVector.add(b.getVelVec(), MyVector.multiply(s.getDirectionalVector(), ScissorsCalculations.average_velocity)));
//        b.setVelVec(MyVector.add(vOrthogonal, MyVector.add(vParallel, s.getDirectionalVector())));

    }

    public static void deflect_Static(Ball b, int decision) {
        MyVector normedCenterLine = new MyVector(0,0), vOrthogonal, vParallel;
        switch (decision) {
            case 0: // lambda
                normedCenterLine = MyVector.norm(MyVector.subtract(b.getPosVec(), lambda_dp));
                lambda_dp = new MyVector(0,0);
                break;
            case 1: // rho
                normedCenterLine = MyVector.norm(MyVector.subtract(b.getPosVec(), rho_dp));
                rho_dp = new MyVector(0,0);
                break;
        }
        vOrthogonal = MyVector.subtract(MyVector.orthogonalProjection(b.getVelVec(), normedCenterLine), b.getVelVec());
        vParallel = MyVector.orthogonalProjection(b.getVelVec(), normedCenterLine);

        b.setVelVec(MyVector.add(vOrthogonal, MyVector.multiply(vParallel, -1)));
    }
    public static void deflect_Dynamic(Ball b, Scissors s, int decision) {
        MyVector normedCenterLine = new MyVector(0,0), vOrthogonal, vParallel, vParallel_Blade = new MyVector(0,0);
        switch (decision) {
            case 0: // lambda
                normedCenterLine = MyVector.norm(MyVector.subtract(b.getPosVec(), lambda_dp));
                ScissorsCalculations.calcDeflectVelocity(s, lambda_dp, rho_dp);
                vParallel_Blade = MyVector.multiply(MyVector.multiply(normedCenterLine, -1), ScissorsCalculations.lambda_velocity);
                lambda_dp = new MyVector(0,0);
                break;
            case 1: // rho
                normedCenterLine = MyVector.norm(MyVector.subtract(b.getPosVec(), rho_dp));
                ScissorsCalculations.calcDeflectVelocity(s, lambda_dp, rho_dp);
                vParallel_Blade = MyVector.multiply(MyVector.multiply(normedCenterLine, -1), ScissorsCalculations.rho_velocity);
                rho_dp = new MyVector(0,0);
                break;
        }
        vOrthogonal = MyVector.subtract(MyVector.orthogonalProjection(b.getVelVec(), normedCenterLine), b.getVelVec());
        vParallel = MyVector.orthogonalProjection(b.getVelVec(), normedCenterLine);

        b.setVelVec(MyVector.add(vOrthogonal, Collision.centerShock(vParallel, b.getMass(), vParallel_Blade, 1.5)));
//        b.setVelVec(MyVector.add(vOrthogonal, MyVector.multiply(vParallel, -1)));



        /*if (s.isClosing() & (lambda_hc.x < b.getPosVec().x & b.getPosVec().x < rho_hc.x)) {
//            centerShock(b.getVelVec(), b.getMass(), Calculator.getLambda_velocity(), 20);
//            b.setVelVec(MyVector.multiply(MyVector.add(vOrthogonal, MyVector.multiply(vParallel, -1)), (MainWindowModel.get().getScissorsSpeed()+1)));
//            b.setVelVec(MyVector.add(vOrthogonal, MyVector.multiply( MyVector.multiply(vParallel, -1), (MainWindowModel.get().getScissorsSpeed()+1))));
//            vParallel = MyVector.multiply(vParallel, MainWindowModel.get().getScissorsSpeed()+1);
              b.setAccVec(MyVector.add(b.getAccVec(), MyVector.multiply(MyVector.multiply(normedCenterLine, -1), (MainWindowModel.get().getScissorsSpeed()*600))));
              System.out.println("Accelerated");
        }*/
    }

    /*
    * normedCenterLine = MyVector.norm(MyVector.subtract(b.getPosVec(), lambda_dp));
                ScissorsCalculations.calcDeflectVelocity(s, lambda_dp, rho_dp);

                /*vOrthogonal = MyVector.subtract(MyVector.orthogonalProjection(b.getVelVec(), normedCenterLine), b.getVelVec());
                vParallel = MyVector.orthogonalProjection(b.getVelVec(), normedCenterLine);*/

//                if (s.isClosing() & (lambda_hc.x < b.getPosVec().x & b.getPosVec().x < rho_hc.x)) {

//        MyVector lambda_Vel = Collision.centerShock(b.getVelVec(), b.getMass(), MyVector.multiply(normedCenterLine, -Calculator.getLambda_velocity()), 20);



//            b.setVelVec(MyVector.multiply(MyVector.add(vOrthogonal, MyVector.multiply(vParallel, -1)), (MainWindowModel.get().getScissorsSpeed()+1)));
//            b.setVelVec(MyVector.add(vOrthogonal, MyVector.multiply( MyVector.multiply(vParallel, -1), (MainWindowModel.get().getScissorsSpeed()+1))));
//            vParallel = MyVector.multiply(vParallel, MainWindowModel.get().getScissorsSpeed()+1);
//                    b.setAccVec(MyVector.add(b.getAccVec(), MyVector.multiply(MyVector.multiply(normedCenterLine, -1), (MainWindowModel.get().getScissorsSpeed()*600))));
//        System.out.println("Accelerated");
//    }


    private static MyVector bladeShock(MyVector p1, MyVector v1, double m1, MyVector p2, MyVector v2, double m2) {
        MyVector normedCenterLine = MyVector.norm(MyVector.subtract(p1, p2));
        // Find the orthogonal velocity vector of b1 and the parallel velocity vector of b2
        MyVector v1Orthogonal = MyVector.subtract(MyVector.orthogonalProjection(v1, normedCenterLine), v1);
        MyVector v2Parallel = MyVector.orthogonalProjection(v2, normedCenterLine);

        normedCenterLine = MyVector.multiply(normedCenterLine, -1);
        MyVector v1Parallel = MyVector.orthogonalProjection(v1, normedCenterLine);

        return MyVector.add(v1Orthogonal, Collision.centerShock(v1Parallel, m1, v2Parallel, m2));
        /*centerShock(b.getVelVec(), b.getMass(), MyVector.multiply(normedCenterLine, -Calculator.getLambda_velocity()), 20);*/
    }

    public static void bounceVelocity(Ball b, int decision) {
        MyVector normedCenterLine, vOrthogonal, vParallel;
        switch (decision) {
            case 0:
                // Calculate values
                // The directional vector between the ball's position and its dropped perpendicular.
                // It has to be normed:
                normedCenterLine = MyVector.norm(MyVector.subtract(b.getPosVec(), ScissorsCollisions.lambda_dp));

                // Find the orthogonal and the parallel velocity vectors of b
                vOrthogonal = MyVector.subtract(MyVector.orthogonalProjection(b.getVelVec(), normedCenterLine), b.getVelVec());
                vParallel = MyVector.orthogonalProjection(b.getVelVec(), normedCenterLine);

                b.setVelVec(MyVector.add(vOrthogonal, MyVector.multiply(vParallel, -1)));
                ScissorsCollisions.lambda_dp = new MyVector(0,0);
                break;
            case 1:
                // Calculate values
                // The directional vector between the ball's position and its dropped perpendicular.
                // It has to be normed:
                normedCenterLine = MyVector.norm(MyVector.subtract(b.getPosVec(), ScissorsCollisions.rho_dp));

                // Find the orthogonal and the parallel velocity vectors of b
                vOrthogonal = MyVector.subtract(MyVector.orthogonalProjection(b.getVelVec(), normedCenterLine), b.getVelVec());
                vParallel = MyVector.orthogonalProjection(b.getVelVec(), normedCenterLine);

                b.setVelVec(MyVector.add(vOrthogonal, MyVector.multiply(vParallel, -1)));
                ScissorsCollisions.rho_dp = new MyVector(0,0);
                break;
        }
    }
}
