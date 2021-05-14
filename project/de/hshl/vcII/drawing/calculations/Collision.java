package project.de.hshl.vcII.drawing.calculations;

import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.mvc.MainWindowModel;
import project.de.hshl.vcII.utils.MyVector;

import java.util.ArrayList;
import java.util.List;

public class Collision {
    public static void collide(Ball b){
        //-Collision-test-----------------------------------------------------------------------------------------------
        // Make a deep copy of the balls list
        List<Ball> balls = new ArrayList<>(MainWindowModel.get().getBallManager().getBalls());
        // Don't copy the Ball given as a parameter
        balls.remove(b);

        // Iterate though every ball except the one given in the parameters, and check their collision.
        for (Ball b2 : balls)
            ballOnBall(b, b2);
    }

    private static void ballOnBall(Ball b1, Ball b2) {
        //-Do-two-balls-collide-----------------------------------------------------------------------------------------

        // finds the radii of b1, and b2
        double r_b1 = b1.getRadius(),
                r_b2 = b2.getRadius();

        // finds the center-points of b1, and b2
        MyVector pos_b1 = b1.getPosVec(),
                  pos_b2 = b2.getPosVec();

        if(MyVector.distance(pos_b1, pos_b2) > r_b1+r_b2)
            return;

        //-If-they-collide----------------------------------------------------------------------------------------------
        angledShock(b1, b2);
    }

    private static void centerShock(Ball b1, Ball b2){
        //-------Calculate-values---------------------------------------------------------------------------------------

        // The first three steps are the same for both balls
        MyVector step1 = MyVector.add(MyVector.multiply(b1.getVelVec(), b1.getMass()), MyVector.multiply(b2.getVelVec(), b2.getMass()));
        MyVector step2 = MyVector.divide(step1, b1.getMass() + b2.getMass());
        MyVector step3 = MyVector.multiply(step2, 2);

        MyVector step4_b1 = MyVector.subtract(step3, b1.getVelVec());
        MyVector step4_b2 = MyVector.subtract(step3, b2.getVelVec());

        //-------Set-values---------------------------------------------------------------------------------------------

        b1.setVelVec(step4_b1);
        b2.setVelVec(step4_b2);
    }

    private static void angledShock(Ball b1, Ball b2){
        //-------Calculate-values---------------------------------------------------------------------------------------

        // Norm the line between the two centers of b1 and b2.
        MyVector normedCenterLine = MyVector.norm(MyVector.subtract(b2.getPosVec(), b1.getPosVec()));

        // Find the orthogonal velocity vector of b1 and the parallel velocity vector of b2
        MyVector v1Orthogonal = MyVector.subtract(b1.getVelVec(), MyVector.multiply(normedCenterLine, MyVector.dot(normedCenterLine, b1.getVelVec())));
        MyVector v2Parallel = MyVector.multiply(normedCenterLine, MyVector.dot(b2.getVelVec(), normedCenterLine));

        // Find the orthogonal velocity vector of b2 and the parallel velocity vector of b1
        MyVector v2Orthogonal = MyVector.subtract(b2.getVelVec(), MyVector.multiply(normedCenterLine, MyVector.dot(normedCenterLine, b2.getVelVec())));
        MyVector v1Parallel = MyVector.multiply(normedCenterLine, MyVector.dot(b1.getVelVec(), normedCenterLine));

        //-------Set-values---------------------------------------------------------------------------------------------

        b1.setVelVec(MyVector.add(v1Orthogonal, v2Parallel));
        b2.setVelVec(MyVector.add(v2Orthogonal, v1Parallel));
    }
/*
    public static void collide(Kugel k){
        if(insideWorld(k)){
            List<Kugel> kugelList = MainWindowModel.get().getEntityManager().getKugeln();
            for (int j = kugelList.indexOf(k) + 1; j < kugelList.size(); j++) {
                Kugel k1 = MainWindowModel.get().getEntityManager().getKugeln().get(j);
                MyVector intersectionLine = sphereOnSphere(k, k1);
                if (intersectionLine != null) {
                    System.out.println("SOS: INTERSECTION");
                    handleCollisions(intersectionLine); //TODO Collision handling
                }
            }
            List<Block> blockList = MainWindowModel.get().getEntityManager().getBlocks();
            for(Block b: blockList) {
                int blockIndex = blockList.indexOf(b);
                MyVector intersectionLine = sphereOnBlock(k, b);
                if (intersectionLine != null) {
                    System.out.println("SOB: INTERSECTION");
                    handleCollisions(intersectionLine); //TODO Collision handling
                }
                for(int i = blockIndex + 1 ; i < blockList.size(); i++){
                    Block b2 = MainWindowModel.get().getEntityManager().getBlocks().get(i);
                    intersectionLine = blockOnBlock(b, b2);
                    if (intersectionLine != null) {
                        System.out.println("BOB: INTERSECTION");
                        handleCollisions(intersectionLine); //TODO Collision handling
                    }
                }
            }
        }
    }

    private static boolean insideWorld(Kugel k) {
        return k.getCollision().intersects(MainWindowModel.get().getADrawingPane().getBoundsInParent());
    }

    private static MyVector sphereOnSphere(Kugel k1, Kugel k2){
        // finds the radii of k1, and k2
        double r_k1 = k1.getCollision().getRadius(),
                r_k2 = k2.getCollision().getRadius();

        // finds the center-points of k1, and k2
        MyVector pos_k1 = new MyVector(k1.getPosX(), k1.getPosY()),
                pos_k2 = new MyVector(k2.getPosX(), k2.getPosY());

        if(Math.sqrt(Math.pow(pos_k1.y-pos_k1.x,2)+Math.pow(pos_k2.y-pos_k2.x,2)) < r_k1+r_k2)
//            if(!k1.getCollision().intersects(k2.getCollision().getBoundsInParent())) // TODO leichter?
            return null;

        // return the line orthogonal to the colliding spheres connected centers

        MyVector spheresCenterToCenter = MyVector.subtract(pos_k2, pos_k1);

        return null;
    }

    private static MyVector blockOnBlock(Block b1, Block b2){
        if(!b1.getCollision().getBoundsInParent().intersects(b2.getCollision().getBoundsInParent()))
            return null;

        return null;
    }

    private static MyVector sphereOnBlock(Kugel k, Block b) {
        Rectangle rec = b.getCollision();
        if (!k.getCollision().intersects(
                rec.getX() - Entity.DEFAULT_RADIUS,
                rec.getY() - Entity.DEFAULT_RADIUS,
                rec.getWidth() + Entity.DEFAULT_RADIUS,
                rec.getHeight() + 2 * Entity.DEFAULT_RADIUS)){
//            Math.sqrt(Math.pow(pos_k1.y-pos_k1.x,2)+Math.pow(pos_k2.y-pos_k2.x,2)
            return null;
        }

        return null;


        if(k.getCollision().getBoundsInParent().intersects(b.getCollision().getBoundsInParent())){
            if(k.getV().x < b.getPosX()){
                if(k.getV().y < b.getPosY()){
                    if(MyVector.distance(new MyVector(k.getPosX(), k.getPosY()), new MyVector(b.getPosX(), b.getPosY())) < Entity.DEFAULT_RADIUS){
                        // TODO PosX und PosY setzen
                    }
                } else if(k.getV().y > b.getPosY() + Entity.DEFAULT_HEIGHT){
                    // TODO Abstand sphere oben rechts <= Entity.DEFAULT_RADIUS
                }
            } else if(k.getV().x > b.getPosX() + Entity.DEFAULT_WIDTH){
                if(k.getV().y < b.getPosY()){
                    // TODO Abstand sphere unten links <= Entity.DEFAULT_RADIUS
                } else if(k.getV().y > b.getPosY() + Entity.DEFAULT_HEIGHT){
                    // TODO Abstand sphere unten rechts <= Entity.DEFAULT_RADIUS
                }
            }
            return true;
        }
        return false;
    }

    private static void handleCollisions(MyVector intersectionLine) {

    }
 */
}

