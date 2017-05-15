package interop.framework.controller;

import interop.framework.Framework;
import interop.log.model.ParsedLAS;
import interop.log.util.LASParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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

    @FXML TreeView<HBox> trainingFilesTree;
    @FXML TreeView<HBox> validationFilesTree;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TreeItem<HBox> tRoot = new TreeItem<>();
        tRoot.setExpanded(true);

        trainingFilesTree.setRoot(tRoot);
        trainingFilesTree.setShowRoot(false);

        TreeItem<HBox> vRoot = new TreeItem<>();
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
                this.makeBranch(las.getWellName(), this.trainingFilesTree.getRoot());
            } else if(event.getSource() == this.addValidationFileButton) {
                Framework.getInstance().addValidationParsedLas(las);
                this.makeBranch(las.getWellName(), this.validationFilesTree.getRoot());
            }
        }
    }

    private TreeItem<HBox> makeBranch(String las, TreeItem<HBox> parent) {
        TreeItem<HBox> item = new TreeItem<>(new HBox());
        item.setExpanded(true);
        item.getValue().getChildren().add(new Label(las));
        item.getValue().getChildren().add(new ImageView(new Image("del.png")));
        parent.getChildren().add(item);
        return item;
    }



}
