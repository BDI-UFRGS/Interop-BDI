package interop.framework;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class LogEditor implements Initializable {

    @FXML Label logID;
    @FXML TextField logName;
    @FXML CheckBox training;

    @FXML Button saveChanges;
    @FXML Button cancel;

    public static File file;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(file == null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.showOpenDialog(Framework.instance);
        }


        logID.setText("Log ID: " + new Random().nextInt());

        //mainPane.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
    }

}
