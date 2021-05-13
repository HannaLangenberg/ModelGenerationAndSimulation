package project.de.hshl.vcII.drawing.visuals;

import project.de.hshl.vcII.utils.Utils;
import project.de.hshl.vcII.mvc.MainWindowModel;


public class Mode {

    public void toggleMode(boolean mode) {
        MainWindowModel mainWindowModel = MainWindowModel.get();
        //TODO geht bestimmt effizienter
        if(mode){
            mainWindowModel.getStage().getScene().getStylesheets().remove(Utils.darkMode);
            mainWindowModel.getStage().getScene().getStylesheets().add(Utils.lightMode);
        }
        else {
            mainWindowModel.getStage().getScene().getStylesheets().remove(Utils.lightMode);
            mainWindowModel.getStage().getScene().getStylesheets().add(Utils.darkMode);
        }
    }
}