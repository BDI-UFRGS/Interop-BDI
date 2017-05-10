package interop.framework;

import interop.framework.controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

/**
 * @author Lucas Hagen
 */

public class Page {

    private Parent parent;
    private Controller controller;

    public Page(FXMLLoader loader) throws IOException {
        if(loader.getRoot() == null)
            loader.load();

        this.parent = loader.getRoot();
        this.controller = loader.getController();
    }

    public Page(Parent parent, Controller controller) {
        this.parent = parent;
        this.controller = controller;
    }

    public Parent getParent() {
        return parent;
    }

    public Controller getController() {
        return controller;
    }
}
