package project.de.hshl.vcII.mvc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.utils.Utils;

public class CurrentParamsController {
    private static CurrentParamsController currentParamsController;
    @FXML
    public AnchorPane cRootPane;
    @FXML
    public Label l_current_Wind;
    @FXML
    public Label l_current_Delta_T;
    @FXML
    public Label l_current_FPS;
    @FXML
    public Label l_current_Gravitation;
    @FXML
    public TableView<Ball> tv_ball_params;
    @FXML
    public TableColumn<Ball, Integer> tc_No;
    @FXML
    public TableColumn<Ball, String> tc_Pos;
    @FXML
    public TableColumn<Ball, String> tc_V;
    @FXML
    public TableColumn<Ball, Integer> tc_Radius;
    @FXML
    public TableColumn<Ball, Integer> tc_Mass;

    private ObservableList<Ball> ballObservableList;

    MainWindowModel mainWindowModel = MainWindowModel.get();

    @FXML
    void initialize() {
        l_current_Gravitation.setText("(" + Utils.GRAVITY.x + "/" + Utils.GRAVITY.y + ")");
        l_current_Delta_T.setText(String.valueOf(Utils.DELTA_T));
        l_current_FPS.setText("60" + " fps");
        ballObservableList = FXCollections.observableArrayList();
    }

    public void update() {
        l_current_Wind.setText("(" +  Math.round(Utils.getWind().x) + "/" + Math.round(Utils.getWind().y) + ")");

        if(ballObservableList.size() != 0) {
            ballObservableList.clear();
        }
        ballObservableList.addAll(mainWindowModel.getBallManager().getBalls());

        tc_No.setCellValueFactory(new PropertyValueFactory<>("number"));
        tc_Pos.setCellValueFactory(new PropertyValueFactory<>("posVec"));
        tc_V.setCellValueFactory(new PropertyValueFactory<>("vel0Vec"));
        tc_Radius.setCellValueFactory(new PropertyValueFactory<>("radius"));
        tc_Mass.setCellValueFactory(new PropertyValueFactory<>("mass"));

        tv_ball_params.setItems(ballObservableList);
    }

    public void reset() {
        ballObservableList.clear();
    }
}
