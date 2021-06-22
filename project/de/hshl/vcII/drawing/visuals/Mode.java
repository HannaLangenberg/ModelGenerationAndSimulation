package project.de.hshl.vcII.drawing.visuals;

import project.de.hshl.vcII.mvc.MainModel;
import project.de.hshl.vcII.utils.Utils;

public class Mode {

    private boolean mode = false;

    /**
     * Turn the darkmode on/off.
     * @param mode true if lightmode is chosen, false for darkmode
     */
    public void toggleMode(boolean mode) {
        this.mode = mode;
        MainModel mainWindowModel = MainModel.get();
        if(mode){
            mainWindowModel.getStage().getScene().getStylesheets().remove(Utils.darkMode);
            mainWindowModel.getStage().getScene().getStylesheets().add(Utils.lightMode);
        }
        else {
            mainWindowModel.getStage().getScene().getStylesheets().remove(Utils.lightMode);
            mainWindowModel.getStage().getScene().getStylesheets().add(Utils.darkMode);
        }
    }

    public boolean isMode() {
        return mode;
    }
}