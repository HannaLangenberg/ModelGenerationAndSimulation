package project.de.hshl.vcII.entities.stationary;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import project.de.hshl.vcII.utils.MyVector;

/**
 * Wall is an abstract class.
 * It is a Framework for all non-moving (wall-/floor-like) entities that should be a Wall.
 */

public class Wall {
    public static final double DEFAULT_WIDTH = 300, DEFAULT_HEIGHT = 100;

    private Image texture;
    private ImageView viewTexture;
    private Rectangle collision;
    private MyVector posVec;
    private double spin;
    private int number;

    public Wall(){
        texture = new Image(getClass().getResourceAsStream("/img/blocks/BlockNormal.png"));
        collision = new Rectangle(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        viewTexture = new ImageView(texture);
        posVec = new MyVector(0, 0);
        number = 0;
    }

    // Getters and setters
    public Rectangle getCollision(){
        return collision;
    }

    public ImageView getTexture() {
        return viewTexture;
    }

    public MyVector getPosVec() {
        return posVec;
    }
    public void setPosVec(MyVector posVec) {
        this.posVec = posVec;
        viewTexture.setX(posVec.x - collision.getWidth()/2);
        viewTexture.setY(posVec.y - collision.getHeight()/2);
        collision.setX(posVec.x - collision.getWidth()/2);
        collision.setY(posVec.y - collision.getHeight()/2);
    }

    public double getSpin() {
        return spin;
    }
    public void setSpin(double spin) {
        this.spin = spin;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
