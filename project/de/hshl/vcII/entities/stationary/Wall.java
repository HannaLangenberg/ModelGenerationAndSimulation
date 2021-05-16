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

    public Wall(){
        texture = new Image(getClass().getResourceAsStream("/img/blocks/BlockNormal.png"));
        collision = new Rectangle(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        viewTexture = new ImageView(texture);
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
    }
    public double getSpin() {
        return spin;
    }
    public void setSpin(double spin) {
        this.spin = spin;
    }
}
