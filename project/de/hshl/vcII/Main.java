package project.de.hshl.vcII;

import project.de.hshl.vcII.mvc.MainWindowModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Main class.
 * Load the main stage, sets the title, sets the style, and starts the Application
 */

public class Main extends Application {

    public static final String TITLE = "Kugelbahn";

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainWindowModel.get().setStage(primaryStage);
        Parent root = FXMLLoader.load(getClass().getResource("mvc/view/mainWindow.fxml"));
        primaryStage.setTitle(TITLE);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        Scene s = new Scene(root);
        primaryStage.setScene(s);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
