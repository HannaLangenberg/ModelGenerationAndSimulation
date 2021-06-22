package project.de.hshl.vcII.drawing.calculations;

import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.entities.stationary.Scissors;
import project.de.hshl.vcII.mvc.MainModel;
import project.de.hshl.vcII.utils.MyVector;

public class ScissorsCollisions {
    private static int closest_decision;
    static double lambda, rho, blade_length, a_inside_Blades;
    private static boolean lambda_onBlade, lambda_onPosTip, lambda_onNegTip;
    private static boolean rho_onBlade, rho_onPosTip, rho_onNegTip;
    private static boolean inside_Blades;
    private static boolean collision_LambdaOnBlade, collision_LambdaOnTip;
    private static boolean collision_RhoOnBlade, collision_RhoOnTip;
    static MyVector lambda_dp = new MyVector(0,0), rho_dp = new MyVector(0,0);
    private static MyVector lambda_rho_Parameters;
    private static MyVector possibleTip = new MyVector(0,0);
    private static Scissors s;
    private static int side = 0;

    /**
     * Checks for any blade collision with the ball specified (also takes the epsilon value into account).
     * @param b Ball, the specified Ball-Object
     * @param e double, safety distance for checking for collisions.
     */
    public static void checkScissors(Ball b, double e) {
        if(MainModel.get().getScissorsManager().getS() != null) {
            s = MainModel.get().getScissorsManager().getS();
            lambda_rho_Parameters = ScissorsCalculations.calc_lambda_rho_Parameters(s, b, s.getLlStart(), s.getRlStart());

            checkBlades(b, e);
            checkTips(b, e);

            reset();
            ScissorsCalculations.reset();
        }
    }

    //_HELPER___________________________________________________________________________________________________________
    private static void checkBlades(Ball b, double e) {
        lambda = lambda_rho_Parameters.x; // Wo befinden wir uns auf dem von der Klinge aufgespannten Vektor
        rho    = lambda_rho_Parameters.y;

        lambda_onBlade = lambda_rho_Parameters.x >= 0 & lambda_rho_Parameters.x <= 1; // sind wir auf der Klinge
        rho_onBlade    = lambda_rho_Parameters.y >= 0 & lambda_rho_Parameters.y <= 1;

        inside_Blades = ScissorsCalculations.checkPosition(s, b);


        if(lambda_onBlade) // Parametermäßig auf der linken Klinge
        {
            lambda_dp = MyVector.insertScalingFactorIntoEquation(s.getCrossingPoint(), MyVector.subtract(s.getCrossingPoint(), s.getLlStart()), lambda);
            collision_LambdaOnBlade = Calculator.checkDistance(b, lambda_dp, e); // Kollidieren mit der linken Klinge
        }
        if(rho_onBlade) // Parametermäßig auf der rechten Klinge
        {
            rho_dp =  MyVector.insertScalingFactorIntoEquation(s.getCrossingPoint(), MyVector.subtract(s.getCrossingPoint(), s.getRlStart()), rho);
            collision_RhoOnBlade = Calculator.checkDistance(b, rho_dp, e); // Kollidieren mit der rechten Klinge
        }

        /*
         * durch das epsilon werden an engen Stellen beide Kollisions-booleans auf true gesetzt.
         * Daher prüfen welche die nähere Klinge ist
         * */
        closest_decision = checkClosest(b); // 0 = left blade 1 = right blade 2 = both blades
        switch (closest_decision) {
            case 0: // left blade
                if (collision_LambdaOnBlade) {
                    if(s.isClosing() & inside_Blades)
                        deflect_Kinetic(b, s, 0);
                    else
                        deflect_Static(b, 0);
                }
                break;

            case 1: // right blade
                if (collision_RhoOnBlade) {
                    if(s.isClosing() & inside_Blades)
                        deflect_Kinetic(b, s,1);
                    else
                        deflect_Static(b, 1);
                }
                break;
        }
        if (collision_LambdaOnBlade & collision_RhoOnBlade & s.isClosing())
            shoot(b, s);
    }

    private static void checkTips(Ball b, double e) {
        blade_length    = MyVector.distance(s.getCrossingPoint(), s.getLlStart());

        lambda_onPosTip = (lambda >=  1 + (-b.getRadius()-e-1)/blade_length)   &  (lambda <=  1 + (b.getRadius()+e+1)/blade_length);
        lambda_onNegTip = (lambda >=  0 + (-b.getRadius()-e-1)/blade_length)   &  (lambda <=  0 + (b.getRadius()+e+1)/blade_length);
        rho_onPosTip    = (   rho >=  1 + (-b.getRadius()-e-1)/blade_length)   &  (   rho <=  1 + (b.getRadius()+e+1)/blade_length);
        rho_onNegTip    = (   rho >=  0 + (-b.getRadius()-e-1)/blade_length)   &  (   rho <=  0 + (b.getRadius()+e+1)/blade_length);

        switch (closest_decision) {
            case 0:
                if(lambda_onPosTip) {
                    possibleTip = s.getLlStart();
                }
                else if(lambda_onNegTip) {
                    possibleTip = s.getCrossingPoint();
//                        possibleTip = s.getLlEnd();
                }
                break;
            case 1:
                if(rho_onPosTip) {
                    possibleTip = s.getRlStart();
                }
                else if(rho_onNegTip) {
                    possibleTip = s.getCrossingPoint();
//                        possibleTip = s.getRlEnd();
                }
                break;
            case 2:
                break;
        }

        if(Calculator.checkDistance(b, possibleTip, e))
            deflect_Static(b, 2);
    }

    private static int checkClosest(Ball b) {
        if (ScissorsCalculations.f < 0.5)
            return 0;                               // Collision on left blade
        else
            return 1;                               // collision on right blade
    }

    /*
    * directional vector ist immer die parallele Komponente und auch nur die wird mit der Elastizität skaliert
    * */
    public static void shoot(Ball b, Scissors s) {
        s.calcDirectionalVector();
        ScissorsCalculations.calcAverageDeflectVelocity(s, lambda_dp, rho_dp);
        b.setVelVec(MyVector.add(b.getVelVec(), MyVector.multiply(MyVector.multiply(s.getDirectionalVector(), ScissorsCalculations.average_velocity), b.getElasticity())));

    }

    public static void deflect_Static(Ball b, int decision) {
        switch (decision) {
            case 0: // lambda
                if (inside_Blades) {
                    side = 0;
                }
                else
                    side = 2;
                CollisionHandling.bounceVelocity(b, side, lambda_dp);
                lambda_dp = new MyVector(0,0);
                break;
            case 1: // rho
                if (inside_Blades) {
                    side = 3;
                }
                else
                    side = 1;
                CollisionHandling.bounceVelocity(b, side, rho_dp);
                rho_dp = new MyVector(0,0);
                break;
            case 2:
                break;
        }

    }
    public static void deflect_Kinetic(Ball b, Scissors s, int decision) {
        MyVector normedCenterLine = new MyVector(0,0), vOrthogonal, vParallel;
        MyVector vParallel_Blade = new MyVector(0,0);
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
        vOrthogonal = Calculator.calc_O_component(normedCenterLine, b);
        vParallel = Calculator.calc_P_component(normedCenterLine, b);

        if (vParallel.y > 0 || vParallel.x > 0) {
            b.setVelVec(MyVector.add(vOrthogonal, MyVector.multiply(Collisions.centerShock(vParallel, b.getMass(), vParallel_Blade, 1.5), b.getElasticity())));
        }
    }

    private static void reset() {
        collision_LambdaOnBlade = collision_LambdaOnTip = collision_RhoOnBlade = collision_RhoOnTip = inside_Blades = false;
        lambda_onBlade = lambda_onPosTip = lambda_onNegTip = rho_onBlade = rho_onPosTip = rho_onNegTip = false;
        lambda = rho = side = 0;
    }
}
