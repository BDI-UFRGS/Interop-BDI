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
        TreeItem<String> root = new TreeItem<>();
        root.setExpanded(true);

        trainingFilesTree.setRoot(root);
        trainingFilesTree.setShowRoot(false);
        makeBranch("Teste", makeBranch("Teste1", root));
        makeBranch("Teste2", root);
        makeBranch("Teste3", root);
        makeBranch("Teste4", root);
        makeBranch("Teste5", root);
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
            } else if(event.getSource() == this.addValidationFileButton) {
                Framework.getInstance().addValidationParsedLas(las);
            }
        }


    }

    private TreeItem<String> makeBranch(String las, TreeItem<String> parent) {
        TreeItem<String> item = new TreeItem<>(las);
        item.setExpanded(true);
        parent.getChildren().add(item);
        return item;
    }



}
