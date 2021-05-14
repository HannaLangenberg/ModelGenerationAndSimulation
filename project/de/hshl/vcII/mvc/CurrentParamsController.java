package project.de.hshl.vcII.mvc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.utils.Utils;

public class CurrentParamsController {
    @FXML
    public AnchorPane cRootPane;
    @FXML
    public Label l_current_Wind;
    @FXML
    public Label l_current_Delta_T;
    @FXML
    public Label l_current_SimSpd;
    @FXML
    public Label l_current_FPS;
    @FXML
    public Label l_current_Gravitation;
    @FXML
    public ListView<String> lv_balls;
    private ObservableList<String> observableList;

    MainWindowModel mainWindowModel = MainWindowModel.get();

    @FXML
    void initialize() {
        l_current_Wind.setText("(" +  Math.round(Utils.getWind().x) + "/" + Math.round(Utils.getWind().y) + ")");
        l_current_Gravitation.setText("(" + Utils.GRAVITY.x + "/" + Utils.GRAVITY.y + ")");
        l_current_Delta_T.setText(String.valueOf(Utils.DELTA_T));
        l_current_SimSpd.setText(String.valueOf(Utils.sim_Spd));
        l_current_FPS.setText("60" + " fps");
        observableList = FXCollections.observableArrayList();
    }

    public void update() {
        if(observableList.size() != 0)
            observableList.clear();

        for (Ball b : mainWindowModel.getBallManager().getBalls()) {
            observableList.add(b.toString());
        }
        lv_balls.setItems(observableList);
    }

    public void reset() {
        observableList.clear();
    }
}
