package interop.framework.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Main FXML. This is the controller of the main MenuBar.
 * The content is displayed inside the centerVBox.
 *
 * @author Lucas Hagen
 */
public class MainController implements Controller, Initializable {

    @FXML BorderPane mainPane;
    @FXML MenuItem addProfile;
    @FXML VBox centerVBox;
    @FXML MenuBar mainMenuBar;

    private Controller contentController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Sets the main content.
     *
     * @param fxml URL to FXML file
     * @throws IOException
     */
    public void setCenterFXML(URL fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(fxml);
        this.setCenter(loader.load());
        this.contentController = loader.getController();
    }

    /**
     * Removes any content already displayed.
     */
    public void clearCenter() {
        this.centerVBox.getChildren().clear();
        this.contentController = null;
    }

    /**
     * Sets center content.
     * @param parent Center Content
     */
    public void setCenter(Parent parent) {
        this.clearCenter();
        this.addCenter(parent);
    }

    /**
     * Adds a center content without removing existent ones.
     * @param parent Center Content
     */
    public void addCenter(Parent parent) {
        this.centerVBox.getChildren().add(parent);
    }

    /**
     * Gets the main content controller.
     * If no content is displayed, null will be returned!
     *
     * @return Controller of the main content
     */
    public Controller getContentController() {
        return this.contentController;
    }

    

}
