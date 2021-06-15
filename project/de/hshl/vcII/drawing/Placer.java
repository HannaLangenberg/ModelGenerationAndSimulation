package project.de.hshl.vcII.drawing;

import project.de.hshl.vcII.drawing.calculations.Calculator;
import project.de.hshl.vcII.drawing.calculations.WallCalculations;
import project.de.hshl.vcII.drawing.calculations.WallCollision;
import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.entities.stationary.Scissors;
import project.de.hshl.vcII.entities.stationary.Wall;
import project.de.hshl.vcII.mvc.MainWindowModel;
import javafx.scene.input.MouseEvent;
import project.de.hshl.vcII.utils.MyVector;

import java.util.List;

/**
 * Placer is for placing the bocks, the sphere, and all Entities added.
 */
public class Placer {
    private Ball ball;
    private Wall wall;
    private Scissors scissors;
    private MainWindowModel mainWindowModel = MainWindowModel.get();
    private double epsilon = 5;
    private MyVector s_t_parameters;


    private double x, y, xStart, yStart, xEnd, yEnd;

    private double snapBallOnWall(Wall w, Ball b, MyVector mouse){
        //-Snap Ball to wall's surface if close enough
        if (w == null && b != null) {
            // if w is null we are about to place a ball → run through walls and calc distance
            List<Wall> walls = mainWindowModel.getWallManager().getWalls();

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
                    if(WallCollision.checkBallPlacement(wall, b, s_t_parameters, epsilon)) {
                        return Calculator.getDroppedPerpendicular().y - b.getRadius() - epsilon/2;
                    }
                    else
                        return mouse.y;
                }
            }
        }
        else if (b == null && w != null){
            // if b is null we are about to place a wall → run through balls and calc distance
            List<Ball> balls = mainWindowModel.getBallManager().getBalls();

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

    public void place(MouseEvent e){
        x = e.getX();
        y = e.getY();

        if(mainWindowModel.getGrid().isSnapOn()){
            mainWindowModel.getGrid().snapToGrid(this);
        }

        if(mainWindowModel.getCurrentlySelected() instanceof Ball) {
            ball = (Ball) mainWindowModel.getCurrentlySelected();
            if (!mainWindowModel.getADrawingPane().getChildren().contains(ball)) {
                ball.setCenterX(x);
                ball.setCenterY(y);
                ball.setPosVec(new MyVector(x, snapBallOnWall(null, ball, new MyVector(x,y))));
                ball.setNumber(mainWindowModel.getBallManager().getBalls().size() + 1);
                mainWindowModel.getBallManager().addBall(ball);
                mainWindowModel.getADrawingPane().getChildren().add(ball);
                mainWindowModel.setCurrentlySelected(ball);
            } else {
                ball.setCenterX(x);
                ball.setCenterY(y);
                ball.setPosVec(new MyVector(x, snapBallOnWall(null, ball, new MyVector(x,y))));
            }
        } else if(mainWindowModel.getCurrentlySelected() instanceof Wall) {
            wall = (Wall) mainWindowModel.getCurrentlySelected();
            if (!mainWindowModel.getADrawingPane().getChildren().contains(wall.getTexture())) {
                wall.setPosVec(new MyVector(x, snapBallOnWall(wall, null, new MyVector(x, y))));
                wall.setNumber(mainWindowModel.getWallManager().getWalls().size() + 1);
                mainWindowModel.getWallManager().addWall(wall);
                mainWindowModel.getADrawingPane().getChildren().add(wall.getTexture());
                mainWindowModel.setCurrentlySelected(wall);
            } else {
                wall.setPosVec(new MyVector(x, snapBallOnWall(wall, null, new MyVector(x, y))));
            }
        }
    }

    public void onMousePressed(MouseEvent e) {
        if(mainWindowModel.getCurrentlySelected() instanceof Scissors) {
            xStart =  xEnd = e.getX();
            yStart =  yEnd = e.getY();
            scissors = new Scissors();
            redrawLines_s();
        }
    }

    public void onMouseDragged(MouseEvent e) {
        if(mainWindowModel.getCurrentlySelected() instanceof Scissors) {
            xEnd = e.getX();
            yEnd = e.getY();
            redrawLines_s();
        }
    }

    public void onMouseReleased(MouseEvent e) {
        if(mainWindowModel.getCurrentlySelected() instanceof Scissors) {
            xEnd = e.getX();
            yEnd = e.getY();
            drawLines_s();
        }
    }

    public void redrawLines_s() {
        mainWindowModel.getADrawingPane().getChildren().remove(scissors.getG());
        if(Math.abs(xEnd - xStart) < 200)
            scissors.getRectangle().setWidth(Math.abs(xEnd - xStart));
        if (Math.abs(yEnd - yStart) < 400)
            scissors.getRectangle().setHeight(Math.abs(yEnd - yStart));
        scissors.setPosVec(new MyVector(xStart, yStart));
        mainWindowModel.getADrawingPane().getChildren().add(scissors.getG());
    }

    public void drawLines_s() {
        mainWindowModel.getScissorsManager().setS(scissors);
        mainWindowModel.setCurrentlySelected(scissors);
        mainWindowModel.getADrawingPane().getChildren().remove(scissors.getG());
        mainWindowModel.getADrawingPane().getChildren().add(scissors.getG());
    }

    public Scissors getScissors() {
        return scissors;
    }

    public void setScissors(Scissors scissors) {
        this.scissors = scissors;
    }

    public Ball getBall() {
        return ball;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

}
