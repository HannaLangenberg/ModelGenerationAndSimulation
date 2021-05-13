package project.de.hshl.vcII.mvc;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.shape.Polygon;
import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.entities.Entity;
import project.de.hshl.vcII.entities.stationary.BlockNormal;

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
import java.net.URL;
import java.util.ResourceBundle;

/**
 * MainWindowController is used an a controller for the mvc-pattern.
 */

public class MainWindowController implements Initializable {
    @FXML
    public ChoiceBox<String> cb_Material;
    @FXML
    public Slider sl_SimSpd;
    @FXML
    public TextField tf_Wind_Y;
    @FXML
    public TextField tf_Wind_X;
    @FXML
    public GridPane gp_Wind;
    @FXML
    public CheckBox chb_Wind;
    @FXML
    public TextField tf_v0_Y;
    @FXML
    public TextField tf_v0_X;
    @FXML
    public Button btn_start_stop;
    @FXML
    public Polygon d_play;
    @FXML
    public HBox hb_pause;
    @FXML
    public Label l_fps;
    @FXML
    public VBox vb_currentParam;
    @FXML
    public GridPane gp_Wind1;
    @FXML
    public Label l_currentPos;
    @FXML
    public Label l_currentVel;
    @FXML
    public Label l_currentAcc;
    // Declaration of original model.
    private MainWindowModel mainWindowModel;

    // Variables to maintain the screen.
    private double windowCursorPosX, windowCursorPosY;
    private double sceneOnWindowPosX, sceneOnWindowPosY;
    private boolean mousePressedInHeader = false;

    private boolean lightMode = true;

    @FXML
    public AnchorPane aSettingsPane;
    @FXML
    public Label lMode;
    @FXML
    public Label lGridSnapActive;
    @FXML
    public FlowPane fpActiveControls;
    @FXML
    public MenuItem miSnap;
    @FXML
    private StackPane sMinPane, sMinMaxPane, sExitPane;
    @FXML
    private AnchorPane aRootPane, aDrawingPane;
    @FXML
    private HBox hHeader;

    private Ball b;

//    final ObservableList<String> cb_material_List = FXCollections.observableArrayList("Wood", "Other options WIP");

    // Used for resizing.
    private ResizePane resizePane;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_start_stop.setDisable(true);

        // Initialise the original MainWindowModel
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

    public AnchorPane getASettingsPane() { return aSettingsPane;}

    // Is called whenever 'Ball' is clicked in the 'Edit' menu.
    @FXML
    private void choiceBall() {
        mainWindowModel.getBallManager().setB(new Ball());
    }

    // Is called whenever 'Block' is clicked in the 'Edit' menu.
    @FXML
    private void choiceBlock(){
        mainWindowModel.getEntityManager().setE(new BlockNormal(Entity.DEFAULT_SPIN));
    }

    // Is called whenever the 'Start/Stop' button is clicked.
    @FXML
    private void run() {
        mainWindowModel.getSimulator().run();
            if (mainWindowModel.getSimulator().isRunning()) {
                d_play.setVisible(true);
                hb_pause.setVisible(false);
                vb_currentParam.setVisible(false);
            }
            else {
                d_play.setVisible(false);
                hb_pause.setVisible(true);
                b = mainWindowModel.getBallManager().getB();
                l_currentPos.setText("(" + Math.round(b.getPosVec().x) + "/" + Math.round(b.getPosVec().y) + ")");
                l_currentVel.setText("(" + Math.round(b.getVelVec().x) + "/" + Math.round(b.getVelVec().y) + ")");
                l_currentAcc.setText("(" + Math.round(b.getAccVec().x) + "/" + Math.round(b.getAccVec().y) + ")");
                vb_currentParam.setVisible(true);
            }
            // check all TextFields for values
        fillVariables();
    }

    private void fillVariables() {
        Utils.setWind(new MyVector( isInt(tf_Wind_X), isInt(tf_Wind_Y)));
        for(Ball b: mainWindowModel.getBallManager().getBalls()){
            b.setVelVec(new MyVector( isInt(tf_v0_X), isInt(tf_v0_Y)));
            b.setAccVec(new MyVector(0,0));
        }
    }
    // Is called whenever 'Clear Screen' is clicked in the 'File' menu
    @FXML
    private void clearScreen() {
        mainWindowModel.getEntityManager().getBlocks().removeAll(mainWindowModel.getEntityManager().getBlocks());
        aDrawingPane.getChildren().clear();
        hb_pause.setVisible(false);
        d_play.setVisible(false);
        btn_start_stop.setDisable(true);
        aSettingsPane.setDisable(true);
    }

    // Is called whenever a key is pressed.
    @FXML
    private void onKey(KeyEvent e) {
        mainWindowModel.getKeyManager().manageInputs(e.getCode());
    }

    // Is called whenever the mouse is clicked.
    @FXML
    private void onMouse(MouseEvent e){
        switch(e.getButton()) {
            // Which button is pressed?
            case PRIMARY:
                // Left MouseButton.
                if(mainWindowModel.isChoiceEnabled())
                    // Was 'G' previously pressed
                    mainWindowModel.getKeyManager().manageMouse(e);
                else {
                    mainWindowModel.getPlacer().place(e);
                    btn_start_stop.setDisable(false);
                    aSettingsPane.setDisable(false);
                }

                break;
            case SECONDARY:
                mainWindowModel.getKeyManager().manageMouse(e);
        }
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

    @FXML
    public void switch_mode(MouseEvent actionEvent) {
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


    public void tf_v0_X_OnAction(ActionEvent actionEvent) {
    }

    public void tf_v0_Y_OnAction(ActionEvent actionEvent) {
    }

    public void chb_Wind_OnAction(ActionEvent actionEvent) {
        gp_Wind.setDisable(!chb_Wind.isSelected());
    }

    public void tf_Wind_X_OnAction(ActionEvent actionEvent) {
    }

    public void tf_Wind_Y_OnAction(ActionEvent actionEvent) {
    }

    public void sl_SimSpd_OnDragDetected(MouseEvent mouseEvent) {
        Utils.setSim_Spd(sl_SimSpd.getValue());
        System.out.println("-----"+sl_SimSpd.getValue()+"-----");
    }

    public void cb_Material_OnAction(MouseEvent mouseEvent) {
    }

    private double isInt(TextField tf) {
        try {
            return Double.parseDouble(tf.getText());
        }
        catch (NumberFormatException e) {
            tf.setStyle("-fx-border-color: red;");
            /*Alert err = new Alert(Alert.AlertType.ERROR);
            err.setTitle("Keine Zahl!");
            err.setContentText("Es dürfen nur Zahlen eingegeben werden!");
            err.show();*/
            tf.requestFocus();
            return 0.0;
        }
    }

    public void tf_v0_X_OnKeyPressed(KeyEvent keyEvent) {
    }

    public void tf_v0_X_OnMouseExited(MouseEvent mouseEvent) {
//        isInt(tf_v0_X.getText()); //TODO clicked elsewhere
    }


}
