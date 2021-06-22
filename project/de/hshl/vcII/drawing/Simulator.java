package project.de.hshl.vcII.drawing;

import project.de.hshl.vcII.drawing.calculations.BallCollisions;
import project.de.hshl.vcII.drawing.calculations.Movement;
import project.de.hshl.vcII.drawing.calculations.ScissorsCollisions;
import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.entities.stationary.Scissors;
import project.de.hshl.vcII.mvc.MainModel;
import project.de.hshl.vcII.mvc.SettingsController;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Simulator {
    // SES for running the program at {60} FPS.
    private ScheduledExecutorService timer;

    // MWM for getting the model (and all it's saves).
    private MainModel mainModel = MainModel.get();
    private SettingsController settingsController = new SettingsController();

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
        }
    }

    /**
     * Calls all the necessary methods to run the simulation.
     */
    private void runSimulation(){
        runSim_called++;

        // Close the scissors
        if(mainModel.getScissorsManager().getS() != null) {
            s = mainModel.getScissorsManager().getS();
            if (s.isClosing()) {
                s.animate(mainModel.getADrawingPane());
            }
        }

        // Apply the physics to each ball
        for (Ball b: mainModel.getBallManager().getBalls()) {
            Movement.calcAcceleration(b);
            Movement.checkCollisions(b, epsilon);
            BallCollisions.checkBalls(b, epsilon);
            ScissorsCollisions.checkScissors(b, epsilon);
            Movement.calcVelocity(b);
            Movement.calcPosition(b);
            b.draw(mainModel.getADrawingPane());
        }

        // Update TableView
        if (runSim_called%10 == 0) {
            settingsController.updateParams();
        }
    }

    public boolean isRunning() {
        return running;
    }
}