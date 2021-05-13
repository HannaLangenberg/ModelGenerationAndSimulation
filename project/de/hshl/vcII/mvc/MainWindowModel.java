package project.de.hshl.vcII.mvc;


import project.de.hshl.vcII.KeyManager;
import project.de.hshl.vcII.drawing.visuals.Grid;
import project.de.hshl.vcII.drawing.visuals.Mode;
import project.de.hshl.vcII.entities.BallManager;
import project.de.hshl.vcII.entities.EntityManager;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import project.de.hshl.vcII.drawing.Placer;
import project.de.hshl.vcII.drawing.Rotation;
import project.de.hshl.vcII.drawing.Simulator;

/**
 * MainWindowModel is a class used as a model for a mvc-pattern-styled class structure, it is also a representative of a singleton-class.
 * It has many variables and they are accessible via the getters.
 */
public class MainWindowModel {
    // Singleton
    private static MainWindowModel mainWindowModel;

    // TMP
    public static boolean WINDON = true;

    // Objects programed by the Authors
    private Rotation spin;
    private KeyManager keyManager;
    private EntityManager entityManager;
    private BallManager ballManager;
    private Mode mode;
    private Placer placer;
    private Grid grid;
    private Simulator simulator;
    // Objects used in JFX
    private Stage stage;
    private AnchorPane aDrawingPane;
    private AnchorPane aSettingsPane;

    // Variables to maintain the window
    private boolean fullscreen = false, choiceEnabled = false, choiceMade = false;
    private int[] minWindowSize = new int[] {900, 400};
    private double screenWidth, screenHeight;
    private double savedSceneX, savedSceneY, savedSceneWidth, savedSceneHeight;
    private final int taskbarHeight = 36;
    // Variables to maintain the simulation
    private boolean intersects = false;
    private boolean sphereAndBlockintersect = false;
    private boolean blocksIntersect = false;
    private boolean spheresIntersect = false;

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
        entityManager = new EntityManager();
        ballManager = new BallManager();
        keyManager = new KeyManager();
        simulator = new Simulator();
        placer = new Placer();
        grid = new Grid();
        mode = new Mode();
        spin = new Rotation();
        this.aDrawingPane = aDrawingPane;
        placer.init();
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

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public BallManager getBallManager() {
        return ballManager;
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

    public boolean isIntersects() {
        return intersects;
    }

    public void setIntersects(boolean intersects) {
        this.intersects = intersects;
    }

    public boolean isSphereAndBlockintersect() {
        return sphereAndBlockintersect;
    }

    public void setSphereAndBlockintersect(boolean sphereAndBlockintersect) {
        this.sphereAndBlockintersect = sphereAndBlockintersect;
    }

    public boolean isBlocksIntersect() {
        return blocksIntersect;
    }

    public void setBlocksIntersect(boolean blocksIntersect) {
        this.blocksIntersect = blocksIntersect;
    }

    public boolean isSpheresIntersect() {
        return spheresIntersect;
    }

    public void setSpheresIntersect(boolean spheresIntersect) {
        this.spheresIntersect = spheresIntersect;
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

