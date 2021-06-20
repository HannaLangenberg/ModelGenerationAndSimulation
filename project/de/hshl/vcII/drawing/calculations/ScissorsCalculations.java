package project.de.hshl.vcII.drawing.calculations;

import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.entities.stationary.Scissors;
import project.de.hshl.vcII.utils.MyVector;

public class ScissorsCalculations {

    static double lambda, rho, a, o, f;
    private static MyVector a_onLambdaBlade, a_onRhoBlade;
    static double lambda_velocity, rho_velocity, average_velocity;
    static MyVector cp, rv, bp, hv;

    /**
     * Method for checking if the Ball is colliding with the top-bit of the Scissors.
     * (Could be expanded to check collision with the whole Scissors.)
     * @param s check this Scissors
     * @param b check this Ball
     * @return true if the Scissors collides, false if not
     */
    public static boolean checkPosition(Scissors s, Ball b) {
        //Schnittpunkt ball mit hv der kurzen Rechteckseite und linke klinge von cp bis llStart
         calcCommonPoint(s, b);
        // lambda (a) auch für rho verwenden und Punkte für Vektoren berechnen
         a_onLambdaBlade = calcDroppedPerpendicular(s, a, s.getLlStart());
         a_onRhoBlade    = calcDroppedPerpendicular(s, a, s.getRlStart());
        // ball schnittpunkt zwischen 0 und 1 innen
        f = MyVector.insertPintoEquation(a_onLambdaBlade, MyVector.subtract(a_onLambdaBlade, a_onRhoBlade), b.getPosVec());

        if(f >= 0 & f <= 1)
            return true;
        else
            return false;

    }

    private static void calcCommonPoint(Scissors s, Ball b) {
        cp = s.getCrossingPoint();
        rv = MyVector.subtract(s.getCrossingPoint(), s.getLlStart());
        bp = b.getPosVec();
        hv = MyVector.norm(MyVector.subtract(s.getRlStart(), s.getLlStart()));

        // entspricht unserem lambda, also Skalierungsfaktor entlang der linken Klinge
        a = ((cp.y - bp.y) * hv.x + (-cp.x + bp.x) * hv.y)
            /(-rv.y * hv.x + rv.x * hv.y);

        // Skalierungsfaktor entlang des Hilfsvektors
        o = ((cp.x - bp.x)*(-s.getLlStart().y - cp.y) + (cp.y - bp.y)*(rv.x))
            /(-(rv.y) * hv.x + (rv.x) * hv.y);
    }

    //_HELPER___________________________________________________________________________________________________________
    public static void reset() {
        lambda = rho = a = o = f = 0 ;
        lambda_velocity = rho_velocity = average_velocity = 0;
        a_onLambdaBlade = a_onRhoBlade = cp = hv = bp = rv = new MyVector(0,0);
    }

    public static MyVector calc_lambda_rho_Parameters(Scissors s, Ball b, MyVector left, MyVector right) {
        lambda = ((s.getCrossingPoint().x - b.getPosVec().x)*(left.x - s.getCrossingPoint().x)+(s.getCrossingPoint().y - b.getPosVec().y)*(left.y - s.getCrossingPoint().y))
                /(-Math.pow((left.x-s.getCrossingPoint().x), 2) - Math.pow((left.y - s.getCrossingPoint().y), 2));
        rho    = ((s.getCrossingPoint().x - b.getPosVec().x)*(right.x - s.getCrossingPoint().x)+(s.getCrossingPoint().y - b.getPosVec().y)*(right.y - s.getCrossingPoint().y))
                /(-Math.pow((right.x-s.getCrossingPoint().x), 2) - Math.pow((right.y - s.getCrossingPoint().y), 2));

        return new MyVector(lambda, rho);
    }

    public static MyVector calcCoordOnBlade(Scissors s, Ball b, MyVector line) {
        double x = s.getCrossingPoint().x + ((b.getPosVec().y - s.getCrossingPoint().y)/(line.y - s.getCrossingPoint().y))*(line.x - s.getCrossingPoint().x);
        return new MyVector(x, b.getPosVec().y);
    }

    public static MyVector calcDroppedPerpendicular(Scissors s, double omega, MyVector line) {
        return MyVector.add(s.getCrossingPoint(), MyVector.multiply(MyVector.subtract(s.getCrossingPoint(), line), omega));
    }

    public static void calcDeflectVelocity(Scissors s, MyVector lambda_dp, MyVector rho_dp) {
        lambda_velocity = (Math.PI * Math.abs(MyVector.distance(s.getCrossingPoint(), lambda_dp)))/3;
        rho_velocity    = (Math.PI * Math.abs(MyVector.distance(s.getCrossingPoint(), rho_dp)))/3;
    }

    public static void calcAverageDeflectVelocity(Scissors s, MyVector lambda_dp, MyVector rho_dp) {
        calcDeflectVelocity(s, lambda_dp, rho_dp);
        average_velocity = (lambda_velocity + rho_velocity) / 2;
    }
}
