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
       //3333 mainWindowModel.setChoiceMade(false);
        mainWindowModel.setCurrentlySelected(null);
        Rectangle clickingHitBox = new Rectangle((int) e.getX(), (int) e.getY(),1,1);
        for(Wall w : mainWindowModel.getWallManager().getWalls()){
            if(clickingHitBox.intersects(w.getPosVec().x - Wall.DEFAULT_WIDTH/2, w.getPosVec().y - Wall.DEFAULT_HEIGHT/2, Wall.DEFAULT_WIDTH, Wall.DEFAULT_HEIGHT)){
                mark(w);
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
                mark(b);
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

    public void choose(String s){
        if (!mainWindowModel.isChoiceMade()) {
            String[] strings = s.split(" ");
            if (strings[0].equals("Ball")) {
                for (Ball b : mainWindowModel.getBallManager().getBalls()) {
                    if ((b.getNumber() + "").equals(strings[2])) {
                        mainWindowModel.setCurrentlySelected(b);
                    }
                }
            } else if (strings[0].equals("Wand")) {
                for (Wall w : mainWindowModel.getWallManager().getWalls()) {
                    if ((w.getNumber() + "").equals(strings[2])) {
                        mainWindowModel.setCurrentlySelected(w);
                    }
                }
            }
            mainWindowModel.setChoiceMade(true);
        }else{
            unMark();
            mainWindowModel.setChoiceMade(false);
        }
    }

    // Helper
    private void mark(Object o){
        if(o instanceof Ball) {
            ((Ball) o).setStrokeType(StrokeType.OUTSIDE);
            ((Ball) o).setStrokeWidth(2);
            ((Ball) o).setStroke(new Color(0, 0.8, 0, 1));
        } else{
            ((Wall) o).getCollision().setStrokeType(StrokeType.OUTSIDE);
            ((Wall) o).getCollision().setStrokeWidth(2);
            ((Wall) o).getCollision().setStroke(new Color(0, 0.8, 0, 1));
            ((Wall) o).getCollision().setFill(Color.TRANSPARENT);
        }
    }

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
            mainWindowModel.setCurrentlySelected(null);
            mainWindowModel.setChoiceMade(false);
    } 
}