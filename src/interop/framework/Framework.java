package interop.framework;

import interop.framework.controller.MainController;
import interop.log.model.ParsedLAS;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Main Aplication Class
 *
 * @author Lucas Hagen
 */
public class Framework extends Application {

    private static Framework instance;

    private MainController mainController;

    private Stage window;
    private Scene mainScene;

    private List<ParsedLAS> trainingLAS = new ArrayList<>();
    private List<ParsedLAS> validationLAS = new ArrayList<>();


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

    public void addTrainingParsedLas(ParsedLAS las) {
        this.trainingLAS.add(las);
    }

    public void addValidationParsedLas(ParsedLAS las) {
        this.validationLAS.add(las);
    }

    public List<ParsedLAS> getTrainingLAS() {
        return this.trainingLAS;
    }

    public List<ParsedLAS> getValidationLAS() {
        return this.validationLAS;
    }

    public static Framework getInstance() {
        return Framework.instance;
    }


}
