package interop.framework;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by lucas on 17/04/2017.
 */
public class LogEditor {

    public void goTo1() {
        try {
            Main.window.setScene(new Scene(FXMLLoader.load(getClass().getResource("main.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
