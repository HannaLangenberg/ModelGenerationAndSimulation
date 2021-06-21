package project.de.hshl.vcII.drawing.calculations;

import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.mvc.MainModel;
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

    /**
     * Calculates the total Energy at the start using the formula: totE0 = potE + kinE.
     */
    public static void calcInitial_TotalEnergy(Ball b) {
        calcPotentialEnergy(b);
        calcKineticEnergy(b);
        initial_TotE = totE = potE + kinE;
        b.setTotE(Math.round(initial_TotE* 100) / 100.0);
        b.setTotE_c(initial_TotE);
        b.setInitialTotE_c(initial_TotE);
        calcLostEnergy(b);
    }

    /**
     * Calculates the total energy using the formula: totE = potE + kinE + lostE.
     */
    public static void calcTotalEnergy() {
        for (Ball b : MainModel.get().getBallManager().getBalls()) {
            calcPotentialEnergy(b);
            calcKineticEnergy(b);
            calcLostEnergy(b);

            totE = potE + kinE + lostE;
            b.setTotE(Math.round(totE* 100) / 100.0);
            b.setTotE_c(totE);
        }
    }

    /**
     * Calculates the potential energy for the Ball b using the formula: potE = m * g * h.
     * @param b specifies for which ball the energy should be calculated for
     */
    private static void calcPotentialEnergy(Ball b) {
        // potE = m * g * h
        potE = b.getMass() * Utils.CONSTANT_OF_GRAVITATION * (MainModel.get().getADrawingPane().getHeight() - b.getPosVec().y);
        b.setPotE(Math.round(potE* 100) / 100.0);
        b.setPotE_c(potE);
    }

    /**
     * Calculates the kinetic energy for the Ball b using the formula: potE = m/2 * v^2.
     * @param b specifies for which ball the energy should be calculated for
     */
    private static void calcKineticEnergy(Ball b) {
        kinE = (b.getMass() * Math.pow(MyVector.length(b.getVelVec()), 2))/2;
        b.setKinE(Math.round(kinE* 100) / 100.0);
        b.setKinE_c(kinE);
    }

    /**
     * Calculates the lost energy for the Ball b using the formula: lostE = totE - kinE - potE.
     * @param b specifies for which ball the energy should be calculated for
     */
    private static void calcLostEnergy(Ball b) {
        lostE = b.getInitialTotE_c() - potE - kinE;
        b.setLostE(Math.round(lostE * 100) / 100.0);
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
