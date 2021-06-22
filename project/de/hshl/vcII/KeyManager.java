package project.de.hshl.vcII;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.entities.stationary.Scissors;
import project.de.hshl.vcII.entities.stationary.Wall;
import project.de.hshl.vcII.mvc.MainModel;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import project.de.hshl.vcII.utils.MyVector;

/**
 * Manages Key inputs.
 * Also handles mouse inputs.
 */
public class KeyManager {
    private MainModel mainModel;

    public KeyManager(){
        mainModel = MainModel.get();
    }


    /**
     * Toggles only if the 'W' key is pushed.
     * chooses a Ball, Wall or Scissors and highlights it green,
     * @param e to know where the cursor currently is
     */
    public void manageMouse(MouseEvent e){
        mainModel.setCurrentlySelected(null);
        Rectangle clickingHitBox = new Rectangle((int) e.getX(), (int) e.getY(),1,1);
        for(Wall w : mainModel.getWallManager().getWalls()){
            if(clickingHitBox.intersects(w.getPosVec().x - Wall.DEFAULT_WIDTH/2, w.getPosVec().y - Wall.DEFAULT_HEIGHT/2, Wall.DEFAULT_WIDTH, Wall.DEFAULT_HEIGHT)){
                mark(w);
                mainModel.setChoiceMade(true);
                mainModel.setCurrentlySelected(w);
                mainModel.getWallManager().setW(w);
                if(!mainModel.getADrawingPane().getChildren().contains(w.getCollision())) {
                    mainModel.getADrawingPane().getChildren().add(w.getCollision());
                    return;
                }
            }
        }
        if (mainModel.getScissorsManager().getS() != null) {
            Scissors s = mainModel.getScissorsManager().getS();
            if(clickingHitBox.intersects(s.getPosVec().x, s.getPosVec().y, s.getRectangle().getWidth(), s.getRectangle().getHeight())){
                mark(s);
                mainModel.setChoiceMade(true);
                mainModel.setCurrentlySelected(s);
                mainModel.getScissorsManager().setS(s);
                if(!mainModel.getADrawingPane().getChildren().contains(s.getG())) {
                    mainModel.getADrawingPane().getChildren().add(s.getG());
                    return;
                }
            }
        }

        MyVector clickHitBox = new MyVector(e.getX(), e.getY());
        for(Ball b : mainModel.getBallManager().getBalls()){
            if(MyVector.distance(clickHitBox, b.getPosVec()) < b.getRadius()){
                mark(b);
                mainModel.setChoiceMade(true);
                mainModel.setCurrentlySelected(b);
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
                mainModel.setChoiceEnabled(!mainModel.isChoiceEnabled());
                // Remove stroke
                if(mainModel.isChoiceMade()) unMark();
                break;
            case E:
                // The chosen block is rotated left
                if(mainModel.isChoiceMade() & mainModel.getCurrentlySelected() instanceof Wall) MainModel.get().getSpin().rotateRight((Wall) mainModel.getCurrentlySelected());
                else if(mainModel.isChoiceMade() & mainModel.getCurrentlySelected() instanceof Scissors) MainModel.get().getSpin().rotateRight((Scissors) mainModel.getCurrentlySelected());
                break;
            case Q:
                // The chosen block is rotated right
                if(mainModel.isChoiceMade() & mainModel.getCurrentlySelected() instanceof Wall) MainModel.get().getSpin().rotateLeft((Wall) mainModel.getCurrentlySelected());
                else if(mainModel.isChoiceMade() & mainModel.getCurrentlySelected() instanceof Scissors) MainModel.get().getSpin().rotateLeft((Scissors) mainModel.getCurrentlySelected());
                break;
            case S:
                //close scissors
                if(mainModel.getScissorsManager().getS() != null) {
                    mainModel.getScissorsManager().getS().applyRotation(2);
                    mainModel.getScissorsManager().getS().setClosing(true);
                    mainModel.getScissorsManager().getS().getRectangle().setStrokeWidth(0);
                }
                break;
            case DELETE:
                // The chosen block is deleted
                if(mainModel.isChoiceMade()) deleteBlockOrWall();
                break;
            case SPACE:
                mainModel.getSimulator().run();
                break;
        }
    }

    public void choose(String s){
        unMarkAll();
        if (!mainModel.isChoiceMade()) {
            String[] strings = s.split(" ");
            if (strings[0].equals("Ball")) {
                for (Ball b : mainModel.getBallManager().getBalls()) {
                    if ((b.getNumber() + "").equals(strings[2])) {
                        mainModel.setCurrentlySelected(b);
                        mark(b);
                        mainModel.getBallManager().setB(b);
                    }
                }
            } else if (strings[0].equals("Wand")) {
                for (Wall w : mainModel.getWallManager().getWalls()) {
                    if ((w.getNumber() + "").equals(strings[2])) {
                        mainModel.setCurrentlySelected(w);
                        mark(w);
                        mainModel.getWallManager().setW(w);
                        mainModel.setChoiceMade(true);
                        if(!mainModel.getADrawingPane().getChildren().contains(w.getCollision())) {
                            mainModel.getADrawingPane().getChildren().add(w.getCollision());
                            return;
                        }
                    }
                }
            }else {
                mainModel.getScissorsManager().getS().setMarkedStroke();
                mainModel.setCurrentlySelected(mainModel.getScissorsManager().getS());
                mainModel.setChoiceMade(true);
                if(!mainModel.getADrawingPane().getChildren().contains(mainModel.getScissorsManager().getS().getG())) {
                    mainModel.getADrawingPane().getChildren().add(mainModel.getScissorsManager().getS().getG());
                    return;
                }
            }
            mainModel.setChoiceMade(true);
        }else{
            unMark();
            mainModel.setChoiceMade(false);
        }
    }

    // Helper
    public void mark(Object o){
        if(o instanceof Ball) {
            ((Ball) o).setStrokeType(StrokeType.OUTSIDE);
            ((Ball) o).setStrokeWidth(2);
            ((Ball) o).setStroke(new Color(0, 0.8, 0, 1));
        } else if (o instanceof Wall){
            ((Wall) o).getCollision().setStrokeType(StrokeType.OUTSIDE);
            ((Wall) o).getCollision().setStrokeWidth(2);
            ((Wall) o).getCollision().setStroke(new Color(0, 0.8, 0, 1));
            ((Wall) o).getCollision().setFill(Color.TRANSPARENT);
        } else if (o instanceof Scissors){
            ((Scissors) o).setMarkedStroke();
        }
    }

    public void unMarkAll() {
        for(Ball b : mainModel.getBallManager().getBalls()) {
            b.setStroke(b.getStrokeColor());
            b.setStrokeWidth(5);
            b.setStrokeType(StrokeType.CENTERED);
        }
        for(Wall w : mainModel.getWallManager().getWalls()) {
            w.getCollision().setStrokeWidth(0);
        }
        if(mainModel.getScissorsManager().getS() != null) {
            mainModel.getScissorsManager().getS().setUnmarkedStroke();
        }
        mainModel.setChoiceMade(false);
    }

    private void unMark(){
        if(mainModel.getCurrentlySelected() instanceof Wall)
            ((Wall) mainModel.getCurrentlySelected()).getCollision().setStrokeWidth(0);
        else if(mainModel.getCurrentlySelected() instanceof Ball){
            ((Ball) mainModel.getCurrentlySelected()).setStroke(((Ball) mainModel.getCurrentlySelected()).getStrokeColor());
            ((Ball) mainModel.getCurrentlySelected()).setStrokeWidth(5);
            ((Ball) mainModel.getCurrentlySelected()).setStrokeType(StrokeType.CENTERED);
        }
        else if(mainModel.getCurrentlySelected() instanceof Scissors)
            ((Scissors) mainModel.getCurrentlySelected()).setUnmarkedStroke();
        mainModel.setChoiceMade(false);
    }

    private void deleteBlockOrWall() {
        if (mainModel.getCurrentlySelected() instanceof Wall){
            mainModel.getADrawingPane().getChildren().remove(((Wall) mainModel.getCurrentlySelected()).getCollision());
            mainModel.getADrawingPane().getChildren().remove(((Wall) mainModel.getCurrentlySelected()).getTexture());
            mainModel.getWallManager().getWalls().remove(((Wall) mainModel.getCurrentlySelected()));
        }else if(mainModel.getCurrentlySelected() instanceof Ball){
            mainModel.getADrawingPane().getChildren().remove((Ball) mainModel.getCurrentlySelected());
            mainModel.getBallManager().getBalls().remove((Ball) mainModel.getCurrentlySelected());
        }
            mainModel.setCurrentlySelected(null);
            mainModel.setChoiceMade(false);
    }

    /**
     * Just an old artifact left in because it man be needed.
     * @param b     the Ball focused
     * @param posX  cursor position (on X)
     * @param posY  cursor position (on Y)
     */
    private void hoverBox(Ball b, double posX, double posY){
        AnchorPane aRoot = new AnchorPane();
        VBox vbParams = new VBox();

        //Label pos = new Label("Position:\t(" + b.getPosVec().x + "/" + b.getPosVec().y + ")");
        Label v = new Label("Geschw.\t(" + b.getVelVec().x + "/" + b.getVelVec().y + ")");
        Label a = new Label("Beschl.\t(" + b.getAccVec().x + "/" + b.getAccVec().y + ")");
        Label radius = new Label("Radius:\t" + b.getRadius());
        Label mass = new Label("Masse:\t" + b.getMass());
        Label elasticity = new Label("Elastizität:\t" + b.getElasticity());
        Label totE = new Label("ges. Energie:\t" + b.getTotE_c());
        Label potE = new Label("pot. Energie:\t" + b.getPotE_c());
        Label kinE = new Label("kin. Energie:\t" + b.getKinE_c());
        Label lostE = new Label("Verlust:\t" + b.getLostE_c());

        vbParams.getChildren().addAll(v, a, radius, mass, elasticity, totE, potE, kinE,lostE);
        aRoot.getChildren().add(vbParams);
        aRoot.setMaxSize(150, 300);
        aRoot.setMinSize(150, 300);
        aRoot.setPrefSize(150, 300);

        Scene hoverBox = new Scene(aRoot);
        Stage secondary = new Stage();
        secondary.initStyle(StageStyle.UNDECORATED);
        secondary.setScene(hoverBox);
        secondary.show();
    }
}