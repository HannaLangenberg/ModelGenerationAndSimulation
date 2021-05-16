package project.de.hshl.vcII.entities.stationary;

import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class WallManager{
    private Wall w;
    private List<Wall> walls = new ArrayList<>();

    //----Getter-&-Setter-----------------------------------------------------------------------------------------------
    public void setW(Wall w) {
        this.w = w;
    }
    public Wall getW() {
        return w;
    }

    public void addWall(Wall w){
        walls.add(w);
    }
    public List<Wall> getWalls() {
        return walls;
    }

    public List<Rectangle> getWallCollisionBounds(){
        List<Rectangle> wallBounds = new ArrayList<>();
        for(Wall w : walls){
            wallBounds.add(w.getCollision());
        }
        return wallBounds;
    }
}
