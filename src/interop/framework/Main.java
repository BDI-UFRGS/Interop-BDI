package interop.framework;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Main implements Initializable {

    @FXML BorderPane mainPane;
    @FXML MenuItem addProfile;
    @FXML VBox centerVBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.setCenterFXML("LogEditor.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCenterFXML(String fxml) throws IOException {
        this.setCenter(FXMLLoader.load(getClass().getResource(fxml)));
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
}
