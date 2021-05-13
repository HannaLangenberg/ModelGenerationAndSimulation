package project.de.hshl.vcII.drawing.calculations;

import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.mvc.MainWindowModel;
import project.de.hshl.vcII.utils.MyVector;
import project.de.hshl.vcII.utils.Utils;

public class Movement {
    private static double spdX = 2;
    private static double spdY = 0;

    /*// Debugging Methode 'ungestörte' Bewegung auf Achsen
    public static void doSmth(Ball b) {
        // That shit's working. Don't you dare touching it! Admins only! Aka Moi!!
        b.setCenterX(b.getCenterX() + spdX * Utils.getSim_Spd());
        if(b.getCenterX() > MainWindowModel.get().getADrawingPane().getWidth() - b.getRadius() || b.getCenterX() < b.getRadius()) {
            spdX *= -1;
        }

        b.setCenterY(b.getCenterY() + spdY);
        if(b.getCenterY() > MainWindowModel.get().getADrawingPane().getHeight() - b.getRadius() || b.getCenterY() < b.getRadius()) {
            spdY *= -1;
        }
    }*/


    public static void calcAcceleration(Ball b) {
        // add every single ACCELERATION to get accVec
        b.setAccVec(MyVector.add(Utils.getWind(), Utils.GRAVITY));
    }
    public static void calcVelocity(Ball b) {
        // VELOCITY dependent on acceleration: v = v_0 + a * t
        b.setVelVec(MyVector.add(b.getVelVec(), MyVector.multiply(b.getAccVec(), Utils.DELTA_T)));

//        b.setVelVec(MyVector.multiply(b.getVelVec(), Utils.getSim_Spd())); // TODO pro Iteration 1/60 multipliziert, daher durche decke
    }
    public static void calcPosition(Ball b) {
        // take acceleration into consideration:
        // s = s_0 + v_0 * dt
        //                   + 0.5 * a * dt^2
        b.setPosVec(MyVector.add(b.getPosVec(), MyVector.multiply(b.getVelVec(), Utils.DELTA_T)));
        b.setPosVec(MyVector.add(b.getPosVec(), MyVector.multiply(MyVector.multiply(b.getAccVec(), Math.pow(Utils.DELTA_T, 2)), 0.5)));

    }
    public static void checkPosition(Ball b) {
        if(b.getPosVec().x > MainWindowModel.get().getADrawingPane().getWidth() - b.getRadius() || b.getPosVec().x < b.getRadius()) {
            b.setVelVec(new MyVector(-b.getVelVec().x, b.getVelVec().y));
        }

        if(b.getPosVec().y > MainWindowModel.get().getADrawingPane().getHeight() - b.getRadius() || b.getPosVec().y < b.getRadius()) {
            b.setVelVec(new MyVector(b.getVelVec().x, -b.getVelVec().y));
        }
    }
}