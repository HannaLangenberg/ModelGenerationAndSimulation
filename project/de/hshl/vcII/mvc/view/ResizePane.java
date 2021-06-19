package project.de.hshl.vcII.mvc.view;

import project.de.hshl.vcII.mvc.MainModel;
import javafx.beans.value.ChangeListener;
import javafx.scene.Cursor;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.awt.*;
// - No need for comments as it is of no interest for mod. sim. but if the authors are asked they explain it. ----------

public class ResizePane extends AnchorPane {

    private final Line[] lines;
    private final Point[] alignPoints;
    private double windowCursorPosX, windowCursorPosY;
    private double scenePosX, scenePosY;
    private double sceneWidth, sceneHeight;
    private double offsetX, offsetY;
    private final MainModel mainWindowModel;


    public ResizePane() {
        alignPoints = new Point[13];
        lines = new Line[12];
        this.setPickOnBounds(false);

        mainWindowModel = MainModel.get();
    }

    public void initResizeLines() {
        ChangeListener sizeChangeListener = (ChangeListener<Double>) (observable, oldValue, newValue) -> alignResizeLines();
        mainWindowModel.getStage().widthProperty().addListener(sizeChangeListener);
        mainWindowModel.getStage().heightProperty().addListener(sizeChangeListener);

        for (int i = 0; i < 13; i++) {
            alignPoints[i] = new Point();
        }
        for (int i = 0; i < 12; i++) {
            lines[i] = new Line();
            lines[i].setStrokeWidth(4);
            lines[i].setStroke(Color.TRANSPARENT);
            this.getChildren().add(lines[i]);
            switch (i) {
                case 0:
                    lines[i].setCursor(Cursor.N_RESIZE);
                    lines[i].setOnMousePressed(event -> {
                        windowCursorPosY = event.getScreenY();
                        scenePosY = mainWindowModel.getStage().getY();
                        sceneHeight = mainWindowModel.getStage().getHeight();
                    });
                    lines[i].setOnMouseDragged(event -> {
                        offsetY = event.getScreenY() - windowCursorPosY;
                        if (sceneHeight - offsetY > mainWindowModel.getMinWindowSize()[1]) {
                            mainWindowModel.getStage().setY(scenePosY + offsetY);
                            mainWindowModel.getStage().setHeight(sceneHeight - offsetY);
                            updateResizedGrid();
                        }
                    });
                    continue;
                case 1:
                case 2:
                    lines[i].setCursor(Cursor.NE_RESIZE);
                    lines[i].setOnMousePressed(event -> {
                        windowCursorPosX = event.getScreenX();
                        windowCursorPosY = event.getScreenY();
                        scenePosY = mainWindowModel.getStage().getY();
                        sceneWidth = mainWindowModel.getStage().getWidth();
                        sceneHeight = mainWindowModel.getStage().getHeight();
                    });
                    lines[i].setOnMouseDragged(event -> {
                        offsetX = event.getScreenX() - windowCursorPosX;
                        offsetY = event.getScreenY() - windowCursorPosY;
                        if (sceneWidth + offsetX > mainWindowModel.getMinWindowSize()[0])
                            mainWindowModel.getStage().setWidth(sceneWidth + offsetX);
                        updateResizedGrid();
                        if (sceneHeight - offsetY > mainWindowModel.getMinWindowSize()[1]) {
                            mainWindowModel.getStage().setHeight(sceneHeight - offsetY);
                            mainWindowModel.getStage().setY(scenePosY + offsetY);
                            updateResizedGrid();
                        }
                    });
                    continue;
                case 3:
                    lines[i].setCursor(Cursor.E_RESIZE);
                    lines[i].setOnMousePressed(event -> {
                        windowCursorPosX = event.getScreenX();
                        sceneWidth = mainWindowModel.getStage().getWidth();
                    });
                    lines[i].setOnMouseDragged(event -> {
                        offsetX = event.getScreenX() - windowCursorPosX;
                        if (sceneWidth + offsetX > mainWindowModel.getMinWindowSize()[0])
                            mainWindowModel.getStage().setWidth(sceneWidth + offsetX);
                        updateResizedGrid();
                    });
                    continue;
                case 4:
                case 5:
                    lines[i].setCursor(Cursor.SE_RESIZE);
                    lines[i].setOnMousePressed(event -> {
                        windowCursorPosX = event.getScreenX();
                        windowCursorPosY = event.getScreenY();
                        sceneWidth = mainWindowModel.getStage().getWidth();
                        sceneHeight = mainWindowModel.getStage().getHeight();
                    });
                    lines[i].setOnMouseDragged(event -> {
                        offsetX = event.getScreenX() - windowCursorPosX;
                        offsetY = event.getScreenY() - windowCursorPosY;
                        if (sceneWidth + offsetX > mainWindowModel.getMinWindowSize()[0])
                            mainWindowModel.getStage().setWidth(sceneWidth + offsetX);
                        updateResizedGrid();
                        if (sceneHeight + offsetY > mainWindowModel.getMinWindowSize()[1])
                            mainWindowModel.getStage().setHeight(sceneHeight + offsetY);
                        updateResizedGrid();
                    });
                    continue;
                case 6:
                    lines[i].setCursor(Cursor.S_RESIZE);
                    lines[i].setOnMousePressed(event -> {
                        windowCursorPosY = event.getScreenY();
                        sceneHeight = mainWindowModel.getStage().getHeight();
                    });
                    lines[i].setOnMouseDragged(event -> {
                        offsetY = event.getScreenY() - windowCursorPosY;
                        if (sceneHeight + offsetY > mainWindowModel.getMinWindowSize()[1])
                            mainWindowModel.getStage().setHeight(sceneHeight + offsetY);
                        updateResizedGrid();
                    });
                    continue;
                case 7:
                case 8:
                    lines[i].setCursor(Cursor.SW_RESIZE);
                    lines[i].setOnMousePressed(event -> {
                        windowCursorPosX = event.getScreenX();
                        windowCursorPosY = event.getScreenY();
                        scenePosX = mainWindowModel.getStage().getX();
                        sceneWidth = mainWindowModel.getStage().getWidth();
                        sceneHeight = mainWindowModel.getStage().getHeight();
                    });
                    lines[i].setOnMouseDragged(event -> {
                        offsetX = event.getScreenX() - windowCursorPosX;
                        offsetY = event.getScreenY() - windowCursorPosY;
                        if (sceneWidth - offsetX > mainWindowModel.getMinWindowSize()[0]) {
                            mainWindowModel.getStage().setX(scenePosX + offsetX);
                            mainWindowModel.getStage().setWidth(sceneWidth - offsetX);
                            updateResizedGrid();
                        }
                        if (sceneHeight + offsetY > mainWindowModel.getMinWindowSize()[1])
                            mainWindowModel.getStage().setHeight(sceneHeight + offsetY);
                        updateResizedGrid();
                    });
                    continue;
                case 9:
                    lines[i].setCursor(Cursor.W_RESIZE);
                    lines[i].setOnMousePressed(event -> {
                        windowCursorPosX = event.getScreenX();
                        scenePosX = mainWindowModel.getStage().getX();
                        sceneWidth = mainWindowModel.getStage().getWidth();
                    });
                    lines[i].setOnMouseDragged(event -> {
                        offsetX = event.getScreenX() - windowCursorPosX;
                        if (sceneWidth - offsetX > mainWindowModel.getMinWindowSize()[0]) {
                            mainWindowModel.getStage().setX(scenePosX + offsetX);
                            mainWindowModel.getStage().setWidth(sceneWidth - offsetX);
                            updateResizedGrid();
                        }
                    });
                    continue;
                case 10:
                case 11:
                    lines[i].setCursor(Cursor.NW_RESIZE);
                    lines[i].setOnMousePressed(event -> {
                        windowCursorPosX = event.getScreenX();
                        windowCursorPosY = event.getScreenY();
                        scenePosX = mainWindowModel.getStage().getX();
                        scenePosY = mainWindowModel.getStage().getY();
                        sceneWidth = mainWindowModel.getStage().getWidth();
                        sceneHeight = mainWindowModel.getStage().getHeight();
                    });
                    lines[i].setOnMouseDragged(event -> {
                        offsetX = event.getScreenX() - windowCursorPosX;
                        offsetY = event.getScreenY() - windowCursorPosY;
                        if (sceneWidth - offsetX > mainWindowModel.getMinWindowSize()[0]) {
                            mainWindowModel.getStage().setX(scenePosX + offsetX);
                            mainWindowModel.getStage().setWidth(sceneWidth - offsetX);
                            updateResizedGrid();
                        }
                        if (sceneHeight - offsetY > mainWindowModel.getMinWindowSize()[1]) {
                            mainWindowModel.getStage().setY(scenePosY + offsetY);
                            mainWindowModel.getStage().setHeight(sceneHeight - offsetY);
                            updateResizedGrid();
                        }
                    });
                    lines[i].setOnMouseReleased(event -> {
                        if (event.getScreenY() == 0 || event.getScreenY() == mainWindowModel.getScreenHeight() - 1) {
                            mainWindowModel.getStage().setHeight(mainWindowModel.getScreenHeight() - 40);
                            mainWindowModel.getStage().setY(0);
                        }
                    });
            }
        }
    }

    public void alignResizeLines() {
        alignPoints[0] = new Point(10, 2);
        alignPoints[1] = new Point((int) mainWindowModel.getStage().getWidth() -10, 2);
        alignPoints[2] = new Point((int) mainWindowModel.getStage().getWidth() - 2, 2);
        alignPoints[3] = new Point((int) mainWindowModel.getStage().getWidth() - 2, 10);
        alignPoints[4] = new Point((int) mainWindowModel.getStage().getWidth() - 2, (int) mainWindowModel.getStage().getHeight() - 10);
        alignPoints[5] = new Point((int) mainWindowModel.getStage().getWidth() - 2, (int) mainWindowModel.getStage().getHeight() - 2);
        alignPoints[6] = new Point((int) mainWindowModel.getStage().getWidth() - 10, (int) mainWindowModel.getStage().getHeight() - 2);
        alignPoints[7] = new Point(10, (int) mainWindowModel.getStage().getHeight() - 2);
        alignPoints[8] = new Point(2, (int) mainWindowModel.getStage().getHeight() - 2);
        alignPoints[9] = new Point(2, (int) mainWindowModel.getStage().getHeight() - 10);
        alignPoints[10] = new Point(2, 10);
        alignPoints[11] = new Point(2, 2);
        alignPoints[12] = new Point(10, 2);

        for (int i = 0; i < 12; i++) {
            lines[i].setStartX(alignPoints[i].getX());
            lines[i].setEndX(alignPoints[i + 1].getX());
            lines[i].setStartY(alignPoints[i].getY());
            lines[i].setEndY(alignPoints[i + 1].getY());
        }
    }

    public void updateResizedGrid() {
        mainWindowModel.getADrawingPane().setPrefSize(mainWindowModel.getStage().getWidth()-mainWindowModel.getASettingsPane().getWidth(), mainWindowModel.getStage().getHeight());
        mainWindowModel.getGrid().updateGrid(mainWindowModel.getADrawingPane());
    }
}