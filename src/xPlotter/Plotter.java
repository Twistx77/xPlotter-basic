package xPlotter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Plotter extends Application {

    MainWindowController mainWindowController;
    ComProtocol comProtocol;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
        Parent root = loader.load();
        mainWindowController = loader.getController();

        comProtocol = new ComProtocol(mainWindowController);

        mainWindowController.setCommProtocol(comProtocol);
        primaryStage.setTitle("Plotter");
        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
