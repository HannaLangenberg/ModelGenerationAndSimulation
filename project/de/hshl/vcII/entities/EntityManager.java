package project.de.hshl.vcII.entities;

import project.de.hshl.vcII.entities.stationary.Block;
import project.de.hshl.vcII.entities.moving.Kugel;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * EntityManager class for saving all entities on screen.
 * uses two lists; One for Objects with the type Kugel, on for Objects with the type Block
 */
public class EntityManager {
    private List<Kugel> kugeln = new ArrayList<>();
    private List<Block> blocks = new ArrayList<>();

    private Entity e;

    public void addKugel(Kugel kugel){
        kugeln.add(kugel);
    }

    public void  addBlock(Block block){
        blocks.add(block);
    }



    /*
     * Getters and setters
     */

    public List<Block> getBlocks() {
        return blocks;
    }

    public List<Kugel> getKugeln() {
        return kugeln;
    }

    public Entity getE(){
        return e;
    }

    public void setE(Entity e){
        this.e = e;
    }

    public List<Rectangle> getBlockCollisionBounds() {
        List<Rectangle> rectList = new ArrayList<>();

        for(Block b : blocks){
            rectList.add(b.getCollision());
        }

        return rectList;
    }
}
