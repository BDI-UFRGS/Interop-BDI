package interop.framework;

import interop.framework.controller.VLASEditorController;
import interop.framework.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Framework extends Application {

    private static Framework instance;

    private Stage window;
    private Scene mainScene;

    private MainController mainController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Framework.instance = this;

        // Initializes Window
        window = primaryStage;
        window.getIcons().setAll(new Image("oil.png"));
        window.setTitle("Interop-BDI");
        window.setResizable(false);

        // Loads main screen (MenuBar)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Main.fxml"));
        mainScene = new Scene(loader.load());
        mainController = loader.getController();

        getMainController().setCenterFXML(getClass().getResource("fxml/VLASEditor.fxml"));
        ((VLASEditorController) getMainController().getContentController()).setupTableColumns();

        window.setScene(mainScene);
        window.show();


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

    public static Framework getInstance() {
        return Framework.instance;
    }


}
