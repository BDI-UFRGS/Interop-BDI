package interop.framework.controller;

import interop.framework.Framework;
import interop.log.model.ParsedLAS;
import interop.log.util.LASParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Lucas Hagen
 */

public class HomeController implements Controller, Initializable {

    @FXML Button addTrainingFileButton;
    @FXML Button addValidationFileButton;

    @FXML TreeView<String> trainingFilesTree;
    @FXML TreeView<String> validationFilesTree;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TreeItem<String> tRoot = new TreeItem<>();
        tRoot.setExpanded(true);

        trainingFilesTree.setRoot(tRoot);
        trainingFilesTree.setShowRoot(false);

        TreeItem<String> vRoot = new TreeItem<>();
        vRoot.setExpanded(true);

        validationFilesTree.setRoot(vRoot);
        validationFilesTree.setShowRoot(false);
    }

    @FXML
    public void addFile(ActionEvent event) {
        FileChooser chooser = new FileChooser();

        chooser.setTitle("Select a LAS File");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("LAS Files (*.las)", "*.las"));
        File file = chooser.showOpenDialog(Framework.getInstance().getWindow());

        ParsedLAS las = null;

        try {
            las = new LASParser().parseLAS(file.getAbsolutePath());
        } catch (Exception e) {

        }

        if(las != null) {
            if(event.getSource() == this.addTrainingFileButton) {
                Framework.getInstance().addTrainingParsedLas(las);
                this.branchLAS(las.getWellName(), this.trainingFilesTree.getRoot());
            } else if(event.getSource() == this.addValidationFileButton) {
                Framework.getInstance().addValidationParsedLas(las);
                this.branchLAS(las.getWellName(), this.validationFilesTree.getRoot());
            }
        }
    }


    private TreeItem<String> branchLAS(String las, TreeItem<String> parent) {
        TreeItem<String> item = new TreeItem(las);
        item.setExpanded(true);
        parent.getChildren().add(item);
        return item;
    }

    private TreeItem<String> branchXML(String xml, TreeItem<String> parent) {
        TreeItem<String> item = new TreeItem(xml);
        item.setExpanded(true);
        parent.getChildren().add(item);
        return item;
    }



}
