package interop.framework;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Framework extends Application {

    private Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.window = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        window.setTitle("Interop-BDI");
        window.setScene(new Scene(root));
        window.show();


    }

}
