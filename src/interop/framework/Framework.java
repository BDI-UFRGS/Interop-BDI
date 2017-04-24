package interop.framework;

import interop.framework.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Framework extends Application {

    private Stage window;
    private Scene mainScene;

    private MainController mainController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Initializes Window
        window = primaryStage;
        window.getIcons().setAll(new Image("oil.png"));
        window.setTitle("Interop-BDI");
        window.setResizable(false);

        // Loads main screen (MenuBar)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Main.fxml"));
        mainScene = new Scene(loader.load());
        mainController = loader.getController();

        window.setScene(mainScene);
        window.show();

        getMainController().setCenterFXML(getClass().getResource("fxml/LogEditor.fxml"));
    }

    public Stage getWindow() {
        return this.window;
    }

    public Scene getMainScene() {
        return this.mainScene;
    }

    public MainController getMainController() {
        return this.mainController;
    }


}
