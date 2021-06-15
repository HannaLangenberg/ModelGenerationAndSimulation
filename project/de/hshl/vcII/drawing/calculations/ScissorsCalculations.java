package project.de.hshl.vcII.drawing.calculations;

import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.entities.stationary.Scissors;
import project.de.hshl.vcII.utils.MyVector;

public class ScissorsCalculations {

    private static double lambda;
    private static double rho;
    static double lambda_velocity, rho_velocity, average_velocity;

    public static int checkPosition(Ball b, MyVector hc) {
        if(b.getPosVec().x < hc.x) // Links
            return 0;
        else if(b.getPosVec().x > hc.x) // Rechts
            return 1;
        else return 2;
    }

    public static MyVector calc_lambda_rho_Parameters(Scissors s, Ball b, MyVector left, MyVector right) {
        lambda = ((s.getCrossingPoint().x - b.getPosVec().x)*(left.x - s.getCrossingPoint().x)+(s.getCrossingPoint().y - b.getPosVec().y)*(left.y - s.getCrossingPoint().y))
                /(-Math.pow((left.x-s.getCrossingPoint().x), 2) - Math.pow((left.y - s.getCrossingPoint().y), 2));
        rho    = ((s.getCrossingPoint().x - b.getPosVec().x)*(right.x - s.getCrossingPoint().x)+(s.getCrossingPoint().y - b.getPosVec().y)*(right.y - s.getCrossingPoint().y))
                /(-Math.pow((right.x-s.getCrossingPoint().x), 2) - Math.pow((right.y - s.getCrossingPoint().y), 2));

        return new MyVector(lambda, rho);
    }

    public static MyVector calcMissingXCoordinate(Scissors s, Ball b, MyVector line) {
        double x = s.getCrossingPoint().x + ((b.getPosVec().y - s.getCrossingPoint().y)/(line.y - s.getCrossingPoint().y))*(line.x - s.getCrossingPoint().x);
        return new MyVector(x, b.getPosVec().y);
    }

    // calc dp
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
