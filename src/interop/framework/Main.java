package interop.framework;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Main implements Initializable {

    @FXML BorderPane mainPane;
    @FXML MenuItem addProfile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            mainPane.setCenter(FXMLLoader.load(getClass().getResource("LogEditor.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
