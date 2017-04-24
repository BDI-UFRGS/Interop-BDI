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

public class MainController implements Controller, Initializable {

    @FXML BorderPane mainPane;
    @FXML MenuItem addProfile;
    @FXML VBox centerVBox;
    @FXML MenuBar mainMenuBar;

    private Controller contentController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setCenterFXML(URL fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(fxml);
        this.setCenter(loader.load());
        this.contentController = loader.getController();
    }

    public void clearCenter() {
        this.centerVBox.getChildren().clear();
    }

    public void setCenter(Parent parent) {
        this.clearCenter();
        this.addCenter(parent);
    }

    public void addCenter(Parent parent) {
        this.centerVBox.getChildren().add(parent);
    }

    public Controller getContentController() {
        return this.contentController;
    }

    

}
