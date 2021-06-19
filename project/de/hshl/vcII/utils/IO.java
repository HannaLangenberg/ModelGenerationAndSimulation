package project.de.hshl.vcII.utils;

import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.entities.stationary.Wall;
import project.de.hshl.vcII.mvc.MainWindowModel;

import java.io.*;
import java.util.Arrays;

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
                    balls = balls.concat(b.save());
            }
            if(!mainWindowModel.getWallManager().getWalls().isEmpty()){
                for (Wall w : mainWindowModel.getWallManager().getWalls())
                    walls = walls.concat(w.save());
            }
            if(mainWindowModel.getScissorsManager().getS() != null){
                scissors = mainWindowModel.getScissorsManager().getS().save();
            }
            bW.write(balls);
            bW.write(walls);
            bW.write(scissors);
            bW.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void load(){
        MainWindowModel mainWindowModel1 = MainWindowModel.get();
        try {
            //_Load_from_the_saved_file___________________________________________________________________________________
            BufferedReader bR = new BufferedReader(new FileReader("TESTSAVE.txt"));
            String currentLine;
            String csv = "";
            int ballCount = mainWindowModel1.getBallManager().getBalls().size(),
                    wallCount = mainWindowModel1.getWallManager().getWalls().size(),
                    scissorsCount = mainWindowModel1.getScissorsManager().getS() == null ? 0 : 1,
                    offset = 1;
            while ((currentLine = bR.readLine()) != null) {
                if(!currentLine.equals("")) {
                    csv = csv.concat(currentLine);
                }
            }
            //TODO: Exception handling
            String[] splitCsv = csv.split(";"); //first element has to begone
            System.out.println(Arrays.toString(splitCsv));

            String[] ballsCsv = new String[ballCount];
            if(splitCsv[offset] != null) {
                for (int i = 0; i < ballCount; i++) {
                    ballsCsv[i] = splitCsv[i + offset];
                }
            }

            System.out.println(Arrays.toString(ballsCsv));

            String[] wallsCsv = new String[wallCount];
            if(splitCsv[ballCount+offset] != null) {
                for (int i = 0; i < wallCount; i++) {
                    wallsCsv[i] = splitCsv[i + ballCount + offset];
                }
            }

            String scissorsCsv= "";
            if(splitCsv[wallCount+offset] != null) {
                scissorsCsv = splitCsv[wallCount + ballCount + offset];
            }

            for(String ballCsv: ballsCsv){
                int paramCount = 18;
                String[] params = ballCsv.split(",");
                String[] rawData = new String[paramCount];
                for(int i = 0; i < paramCount; i++){
                    rawData[i] = params[i].split(": ")[1];
                }
                //TODO: Remove Arrow, number
                Ball b = new Ball(rawData[0], rawData[1], rawData[2], rawData[3], rawData[4], rawData[5], rawData[6],
                        rawData[7], rawData[8], rawData[9], rawData[10], rawData[11] /*Arrow*/, rawData[12], rawData[13],
                        rawData[14] /*Arrow*/, rawData[15], rawData[16], rawData[17]);
                mainWindowModel1.setCurrentlySelected(b);
                mainWindowModel1.getPlacer().place(b.getPosVec());
                System.out.println(Arrays.toString(rawData));
            }

            for(String wallCsv: wallsCsv){
                int paramCount = 7;
                String[] params = wallCsv.split(",,");
                String[] rawData = new String[paramCount];
                for(int i = 0; i < paramCount; i++){
                    rawData[i] = params[i].split(": ")[1];
                }
                //TODO: Remove number
                Wall w = new Wall("/img/blocks/BlockNormal.png", rawData[1], rawData[2], rawData[3], rawData[4], rawData[5], rawData[6]);
                mainWindowModel1.setCurrentlySelected(w);
                mainWindowModel1.getPlacer().place(w.getPosVec());
                System.out.println(Arrays.toString(rawData));
            }

            /*
            int paramCount = 6;
            String[] params = scissorsCsv.split(",,");
            String[] rawData = new String[paramCount];
            for(int i = 0; i < paramCount; i++){
                rawData[i] = params[i].split(": ")[1];
            }
            //TODO: Make constructor for Sissors
            System.out.println(Arrays.toString(rawData));
             */
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}