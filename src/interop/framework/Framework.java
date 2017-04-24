package interop.framework;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Framework extends Application {

    public static Framework instance;

    private Stage window;
    private Scene mainScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        instance = this;

        this.window = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        mainScene = new Scene(root);

        window.getIcons().setAll(new Image("oil.png"));
        window.setTitle("Interop-BDI");
        window.setResizable(false);
        window.setScene(mainScene);
        window.show();
    }

}
