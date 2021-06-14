package project.de.hshl.vcII.drawing.calculations;

import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.utils.MyVector;

public class Calculator {
    private static double f_R_max;


    public static double getLambda_velocity() {
        return ScissorsCalculations.lambda_velocity;
    }

    public static void setLambda_velocity(double lambda_velocity) {
        ScissorsCalculations.lambda_velocity = lambda_velocity;
    }

    public static double getRho_velocity() {
        return ScissorsCalculations.rho_velocity;
    }

    public static void setRho_velocity(double rho_velocity) {
        ScissorsCalculations.rho_velocity = rho_velocity;
    }


    //_GETTERS_SETTERS__________________________________________________________________________________________________
    public static void setDroppedPerpendicular(MyVector droppedPerpendicular) {
        WallCalculations.droppedPerpendicular = droppedPerpendicular;
    }
    public static MyVector getDroppedPerpendicular() {
        return WallCalculations.droppedPerpendicular;
    }

    public static void setA_H(MyVector a_H) {
        WallCalculations.a_H = a_H;
    }
    public static MyVector getA_H() {
        return WallCalculations.a_H;
    }

    public static void setA_N(MyVector a_N) {
        WallCalculations.a_N = a_N;
    }
    public static MyVector getA_N() {
        return WallCalculations.a_N;
    }

    public static void setA_R_H(MyVector a_R_H) {
        WallCalculations.a_R_H = a_R_H;
    }
    public static MyVector getA_R_H() {
        return WallCalculations.a_R_H;
    }

    public static void setA_R_G(MyVector a_R_G) {
        WallCalculations.a_R_G = a_R_G;
    }
    public static MyVector getA_R_G() {
        return WallCalculations.a_R_G;
    }

    public static void setF_H(double f_H) {
        WallCalculations.f_H = f_H;
    }
    public static double getF_H() {
        return WallCalculations.f_H;
    }

    public static void setF_N(double f_N) {
        WallCalculations.f_N = f_N;
    }
    public static double getF_N() {
        return WallCalculations.f_N;
    }

    public static void setF_R_H(double f_R_H) {
        WallCalculations.f_R_H = f_R_H;
    }
    public static double getF_R_H() {
        return WallCalculations.f_R_H;
    }

    public static void setF_R_G(double f_R_G) {
        WallCalculations.f_R_G = f_R_G;
    }
    public static double getF_R_G() {
        return WallCalculations.f_R_G;
    }

    public static void setF_R_max(double f_R_max) {
        Calculator.f_R_max = f_R_max;
    }
    public static double getF_R_max() {
        return f_R_max;
    }

    public static void setAngle_max_H(double angle_max_H) {
        WallCalculations.angle_max_H = angle_max_H;
    }
    public static double getAngle_max_H() {
        return WallCalculations.angle_max_H;
    }
}
