package project.de.hshl.vcII.drawing.calculations;

import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.mvc.MainWindowModel;
import project.de.hshl.vcII.utils.MyVector;

import java.util.List;

public class Collision {
    public static void collide(Ball b){
        List<Ball> balls = MainWindowModel.get().getBallManager().getBalls();
        int ballIndex = balls.indexOf(b);
        for(int i = ballIndex + 1; i < balls.size(); i++){
            Ball b2 = balls.get(i);
            ballOnBall(b, b2);
        }
    }

    private static void ballOnBall(Ball b, Ball b2) {
        // finds the radii of k1, and k2
        double r_b = b.getRadius(),
                r_b2 = b2.getRadius();

        // finds the center-points of k1, and k2
        MyVector pos_b = b.getPosVec(),
                  pos_b2 = b2.getPosVec();

        if(Math.sqrt(Math.pow(pos_b.y-pos_b.x,2)+Math.pow(pos_b2.y-pos_b2.x,2)) < r_b+r_b2)
            return;

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

