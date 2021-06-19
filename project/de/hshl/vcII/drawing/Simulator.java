package project.de.hshl.vcII.drawing;

import project.de.hshl.vcII.drawing.calculations.BallCollisions;
import project.de.hshl.vcII.drawing.calculations.Calculator;
import project.de.hshl.vcII.drawing.calculations.Movement;
import project.de.hshl.vcII.drawing.calculations.ScissorsCollisions;
import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.entities.stationary.Scissors;
import project.de.hshl.vcII.mvc.MainModel;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Simulator {
    // SES for running the program at {60} FPS.
    private ScheduledExecutorService timer;

    // MWM for getting the model (and all it's saves).
    private MainModel mainWindowModel = MainModel.get();

    // Variable to maintain the simulation.
    private boolean running = false;
    private int runSim_called = 0;
    private double epsilon = 5;
    private Scissors s;

    /**
     * Starts and stops simulating.
     */
    public void run(){
        if(!running)
        {
            timer = new ScheduledThreadPoolExecutor(3);
            timer.scheduleAtFixedRate(this::runSimulation, 0, 1000/60, TimeUnit.MILLISECONDS);
            running = true;
        }
        else
        {
            timer.shutdownNow();
            running = false;
            /*for(Ball b : mainWindowModel.getBallManager().getBalls()) {
                System.out.println("PosX: " + b.getCenterX() + " PosY: " + b.getCenterY()
                        + " Velocity: {" + b.getVelVec().x + ". " + b.getVelVec().y + "}");
            }*/
        }
    }
    /**
     * Calls all the necessary methods to run the simulation.
     */
    private void runSimulation(){
        runSim_called++;

        if(mainWindowModel.getScissorsManager().getS() != null) {
            s = mainWindowModel.getScissorsManager().getS();
            if (s.isClosing()) {
                s.animate(mainWindowModel.getADrawingPane());
            }
        }

        for (Ball b: mainWindowModel.getBallManager().getBalls()) {
            Movement.calcAcceleration(b);
            Movement.checkCollisions(b, epsilon);
            BallCollisions.checkBalls(b, epsilon);
            ScissorsCollisions.checkScissors(b, epsilon);
            Movement.calcVelocity(b);
            Movement.calcPosition(b);
            b.draw(mainWindowModel.getADrawingPane());
        }
        if (runSim_called%10 == 0) {
            Calculator.calcTotalEnergy();
        }
    }

    public boolean isRunning() {
        return running;
    }
}