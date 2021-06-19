package project.de.hshl.vcII.utils;

import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.entities.stationary.Wall;
import project.de.hshl.vcII.mvc.MainWindowModel;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            //_Load_in_the_saved_file___________________________________________________________________________________
            BufferedReader bR = new BufferedReader(new FileReader("TESTSAVE.txt"));
            String currentLine;
            String csv = "";
            int ballCount = mainWindowModel1.getBallManager().getBalls().size(),
                    wallCount = mainWindowModel1.getWallManager().getWalls().size(),
                    scissorsCount = mainWindowModel1.getScissorsManager().getS() == null ? 0 : 1,
                    offset = 1;
            while ((currentLine = bR.readLine()) != null) {
                csv = csv.concat(currentLine);
            }
            String[] splitCsv = csv.split(";"); //first element has to begone

            String[] ballsCsv = new String[ballCount];
            if(splitCsv[offset] != null) {
                for (int i = 0; i < ballCount; i++) {
                    ballsCsv[i] = splitCsv[i + offset];
                }
            }

            String[] wallsCsv = new String[wallCount];
            if(splitCsv[ballCount+offset] != null) {
                for (int i = 0; i < wallCount; i++) {
                    wallsCsv[i] = splitCsv[i + ballCount + offset];
                }
            }

            //TODO: Scissors

            System.out.println(Arrays.toString(ballsCsv));
            System.out.println(Arrays.toString(wallsCsv));

            for(String ballCsv: ballsCsv){
                int paramCount = 18;
                String[] params = ballCsv.split(",");
                String[] rawData = new String[paramCount];
                for(int i = 0; i < paramCount; i++){
                    rawData[i] = params[i].split(": ")[1];
                }
                System.out.println(Arrays.toString(rawData));
                //TODO: Make constructor for Arrow
                Ball b = new Ball(rawData[0], rawData[1], rawData[2], rawData[3], rawData[4], rawData[5], rawData[6],
                        rawData[7], rawData[8], rawData[9], rawData[10], rawData[11] /*Arrow*/, rawData[12], rawData[13],
                        rawData[14] /*Arrow*/, rawData[15], rawData[16], rawData[17]);
            }

            for(String wallCsv: wallsCsv){
                int paramCount = 7;
                String[] params = wallCsv.split(",,");
                String[] rawData = new String[paramCount];
                for(int i = 0; i < paramCount; i++){
                    rawData[i] = params[i].split(": ")[1];
                }
                System.out.println(Arrays.toString(rawData));
                //TODO: Remove Errors
                Wall w = new Wall(rawData[0], rawData[1], rawData[2], rawData[3], rawData[4], rawData[5], rawData[6]);
            }
            /*List<String> splitOffBalls = new ArrayList<>(),
                    splitOffWalls = new ArrayList<>();
            for(int i = 0; i < ballCount; i++){
                int paramCount = 18;
                int ballParams = i * paramCount;
                splitOffBalls.add(splitTextFile[ballParams + 0]);
                splitOffBalls.add(splitTextFile[ballParams + 1]);
                splitOffBalls.add(splitTextFile[ballParams + 2]);
                splitOffBalls.add(splitTextFile[ballParams + 3]);
                splitOffBalls.add(splitTextFile[ballParams + 4]);
                splitOffBalls.add(splitTextFile[ballParams + 5]);
                splitOffBalls.add(splitTextFile[ballParams + 6]);
                splitOffBalls.add(splitTextFile[ballParams + 7]);
                splitOffBalls.add(splitTextFile[ballParams + 8]);
                splitOffBalls.add(splitTextFile[ballParams + 9]);
                splitOffBalls.add(splitTextFile[ballParams + 10]);
                splitOffBalls.add(splitTextFile[ballParams + 11]);
                splitOffBalls.add(splitTextFile[ballParams + 12]);
                splitOffBalls.add(splitTextFile[ballParams + 13]);
                splitOffBalls.add(splitTextFile[ballParams + 14]);
                splitOffBalls.add(splitTextFile[ballParams + 15]);
                splitOffBalls.add(splitTextFile[ballParams + 16]);
                splitOffBalls.add(splitTextFile[ballParams + 17]);
            }
            System.out.println(splitOffBalls);
            // Split off the balls

            String tFWithOutBalls = splitOffBalls[splitOffBalls.length-1];
            String[] balls = new String[splitOffBalls.length-2];
            for(int i = 0; i < splitOffBalls.length-2; i++){
                balls[i] = splitOffBalls[i]; // TEST //
            }
            String[] splitOffWalls = tFWithOutBalls.split("texture:");
            String scissors = splitOffWalls[splitOffWalls.length-1];
            String[] walls = new String[splitOffWalls.length-1];
            for(int i = 0; i < splitOffBalls.length-2; i++){
                walls[i] = splitOffWalls[i];
            }
            // Now all the Data is in balls[], walls[], and scissors
            String singleBalls = Arrays.toString(balls);
            singleBalls = singleBalls. replace("[", "");
            singleBalls = singleBalls. replace("]", "");
            System.out.println(singleBalls);

             */
            //_Create_game_objects______________________________________________________________________________________
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}