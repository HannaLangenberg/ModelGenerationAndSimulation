package project.de.hshl.vcII.entities.stationary;

import project.de.hshl.vcII.entities.Entity;
import javafx.scene.shape.Rectangle;

/**
 * Block is an abstract class.
 * It is a Framework for all non-moving (wall-/floor-like) entities that should be a Block.
 */

public abstract class Block extends Entity {
    protected Rectangle collision;

    public Block(double spin) {
        super(spin);
    }

    // Getters and setters
    public Rectangle getCollision(){
        return collision;
    }

    public void setBoundingX(double boundingX) {
        collision.setX(boundingX - DEFAULT_WIDTH/2);
    }

    public void setBoundingY(double boundingY) {
        collision.setY(boundingY - DEFAULT_HEIGHT/2);
    }
}
