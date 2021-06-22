package project.de.hshl.vcII.utils;

import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.entities.stationary.Wall;
import project.de.hshl.vcII.mvc.MainModel;

import java.io.*;

public class IO {

    /**
     * Openes a buffered writer and writes data in the specified file. Closes it afterwards.
     * @param file      specified file
     */
    public static void save(File file){
        if(file == null)
            return;
        MainModel mainModel = MainModel.get();
        try {
            BufferedWriter bW = new BufferedWriter(new FileWriter(file));
            String balls = "";
            String walls = "";
            String scissors = "";

            if(!mainModel.getBallManager().getBalls().isEmpty()) {
                for (Ball b : mainModel.getBallManager().getBalls())
                    balls = balls.concat(b.save());
            }
            if(!mainModel.getWallManager().getWalls().isEmpty()){
                for (Wall w : mainModel.getWallManager().getWalls())
                    walls = walls.concat(w.save());
            }
            if(mainModel.getScissorsManager().getS() != null){
                scissors = mainModel.getScissorsManager().getS().save();
            }
            bW.write(balls);
            bW.write(walls);
            bW.write(scissors);
            bW.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Openes a buffered loader and loads a chosen file. Closes it afterwards.
     * @param file      specified file
     */
    public static void load(File file){
        if (file == null)
            return;
        MainModel mainModel = MainModel.get();
        try {
            //_LOAD_GAME_FROM_FILE______________________________________________________________________________________
            BufferedReader bR = new BufferedReader(new FileReader(file));
            String currentLine;
            String csv = "";
            int ballCount = 0,
                    wallCount = 0,
                    scissorsCount = 0,
                    offset = 1;
            while ((currentLine = bR.readLine()) != null) {
                if(!currentLine.equals("")) {
                    csv = csv.concat(currentLine);
                }
            }
            // eg: csv = Ball;Ball;Ball;Wall;Wall;Wall;Wall;Scissors
            //TODO: Exception handling

            // Separate values (at ';')
            String[] splitCsv = csv.split(";"); //first element has to begone

            //eg: splitCsv: [Ball, Ball, Ball, Wall, Wall, Wall, Wall, Scissors]

            // count the number of ball, walls, and scissors
            for(String count : splitCsv){
                if(count.contains("ball "))// 80 Ap
                    // eg: ballCount = 3
                    ballCount++;
                if (count.contains("wall "))
                    // eg: wallCount = 4
                    wallCount++;
                if(count.contains("scissors "))
                    // eg: scissorsCount = 1
                    scissorsCount++;
            }

            // Stitch the correct values together
            String[] ballsCsv = new String[ballCount];
            if(ballCount != 0) {
                for (int i = 0; i < ballCount; i++) {
                    ballsCsv[i] = splitCsv[i + offset];
                }
            }

            String[] wallsCsv = new String[wallCount];
            if(ballCount != 0) {
                for (int i = 0; i < wallCount; i++) {
                    wallsCsv[i] = splitCsv[i + ballCount + offset];
                }
            }

            //_CREATE_THE_GAME-OBJECTS__________________________________________________________________________________
            // Split each one at ',,'
            // Split again at ': '  and always save the 2nd value in the rawData String[]
            for(String ballCsv: ballsCsv){
                int paramCount = 21;
                String[] params = ballCsv.split(",,");
                String[] rawData = new String[paramCount];
                for(int i = 0; i < paramCount; i++){
                    rawData[i] = params[i].split(": ")[1];
                }
                //System.out.println(Arrays.toString(rawData));
                //TODO: Remove Arrow
                Ball b = new Ball(rawData[0], rawData[1], rawData[2], rawData[3], rawData[4], rawData[5], rawData[6],
                        rawData[7], rawData[8], rawData[9], rawData[10], rawData[11] /*Arrow*/, rawData[12], rawData[13],
                        rawData[14] /*Arrow*/, rawData[15], rawData[16], rawData[17], rawData[18], rawData[19], rawData[20]);
                mainModel.setCurrentlySelected(b);
                mainModel.getPlacer().place(b, b.getPosVec());
                mainModel.getBallManager().getBalls().add(b);
            }

            for(String wallCsv: wallsCsv){
                int paramCount = 6;
                String[] params = wallCsv.split(",,");
                String[] rawData = new String[paramCount];
                for(int i = 0; i < paramCount; i++){
                    rawData[i] = params[i].split(": ")[1];
                }
                //System.out.println(Arrays.toString(rawData));
                Wall w = new Wall("/img/blocks/BlockNormal.png", rawData[1], rawData[2], rawData[3], rawData[4], rawData[5]);
                mainModel.getPlacer().place(w, w.getPosVec());
                mainModel.getWallManager().getWalls().add(w);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mainModel.getMainController().activateLists();
    }
}