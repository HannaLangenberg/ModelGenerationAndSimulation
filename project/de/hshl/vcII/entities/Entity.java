package project.de.hshl.vcII.entities;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Entity is an abstract class.
 * It is a Framework for all entities.
 * it houses the 3 public static variables
 * DEFAULT_WIDTH, DEFAULT_HEIGHT, and DEFAULT_RADIUS
 */

public abstract class Entity {
    public static final double DEFAULT_WIDTH = 300, DEFAULT_HEIGHT = 100;
    public static final double DEFAULT_RADIUS = 25;
    public static final double DEFAULT_SPIN = 0;
    protected Image texture;
    protected ImageView viewTexture;
    protected double posX, posY;
    protected double spin;

    public Entity(double spin){
        this.spin = spin;
    }

    public abstract ImageView getTexture();

    public void setPosX(double posX, double width) {
        this.viewTexture.fitWidthProperty().set(width);
        this.posX = posX - viewTexture.fitWidthProperty().get()/2;
        this.viewTexture.setX(this.posX);
    }

    public void setPosY(double posY, double height) {
        this.viewTexture.fitHeightProperty().set(height);
        this.posY = posY - viewTexture.fitHeightProperty().get()/2;
        this.viewTexture.setY(this.posY);
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public double getSpin() {
        return spin;
    }

    public void setSpin(double spin) {
        this.spin = spin;
    }
}
