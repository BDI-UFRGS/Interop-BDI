package interop.framework;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LogEditor implements Initializable {

    @FXML private static  Label logID;
    @FXML private static TextField logName;
    @FXML private static CheckBox training;

    @FXML private static Button saveChanges;
    @FXML private static Button cancel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //logID.setText("Log ID: " + new Random().nextLong());
    }

    public static TextField getLogName() {
        return logName;
    }

}
