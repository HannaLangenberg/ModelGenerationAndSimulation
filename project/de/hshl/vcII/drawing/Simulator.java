package project.de.hshl.vcII.drawing;

import project.de.hshl.vcII.drawing.calculations.Collision;
import project.de.hshl.vcII.drawing.calculations.Movement;
import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.mvc.MainWindowModel;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Simulator {
    // SES for running the program at {60} FPS.
    private ScheduledExecutorService timer;

    // MWM for getting the model (and all it's saves).
    private MainWindowModel mainWindowModel = MainWindowModel.get();

    // Variable to maintain the simulation.
    private boolean running = false;
    private int secondsPassed = 0;
    private int runSim_called = 0;
    int fps = 60;
    double timePerTick = 1000000000.0 / fps;
    double delta = 0;
    long now, lastTime = System.nanoTime();

    long time_btw_frames = 0;
    int ticks = 0;


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
            for(Ball b : mainWindowModel.getBallManager().getBalls())
                System.out.println("PosX: " + b.getCenterX() + " PosY: " + b.getCenterY()
                        + " Velocity: {" + b.getVelVec().x + ". " + b.getVelVec().y + "}");
        }
    }

    /**
     * Calls all the necessary methods to run the simulation.
     */
    private void runSimulation(){
        runSim_called++;
        /*runSim_called++;
        if(runSim_called%60 == 0){
            secondsPassed++;
            System.out.println(secondsPassed);
        }*/
        for (Ball b: mainWindowModel.getBallManager().getBalls()) {
//            Movement.doSmth(b);
//            Collision.collide(b);
            Movement.calcAcceleration(b);
            Movement.calcVelocity(b);
            Movement.checkPosition(b);
            Movement.calcPosition(b);
            b.draw(mainWindowModel.getADrawingPane());
        }
    }

    public boolean isRunning() {
        return running;
    }
}