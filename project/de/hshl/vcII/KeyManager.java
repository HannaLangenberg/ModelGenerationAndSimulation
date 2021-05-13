package project.de.hshl.vcII;

import project.de.hshl.vcII.entities.stationary.Block;
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
    private Block b;

    public KeyManager(){
        mainWindowModel = MainWindowModel.get();
    }

    /**
     * Toggles only if the 'G' key is pushed.
     * chooses a Block and highlights it green,
     * @param e to know where the cursor currently is
     */
    public void manageMouse(MouseEvent e){
        mainWindowModel.setChoiceMade(false);
        this.b = null;
        for(Block b : mainWindowModel.getEntityManager().getBlocks()){
            System.out.println(1);
            if(new Rectangle((int) e.getX(), (int) e.getY(),1,1).intersects(b.getPosX(), b.getPosY(), Block.DEFAULT_WIDTH, Block.DEFAULT_HEIGHT)){
                b.getCollision().setStyle("-fx-stroke-type: outside; " +
                        "-fx-stroke: #00AA00;" +
                        "-fx-stroke-width: 2;");
                b.getCollision().setFill(Color.TRANSPARENT);
                mainWindowModel.setChoiceMade(true);
                this.b = b;
                if(mainWindowModel.getADrawingPane().getChildren().contains(b.getCollision()))
                    return;
                mainWindowModel.getADrawingPane().getChildren().add(b.getCollision());

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
                // Only if one Block is chosen:
                mainWindowModel.getADrawingPane().getChildren().removeAll(mainWindowModel.getEntityManager().getBlockCollisionBounds());
                break;
            case E:
                // The chosen block is rotated left
                if(mainWindowModel.isChoiceMade()) MainWindowModel.get().getSpin().rotateRight(b);
                break;
            case Q:
                // The chosen block is rotated right
                if(mainWindowModel.isChoiceMade()) MainWindowModel.get().getSpin().rotateLeft(b);
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
        mainWindowModel.getADrawingPane().getChildren().remove(b.getCollision());
        mainWindowModel.getADrawingPane().getChildren().remove(b.getTexture());
        mainWindowModel.getEntityManager().getBlocks().remove(b);
        mainWindowModel.setChoiceMade(false);
    }
}