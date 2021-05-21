package project.de.hshl.vcII;

import project.de.hshl.vcII.entities.stationary.Wall;
import project.de.hshl.vcII.mvc.MainWindowModel;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Manages Key inputs.
 * If 'G' key is pressed it also handles mouse input
 */
public class KeyManager {
    private MainWindowModel mainWindowModel;
    private Wall wall;

    public KeyManager(){
        mainWindowModel = MainWindowModel.get();
    }

    /**
     * Toggles only if the 'W' key is pushed.
     * chooses a Wall and highlights it green,
     * @param e to know where the cursor currently is
     */
    public void manageMouse(MouseEvent e){
        mainWindowModel.setChoiceMade(false);
        this.wall = null;
        for(Wall w : mainWindowModel.getWallManager().getWalls()){
            if(new Rectangle((int) e.getX(), (int) e.getY(),1,1).intersects(w.getPosVec().x, w.getPosVec().y, Wall.DEFAULT_WIDTH, Wall.DEFAULT_HEIGHT)){
                w.getCollision().setStyle("-fx-stroke-type: outside; " +
                        "-fx-stroke: #008000;" +
                        "-fx-stroke-width: 2;");
                w.getCollision().setFill(Color.TRANSPARENT);
                mainWindowModel.setChoiceMade(true);
                this.wall = w;
                mainWindowModel.getWallManager().setW(w);
                if(mainWindowModel.getADrawingPane().getChildren().contains(w.getCollision()))
                    continue;
                mainWindowModel.getADrawingPane().getChildren().add(w.getCollision());
            }
        }
    }

    /**
     * Manages all key inputs (currently handled: W, E, Q and DEL)
     * @param keyCode to know which key is pressed
     */
    public void manageInputs(KeyCode keyCode){
        switch (keyCode){
            case W:
                // The block can be picked
                mainWindowModel.setChoiceEnabled(!mainWindowModel.isChoiceEnabled());
                mainWindowModel.setChoiceMade(false);
                // Remove all borders
                //mainWindowModel.getADrawingPane().getChildren().removeAll(mainWindowModel.getWallManager().getWallCollisionBounds());
                break;
            case E:
                // The chosen block is rotated left
                if(mainWindowModel.isChoiceMade()) MainWindowModel.get().getSpin().rotateRight(wall);
                break;
            case Q:
                // The chosen block is rotated right
                if(mainWindowModel.isChoiceMade()) MainWindowModel.get().getSpin().rotateLeft(wall);
                break;
            case DELETE:
                // The chosen block is deleted
                if(mainWindowModel.isChoiceMade()) deleteBlock();
            case SPACE:
                mainWindowModel.getSimulator().run();

        }
    }

    // Helper
    private void deleteBlock() {
        mainWindowModel.getADrawingPane().getChildren().remove(wall.getCollision());
        mainWindowModel.getADrawingPane().getChildren().remove(wall.getTexture());
        mainWindowModel.getWallManager().getWalls().remove(wall);
        mainWindowModel.setChoiceMade(false);
    } 
}