package project.de.hshl.vcII.mvc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import project.de.hshl.vcII.drawing.calculations.Calculator;
import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.utils.Utils;

public class CurrentParamsController {
    public Label lWind;
    public Label lDt;
    public Label lGravitation;
    public TableView<Ball> tv_ball_params;
    public TableColumn<Ball, Integer> tc_No;
    public TableColumn<Ball, String> tc_Pos;
    public TableColumn<Ball, String> tc_V;
    public TableColumn<Ball, Integer> tc_Radius;
    public TableColumn<Ball, Integer> tc_Mass;
    public TableColumn<Ball, String> tc_PotE;
    public TableColumn<Ball, String> tc_KinE;
    public TableColumn<Ball, String> tc_LostE;
    public TableColumn<Ball, String> tc_TotE;
    public TableColumn<Ball, String> tc_Elasticity;

    private ObservableList<Ball> ballObservableList;

    MainModel mainWindowModel = MainModel.get();

    public void initialize(Label lWind, Label lDt, Label lGravitation, TableView<Ball> tv_ball_params,
                           TableColumn<Ball, Integer> tc_Radius, TableColumn<Ball, Integer> tc_Mass,
                           TableColumn<Ball, String> tc_V, TableColumn<Ball, String> tc_PotE,
                           TableColumn<Ball, String> tc_KinE, TableColumn<Ball, String> tc_LostE,
                           TableColumn<Ball, String> tc_TotE, TableColumn<Ball, String> tc_Elasticity,
                           TableColumn<Ball, String> tc_Pos, TableColumn<Ball, Integer> tc_No) {
        this.lWind = lWind;
        this.lGravitation = lGravitation;
        this.lDt = lDt;
        this.tv_ball_params = tv_ball_params;
        this.tc_Radius = tc_Radius;
        this.tc_Mass = tc_Mass;
        this.tc_V = tc_V;
        this.tc_PotE = tc_PotE;
        this.tc_KinE = tc_KinE;
        this.tc_LostE = tc_LostE;
        this.tc_TotE = tc_TotE;
        this.tc_Elasticity = tc_Elasticity;
        this.tc_Pos = tc_Pos;
        this.tc_No = tc_No;
        lGravitation.setText("(" + Utils.GRAVITY.x + "/" + Utils.GRAVITY.y + ")");
        lDt.setText(String.valueOf(Utils.DELTA_T));
        lWind.setText("(" +  Math.round(Utils.getWind().x) + "/" + Math.round(Utils.getWind().y) + ")");
        ballObservableList = FXCollections.observableArrayList();
    }

    /**
     * Updates the TableView that displays the current ball parameters. Called whenever an initial velocity is set and
     * every 10th frame to be up to date.
     */
    public void update() {
        Calculator.calcTotalEnergy();

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
        tc_LostE.setCellValueFactory(new PropertyValueFactory<>("lostE"));

        tv_ball_params.setItems(ballObservableList);
    }

    public void reset() {
        MainModel.get().getSettingsController().reset();
        ballObservableList.clear();
        lWind.setText("(" +  0 + "/" + 0 + ")");
    }

    public Label getlWind() {
        return lWind;
    }
}
