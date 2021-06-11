package project.de.hshl.vcII.drawing.visuals;

import project.de.hshl.vcII.utils.Utils;
import project.de.hshl.vcII.mvc.MainWindowModel;


public class Mode {

    private boolean mode = false;

    public void toggleMode(boolean mode) {
        MainWindowModel mainWindowModel = MainWindowModel.get();
        if(mode){
            mainWindowModel.getStage().getScene().getStylesheets().remove(Utils.darkMode);
            mainWindowModel.getStage().getScene().getStylesheets().add(Utils.lightMode);
        }
        else {
            mainWindowModel.getStage().getScene().getStylesheets().remove(Utils.lightMode);
            mainWindowModel.getStage().getScene().getStylesheets().add(Utils.darkMode);
        }
        mode = !mode;
    }

    public boolean isMode() {
        return mode;
    }

    public void setMode(boolean mode) {
        this.mode = mode;
    }
}