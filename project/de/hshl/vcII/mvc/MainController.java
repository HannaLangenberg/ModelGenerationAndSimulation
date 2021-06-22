package project.de.hshl.vcII.mvc;

import javafx.beans.InvalidationListener;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.shape.Polygon;
import javafx.stage.FileChooser;
import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.entities.stationary.Scissors;
import project.de.hshl.vcII.entities.stationary.Wall;

import project.de.hshl.vcII.mvc.view.ResizePane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import project.de.hshl.vcII.utils.IO;
import project.de.hshl.vcII.utils.MyVector;
import project.de.hshl.vcII.utils.Utils;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * MainController is used an a controller for the mvc-pattern.
 */

public class MainController implements Initializable {
    @FXML
    private CheckBox cb_choice_active;
    @FXML
    private Slider sl_Elasticity;
    @FXML
    private Label lCurrentElasticity;
    @FXML
    private CheckBox chb_Wind;
    @FXML
    private Slider sl_Weight, sl_Radius, sl_ScissorsSpeed;
    @FXML
    private TextField tf_Wind_Y, tf_Wind_X, tf_v0_Y, tf_v0_X;
    @FXML
    private Label lMode, lGridSnapActive, lCurrentWeight, lCurrentRadius, lWind, lGravity, lDt;
    @FXML
    private Button btn_start_stop;
    @FXML
    private Polygon d_play;
    @FXML
    private MenuItem miSnap;
    @FXML
    private HBox hHeader, hb_pause;
    @FXML
    private VBox vb_displayCurrentParams;
    @FXML
    private GridPane gp_Wind;
    @FXML
    private ComboBox<String> cb_choose;
    @FXML
    private StackPane sMinPane, sMinMaxPane, sExitPane;
    @FXML
    private AnchorPane aRootPane, aDrawingPane, aSettingsPane;
    @FXML
    public TableView<Ball> tv_ball_params;
    @FXML
    public TableColumn<Ball, Integer> tc_No;
    @FXML
    public TableColumn<Ball, String> tc_Pos;
    @FXML
    public TableColumn<Ball, String> tc_V;
    @FXML
    public TableColumn<Ball, Integer> tc_Radius;
    @FXML
    public TableColumn<Ball, Integer> tc_Mass;
    @FXML
    public TableColumn<Ball, String> tc_PotE;
    @FXML
    public TableColumn<Ball, String> tc_KinE;
    @FXML
    public TableColumn<Ball, String> tc_LostE;
    @FXML
    public TableColumn<Ball, String> tc_TotE;
    @FXML
    public TableColumn<Ball, String> tc_Elasticity;
    // A second controller for better overview
    private CurrentParamsController currentParamsController = new CurrentParamsController();
    private SettingsController settingsController = new SettingsController();
    private CustomHeaderController customHeaderController = new CustomHeaderController();
    // Declaration of original model.
    private MainModel mainModel;

    // Variables to maintain the screen.
    private double windowCursorPosX, windowCursorPosY;
    private double sceneOnWindowPosX, sceneOnWindowPosY;
    private boolean mousePressedInHeader = false;
    private boolean lightMode = true;
    private boolean firstTime = true;

    // Used for resizing.
    private ResizePane resizePane;

    /**
     * Used to initialize everything
     * @param url used for background processes in the big blackbox that is JavaFx but unused by us
     * @param resourceBundle used for background processes in the big blackbox that is JavaFx but unused by us
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_start_stop.setDisable(true);
        aSettingsPane.setDisable(true);

        // Get the original MainModel
        mainModel = MainModel.get();

        mainModel.setMainController(this);

        currentParamsController.initialize(lWind, lDt, lGravity, tv_ball_params, tc_Radius, tc_Mass, tc_V, tc_PotE, tc_KinE,
                tc_LostE, tc_TotE, tc_Elasticity, tc_Pos, tc_No);
        mainModel.setCurrentParamsController(currentParamsController);
        settingsController.initialize(sl_ScissorsSpeed, sl_Radius, lCurrentRadius, sl_Weight, lCurrentWeight, sl_Elasticity,
                lCurrentElasticity, tf_Wind_X, tf_Wind_Y, tf_v0_X, tf_v0_Y, vb_displayCurrentParams);
        mainModel.setSettingsController(settingsController);
        customHeaderController.initialize(aSettingsPane, resizePane, sMinPane, sMinMaxPane, sExitPane, hHeader, sceneOnWindowPosX, sceneOnWindowPosY,
                mousePressedInHeader, windowCursorPosX, windowCursorPosY);

        aDrawingPane.getStyleClass().add("anchor-drawing-pane");

        // Setting up the screen dimensions.
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        mainModel.setScreenWidth(dimension.width);
        mainModel.setScreenHeight(dimension.height);

        // Setting up the pane used for resizing.
        resizePane = new ResizePane();
        aRootPane.getChildren().add(resizePane);

        // Inits when stage is Shown (booted up)
        mainModel.getStage().setOnShown(e -> {
            mainModel.init(aDrawingPane);
            mainModel.initSettings(aSettingsPane);
            resizePane.initResizeLines();
            resizePane.alignResizeLines();
            customHeaderController.draggablePrimaryStage();
            mainModel.getMode().toggleMode(lightMode);
        });
    }

    //_MENU_____________________________________________________________________________________________________________

    // Start the simulation
    // Is called whenever the 'Start/Stop' button is clicked.
    @FXML
    private void run(){
        //settingsController.fillVariables();
        if (firstTime) {
            //settingsController.setV0();
            tf_v0_X.setDisable(true);
            tf_v0_Y.setDisable(true);
            firstTime = false;
        }
        if(!mainModel.isArrowsActive()) mainModel.getBallManager().removeArrows();

        mainModel.getSimulator().run();

        if (mainModel.getSimulator().isRunning()) {
            d_play.setVisible(true);
            hb_pause.setVisible(false);
        } else {
            d_play.setVisible(false);
            hb_pause.setVisible(true);
            settingsController.updateParams();
        }
    }

    //TODO: Was schlaues dazu schreiben.
    @FXML
    private void d_play_changeSimSpd(){

    }

    // Is called whenever 'clear screen' is clicked in the 'File' menu.
    @FXML
    private void clearScreen() {
        mainModel.getBallManager().getBalls().removeAll(mainModel.getBallManager().getBalls());
        mainModel.getWallManager().getWalls().removeAll(mainModel.getWallManager().getWalls());
        mainModel.getScissorsManager().setS(null);
        mainModel.setCurrentlySelected(null);
        aDrawingPane.getChildren().clear();
        hb_pause.setVisible(false);
        d_play.setVisible(false);
        btn_start_stop.setDisable(true);
        aSettingsPane.setDisable(true);
        tf_v0_X.setDisable(false);
        tf_v0_Y.setDisable(false);
        mainModel.getSettingsController().getChangedBalls().clear();
        mainModel.getCurrentParamsController().reset();
        firstTime = true;
    }
    // Is called whenever 'save' is clicked in the 'File' menu.
    @FXML
    private void save(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("source/res/saves"));;
        fileChooser.setTitle("Speichere die aktuelle Szene:");
        File save = fileChooser.showSaveDialog(aRootPane.getScene().getWindow());
        IO.save(save);
    }
    // Is called whenever 'load' is clicked in the 'File' menu.
    @FXML
    private void load(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("source/res/saves"));
        fileChooser.setTitle("Wähle eine zuvor gespeicherte Datei aus: ");
        File file = fileChooser.showOpenDialog(aRootPane.getScene().getWindow());
        IO.load(file);
        settingsController.updateParams();
        aSettingsPane.setDisable(false);
        
        btn_start_stop.setDisable(false);
    }

    // Is called whenever 'Ball' is clicked in the 'Edit' menu.
    @FXML
    private void choiceBall() {
        Ball b = new Ball(0, new MyVector(0,0), new MyVector(0,0), new MyVector(0,0), new MyVector(0,0), new MyVector(0,0), 25, 2.75,0.5,0,0,0,0);
        mainModel.setCurrentlySelected(b);
        mainModel.getBallManager().setB(b);
        mainModel.getWallManager().setW(null);
        activateLists();
    }
    // Is called whenever 'Wall' is clicked in the 'Edit' menu.
    @FXML
    private void choiceWall(){
        Wall w = new Wall();
        mainModel.setCurrentlySelected(w);
        mainModel.getWallManager().setW(w);
        mainModel.getBallManager().setB(null);
        activateLists();
    }
    // Is called whenever 'Elastic band' is clicked in the 'Edit' menu.
    @FXML
    private void choiceSchere(){
        Scissors s = new Scissors();
        mainModel.setCurrentlySelected(s);
        mainModel.getBallManager().setB(null);
        mainModel.getWallManager().setW(null);
    }

    // Is called whenever 'Toggle Grid' is clicked in the 'Grid' menu
    @FXML
    private void toggleGrid() {
        mainModel.getGrid().toggleGrid(aDrawingPane);
    }
    // Is called whenever 'Snap To Grid' is clicked in the 'Grid' menu
    @FXML
    private void snapToGrid() {
        mainModel.getGrid().toggleSnapToGrid();
        if(mainModel.getGrid().isSnapOn()){
            lGridSnapActive.setVisible(true);
            miSnap.setStyle("-fx-border-color: green;" +
                    "-fx-border-width: 0 0 2 0;");
        }
        else {
            lGridSnapActive.setVisible(false);
            miSnap.setStyle("-fx-border-width: 0 0 0 0;");
        }
    }

    // Is called when ever 'Light' / 'Dark' is clicked
    @FXML
    private void switch_mode() {
        mainModel.getMode().toggleMode(!lightMode);
        lightMode = !lightMode;
        if (!lightMode) {
            lMode.setText("Dark");
        }
        else {
            lMode.setText("Light");
        }
        System.out.println(lightMode);
    }
    //_PARAMETER_SETTING________________________________________________________________________________________________
    //_choicebox__
    @FXML
    private void chb_Wind_OnAction(ActionEvent actionEvent) {
        gp_Wind.setDisable(!chb_Wind.isSelected());

        if(!chb_Wind.isSelected())
        {
            Utils.setWind(new MyVector(0,0));
            settingsController.updateParams();
        }
    }

    @FXML
    private void chb_Choice_OnAction(ActionEvent actionEvent) {
        cb_choose.setDisable(!cb_choice_active.isSelected());
        if (cb_choose.isDisabled()) {
            mainModel.getKeyManager().unMarkAll();
        }
        else {
            cb_choose.setPromptText("wähle...");
            cb_choose.getSelectionModel().selectedItemProperty().addListener((oV, oldValue, newValue) -> cb_choose(newValue));
        }
    }

    //_slider__
    @FXML
    private void sl_Weight_OnDragDetected(MouseEvent mouseEvent) {
        settingsController.sl_Weight_OnDragDetected();
    }
    @FXML
    private void sl_Radius_OnDragDetected(){
        settingsController.sl_Radius_OnDragDetected();
    }
    @FXML
    private void sl_Elasticity_OnDragDetected(MouseEvent mouseEvent) {
        settingsController.sl_Elasticity_OnDragDetected();
    }
    @FXML
    private void sl_ScissorsSpeed_OnDragDetected(){
        settingsController.sl_ScissorsSpeed_OnDragDetected();
    }


    //_MOUSE_EVENTS_____________________________________________________________________________________________________
    // Is called whenever the mouse is clicked.
    @FXML
    private void onMouse(MouseEvent e){
        switch(e.getButton()) {
            // Which button is pressed?
            case PRIMARY:
                // Left MouseButton.
                if(mainModel.isChoiceEnabled())
                    // Was 'W' previously pressed
                    mainModel.getKeyManager().manageMouse(e);
                else {
                    mainModel.getPlacer().place(e);
                    if(mainModel.getCurrentlySelected() instanceof Ball) {
                        btn_start_stop.setDisable(false);
                        aSettingsPane.setDisable(false);
                    }
                }


                break;
            case SECONDARY:
                if(mainModel.isChoiceEnabled())
                    // Was 'W' previously pressed
                    mainModel.getKeyManager().manageMouse(e);
                break;
        }
    }

    @FXML
    private void onMousePressed(MouseEvent e){
        if(mainModel.getScissorsManager().getS() == null)
            mainModel.getPlacer().onMousePressed(e);
    }

    @FXML
    private void onMouseDragged(MouseEvent e) {
        if(mainModel.getScissorsManager().getS() == null)
            mainModel.getPlacer().onMouseDragged(e);
    }

    @FXML
    private void onMouseReleased(MouseEvent e) {
        if(mainModel.getScissorsManager().getS() == null) {
            mainModel.getPlacer().onMouseReleased(e);
            activateLists();
        }
    }

    // For CustomHeaderController
    @FXML
    private void minimize() {
        customHeaderController.minimize();
    }

    @FXML
    private void mouseEnteredMinimize(MouseEvent mouseEvent) {
        customHeaderController.mouseEnteredMinimize();
    }

    @FXML
    private void mouseExitedMinimize(MouseEvent mouseEvent) {
        customHeaderController.mouseExitedMinimize();
    }


    @FXML
    private void minMax(MouseEvent mouseEvent) {
        customHeaderController.minMax();
    }

    @FXML
    private void mouseEnteredMinMax(MouseEvent mouseEvent) {
        customHeaderController.mouseEnteredMinMax();
    }

    @FXML
    private void mouseExitedMinMax(MouseEvent mouseEvent) {
        customHeaderController.mouseExitedMinMax();
    }


    @FXML
    private void exit(MouseEvent mouseEvent) {
        customHeaderController.exit();
    }

    @FXML
    private void mouseEnteredExit(MouseEvent mouseEvent) {
        customHeaderController.mouseEnteredExit();
    }

    @FXML
    private void mouseExitedExit(MouseEvent mouseEvent) {
        customHeaderController.mouseExitedExit();
    }

    //_KEY_EVENTS_______________________________________________________________________________________________________
    @FXML
    private void onKey(KeyEvent e) {
        mainModel.getKeyManager().manageInputs(e.getCode());
    }

    @FXML
    private void arrows() {
        mainModel.setArrowsActive(!mainModel.isArrowsActive());
    }

    //_MISC_____________________________________________________________________________________________________________
    public void activateLists(){
        // Add change listener for cb_choose to always keep it updated
        mainModel.getBallManager().getBalls().addListener((InvalidationListener) observable -> {
            cb_update();
        });
        mainModel.getWallManager().getWalls().addListener((InvalidationListener) observable -> {
            cb_update();
        });
        if(mainModel.getScissorsManager().getS() != null)
            cb_update();

    }

    public void cb_update() {
        cb_choose.getItems().clear();
        for (Ball b : mainModel.getBallManager().getBalls())
            cb_choose.getItems().add("Ball Nummer " + b.getNumber());

        for (Wall w : mainModel.getWallManager().getWalls())
            cb_choose.getItems().add("Wand Nummer " + w.getNumber());

        if(mainModel.getScissorsManager().getS() != null)
            cb_choose.getItems().add("Schere");
    }

    public void cb_choose(String s) {
        if(cb_choose.getValue() == null) return;
        mainModel.getKeyManager().choose(s);
    }

    @FXML
    public void selectBall_onAction(ActionEvent actionEvent) {
        Ball b = (Ball) tv_ball_params.getSelectionModel().getSelectedItem();
        mainModel.getKeyManager().unMarkAll();
        mainModel.setCurrentlySelected(b);
        mainModel.getKeyManager().mark(b);
        mainModel.getBallManager().setB(b);
        mainModel.setChoiceMade(true);
    }

    public boolean isFirstTime() {
        return firstTime;
    }

    public void setFirstTime(boolean firstTime) {
        this.firstTime = firstTime;
    }
}
