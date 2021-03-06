package project.de.hshl.vcII.drawing.calculations;

import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.mvc.MainModel;
import project.de.hshl.vcII.utils.MyVector;
import project.de.hshl.vcII.utils.Utils;

public class Collisions {
    static int side;
    static MyVector dp;

    /**
     * Check if the Ball b will go outside the border of the window, if the ball gets outside the Window, it is deflected.
     * If the ball has a too small velocity in y direction, it doesn't bounce anymore, it just rolls using friction.
     * If the velocity of the Ball surpasses a certain value (Math.abs(b.getVelVec().x) < 2.5) the Ball stops rolling.
     * @param b Ball, the specified Ball-Object
     * @param e double, safety distance for checking for collisions.
     */
    public static void checkScreen(Ball b, double e)
    {
        /*
        * Ist colliding_Bounce true muss der Energieverlust auf die parallele Komponente angewendet werden
        * Ist colliding_Friction true muss die (Roll-) Reibung auf die orthogonale Komponente angewendet werden
        * */
        if(b.getPosVec().y > MainModel.get().getADrawingPane().getHeight() - b.getRadius() - e
                || b.getPosVec().y < b.getRadius() + e
                || b.getPosVec().x > MainModel.get().getADrawingPane().getWidth() - b.getRadius() - e
                || b.getPosVec().x < b.getRadius() + e)
        {
            b.setColliding_Parallel_B(true);
        }

        if(b.isColliding_Parallel_B()) {
            // Gegen "Decke" → einfach nur abstoßen
            // Gegen Boden → Bouncen irgendwann verbieten
            // Beides mit Energieverlust

            if ((b.getPosVec().x > MainModel.get().getADrawingPane().getWidth() - b.getRadius() - e & b.getVelVec().x > 0)
                    || (b.getPosVec().x < b.getRadius() + e & b.getVelVec().x < 0)) {
                b.setVelVec(new MyVector(-b.getVelVec().x * b.getElasticity(), b.getVelVec().y)); // Energieverlust
            }

            if ((b.getPosVec().y < b.getRadius() + e) & (b.getVelVec().y < 0)) {
                b.setVelVec(new MyVector(b.getVelVec().x, -b.getVelVec().y * b.getElasticity())); // Energieverlust
            }

            if ((b.getPosVec().y > MainModel.get().getADrawingPane().getHeight() - b.getRadius() - e) & (b.getVelVec().y > 0)) {
                b.setVelVec(new MyVector(b.getVelVec().x, -b.getVelVec().y * b.getElasticity())); // Energieverlust
                if (Math.abs(b.getVelVec().y) < Utils.CONSTANT_OF_GRAVITATION / 3) {
                    b.setVelVec(new MyVector(b.getVelVec().x, 0));
                    b.setColliding_Orthogonal_F(true);
                }
            }
            if (b.getVelVec().y == 0 & b.isColliding_Parallel_B()) {
                b.setAccVec(MyVector.add(b.getAccVec(), new MyVector(0, -Utils.CONSTANT_OF_GRAVITATION)));
            }

            b.setColliding_Parallel_B(false);

            if (b.isColliding_Orthogonal_F()) {
                // Horizontale Ebene → F_G = F_N
                // F_G = Utils.CONSTANT_OF_GRAVITATION
                // f_R_R = R_R_K * F_N
                // a_R = gespiegelte x-Komponente von bVelVec
                // a_R_R = a_R * f_R_R

                double f_R_R = b.getRolVec().x * Utils.CONSTANT_OF_GRAVITATION;
                if (Math.abs(b.getVelVec().x) < 2.5) {
                    b.setVelVec(new MyVector(0, b.getVelVec().y));
                    b.setColliding_Orthogonal_F(false);
                } else {
                    b.setAccVec(MyVector.add(b.getAccVec(), MyVector.multiply(new MyVector(-b.getVelVec().x, 0), f_R_R)));
                }
            }
        }
    }

    public static void reset() {
        dp = new MyVector(0,0);
        side = 4;
    }

    /**
     * This method is used in both BallCollisions and ScissorsCollisions .
     */
    static MyVector centerShock(MyVector v1, double m1, MyVector v2, double m2){
        //-------Calculate-values---------------------------------------------------------------------------------------

        MyVector step1 = MyVector.add(MyVector.multiply(v1, m1), MyVector.multiply(v2, m2));
        MyVector step2 = MyVector.divide(step1, m1 + m2);
        MyVector step3 = MyVector.multiply(step2, 2);

        return MyVector.subtract(v1, step3);
    }
}

