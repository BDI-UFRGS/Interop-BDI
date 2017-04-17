package interop.framework;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static Main main;
    public Stage window;

    @FXML BorderPane mainPane;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.window = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        window.setTitle("Interop-BDI");
        window.setScene(new Scene(root));
        window.show();
    }

    public void setCenterContent(String parent) throws IOException {
        setCenterParent(FXMLLoader.load(getClass().getResource(parent)));
    }

    public void setCenterParent(Parent parent) {
        this.mainPane.setCenter(parent);
    }

}
