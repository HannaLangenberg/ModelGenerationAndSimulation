package project.de.hshl.vcII;

import project.de.hshl.vcII.mvc.MainWindowModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

/**
 * Main class.
 * Load the main stage, set the model, the title and the style, and start the Application.
 */

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainWindowModel.get().setStage(primaryStage);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mvc/view/combined.fxml")));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        Scene s = new Scene(root);
        primaryStage.setScene(s);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
