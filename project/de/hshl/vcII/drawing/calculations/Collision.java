package project.de.hshl.vcII.drawing.calculations;

import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.entities.stationary.Wall;
import project.de.hshl.vcII.mvc.MainWindowModel;
import project.de.hshl.vcII.utils.MyVector;

public class Collision {
    public static void checkScreen(Ball b, double e)
    {
        if(b.getPosVec().y > MainWindowModel.get().getADrawingPane().getHeight() - b.getRadius() - e || b.getPosVec().y < b.getRadius() + e) {
            b.setVel0Vec(new MyVector(b.getVel0Vec().x, -b.getVel0Vec().y));
            b.setVelVec(new MyVector(b.getVelVec().x, -b.getVelVec().y));
        }

        if(b.getPosVec().x > MainWindowModel.get().getADrawingPane().getWidth() - b.getRadius() - e || b.getPosVec().x < b.getRadius() + e) {
            b.setVel0Vec(new MyVector(-b.getVel0Vec().x, b.getVel0Vec().y));
            b.setVelVec(new MyVector(-b.getVelVec().x, b.getVelVec().y));
        }
    }


    // Used in BallCollisions and ScissorsCollisions
    static MyVector centerShock(MyVector v1, double m1, MyVector v2, double m2){
        //-------Calculate-values---------------------------------------------------------------------------------------

        MyVector step1 = MyVector.add(MyVector.multiply(v1, m1), MyVector.multiply(v2, m2));
        MyVector step2 = MyVector.divide(step1, m1 + m2);
        MyVector step3 = MyVector.multiply(step2, 2);

        return MyVector.subtract(v1, step3);
    }
}

