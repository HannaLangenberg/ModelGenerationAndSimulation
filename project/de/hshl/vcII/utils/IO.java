package project.de.hshl.vcII.utils;

import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.entities.stationary.Wall;
import project.de.hshl.vcII.mvc.MainWindowModel;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class IO {
    public static void save(){
        MainWindowModel mainWindowModel = MainWindowModel.get();
        try {
            BufferedWriter bW = new BufferedWriter(new FileWriter("TESTSAVE.txt"));
            String balls = "";
            String walls = "";
            String scissors = "";

            if(!mainWindowModel.getBallManager().getBalls().isEmpty()) {
                for (Ball b : mainWindowModel.getBallManager().getBalls())
                    balls = balls.concat("\n" + b.save());
            }
            if(!mainWindowModel.getWallManager().getWalls().isEmpty()){
                for (Wall w : mainWindowModel.getWallManager().getWalls())
                    walls = walls.concat("\n" + w.save());
            }
            if(mainWindowModel.getScissorsManager().getS() != null){
                scissors = "\n" + mainWindowModel.getScissorsManager().getS().save();
            }
            MyVector vec = new MyVector("(0/12.345345345)");
            bW.write(balls);
            bW.write(walls);
            bW.write(scissors);
            bW.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
