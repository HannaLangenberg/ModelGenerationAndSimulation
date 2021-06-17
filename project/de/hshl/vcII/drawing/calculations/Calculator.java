package project.de.hshl.vcII.drawing.calculations;

import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.mvc.MainWindowModel;
import project.de.hshl.vcII.utils.MyVector;
import project.de.hshl.vcII.utils.Utils;

public class Calculator {
    private static double initial_TotE, totE, potE, kinE, lostE = 0;

    public static boolean checkDistance(Ball b, MyVector dp_Coord, double epsilon) {
        return MyVector.distance(b.getPosVec(), dp_Coord) <= b.getRadius() + epsilon;
    }

    public static void calcInitial_TotalEnergy() {
        // initial_TotE = potE + kinE
        for (Ball b : MainWindowModel.get().getBallManager().getBalls()) {
            calcPotentialEnergy(b);
            calcKineticEnergy(b);
            calcLostEnergy(b);
            initial_TotE = totE = potE + kinE;
            b.setTotE(initial_TotE);
        }
    }

    public static void calcTotalEnergy() {
        // totE = potE + kinE + lossE
        for (Ball b : MainWindowModel.get().getBallManager().getBalls()) {
            calcPotentialEnergy(b);
            calcKineticEnergy(b);
            calcLostEnergy(b);

            totE = potE + kinE + lostE;
            b.setTotE(totE);
        }
    }

    private static void calcPotentialEnergy(Ball b) {
        // potE = m * g * h
        potE = b.getMass() * Utils.CONSTANT_OF_GRAVITATION * (MainWindowModel.get().getADrawingPane().getHeight() - b.getPosVec().y);
        b.setPotE(potE);
    }

    private static void calcKineticEnergy(Ball b) {
        // kinE = 1/2 * m * v^2
        kinE = (b.getMass() * Math.pow(MyVector.length(b.getVelVec()), 2))/2;
        b.setKinE(kinE);
    }

    private static void calcLostEnergy(Ball b) {
        // lossE = totE - kinE - potE
        lostE = totE - potE - kinE;
        b.setLostE(lostE);
    }

    //_GETTERS_SETTERS__________________________________________________________________________________________________
    public static void setDroppedPerpendicular(MyVector droppedPerpendicular) {
        WallCalculations.droppedPerpendicular = droppedPerpendicular;
    }
    public static MyVector getDroppedPerpendicular() {
        return WallCalculations.droppedPerpendicular;
    }
}
