package project.de.hshl.vcII.utils;

/**
 *  Utilities class.
 */
public class Utils {
    // Constant gravity (9.81 m/s^2 on Earth)
    public static final MyVector GRAVITY = new MyVector(0,9.81);
    // Constant for the speed on which the simulation runs (standard value is 10%)
    public static final double DELTA_T = 0.16;// 1/60 → 0.016

    public static double sim_Spd = 1;
    private static MyVector wind = new MyVector(0,0);

    public static final String lightMode = "modes/MainWindowStyleLight.css";
    public static final String darkMode = "modes/MainWindowStyleDark.css";
    // File paths to the light-/dark mode CSS-Data

    //----Getter-&-Setter-----------------------------------------------------------------------------------------------
    public static void setSim_Spd(double sim_Spd) {
        Utils.sim_Spd = sim_Spd;
    }
    public static double getSim_Spd() {
        return sim_Spd;
    }

    public static void setWind(MyVector wind) {
        Utils.wind = wind;
    }
    public static MyVector getWind() {
        return wind;
    }


}
