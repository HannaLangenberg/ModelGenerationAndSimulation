package project.de.hshl.vcII.entities.stationary;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class WallManager{
    private Wall w;
    private ObservableList<Wall> walls = FXCollections.observableArrayList();

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
    public ObservableList<Wall> getWalls() {
        return walls;
    }

}
