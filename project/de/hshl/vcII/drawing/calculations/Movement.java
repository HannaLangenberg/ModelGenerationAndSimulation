package project.de.hshl.vcII.drawing.calculations;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import project.de.hshl.vcII.drawing.visuals.Arrow;
import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.entities.stationary.Wall;
import project.de.hshl.vcII.mvc.MainWindowModel;
import project.de.hshl.vcII.utils.MyVector;
import project.de.hshl.vcII.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Movement {
    private static double spdX = 2;
    private static double spdY = 0;
    private static int rebound = -1;

    private static List<Arrow> arrows = new ArrayList<>();
    private static List<Ball> balls = MainWindowModel.get().getBallManager().getBalls();

    public static void applyForce(Ball b) {
        double f_N;
        MyVector a_R;
        MyVector a_R_H;
        double a = 0;
        double x,y;

        f_N = Utils.CONSTANT_OF_GRAVITATION * b.getMass() * Math.cos(Math.toRadians(a));

        x =  Math.sin(Math.toRadians(a)) * f_N;
        y =  Math.cos(Math.toRadians(a)) * f_N;

        a_R = new MyVector(x,y);

        a_R_H = MyVector.multiply(a_R, 0.9);

        b.setAccVec(MyVector.add(b.getAccVec(), MyVector.divide(a_R_H, b.getMass())));
    }

    public static void applyFriction() {

    }

    public static void applyForce(Wall w, Ball b, double decision) {

    }
    public static void calcFriction(Ball b) {
        // Get all walls.
        List<Wall> walls = MainWindowModel.get().getWallManager().getWalls();

        // Iterate through the balls and walls and check for collisions
        for (Wall w : walls)
            Calculator.initializeForces(w, b, w.getOrientation());
    }

    public static void calcAcceleration(Ball b) {
        // add every single ACCELERATION to get accVec
        b.setAccVec(MyVector.add(Utils.getWind(), Utils.GRAVITY));
    }

    public static void calcVelocity(Ball b) {
        // v = v_0 + a * t

        b.setVel0Vec(b.getVelVec());
        b.setVelVec(MyVector.add(b.getVelVec(), MyVector.multiply(b.getAccVec(), Utils.DELTA_T)));

    }
    public static void checkPosition(Ball b, double e) {
        if(b.getPosVec().x > MainWindowModel.get().getADrawingPane().getWidth() - b.getRadius() - e || b.getPosVec().x < b.getRadius() + e) {
            b.setVel0Vec(new MyVector(-b.getVel0Vec().x, b.getVel0Vec().y));
            b.setVelVec(new MyVector(-b.getVelVec().x, b.getVelVec().y));
//            applyForce(b);
        }

        if(b.getPosVec().y > MainWindowModel.get().getADrawingPane().getHeight() - b.getRadius() - e || b.getPosVec().y < b.getRadius() + e) {
            b.setVel0Vec(new MyVector(b.getVel0Vec().x, -b.getVel0Vec().y));
            b.setVelVec(new MyVector(b.getVelVec().x, -b.getVelVec().y));
//            applyForce(b);
        }
    }
    public static void checkCollisions(Ball b, double epsilon) {
        // Check if ball hits screen bounds
        Collision.checkScreen(b, epsilon);

        // Check if ball hits wall
        List<Wall> walls = MainWindowModel.get().getWallManager().getWalls();

        // Iterate through the balls and walls and check for collisions
        for (Wall w : walls)
            Collision.checkWalls(w,b,epsilon);
    }

    public static void calcPosition(Ball b) {
        // s = s_0 + v_0 * dt
        //                   + 0.5 * a * dt^2
//        b.setPosVec(MyVector.add(b.getPosVec(), MyVector.multiply(b.getVelVec(), Utils.DELTA_T)));
        b.setPosVec(MyVector.add(b.getPosVec(), MyVector.multiply(b.getVel0Vec(), Utils.DELTA_T)));
        b.setPosVec(MyVector.add(b.getPosVec(), MyVector.multiply(MyVector.multiply(b.getAccVec(), Math.pow(Utils.DELTA_T, 2)), 0.5)));

        b.setAccVec(MyVector.multiply(b.getAccVec(),0));

    }



    // Debugging Methode 'ungestörte' Bewegung auf Achsen
    public static void doSmth(Ball b, double epsilon) {
        // That shit's working. Don't you dare touching it! Admins only! Aka Moi!!
//        b.setCenterX(b.getCenterX() + spdX * Utils.getSim_Spd());
//        b.setVel0Vec(b.getVelVec());
        b.setVelVec(MyVector.add(b.getVelVec(), b.getAccVec()));

        /*if(b.getCenterX() > MainWindowModel.get().getADrawingPane().getWidth() - b.getRadius() - epsilon || b.getCenterX() < b.getRadius() + epsilon) {
//            spdX *= -1;
            b.setVelVec(new MyVector(b.getVelVec().x * rebound, b.getVelVec().y));
        }

//        b.setCenterY(b.getCenterY() + spdY);
        if(b.getCenterY() > MainWindowModel.get().getADrawingPane().getHeight() - b.getRadius() - epsilon|| b.getCenterY() < b.getRadius() + epsilon) {
//            spdY *= -1;
            b.setVelVec(new MyVector(b.getVelVec().x, b.getVelVec().y * rebound));
        }*/

    }

    public static void drawArrows() {
        Arrow arrow;
        for (Ball b : balls) {
            arrow = new Arrow();
            arrow.toFront();
            MyVector direction = MyVector.add(b.getPosVec(), b.getVelVec());
            Stop[] stops = new Stop[]{new Stop(0, javafx.scene.paint.Color.RED), new Stop(1, Color.BLUE)};
            arrow.getLine().setStroke(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops));
            arrow.setStartX(b.getPosVec().x);
            arrow.setStartY(b.getPosVec().y);
            arrow.setEndX(direction.x);
            arrow.setEndY(direction.y);
            arrow.toFront();
            arrows.add(arrow);
        }
        MainWindowModel.get().getADrawingPane().getChildren().addAll(arrows);

    }

    public static void removeArrows() {
        MainWindowModel.get().getADrawingPane().getChildren().removeAll(arrows);
        arrows.clear();
    }

}