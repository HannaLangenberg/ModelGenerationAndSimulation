package project.de.hshl.vcII;

import project.de.hshl.vcII.entities.stationary.Wall;
import project.de.hshl.vcII.mvc.MainWindowController;
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
    private MainWindowController mainWindowController;
    private Wall w;

    public KeyManager(){
        mainWindowModel = MainWindowModel.get();
    }

    /**
     * Toggles only if the 'G' key is pushed.
     * chooses a Wall and highlights it green,
     * @param e to know where the cursor currently is
     */
    public void manageMouse(MouseEvent e){
        mainWindowModel.setChoiceMade(false);
        this.w = null;
        for(Wall w : mainWindowModel.getWallManager().getWalls()){
            System.out.println(1);
            if(new Rectangle((int) e.getX(), (int) e.getY(),1,1).intersects(w.getPosVec().x, w.getPosVec().y, Wall.DEFAULT_WIDTH, Wall.DEFAULT_HEIGHT)){
                w.getCollision().setStyle("-fx-stroke-type: outside; " +
                        "-fx-stroke: #008000;" +
                        "-fx-stroke-width: 2;");
                w.getCollision().setFill(Color.TRANSPARENT);
                mainWindowModel.setChoiceMade(true);
                this.w = w;
                if(mainWindowModel.getADrawingPane().getChildren().contains(w.getCollision()))
                    return;
                mainWindowModel.getADrawingPane().getChildren().add(w.getCollision());

            }
        }
    }

    /**
     * Manages all key inputs (currently handled: G, E, Q and DEL)
     * @param keyCode to know which key is pressed
     */
    public void manageInputs(KeyCode keyCode){
        switch (keyCode){
            case G:
                // The chosen block can be picked
                mainWindowModel.setChoiceEnabled(!mainWindowModel.isChoiceEnabled());
                mainWindowModel.setChoiceMade(false);
                // Only if a Wall is chosen:
                mainWindowModel.getADrawingPane().getChildren().removeAll(mainWindowModel.getWallManager().getWallCollisionBounds());
                break;
            case E:
                // The chosen block is rotated left
                if(mainWindowModel.isChoiceMade()) MainWindowModel.get().getSpin().rotateRight(w);
                break;
            case Q:
                // The chosen block is rotated right
                if(mainWindowModel.isChoiceMade()) MainWindowModel.get().getSpin().rotateLeft(w);
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
        mainWindowModel.getADrawingPane().getChildren().remove(w.getCollision());
        mainWindowModel.getADrawingPane().getChildren().remove(w.getTexture());
        mainWindowModel.getWallManager().getWalls().remove(w);
        mainWindowModel.setChoiceMade(false);
    }
}