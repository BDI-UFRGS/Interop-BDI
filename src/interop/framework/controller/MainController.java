package interop.framework.controller;

import interop.anp.ANPFile;
import interop.framework.AlertBox;
import interop.framework.Framework;
import interop.framework.Page;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;

/**
 * Main FXML. This is the controller of the main MenuBar.
 * The content is displayed inside the centerVBox.
 *
 * @author Lucas Hagen
 */
public class MainController implements Controller, Initializable {

    @FXML BorderPane mainPane;
    @FXML VBox centerVBox;
    @FXML MenuBar mainMenuBar;
    @FXML MenuItem setDB;

    private Page currentPage;
    private Page homePage;

    private Stack<Page> pages;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pages =  new Stack<>();
        openHomePage();
    }

    /**
     * Sets the main content.
     *
     * @param fxml URL to FXML file
     * @throws IOException Input/Output error
     */
    public void setPageFXML(URL fxml, boolean saveCurrentPage) throws IOException {
        FXMLLoader loader = new FXMLLoader(fxml);
        Page page = new Page(loader);

        this.setPage(page, saveCurrentPage);
    }

    /**
     * Sets the main content with a ControllerFactory.
     *
     * @param fxml URL to FXML file
     * @param params Params
     */
    public void setPageFXML(URL fxml, boolean saveCurrentPage, Object params, Class controllerClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        FXMLLoader loader = new FXMLLoader(fxml);
        Object b = controllerClass.getDeclaredConstructor(Object.class).newInstance(params);
        loader.setControllerFactory(p -> b);

        loader.load();
        Page page = new Page(loader);

        setPage(page, saveCurrentPage);
    }

    /**
     * Removes any content already displayed.
     */
    private void clearPage() {
        this.centerVBox.getChildren().clear();
        this.currentPage = null;
    }

    public void closePage() {
        if(pages.isEmpty()) {
            openHomePage();
        } else {
            setPage(pages.pop(), false);
        }

        System.out.println(pages.size());
    }

    public void closePage(Controller controller) {
        if(this.currentPage.getController() == controller) {
            if (pages.isEmpty()) {
                openHomePage();
            } else {
                setPage(pages.pop(), false);
            }
        }
    }

    private void savePage() {
        if(this.currentPage != null) {
            pages.push(currentPage);
        }
    }

    /**
     * Sets center content.
     * @param page Center Content
     */
    public void setPage(Page page, boolean saveCurrentPage) {
        if(saveCurrentPage)
            this.savePage();
        clearPage();

        this.currentPage = page;
        this.centerVBox.getChildren().add(page.getParent());

    }

    /**
     * Gets the main content controller.
     * If no content is displayed, null will be returned!
     *
     * @return Controller of the main content
     */
    public Page getCurrentPage() {
        return this.currentPage != null ? this.currentPage : this.homePage;
    }

    public void openHomePage() {
        pages.clear();

        if(homePage == null) {
            try {
                homePage = new Page(new FXMLLoader(getClass().getResource("/interop/framework/fxml/Home.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.currentPage = null;
        this.centerVBox.getChildren().setAll(homePage.getParent());
    }

    /**
     *  ================================================================================================================
     *                                 MENUBAR ACTIONS
     *  ================================================================================================================
     */

    public void setDBPath() {
        FileChooser chooser = new FileChooser();

        chooser.setTitle("Select a StrataDB file.");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Database (*.db)", "*.db"));
        File file = chooser.showOpenDialog(Framework.getInstance().getWindow());

        if(file != null)
            Framework.getInstance().setStrataDBPath(file.getAbsolutePath());
    }

    public void convertANPFile(ActionEvent actionEvent) {
        if(Framework.getInstance().getStrataDBPath() == null) {
            new AlertBox(Alert.AlertType.ERROR, "ANP Converter", "Conversion Finished", "Error! Strataledge DB Path not set!").showAndWait();
            return;
        }


        FileChooser chooser = new FileChooser();

        chooser.setTitle("Select a ANP File");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text File (*.txt)", "*.txt"));
        List<File> files = chooser.showOpenMultipleDialog(Framework.getInstance().getWindow());

        if(files == null || files.size() == 0)
            return;

        for(File file : files) {
            FileChooser saveChooser = new FileChooser();
            saveChooser.setTitle("Save Strataledge XML");
            saveChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml"));
            saveChooser.setInitialDirectory(file.getParentFile());
            saveChooser.setInitialFileName(file.getName().replaceAll("\\.txt(?=$)", ".xml"));

            File save = saveChooser.showSaveDialog(Framework.getInstance().getWindow());

            if (save == null)
                return;

            try {
                ANPFile anpFile = new ANPFile(file);
                anpFile.saveToXML(save);

                new AlertBox(Alert.AlertType.INFORMATION, "ANP Converter", "Conversion Finished", "Success!").showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
                new AlertBox(Alert.AlertType.ERROR, "ANP Converter", "Conversion Finished", "Error! " + e.getMessage()).showAndWait();
            }
        }
    }
}
