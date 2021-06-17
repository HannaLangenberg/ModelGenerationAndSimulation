package project.de.hshl.vcII.mvc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    @FXML
    public TableColumn<Ball, String> tc_PotE;
    @FXML
    public TableColumn<Ball, String> tc_KinE;
    @FXML
    public TableColumn<Ball, String> tc_LossE;
    @FXML
    public TableColumn<Ball, String> tc_TotE;
    @FXML
    public TableColumn<Ball, String> tc_Elasticity;
    @FXML
    public Label l_current_Elasticity;

    private ObservableList<Ball> ballObservableList;

    MainWindowModel mainWindowModel = MainWindowModel.get();

    @FXML
    void initialize() {
        l_current_Gravitation.setText("(" + Utils.GRAVITY.x + "/" + Utils.GRAVITY.y + ")");
        l_current_Delta_T.setText(String.valueOf(Utils.DELTA_T));
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
        tc_Elasticity.setCellValueFactory(new PropertyValueFactory<>("elasticity"));
        tc_TotE.setCellValueFactory(new PropertyValueFactory<>("totE"));
        tc_PotE.setCellValueFactory(new PropertyValueFactory<>("potE"));
        tc_KinE.setCellValueFactory(new PropertyValueFactory<>("kinE"));
        tc_LossE.setCellValueFactory(new PropertyValueFactory<>("lostE"));

        tv_ball_params.setItems(ballObservableList);
    }

    public void reset() {
        ballObservableList.clear();
    }
}
