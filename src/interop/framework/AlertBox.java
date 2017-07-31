package interop.framework;

import javafx.scene.control.Alert;

/**
 * @author Lucas Hagen
 */

public class AlertBox extends Alert {


    public AlertBox(AlertType alertType, String title, String header, String message) {
        super(alertType);

        setTitle(title);
        setHeaderText(header);
        setContentText(message);
    }
}
