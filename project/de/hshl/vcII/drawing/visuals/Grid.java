package project.de.hshl.vcII.drawing.visuals;

import project.de.hshl.vcII.drawing.Placer;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    private List<Line> xLines = new ArrayList<>();
    private List<Line> yLines = new ArrayList<>();
    private boolean gridOn = false;
    private boolean snapOn = false;
    private double gridSpacingX = 100, gridSpacingY = 100;

    /**
     * Turn the Grin on/off.
     * @param aDrawingPane draws the grid on the specified Pane
     */
    public void toggleGrid(AnchorPane aDrawingPane){
        if(gridOn){
            removeGrid(aDrawingPane);
            gridOn = false;
        }
        else {
            makeGrid(aDrawingPane);
            gridOn = true;
        }
    }

    public void removeGrid(AnchorPane aDrawingPane) {
        aDrawingPane.getChildren().removeAll(xLines);
        aDrawingPane.getChildren().removeAll(yLines);
        xLines.clear();
        yLines.clear();
    }

    public void makeGrid(AnchorPane aDrawingPane) {

        double screenX = aDrawingPane.getPrefWidth(),
               screenY = aDrawingPane.getPrefHeight();
        double posX = 0, posY = 0;
        while (posX <= screenX) {
            xLines.add(new Line(posX, 0, posX, screenY));
            posX += gridSpacingX;
        }
        while (posY <= screenY) {
            yLines.add(new Line(0, posY, screenX, posY));
            posY += gridSpacingY;
        }
        aDrawingPane.getChildren().addAll(xLines);
        aDrawingPane.getChildren().addAll(yLines);
    }
    public void updateGrid(AnchorPane aDrawingPane) {
        if(isGridOn()) {
            removeGrid(aDrawingPane);
            makeGrid(aDrawingPane);
        }
    }

    public boolean isGridOn() {
        return gridOn;
    }

    //_SNAP_____________________________________________________________________________________________________________
    public void toggleSnapToGrid(){
        snapOn = !snapOn;
    }

    public void snapToGrid(Placer placer){
        if(placer.getBall() != null) return;
        placer.setX(closestTo(gridSpacingX, placer.getX()));
        placer.setY(closestTo(gridSpacingY, placer.getY()));
    }

    //Ich persönlich finde dieses einfacher zu nutzen!
    private double closestTo(double spacing, double mousePos){

        double roundedPos = Math.round(mousePos/spacing)*spacing;

        if(mousePos%spacing < (spacing/2))
        {
            return (roundedPos + (spacing/2));
        }
        else
        {
            return (roundedPos - (spacing/2));
        }
    }

    public boolean isSnapOn() {
        return snapOn;
    }
}
