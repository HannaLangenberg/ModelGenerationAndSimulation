package project.de.hshl.vcII.drawing.calculations;

import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.mvc.MainWindowModel;
import project.de.hshl.vcII.utils.MyVector;
import project.de.hshl.vcII.utils.Utils;

/**
 * This class is responsible for general calculations.
 *
 * The declaration who wrote which part can be found in the method comments.
 * */
public class Calculator {
    private static double initial_TotE, totE, potE, kinE, lostE = 0;

    /**
     * This method is used in the bounce... (Wall/Scissors) / and ...shock (Ball) methods.
     * It returnes the parallel component of the transferred ball's velocity vector.
     * To do so orthogonal projection is used.
     * @param normedCenterLine normed line → shock normal
     * @param b                current ball
     * @return                 parallel component of current ball's velVec
     * @author Hanna Langenberg
     */
    static MyVector calc_P_component(MyVector normedCenterLine, Ball b) {
        return MyVector.orthogonalProjection(b.getVelVec(), normedCenterLine);
    }
    /**
     * This method is used in the bounce... (Wall/Scissors) / and ...shock (Ball) methods.
     * It returnes the orthogonal component of the transferred ball's velocity vector.
     * To do so orthogonal projection is used and subtracted from the velVec.
     * @param normedCenterLine normed line → shock normal
     * @param b                current ball
     * @return                 orthogonal component of current ball's velVec
     * @author Hanna Langenberg
     */
    static MyVector calc_O_component(MyVector normedCenterLine, Ball b) {
        return  MyVector.subtract(MyVector.orthogonalProjection(b.getVelVec(), normedCenterLine), b.getVelVec());
    }

    /**
     * This method is used for wall and scissors collisions to check if a collision happend with/at the calculated
     * dropped perpendicular.
     * @param b          current ball
     * @param dp_Coord   dropped perpendicular of the ball
     * @param epsilon    safety distance for checking for collisions. [5px]
     * @return           true if collision happened
     * @author Jan-Gustav Liedtke
     */
    public static boolean checkDistance(Ball b, MyVector dp_Coord, double epsilon) {
        return MyVector.distance(b.getPosVec(), dp_Coord) <= b.getRadius() + epsilon;
    }


    public static void calcInitial_TotalEnergy() {
        // initial_TotE = potE + kinE
        for (Ball b : MainWindowModel.get().getBallManager().getBalls()) {
            calcPotentialEnergy(b);
            calcKineticEnergy(b);
            initial_TotE = totE = potE + kinE;
            calcLostEnergy(b);
            b.setTotE(Math.round(initial_TotE));
            b.setTotE_c(initial_TotE);
            b.setInitialTotE_c(initial_TotE);
        }
    }

    public static void calcTotalEnergy() {
        // totE = potE + kinE + lossE
        for (Ball b : MainWindowModel.get().getBallManager().getBalls()) {
            calcPotentialEnergy(b);
            calcKineticEnergy(b);
            calcLostEnergy(b);

            totE = potE + kinE + lostE;
            b.setTotE(Math.round(totE));
            b.setTotE_c(totE);
        }
    }

    private static void calcPotentialEnergy(Ball b) {
        // potE = m * g * h
        potE = b.getMass() * Utils.CONSTANT_OF_GRAVITATION * (MainWindowModel.get().getADrawingPane().getHeight() - b.getPosVec().y);
        b.setPotE(Math.round(potE));
        b.setPotE_c(potE);
    }

    private static void calcKineticEnergy(Ball b) {
        // kinE = 1/2 * m * v^2
        kinE = (b.getMass() * Math.pow(MyVector.length(b.getVelVec()), 2))/2;
        b.setKinE(Math.round(kinE));
        b.setKinE_c(kinE);
    }

    private static void calcLostEnergy(Ball b) {
        // lossE = totE - kinE - potE
        lostE = b.getInitialTotE_c() - potE - kinE;
        b.setLostE(Math.round(lostE));
        b.setLostE_c(lostE);
    }

    //_GETTERS_SETTERS__________________________________________________________________________________________________
    public static void setDroppedPerpendicular(MyVector droppedPerpendicular) {
        WallCalculations.droppedPerpendicular = droppedPerpendicular;
    }
    public static MyVector getDroppedPerpendicular() {
        return WallCalculations.droppedPerpendicular;
    }
}
