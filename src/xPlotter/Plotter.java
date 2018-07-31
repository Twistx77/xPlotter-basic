package xPlotter;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Plotter extends Application {

    MainWindowController mainWindowController;
    ComProtocol comProtocol;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
        Parent root = loader.load();
        mainWindowController = loader.getController();


        Signals signals = new Signals(mainWindowController);
        comProtocol = new ComProtocol(mainWindowController, signals);

        mainWindowController.setCommProtocol(comProtocol);
        primaryStage.setTitle("Plotter");
        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.show();

        // Disconnect if window is closed before disconnecting.
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                comProtocol.disconnect();
            }
        });
    }
}
