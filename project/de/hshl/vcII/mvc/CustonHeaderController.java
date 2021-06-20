package project.de.hshl.vcII.mvc;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import project.de.hshl.vcII.mvc.view.ResizePane;

import java.awt.*;

public class CustonHeaderController {
    private MainModel mainModel = MainModel.get();

    private AnchorPane aSettingsPane;
    private ResizePane resizePane;
    private StackPane sMinPane, sMinMaxPane, sExitPane;
    private HBox hHeader;
    private double sceneOnWindowPosX, sceneOnWindowPosY, windowCursorPosX, windowCursorPosY;;
    private boolean mousePressedInHeader;

    public void initialize(AnchorPane aSettingsPane,ResizePane resizePane, StackPane sMinPane, StackPane sMinMaxPane,
                           StackPane sExitPane, HBox hHeader, double sceneOnWindowPosX, double sceneOnWindowPosY,
                           boolean mousePressedInHeader, double windowCursorPosX, double windowCursorPosY) {
        this.aSettingsPane = aSettingsPane;
        this.resizePane = resizePane;
        this.sMinPane = sMinPane;
        this.sMinMaxPane = sMinMaxPane;
        this.sExitPane = sExitPane;
        this.hHeader = hHeader;
        this.sceneOnWindowPosX = sceneOnWindowPosX;
        this.sceneOnWindowPosY = sceneOnWindowPosY;
        this.mousePressedInHeader = mousePressedInHeader;
        this.windowCursorPosX = windowCursorPosX;
        this.windowCursorPosY = windowCursorPosY;
    }

    public void draggablePrimaryStage() {
        EventHandler<MouseEvent> onMousePressed =
                event -> {
                    if (event.getSceneX() < mainModel.getStage().getWidth() - 105) {
                        mousePressedInHeader = true;
                        windowCursorPosX = event.getScreenX();
                        windowCursorPosY = event.getScreenY();
                        sceneOnWindowPosX = mainModel.getStage().getX();
                        sceneOnWindowPosY = mainModel.getStage().getY();
                    }
                };

        EventHandler<MouseEvent> onMouseDragged =
                event -> {
                    if (mousePressedInHeader) {
                        double offsetX = event.getScreenX() - windowCursorPosX;
                        double offsetY = event.getScreenY() - windowCursorPosY;
                        double newPosX = sceneOnWindowPosX + offsetX;
                        double newPosY = sceneOnWindowPosY + offsetY;
                        if (mainModel.isFullscreen()) {
                            toggleDraggedFullScreen(); //Wenn das Fenster im Vollbildmodus gedragged wird, wird es verkleinert
                            sceneOnWindowPosX = mainModel.getStage().getX();
                        } else {
                            mainModel.getStage().setX(newPosX);
                            mainModel.getStage().setY(newPosY);
                        }
                    }
                };

        EventHandler<MouseEvent> onMouseReleased =
                event -> {
                    if (mousePressedInHeader) {
                        mousePressedInHeader = false;
                        if (MouseInfo.getPointerInfo().getLocation().y == 0) toggleFullScreen(); //Wenn das Fenster oben losgelassen wird, wird es in den Vollbildmodus gesetzt
                        else if (mainModel.getStage().getY() < 0) mainModel.getStage().setY(0); //Wenn das Fenster höher als 0 losgelassen wird, wird die Höhe auf 0 gesetzt
                        else if (mainModel.getStage().getY() + 30 > mainModel.getScreenHeight() - 40) mainModel.getStage().setY(mainModel.getScreenHeight() - 70); //Wenn das Fenster in der Taskbar losgelassen wird, wird es drüber gesetzt
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
        Stage stage = mainModel.getStage();
        if (mainModel.isFullscreen()) {
            stage.setX(mainModel.getSavedSceneX());
            stage.setY(mainModel.getSavedSceneY());
            stage.setWidth(mainModel.getSavedSceneWidth());
            stage.setHeight(mainModel.getSavedSceneHeight());
            mainModel.setFullscreen(false);
        }
        else {
            mainModel.setSavedSceneX(stage.getX());
            mainModel.setSavedSceneY(stage.getY());
            mainModel.setSavedSceneWidth(stage.getWidth());
            mainModel.setSavedSceneHeight(stage.getHeight());
            stage.setX(0);
            stage.setY(0);
            stage.setWidth(mainModel.getScreenWidth());
            stage.setHeight(mainModel.getScreenHeight() - mainModel.getTaskbarHeight());
            mainModel.setFullscreen(true);
        }
        mainModel.getADrawingPane().setPrefSize(stage.getWidth()-aSettingsPane.getWidth(), stage.getHeight());
        resizePane.setDisable(mainModel.isFullscreen());
        mainModel.getGrid().updateGrid(mainModel.getADrawingPane());
    }

    private void toggleDraggedFullScreen() {
        Stage stage = mainModel.getStage();
        if (mainModel.isFullscreen()) {
            double mousePosX = MouseInfo.getPointerInfo().getLocation().x;
            double screenWidthThird = mainModel.getScreenWidth() / 3;
            if (mousePosX <= screenWidthThird)
                stage.setX(1);
            else if (mousePosX > screenWidthThird & mousePosX < screenWidthThird * 2)
                stage.setX(mainModel.getScreenWidth() / 2 - mainModel.getSavedSceneWidth() / 2);
            else
                stage.setX(mainModel.getScreenWidth() - mainModel.getSavedSceneWidth());
            stage.setWidth(mainModel.getSavedSceneWidth());
            stage.setHeight(mainModel.getSavedSceneHeight());
            mainModel.setFullscreen(false);
        } else {
            mainModel.setSavedSceneWidth(stage.getWidth());
            mainModel.setSavedSceneHeight(stage.getHeight());
            stage.setX(0);
            stage.setY(0);
            stage.setWidth(mainModel.getScreenWidth());
            stage.setHeight(mainModel.getScreenHeight() - mainModel.getTaskbarHeight());
            mainModel.setFullscreen(true);
        }
        mainModel.getADrawingPane().setPrefSize(stage.getWidth()-aSettingsPane.getWidth(), stage.getHeight());
        resizePane.setDisable(mainModel.isFullscreen());
        mainModel.getGrid().updateGrid(mainModel.getADrawingPane());
    }

    private void hoverFrameControls(StackPane stackPane, boolean hovered) {
        if (hovered) {
            for (int i = 0; i < stackPane.getChildren().size(); i++) {
                if (stackPane.equals(sExitPane))
                    stackPane.setStyle("-fx-background-color: #cc2b2b");
                else
                    stackPane.setStyle("-fx-background-color: #a6a6a6");
            }
            stackPane.setEffect(new Glow(1));
        }
        else {
            for (int i = 0; i < stackPane.getChildren().size(); i++) {
                stackPane.setStyle("-fx--background-color: #c9c9c9");
                stackPane.getChildren().get(i).setEffect(null);
            }
            stackPane.setEffect(null);
        }
    }


    public void minMax() {
        toggleFullScreen();
    }

    public void exit() {
        System.exit(0);
    }

    public void minimize() {
        mainModel.getStage().setIconified(true);
    }


    public void mouseEnteredExit() {
        hoverFrameControls(sExitPane, true);
    }

    public void mouseExitedExit() {
        hoverFrameControls(sExitPane, false);
    }

    public void mouseEnteredMinimize() {
        hoverFrameControls(sMinPane, true);
    }

    public void mouseExitedMinimize() {
        hoverFrameControls(sMinPane, false);
    }

    public void mouseEnteredMinMax() {
        hoverFrameControls(sMinMaxPane, true);
    }

    public void mouseExitedMinMax() {
        hoverFrameControls(sMinMaxPane, false);
    }
}
