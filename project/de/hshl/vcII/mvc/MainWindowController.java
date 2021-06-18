package project.de.hshl.vcII.mvc;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.shape.Polygon;
import project.de.hshl.vcII.drawing.calculations.Calculator;
import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.entities.stationary.Scissors;
import project.de.hshl.vcII.entities.stationary.Wall;

import project.de.hshl.vcII.mvc.view.ResizePane;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import project.de.hshl.vcII.utils.MyVector;
import project.de.hshl.vcII.utils.Utils;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * MainWindowController is used an a controller for the mvc-pattern.
 */

public class MainWindowController implements Initializable {
    @FXML
    public CheckBox cb_choice_active;
    @FXML
    public Slider sl_Elasticity;
    @FXML
    public Label lCurrentElasticity;
    @FXML
    private CheckBox chb_Wind;
    @FXML
    private Slider sl_Weight, sl_Radius, sl_ScissorsSpeed;
    @FXML
    private TextField tf_Wind_Y, tf_Wind_X, tf_v0_Y, tf_v0_X;
    @FXML
    private Label lMode, lGridSnapActive, lCurrentWeight, lCurrentRadius;
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
    // A second controller for better overview
    private SettingsController settingsController = new SettingsController();
    // Declaration of original model.
    private MainWindowModel mainWindowModel;

    // Variables to maintain the screen.
    private double windowCursorPosX, windowCursorPosY;
    private double sceneOnWindowPosX, sceneOnWindowPosY;
    private boolean mousePressedInHeader = false;
    private boolean lightMode = true;
    private boolean firstTime = true;

    // Used for resizing.
    private ResizePane resizePane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_start_stop.setDisable(true);
        aSettingsPane.setDisable(true);
        settingsController.initialize(sl_ScissorsSpeed, sl_Radius, lCurrentRadius, sl_Weight, lCurrentWeight, sl_Elasticity, lCurrentElasticity, tf_Wind_X, tf_Wind_Y,
        tf_v0_X, tf_v0_Y, vb_displayCurrentParams);

        // Get the original MainWindowModel
        mainWindowModel = MainWindowModel.get();

        aDrawingPane.getStyleClass().add("anchor-drawing-pane");

        // Setting up the screen dimensions.
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        mainWindowModel.setScreenWidth(dimension.width);
        mainWindowModel.setScreenHeight(dimension.height);

        // Setting up the pane used for resizing.
        resizePane = new ResizePane();
        aRootPane.getChildren().add(resizePane);

        // Inits when stage is Shown (booted up)
        mainWindowModel.getStage().setOnShown(e -> {
            mainWindowModel.init(aDrawingPane);
            mainWindowModel.initSettings(aSettingsPane);
            resizePane.initResizeLines();
            resizePane.alignResizeLines();
            draggablePrimaryStage();
            mainWindowModel.getMode().toggleMode(lightMode);
        });
    }




    // Start the simulation
    // Is called whenever the 'Start/Stop' button is clicked.
    @FXML
    private void run(){
        // check all TextFields for values
        settingsController.fillVariables();
        if (firstTime) {
            settingsController.setV0();
            Calculator.calcInitial_TotalEnergy();
            firstTime = false;
        }
        if(!mainWindowModel.isArrowsActive()) mainWindowModel.getBallManager().removeArrows();

        mainWindowModel.getSimulator().run();

        if (mainWindowModel.getSimulator().isRunning()) {
            d_play.setVisible(true);
            hb_pause.setVisible(false);
        } else {
            d_play.setVisible(false);
            hb_pause.setVisible(true);
//            settingsController.showCurrentParams();
        }
    }



    //_PARAMETER_SETTING________________________________________________________________________________________________
    //_textfield__
    public void tf_v0_X_OnChange(ActionEvent actionEvent) {
    }
    public void tf_v0_Y_OnChange(ActionEvent actionEvent) {
    }
    public void tf_Wind_X_OnChange(ActionEvent actionEvent) {
    }
    public void tf_Wind_Y_OnChange(ActionEvent actionEvent) {
    }

    //_choicebox__
    public void chb_Wind_OnAction(ActionEvent actionEvent) {
        gp_Wind.setDisable(!chb_Wind.isSelected());

        if(!chb_Wind.isSelected())
        {
            Utils.setWind(new MyVector(0,0));
        }
    }
    public void chb_Choice_OnAction(ActionEvent actionEvent) {
        cb_choose.setDisable(!cb_choice_active.isSelected());
        if (cb_choose.isDisabled()) {
            mainWindowModel.getKeyManager().unMarkAll();
        }
        else {
            cb_choose.setPromptText("wähle...");
            cb_choose.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> oV, String oldValue, String newValue) {
                    cb_choose(newValue);

                }
            });
        }
    }

    //_slider__
    public void sl_Weight_OnDragDetected(MouseEvent mouseEvent) {
        settingsController.sl_Weight_OnDragDetected();
    }
    public void sl_Radius_OnDragDetected(){
        settingsController.sl_Radius_OnDragDetected();
    }
    public void sl_Elasticity_OnDragDetected(MouseEvent mouseEvent) {
        settingsController.sl_Elasticity_OnDragDetected();
    }
    public void sl_ScissorsSpeed_OnDragDetected(){
        settingsController.sl_ScissorsSpeed_OnDragDetected();
    }

    //_button__
    public void btn_showCurrentParams_OnAction() throws IOException {
        settingsController.btn_showCurrentParams_OnAction();
    }
    public void d_play_changeSimSpd(){

    }

    //_Menu__
    @FXML
    public void switch_mode() {
        mainWindowModel.getMode().toggleMode(!lightMode);
        lightMode = !lightMode;
        if (!lightMode) {
            lMode.setText("Dark");
        }
        else {
            lMode.setText("Light");
        }
        System.out.println(lightMode);
    }
    //-Menu-Controls----------------------------------------------------------------------------------------------------
    // Is called whenever 'Clear Screen' is clicked in the 'File' menu
    @FXML
    private void clearScreen() {
        mainWindowModel.getBallManager().getBalls().removeAll(mainWindowModel.getBallManager().getBalls());
        mainWindowModel.getWallManager().getWalls().removeAll(mainWindowModel.getWallManager().getWalls());
        mainWindowModel.getScissorsManager().setS(null);
        mainWindowModel.setCurrentlySelected(null);
        aDrawingPane.getChildren().clear();
        hb_pause.setVisible(false);
        d_play.setVisible(false);
        btn_start_stop.setDisable(true);
        aSettingsPane.setDisable(true);
        settingsController.getCurrentParamsController().reset();
        firstTime = true;
    }
    // Is called whenever 'Ball' is clicked in the 'Edit' menu.
    @FXML
    private void choiceBall() {
        Ball b = new Ball(0, new MyVector(0,0), new MyVector(0,0), new MyVector(0,0), new MyVector(0,0), new MyVector(0,0), 25, 2.75,0.5,0,0,0,0);
        mainWindowModel.setCurrentlySelected(b);
        mainWindowModel.getBallManager().setB(b);
        mainWindowModel.getWallManager().setW(null);
        activateLists();
    }
    // Is called whenever 'Wall' is clicked in the 'Edit' menu.
    @FXML
    private void choiceBlock(){
        Wall w = new Wall();
        mainWindowModel.setCurrentlySelected(w);
        mainWindowModel.getWallManager().setW(w);
        mainWindowModel.getBallManager().setB(null);
        activateLists();
    }
    // Is called whenever 'Elastic band' is clicked in the 'Edit' menu.
    @FXML
    private void choiceSchere(){
        Scissors s = new Scissors();
        mainWindowModel.setCurrentlySelected(s);
        mainWindowModel.getBallManager().setB(null);
        mainWindowModel.getWallManager().setW(null);
    }
    // Is called whenever 'Toggle Grid' is clicked in the 'Grid' menu
    @FXML
    private void toggleGrid() {
        mainWindowModel.getGrid().toggleGrid(aDrawingPane);
    }
    // Is called whenever 'Snap To Grid' is clicked in the 'Grid' menu
    @FXML
    private void snapToGrid() {
        mainWindowModel.getGrid().toggleSnapToGrid();
        if(mainWindowModel.getGrid().isSnapOn()){
            lGridSnapActive.setVisible(true);
            miSnap.setStyle("-fx-border-color: green;" +
                    "-fx-border-width: 0 0 2 0;");
        }
        else {
            lGridSnapActive.setVisible(false);
            miSnap.setStyle("-fx-border-width: 0 0 0 0;");
        }
    }

    private void activateLists(){
        // Add change listener for cb_choose to always keep it updated
        mainWindowModel.getBallManager().getBalls().addListener((InvalidationListener) observable -> {
            cb_update();
        });
        mainWindowModel.getWallManager().getWalls().addListener((InvalidationListener) observable -> {
            cb_update();
        });
        if(mainWindowModel.getScissorsManager().getS() != null)
            cb_update();

    }
    public void cb_update() {
        cb_choose.getItems().clear();
        for (Ball b : mainWindowModel.getBallManager().getBalls())
            cb_choose.getItems().add("Ball Nummer " + b.getNumber());

        for (Wall w : mainWindowModel.getWallManager().getWalls())
            cb_choose.getItems().add("Wand Nummer " + w.getNumber());

        if(mainWindowModel.getScissorsManager().getS() != null)
            cb_choose.getItems().add("Schere");
    }

    public void cb_choose(String s) {
        if(cb_choose.getValue() == null) return;

        mainWindowModel.getKeyManager().choose(s);
    }


    //_MOUSE_EVENTS_____________________________________________________________________________________________________
    //-Mouse-&-Key-Listener---------------------------------------------------------------------------------------------
    // Is called whenever the mouse is clicked.
    @FXML
    private void onMouse(MouseEvent e){
        switch(e.getButton()) {
            // Which button is pressed?
            case PRIMARY:
                // Left MouseButton.
                if(mainWindowModel.isChoiceEnabled())
                    // Was 'W' previously pressed
                    mainWindowModel.getKeyManager().manageMouse(e);
                else {
                    mainWindowModel.getPlacer().place(e);
                    if(mainWindowModel.getCurrentlySelected() instanceof Ball) {
                        btn_start_stop.setDisable(false);
                        aSettingsPane.setDisable(false);
                    }
                }
                if(!mainWindowModel.getSimulator().isRunning() & !mainWindowModel.isChoiceEnabled())
                    mainWindowModel.getKeyManager().manageHover(e);
                else
                    mainWindowModel.setTooltip(null);


                break;
            case SECONDARY:
                if(mainWindowModel.isChoiceEnabled())
                    // Was 'W' previously pressed
                    mainWindowModel.getKeyManager().manageMouse(e);
                break;
        }
    }
    @FXML
    private void onMousePressed(MouseEvent e){
        if(mainWindowModel.getScissorsManager().getS() == null)
            mainWindowModel.getPlacer().onMousePressed(e);
    }
    @FXML
    private void onMouseDragged(MouseEvent e) {
        if(mainWindowModel.getScissorsManager().getS() == null)
            mainWindowModel.getPlacer().onMouseDragged(e);
    }
    @FXML
    private void onMouseReleased(MouseEvent e) {
        if(mainWindowModel.getScissorsManager().getS() == null) {
            mainWindowModel.getPlacer().onMouseReleased(e);
            activateLists();
        }
    }

    //_KEY_EVENTS_______________________________________________________________________________________________________
    // Is called whenever a key is pressed.
    @FXML
    private void onKey(KeyEvent e) {
        mainWindowModel.getKeyManager().manageInputs(e.getCode());
    }

    public void arrows() {
        mainWindowModel.setArrowsActive(!mainWindowModel.isArrowsActive());
    }

    // - Custom header -------------------------------------------------------------------------------------------------
    // - No need for comments as it is of no interest for mod. sim. but if the authors are asked they explain it. ------
    private void draggablePrimaryStage() {

        EventHandler<MouseEvent> onMousePressed =
                event -> {
                    if (event.getSceneX() < mainWindowModel.getStage().getWidth() - 105) {
                        mousePressedInHeader = true;
                        windowCursorPosX = event.getScreenX();
                        windowCursorPosY = event.getScreenY();
                        sceneOnWindowPosX = mainWindowModel.getStage().getX();
                        sceneOnWindowPosY = mainWindowModel.getStage().getY();
                    }
                };

        EventHandler<MouseEvent> onMouseDragged =
                event -> {
                    if (mousePressedInHeader) {
                        double offsetX = event.getScreenX() - windowCursorPosX;
                        double offsetY = event.getScreenY() - windowCursorPosY;
                        double newPosX = sceneOnWindowPosX + offsetX;
                        double newPosY = sceneOnWindowPosY + offsetY;
                        if (mainWindowModel.isFullscreen()) {
                            toggleDraggedFullScreen(); //Wenn das Fenster im Vollbildmodus gedragged wird, wird es verkleinert
                            sceneOnWindowPosX = mainWindowModel.getStage().getX();
                        } else {
                            mainWindowModel.getStage().setX(newPosX);
                            mainWindowModel.getStage().setY(newPosY);
                        }
                    }
                };

        EventHandler<MouseEvent> onMouseReleased =
                event -> {
                    if (mousePressedInHeader) {
                        mousePressedInHeader = false;
                        if (MouseInfo.getPointerInfo().getLocation().y == 0) toggleFullScreen(); //Wenn das Fenster oben losgelassen wird, wird es in den Vollbildmodus gesetzt
                        else if (mainWindowModel.getStage().getY() < 0) mainWindowModel.getStage().setY(0); //Wenn das Fenster höher als 0 losgelassen wird, wird die Höhe auf 0 gesetzt
                        else if (mainWindowModel.getStage().getY() + 30 > mainWindowModel.getScreenHeight() - 40) mainWindowModel.getStage().setY(mainWindowModel.getScreenHeight() - 70); //Wenn das Fenster in der Taskbar losgelassen wird, wird es drüber gesetzt
                    }
                };

        EventHandler<MouseEvent> onMouseDoubleClicked =
                event -> {

                    if(event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY){
                        toggleFullScreen();
                    }
                };

        hHeader.setOnMousePressed(onMousePressed);
        hHeader.setOnMouseDragged(onMouseDragged);
        hHeader.setOnMouseReleased(onMouseReleased);
        hHeader.setOnMouseClicked(onMouseDoubleClicked);
    }

    private void toggleFullScreen() {
        Stage stage = mainWindowModel.getStage();
        if (mainWindowModel.isFullscreen()) {
            stage.setX(mainWindowModel.getSavedSceneX());
            stage.setY(mainWindowModel.getSavedSceneY());
            stage.setWidth(mainWindowModel.getSavedSceneWidth());
            stage.setHeight(mainWindowModel.getSavedSceneHeight());
            mainWindowModel.setFullscreen(false);
        }
        else {
            mainWindowModel.setSavedSceneX(stage.getX());
            mainWindowModel.setSavedSceneY(stage.getY());
            mainWindowModel.setSavedSceneWidth(stage.getWidth());
            mainWindowModel.setSavedSceneHeight(stage.getHeight());
            stage.setX(0);
            stage.setY(0);
            stage.setWidth(mainWindowModel.getScreenWidth());
            stage.setHeight(mainWindowModel.getScreenHeight() - mainWindowModel.getTaskbarHeight());
            mainWindowModel.setFullscreen(true);
        }
        aDrawingPane.setPrefSize(stage.getWidth()-aSettingsPane.getWidth(), stage.getHeight());
        resizePane.setDisable(mainWindowModel.isFullscreen());
        mainWindowModel.getGrid().updateGrid(mainWindowModel.getADrawingPane());
    }

    private void toggleDraggedFullScreen() {
        Stage stage = mainWindowModel.getStage();
        if (mainWindowModel.isFullscreen()) {
            double mousePosX = MouseInfo.getPointerInfo().getLocation().x;
            double screenWidthThird = mainWindowModel.getScreenWidth() / 3;
            if (mousePosX <= screenWidthThird)
                stage.setX(1);
            else if (mousePosX > screenWidthThird & mousePosX < screenWidthThird * 2)
                stage.setX(mainWindowModel.getScreenWidth() / 2 - mainWindowModel.getSavedSceneWidth() / 2);
            else
                stage.setX(mainWindowModel.getScreenWidth() - mainWindowModel.getSavedSceneWidth());
            stage.setWidth(mainWindowModel.getSavedSceneWidth());
            stage.setHeight(mainWindowModel.getSavedSceneHeight());
            mainWindowModel.setFullscreen(false);
        } else {
            mainWindowModel.setSavedSceneWidth(stage.getWidth());
            mainWindowModel.setSavedSceneHeight(stage.getHeight());
            stage.setX(0);
            stage.setY(0);
            stage.setWidth(mainWindowModel.getScreenWidth());
            stage.setHeight(mainWindowModel.getScreenHeight() - mainWindowModel.getTaskbarHeight());
            mainWindowModel.setFullscreen(true);
        }
        aDrawingPane.setPrefSize(stage.getWidth()-aSettingsPane.getWidth(), stage.getHeight());
        resizePane.setDisable(mainWindowModel.isFullscreen());
        mainWindowModel.getGrid().updateGrid(mainWindowModel.getADrawingPane());
    }

    @FXML
    private void mouseEnteredMinimize() {
        hoverFrameControls(sMinPane, true);
    }

    @FXML
    private void mouseExitedMinimize() {
        hoverFrameControls(sMinPane, false);
    }

    @FXML
    private void mouseEnteredMinMax() {
        hoverFrameControls(sMinMaxPane, true);
    }

    @FXML
    private void mouseExitedMinMax() {
        hoverFrameControls(sMinMaxPane, false);
    }

    @FXML
    private void mouseEnteredExit() {
        hoverFrameControls(sExitPane, true);
    }

    @FXML
    private void mouseExitedExit() {
        hoverFrameControls(sExitPane, false);
    }

    private void hoverFrameControls(StackPane stackPane, boolean hovered) {
        if (hovered) {
            for (int i = 0; i < stackPane.getChildren().size(); i++) {
                if (stackPane.equals(sExitPane))
                    stackPane.getChildren().get(i).setStyle("-fx-stroke: #cc2b2b");
                else
                    stackPane.getChildren().get(i).setStyle("-fx-stroke: #2bccbd");
            }
            stackPane.setEffect(new Glow(1));
        }
        else {
            for (int i = 0; i < stackPane.getChildren().size(); i++) {
                stackPane.getChildren().get(i).setStyle("-fx-stroke: #c9c9c9");
                stackPane.getChildren().get(i).setEffect(null);
            }
            stackPane.setEffect(null);
        }
    }
    @FXML
    private void minimize() {
        mainWindowModel.getStage().setIconified(true);
    }
    @FXML
    private void minMax() {
        toggleFullScreen();
    }
    @FXML
    private void exit() {
        System.exit(0);
    }
}
