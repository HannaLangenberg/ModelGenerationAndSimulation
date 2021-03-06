package project.de.hshl.vcII.drawing;

import project.de.hshl.vcII.drawing.calculations.Calculator;
import project.de.hshl.vcII.drawing.calculations.WallCalculations;
import project.de.hshl.vcII.drawing.calculations.WallCollisions;
import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.entities.stationary.Scissors;
import project.de.hshl.vcII.entities.stationary.Wall;
import project.de.hshl.vcII.mvc.MainModel;
import javafx.scene.input.MouseEvent;
import project.de.hshl.vcII.mvc.SettingsController;
import project.de.hshl.vcII.utils.MyVector;

import java.util.List;

/**
 * Placer is for placing the bocks, the sphere, and all Entities added.
 */
public class Placer {
    private Ball ball;
    private Wall wall;
    private Scissors scissors;
    private MainModel mainModel = MainModel.get();
    private double epsilon = 5;
    private MyVector s_t_parameters;

    private double x, y, xStart, yStart, xEnd, yEnd;

    /**
     * If close enough snap the currently placed Wall to the Ball, or vice versa.
     *
     * @param w the instance of Wall to be placed
     * @param b the instance of Ball to be placed
     * @param mouse where the cursor is located currently
     * @return where the Wall/Ball should be placed (gust Y tho because only the Y-Position should snap)
     */
    private double snapBallOnWall(Wall w, Ball b, MyVector mouse){
        //-Snap Ball to wall's surface if close enough
        if (w == null && b != null) {
            // if w is null we are about to place a ball → run through walls and calc distance
            List<Wall> walls = mainModel.getWallManager().getWalls();

            for(Wall wall : walls) {
                if (wall.getSpin() == 0) {
                    double deltaY = Math.abs(mouse.y - wall.getCollision().getY());
                    if(deltaY <= b.getRadius() + epsilon & (wall.getCollision().getX() <= mouse.x & wall.getCollision().getX()+wall.getCollision().getWidth() >= mouse.x))
                    {
                        return wall.getCollision().getY() - b.getRadius()-epsilon/2;
                    }
                    else
                        return mouse.y;
                }
                else {
                    s_t_parameters = WallCalculations.calc_s_t_Parameters(wall, mouse);
                    if(WallCollisions.checkBallPlacement(wall, b, s_t_parameters, epsilon)) {
                        return Calculator.getDroppedPerpendicular().y - b.getRadius() - epsilon/2;
                    }
                    else
                        return mouse.y;
                }
            }
        }
        else if (b == null && w != null){
            // if b is null we are about to place a wall → run through balls and calc distance
            List<Ball> balls = mainModel.getBallManager().getBalls();

            for(Ball ball : balls) {
                double deltaY = mouse.y - ball.getPosVec().y;
                if((deltaY > 0 & deltaY <= ball.getRadius() + epsilon + w.getCollision().getHeight()) & (mouse.x-w.getCollision().getWidth()/2-ball.getRadius() <= ball.getPosVec().x & mouse.x+w.getCollision().getWidth()/2+ball.getRadius() >= ball.getPosVec().x))
                {
                    return ball.getPosVec().y + ball.getRadius() + w.getCollision().getHeight()/2 + epsilon/2;
                }
                else if((deltaY < 0 && deltaY >= -ball.getRadius() - epsilon - w.getCollision().getHeight()/2) & (mouse.x-w.getCollision().getWidth()/2-ball.getRadius() <= ball.getPosVec().x & mouse.x+w.getCollision().getWidth()/2+ball.getRadius() >= ball.getPosVec().x))
                {
                    return ball.getPosVec().y + ball.getRadius() + w.getCollision().getHeight()/2 + epsilon/2;
                }
                else
                    return mouse.y;
            }
        }
        return mouse.y;
    }

    /**
     * Place the currently selected Item (Ball, Wall or Scissors) at the X/Y Location of the cursor.
     *
     * @param e MouseEvent to get the cursor position
     */
    public void place(MouseEvent e){
        x = e.getX();
        y = e.getY();

        if(mainModel.getGrid().isSnapOn()){
            mainModel.getGrid().snapToGrid(this);
        }

        if(mainModel.getCurrentlySelected() instanceof Ball) {
            ball = (Ball) mainModel.getCurrentlySelected();
            if (!mainModel.getADrawingPane().getChildren().contains(ball)) {
                ball.setCenterX(x);
                ball.setCenterY(y);
                ball.setPosVec(new MyVector(x, snapBallOnWall(null, ball, new MyVector(x,y))));
                ball.setNumber(mainModel.getBallManager().getBalls().size() + 1);
                mainModel.getBallManager().addBall(ball);
                mainModel.getADrawingPane().getChildren().add(ball);
                mainModel.setCurrentlySelected(ball);
                Calculator.calcInitial_TotalEnergy(ball);
                mainModel.getSettingsController().updateParams();
            } else {
                ball.setCenterX(x);
                ball.setCenterY(y);
                ball.setPosVec(new MyVector(x, snapBallOnWall(null, ball, new MyVector(x,y))));
                Calculator.calcInitial_TotalEnergy(ball);
                mainModel.getSettingsController().updateParams();
            }
        } else if(mainModel.getCurrentlySelected() instanceof Wall) {
            wall = (Wall) mainModel.getCurrentlySelected();
            if (!mainModel.getADrawingPane().getChildren().contains(wall.getTexture())) {
                wall.setPosVec(new MyVector(x, snapBallOnWall(wall, null, new MyVector(x, y))));
                wall.setNumber(mainModel.getWallManager().getWalls().size() + 1);
                mainModel.getWallManager().addWall(wall);
                mainModel.getADrawingPane().getChildren().add(wall.getTexture());
                mainModel.setCurrentlySelected(wall);
            } else {
                wall.setPosVec(new MyVector(x, snapBallOnWall(wall, null, new MyVector(x, y))));
            }
        }
    }

    /**
     * Used for the load function
     *
     * @param posVec
     */
    public void place(Object o, MyVector posVec) {
        x = posVec.x;;
        y = posVec.y;

        if(o instanceof Ball) {
            ball = (Ball) o;
            if (!mainModel.getADrawingPane().getChildren().contains(ball)) {
                ball.setCenterX(x);
                ball.setCenterY(y);
                ball.setPosVec(new MyVector(x, y));
                ball.setNumber(mainModel.getBallManager().getBalls().size() + 1);
                mainModel.getADrawingPane().getChildren().add(ball);
                mainModel.setCurrentlySelected(ball);
            } else {
                ball.setCenterX(x);
                ball.setCenterY(y);
                ball.setPosVec(new MyVector(x, snapBallOnWall(null, ball, new MyVector(x,y))));
            }
        } else if(o instanceof Wall) {
            wall = (Wall) o;
            if (!mainModel.getADrawingPane().getChildren().contains(wall.getTexture())) {
                wall.setPosVec(new MyVector(x, y));
                wall.setNumber(mainModel.getWallManager().getWalls().size() + 1);
                mainModel.getADrawingPane().getChildren().add(wall.getTexture());
                mainModel.setCurrentlySelected(wall);
            } else {
                wall.setPosVec(new MyVector(x, y));
            }
        }
    }

    // Methods for dragging the scissors________________________________________________________________________________
    public void onMousePressed(MouseEvent e) {
        if(mainModel.getCurrentlySelected() instanceof Scissors) {
            xStart =  xEnd = e.getX();
            yStart =  yEnd = e.getY();
            scissors = new Scissors();
            redrawLines_s();
        }
    }

    public void onMouseDragged(MouseEvent e) {
        if(mainModel.getCurrentlySelected() instanceof Scissors) {
            xEnd = e.getX();
            yEnd = e.getY();
            redrawLines_s();
        }
    }

    public void onMouseReleased(MouseEvent e) {
        if(mainModel.getCurrentlySelected() instanceof Scissors) {
            xEnd = e.getX();
            yEnd = e.getY();
            drawLines_s();
        }
    }

    public void redrawLines_s() {
        mainModel.getADrawingPane().getChildren().remove(scissors.getG());
        if(Math.abs(xEnd - xStart) < 200)
            scissors.getRectangle().setWidth(Math.abs(xEnd - xStart));
        if (Math.abs(yEnd - yStart) < 350)
            scissors.getRectangle().setHeight(Math.abs(yEnd - yStart));
        scissors.setPosVec(new MyVector(xStart, yStart));
        mainModel.getADrawingPane().getChildren().add(scissors.getG());
    }

    public void drawLines_s() {
        mainModel.getScissorsManager().setS(scissors);
        mainModel.setCurrentlySelected(scissors);
        mainModel.getADrawingPane().getChildren().remove(scissors.getG());
        mainModel.getADrawingPane().getChildren().add(scissors.getG());
    }
    //__________________________________________________________________________________________________________________

    //GETTER_AND_SETTER_________________________________________________________________________________________________
    public void setScissors(Scissors scissors) {
        this.scissors = scissors;
    }
    public Scissors getScissors() {
        return scissors;
    }

    public Ball getBall() {
        return ball;
    }

    public void setX(double x) {
        this.x = x;
    }
    public double getX() {
        return x;
    }

    public void setY(double y) {
        this.y = y;
    }
    public double getY() {
        return y;
    }
}
