package project.de.hshl.vcII.entities.stationary;

import project.de.hshl.vcII.entities.Entity;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class BlockNormal extends Block {

    public BlockNormal(double spin){
        super(spin);
        texture = new Image(getClass().getResourceAsStream("/img/blocks/BlockNormal.png"));
        collision = new Rectangle(0, 0, Entity.DEFAULT_WIDTH, Entity.DEFAULT_HEIGHT);
        viewTexture = new ImageView(texture);
    }

    @Override
    public ImageView getTexture() {
        return viewTexture;
    }
}
