package interop.framework.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.File;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class LogEditorController implements Controller, Initializable {

    @FXML Label lasID;
    @FXML TextField lasName;

    @FXML Button saveChanges;
    @FXML Button cancel;

    public static File file;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        lasID.setText("LAS ID: " + new Random().nextInt());

        //mainPane.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
    }

}
