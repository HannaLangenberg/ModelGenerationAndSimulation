package project.de.hshl.vcII.drawing.calculations;

import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.mvc.MainModel;
import project.de.hshl.vcII.utils.MyVector;
import project.de.hshl.vcII.utils.Utils;

public class Collision {
    static int side;
    static MyVector dp;

    public static void checkScreen(Ball b, double e)
    {
        /*
        * Ist colliding_Bounce true muss der Energieverlust auf die parallele Komponente angewendet werden
        * Ist colliding_Friction true muss die (Roll-) Reibung auf die orthogonale Komponente angewendet werden
        * */

        if(b.getPosVec().y > MainModel.get().getADrawingPane().getHeight() - b.getRadius() - e
                || b.getPosVec().y < b.getRadius() + e
                || b.getPosVec().x > MainModel.get().getADrawingPane().getWidth() - b.getRadius() - e
                || b.getPosVec().x < b.getRadius() + e) {
            b.setColliding_Parallel_B(true);
        }


        if(b.isColliding_Parallel_B()) {
            // Gegen "Decke" → einfach nur abstoßen
            // Gegen Boden → Bouncen irgendwann verbieten
            // Beides mit Energieverlust


            if((b.getPosVec().x > MainModel.get().getADrawingPane().getWidth() - b.getRadius() - e & b.getVelVec().x > 0)) {
                dp = new MyVector(MainModel.get().getADrawingPane().getWidth(), b.getPosVec().y);
                side = 3;
            }
            if((b.getPosVec().x < b.getRadius() + e & b.getVelVec().x < 0)) {
                dp = new MyVector(0, b.getPosVec().y);
                side = 1;
            }


            if((b.getPosVec().y < b.getRadius() + e) & (b.getVelVec().y < 0)) {
                dp = new MyVector(b.getPosVec().x, 0);
                side = 2;
            }

            if((b.getPosVec().y > MainModel.get().getADrawingPane().getHeight() - b.getRadius() - e) & (b.getVelVec().y > 0)) {
                dp = new MyVector(b.getPosVec().x, MainModel.get().getADrawingPane().getHeight());
                side = 0;

            }
            CollisionHandling.bounceVelocity(b, side, dp);

            if(side == 0 & !b.isColliding_Orthogonal_F()) {
                CollisionHandling.stopBouncing(b);
            }
        }

        if(b.isColliding_Orthogonal_F()) {
            // Horizontale Ebene → F_G = F_N
            // F_G = Utils.CONSTANT_OF_GRAVITATION
            // f_R_R = R_R_K * F_N
            // a_R = gespiegelte x-Komponente von bVelVec
            // a_R_R = a_R * f_R_R

            double f_R_R = b.getRolVec().x * Utils.CONSTANT_OF_GRAVITATION;
            if(Math.abs(b.getVelVec().x) < 2.5)
            {
                b.setVelVec(new MyVector(0, b.getVelVec().y));
                b.setColliding_Orthogonal_F(false);
            }
            b.setAccVec(MyVector.add(b.getAccVec(), MyVector.multiply(new MyVector(-b.getVelVec().x, 0), f_R_R)));
        }
    }

    public static void reset() {
        dp = new MyVector(0,0);
        side = 0;
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

