package project.de.hshl.vcII.entities.stationary;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ScissorsManager {
    private ObservableList<Scissors> scissorsList = FXCollections.observableArrayList();
    private Scissors s;

    public void addScissors(Scissors scissors) {
        this.scissorsList.add(scissors);
    }

    public ObservableList<Scissors> getScissorsList() {
        return scissorsList;
    }

    public Scissors getS() {
        return s;
    }

    public void setS(Scissors s) {
        this.s = s;
    }
}
