package project.de.hshl.vcII.mvc;


import project.de.hshl.vcII.KeyManager;
import project.de.hshl.vcII.drawing.visuals.Grid;
import project.de.hshl.vcII.drawing.visuals.Mode;
import project.de.hshl.vcII.entities.moving.BallManager;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import project.de.hshl.vcII.drawing.Placer;
import project.de.hshl.vcII.drawing.Rotation;
import project.de.hshl.vcII.drawing.Simulator;
import project.de.hshl.vcII.entities.stationary.ScissorsManager;
import project.de.hshl.vcII.entities.stationary.WallManager;

/**
 * MainWindowModel is a class used as a model for a mvc-pattern-styled class structure, it is also a representative of a singleton-class.
 * It has many variables and they are accessible via the getters.
 */
public class MainWindowModel {
    // Singleton
    private static MainWindowModel mainWindowModel;

    // Objects programed by the Authors
    private Rotation spin;
    private KeyManager keyManager;
    private WallManager wallManager;
    private BallManager ballManager;
    private ScissorsManager scissorsManager;
    private Mode mode;
    private Placer placer;
    private Grid grid;
    private Simulator simulator;
    private Object currentlySelected;
    // Objects used in JFX
    private Stage stage;
    private AnchorPane aDrawingPane;
    private AnchorPane aSettingsPane;

    // Variables to maintain the window
    private boolean fullscreen = false, choiceEnabled = false, choiceMade = false, arrowsActive = false;
    private int[] minWindowSize = new int[] {900, 400};
    private double screenWidth, screenHeight, scissorsSpeed = 1.0;
    private double savedSceneX, savedSceneY, savedSceneWidth, savedSceneHeight;
    private final int taskbarHeight = 36;

    // Private to make it a Singleton-class.
    private MainWindowModel(){
    }

    // To create or, if the class has been called before, get the class.
    public static MainWindowModel get(){
        if(MainWindowModel.mainWindowModel == null) mainWindowModel = new MainWindowModel();
        return MainWindowModel.mainWindowModel;
    }

    // To initialise all Objects
    public void init(AnchorPane aDrawingPane){
        ballManager = new BallManager();
        wallManager = new WallManager();
        scissorsManager = new ScissorsManager();
        keyManager = new KeyManager();
        simulator = new Simulator();
        placer = new Placer();
        grid = new Grid();
        mode = new Mode();
        spin = new Rotation();
        this.aDrawingPane = aDrawingPane;
    }
    public void initSettings(AnchorPane aSettingsPane){
        this.aSettingsPane = aSettingsPane;
    }

    /*
     * Getters and setters.
     */

    public KeyManager getKeyManager() {
        return keyManager;
    }

    public Placer getPlacer() {
        return placer;
    }

    public WallManager getWallManager() {
        return wallManager;
    }

    public BallManager getBallManager() {
        return ballManager;
    }

    public ScissorsManager getScissorsManager() {
        return scissorsManager;
    }

    public Mode getMode() {
        return mode;
    }

    public Grid getGrid() {
        return grid;
    }

    public Simulator getSimulator() {
        return simulator;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public AnchorPane getADrawingPane() {
        return aDrawingPane;
    }

    public AnchorPane getASettingsPane() {
        return aSettingsPane;
    }

    public boolean isChoiceEnabled() {
        return choiceEnabled;
    }

    public void setChoiceEnabled(boolean choiceEnabled) {
        this.choiceEnabled = choiceEnabled;
    }

    public boolean isChoiceMade() {
        return choiceMade;
    }

    public void setChoiceMade(boolean choiceMade) {
        this.choiceMade = choiceMade;
    }

    public Rotation getSpin() {
        return spin;
    }

    public boolean isArrowsActive() {
        return arrowsActive;
    }

    public void setArrowsActive(boolean arrowsActive) {
        this.arrowsActive = arrowsActive;
    }

    public Object getCurrentlySelected() {
        return currentlySelected;
    }

    public void setCurrentlySelected(Object currentlySelected) {
        this.currentlySelected = currentlySelected;
    }

    public void setScissorsSpeed(double scissorsSpeed) {
        this.scissorsSpeed = scissorsSpeed;
    }

    public double getScissorsSpeed() {
        return scissorsSpeed;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    public int[] getMinWindowSize() {
        return minWindowSize;
    }

    public double getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(double screenWidth) {
        this.screenWidth = screenWidth;
    }

    public double getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(double screenHeight) {
        this.screenHeight = screenHeight;
    }

    public double getSavedSceneX() {
        return savedSceneX;
    }

    public void setSavedSceneX(double savedSceneX) {
        this.savedSceneX = savedSceneX;
    }

    public double getSavedSceneY() {
        return savedSceneY;
    }

    public void setSavedSceneY(double savedSceneY) {
        this.savedSceneY = savedSceneY;
    }

    public double getSavedSceneWidth() {
        return savedSceneWidth;
    }

    public void setSavedSceneWidth(double savedSceneWidth) {
        this.savedSceneWidth = savedSceneWidth;
    }

    public double getSavedSceneHeight() {
        return savedSceneHeight;
    }

    public void setSavedSceneHeight(double savedSceneHeight) {
        this.savedSceneHeight = savedSceneHeight;
    }

    public int getTaskbarHeight() {
        return taskbarHeight;
    }
}

