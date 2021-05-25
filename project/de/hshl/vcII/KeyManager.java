package project.de.hshl.vcII;

import javafx.scene.shape.StrokeType;
import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.entities.stationary.Wall;
import project.de.hshl.vcII.mvc.MainWindowModel;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import project.de.hshl.vcII.utils.MyVector;

/**
 * Manages Key inputs.
 * If 'W' key is pressed it also handles mouse input
 */
public class KeyManager {
    private MainWindowModel mainWindowModel;

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
        mainWindowModel.setCurrentlySelected(null);
        Rectangle clickingHitBox = new Rectangle((int) e.getX(), (int) e.getY(),1,1);
        for(Wall w : mainWindowModel.getWallManager().getWalls()){
            if(clickingHitBox.intersects(w.getPosVec().x, w.getPosVec().y, Wall.DEFAULT_WIDTH, Wall.DEFAULT_HEIGHT)){
                w.getCollision().setStrokeType(StrokeType.OUTSIDE);
                w.getCollision().setStrokeWidth(2);
                w.getCollision().setStroke(new Color(0, 0.8, 0, 1));
                w.getCollision().setFill(Color.TRANSPARENT);
                mainWindowModel.setChoiceMade(true);
                mainWindowModel.setCurrentlySelected(w);
                mainWindowModel.getWallManager().setW(w);
                if(!mainWindowModel.getADrawingPane().getChildren().contains(w.getCollision())) {
                    mainWindowModel.getADrawingPane().getChildren().add(w.getCollision());
                    return;
                }
            }
        }
        MyVector clickHitBox = new MyVector(e.getX(), e.getY());
        for(Ball b : mainWindowModel.getBallManager().getBalls()){
            if(MyVector.distance(clickHitBox, b.getPosVec()) < b.getRadius()){
                b.setStrokeType(StrokeType.OUTSIDE);
                b.setStrokeWidth(2);
                b.setStroke(new Color(0, 0.8, 0, 1));
                mainWindowModel.setChoiceMade(true);
                mainWindowModel.setCurrentlySelected(b);
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
                // Remove stroke
                if(mainWindowModel.isChoiceMade()) unMark();
                break;
            case E:
                // The chosen block is rotated left
                if(mainWindowModel.isChoiceMade() & mainWindowModel.getCurrentlySelected() instanceof Wall) MainWindowModel.get().getSpin().rotateRight((Wall) mainWindowModel.getCurrentlySelected());
                break;
            case Q:
                // The chosen block is rotated right
                if(mainWindowModel.isChoiceMade() & mainWindowModel.getCurrentlySelected() instanceof Wall) MainWindowModel.get().getSpin().rotateLeft((Wall) mainWindowModel.getCurrentlySelected());
                break;
            case DELETE:
                // The chosen block is deleted
                if(mainWindowModel.isChoiceMade()) deleteBlockOrWall();
            case SPACE:
                mainWindowModel.getSimulator().run();

        }
    }

    // Helper
    private void unMark(){
        if(mainWindowModel.getCurrentlySelected() instanceof Wall)
            ((Wall) mainWindowModel.getCurrentlySelected()).getCollision().setStrokeWidth(0);
        else if(mainWindowModel.getCurrentlySelected() instanceof Ball){
            ((Ball) mainWindowModel.getCurrentlySelected()).setStroke(((Ball) mainWindowModel.getCurrentlySelected()).getStrokeColor());
            ((Ball) mainWindowModel.getCurrentlySelected()).setStrokeWidth(5);
            ((Ball) mainWindowModel.getCurrentlySelected()).setStrokeType(StrokeType.CENTERED);
        }
        mainWindowModel.setChoiceMade(false);
    }

    private void deleteBlockOrWall() {
        if (mainWindowModel.getCurrentlySelected() instanceof Wall){
            mainWindowModel.getADrawingPane().getChildren().remove(((Wall) mainWindowModel.getCurrentlySelected()).getCollision());
            mainWindowModel.getADrawingPane().getChildren().remove(((Wall) mainWindowModel.getCurrentlySelected()).getTexture());
            mainWindowModel.getWallManager().getWalls().remove(((Wall) mainWindowModel.getCurrentlySelected()));
        }else if(mainWindowModel.getCurrentlySelected() instanceof Ball){
            mainWindowModel.getADrawingPane().getChildren().remove((Ball) mainWindowModel.getCurrentlySelected());
            mainWindowModel.getBallManager().getBalls().remove((Ball) mainWindowModel.getCurrentlySelected());
        }
            mainWindowModel.setChoiceMade(false);
    } 
}