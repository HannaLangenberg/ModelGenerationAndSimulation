package project.de.hshl.vcII.utils;

import javafx.scene.control.TextField;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/**
 *  Utilities class.
 */
public class Utils {
    // Constant gravity (9.81 m/s^2 on Earth)
    public static final MyVector GRAVITY = new MyVector(0,9.81);
    public static final double CONSTANT_OF_GRAVITATION = 9.81;

    // Constant for the speed on which the simulation runs (standard value is 10%)
    public static final double DELTA_T = 0.066;// 1/60 → 0.016

    private static MyVector wind = new MyVector(0,0);

    // File paths to the light-/dark mode CSS-Data
    public static final String darkMode = "modes/MainWindowStyleDark.css";
    public static final String lightMode = "modes/MainWindowStyleLight.css";

    // Used to load a rectangle from the file
    public static Rectangle constructRectFromString(String stringRect) {
        Rectangle rect;

        String sRect = stringRect.replace("Rectangle", "");
        sRect = sRect.replace("[", "");
        sRect = sRect.replace("]", "");
        String[] params = sRect.split(", ");
        double[] rawRect = new double[4];
        for(int i = 0; i < 4; i++){
            rawRect[i] = Double.parseDouble(params[i].split("=")[1]);
        }
        rect = new Rectangle(rawRect[0], rawRect[1], rawRect[2], rawRect[3]);

        return rect;
    }
    // Used to load a line from the file
    public static Line constructLineFromString(String stringLine) {
        Line line;

        String sLine = stringLine.replace("Rectangle", "");
        sLine = sLine.replace("[", "");
        sLine = sLine.replace("]", "");
        String[] params = sLine.split(", ");
        double[] rawLine = new double[4];
        for(int i = 0; i < 4; i++){
            rawLine[i] = Double.parseDouble(params[i].split("=")[1]);
        }
        line = new Line(rawLine[0], rawLine[1], rawLine[2], rawLine[3]);

        return line;
    }

    //----Check---------------------------------------------------------------------------------------------------------
    public static double isDouble(TextField tf) {
        try {
            tf.setStyle("-fx-border-color: none;");
            return Double.parseDouble(tf.getText());
        }
        catch (NumberFormatException e) {
            tf.setStyle("-fx-border-color: red;");
            tf.requestFocus();
            return 0.0;
        }
    }

    //GETTER AND SETTER_________________________________________________________________________________________________
    public static void setWind(MyVector wind) {
        Utils.wind = wind;
    }
    public static MyVector getWind() {
        return wind;
    }


}
