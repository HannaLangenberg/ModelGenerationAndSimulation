package project.de.hshl.vcII.utils;

/**
 * Material class in which all possible materials for this Program are saved is static variables.
 * (This class class is for saving the friction-coefficients: For wood,.)
 */
public class Material {
    private static double WOOD_HR = 1.2, WOOD_GR = 1.3, WOOD_R = 0.006;

    public static double getHaftReibung(String material) {
        switch (material){
            case "wood":
                return WOOD_HR;
            default:
                System.out.println("No material " + material + " found, so the Program will use wood");
                return WOOD_HR;
        }
    }

    public static double getGleitReibung(String material) {
        switch (material){
            case "wood":
                return WOOD_GR;
            default:
                System.out.println("No material " + material + " found, so the Program will use wood");
                return WOOD_GR;
        }
    }

    public static double getRollReibung(String material){
        switch (material){
            case "wood":
                return WOOD_R;
            default:
                System.out.println("No material " + material + " found, so the Program will use wood");
                return WOOD_R;
        }
    }
}