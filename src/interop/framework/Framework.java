package interop.framework;

import interop.framework.controller.MainController;
import interop.log.model.LASList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.commons.compress.compressors.FileNameUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Main Application Class
 *
 * @author Lucas Hagen
 */
public class Framework extends Application {

    private static Framework instance;

    private MainController mainController;

    private Stage window;
    private Scene mainScene;

    private LASList trainingLAS = new LASList();
    private LASList validationLAS = new LASList();

    private String strataDBPath;


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

    public LASList getTrainingLASList() {
        return this.trainingLAS;
    }

    public LASList getValidationLASList() {
        return this.validationLAS;
    }

    public static Framework getInstance() {
        return Framework.instance;
    }

    public String getStrataDBPath() {
        return strataDBPath;
    }

    public String getExportPath() {
        // C:\Users\lucas\Desktop\StrataDB\StrataDB.db
        Path path = Paths.get(getStrataDBPath());
        return path.getParent().toString() + "\\StrataExport.txt";
    }

    public void setStrataDBPath(String strataDBPath) {
        this.strataDBPath = strataDBPath;
    }
}
