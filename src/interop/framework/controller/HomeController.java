package interop.framework.controller;

import interop.framework.AlertBox;
import interop.framework.Framework;
import interop.lithologyDataCollector.SampleLithology;
import interop.log.model.LASList;
import interop.log.model.ParsedLAS;
import interop.log.util.LASParser;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Lucas Hagen
 */

public class HomeController implements Controller, Initializable {

    /**
     * Training Buttons
     */
    @FXML
    private Button addTLAS;
    @FXML
    private Button addTXML;
    @FXML
    private Button removeT;
    @FXML
    private Button exportT;

    /**
     * Validation Buttons
     */
    @FXML
    private Button addVLAS;
    @FXML
    private Button addVXML;
    @FXML
    private Button removeV;
    @FXML
    private Button editV;
    @FXML
    private Button exportV;

    @FXML
    private TreeView<String> trainingFilesTree;
    @FXML
    private TreeView<String> validationFilesTree;

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
    public void addLAS(ActionEvent event) {
        FileChooser chooser = new FileChooser();

        chooser.setTitle("Select a LAS File");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("LAS Files (*.las)", "*.las"));
        List<File> files = chooser.showOpenMultipleDialog(Framework.getInstance().getWindow());

        if(files == null)
            return;

        for(File file : files) {
            ParsedLAS las = null;

            try {
                las = new LASParser(file.getAbsolutePath()).getParsedLAS();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (las != null && las.getWellName() != null) {
                if (event.getSource() == this.addTLAS) {
                    Framework.getInstance().getTrainingLASList().add(las);
                    this.branchLAS(las.getWellName(), this.trainingFilesTree.getRoot());
                } else if (event.getSource() == this.addVLAS) {
                    Framework.getInstance().getValidationLASList().add(las);
                    this.branchLAS(las.getWellName(), this.validationFilesTree.getRoot());
                }
            }
        }
    }

    public void addXML(ActionEvent event) {
        TreeItem<String> parent = null;
        LASList lasList = null;
        if(event.getSource() == addTXML) {
            parent = getSelectedItem(trainingFilesTree);
            lasList = Framework.getInstance().getTrainingLASList();
        } else if(event.getSource() == addVXML){
            parent = getSelectedItem(validationFilesTree);
            lasList = Framework.getInstance().getValidationLASList();
        }

        if(parent == null || !isLAS(parent))
            return;

        FileChooser chooser = new FileChooser();

        chooser.setTitle("Select a XML file");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files (*.xml)", "*.xml"));
        List<File> files = chooser.showOpenMultipleDialog(Framework.getInstance().getWindow());

        if(files == null)
            return;

        ParsedLAS las = lasList.getLAS(parent.getValue());

        for(File file : files) {
            if (!las.getXMLPaths().contains(file.getAbsolutePath())) {
                las.getXMLPaths().add(file.getAbsolutePath());
                branchXML(file.getName(), parent);
            }
        }
    }

    public void removeFile(ActionEvent event) {
        TreeView<String> tree;
        TreeItem<String> item;

        if(event.getSource() == this.removeV) {
            tree = validationFilesTree;
            item = getSelectedItem(tree);

            if(isLAS(item)) {
                removeLASFile(tree, item, Framework.getInstance().getValidationLASList());
            } else {
                removeXMLFile(item,  Framework.getInstance().getValidationLASList());
            }
        } else if(event.getSource() == this.removeT) {
            tree = trainingFilesTree;
            item = getSelectedItem(tree);

            if(isLAS(item)) {
                removeLASFile(tree, item, Framework.getInstance().getTrainingLASList());
            } else {
                removeXMLFile(item,  Framework.getInstance().getTrainingLASList());
            }
        }
    }

    private void removeXMLFile(TreeItem<String> item, LASList lasList) {
        ParsedLAS las = lasList.getLAS(item.getParent().getValue());
        String fullPath = null;

        for(String s : las.getXMLPaths()) {
            if(s.contains(item.getValue()))
                fullPath = s;
        }

        item.getParent().getChildren().remove(item);
        las.getXMLPaths().remove(fullPath);
    }

    private void removeLASFile(TreeView<String> tree, TreeItem<String> item, LASList lasList) {
        Iterator<TreeItem<String>> items = new ArrayList<>(item.getChildren()).iterator();
        while(items.hasNext())
            removeXMLFile(items.next(), lasList);

        lasList.removeLAS(item.getValue());
        item.getParent().getChildren().remove(item);
        tree.getSelectionModel().clearSelection();

        updateValidationButtons();
        updateTrainingButtons();
    }

    public void editLAS() {
        if(getSelectedItem(this.validationFilesTree) != null) {
            String wellName = getSelectedItem(this.validationFilesTree).getValue();
            ParsedLAS toEdit = null;

            for (ParsedLAS las : Framework.getInstance().getValidationLASList()) {
                if (las.getWellName().equalsIgnoreCase(wellName))
                    toEdit = las;
            }

            try {
                Framework.getInstance().getMainController().setPageFXML(getClass().getResource("/interop/framework/fxml/VLASEditor.fxml"), true, toEdit, VLASEditorController.class);
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private TreeItem<String> branchLAS(String las, TreeItem<String> parent) {
        TreeItem<String> item = new TreeItem<>(las);
        item.setExpanded(true);
        parent.getChildren().add(item);
        return item;
    }

    private TreeItem<String> branchXML(String xml, TreeItem<String> parent) {
        TreeItem<String> item = new TreeItem<>(xml);
        item.setExpanded(true);
        parent.getChildren().add(item);
        return item;
    }

    public void updateTrainingButtons() {
        ObservableList<TreeItem<String>> tItems = this.trainingFilesTree.getSelectionModel().getSelectedItems();
        if(tItems.size() == 0) {
            addTXML.setDisable(true);
            removeT.setDisable(true);
        } else if(tItems.size() == 1) {
            removeT.setDisable(false);
            addTXML.setDisable(tItems.get(0).getParent() !=
                    trainingFilesTree.getRoot());
        } else {
            addTXML.setDisable(true);
            removeT.setDisable(false);
        }

        if(trainingFilesTree.getRoot().getChildren().size() == 0 || Framework.getInstance().getStrataDBPath() == null) {
            this.exportT.setDisable(true);
        } else {
            this.exportT.setDisable(false);
        }
    }

    public void updateValidationButtons() {
        ObservableList<TreeItem<String>> tItems = this.validationFilesTree.getSelectionModel().getSelectedItems();
        if(tItems.size() == 0) {
            addVXML.setDisable(true);
            removeV.setDisable(true);
            editV.setDisable(true);
        } else if(tItems.size() == 1) {
            removeV.setDisable(false);
            addVXML.setDisable(tItems.get(0).getParent() != validationFilesTree.getRoot());
            editV.setDisable(tItems.get(0).getParent() != validationFilesTree.getRoot());
        } else {
            addVXML.setDisable(true);
            removeV.setDisable(false);
            editV.setDisable(true);
        }

        if(this.validationFilesTree.getRoot().getChildren().size() == 0 || Framework.getInstance().getStrataDBPath() == null) {
            this.exportV.setDisable(true);
        } else {
            this.exportV.setDisable(false);
        }
    }

    private TreeItem<String> getSelectedItem(TreeView<String> tree) {
        ObservableList<TreeItem<String>> items = tree.getSelectionModel().getSelectedItems();

        return items.size() == 0 ? null : items.get(0);
    }

    private boolean isLAS(TreeItem<String> item) {
        return ((item.getParent() == validationFilesTree.getRoot()) || (item.getParent() == trainingFilesTree.getRoot()));
    }

    public void export(ActionEvent event) {
        LASList lasList = null;
        if(event.getSource() == this.exportT) {
            lasList = Framework.getInstance().getTrainingLASList();
        } else if(event.getSource() == this.exportV) {
            lasList = Framework.getInstance().getValidationLASList();
        }

        FileChooser saveChooser = new FileChooser();
        saveChooser.setTitle("Save StrataExport");
        saveChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));
        saveChooser.setInitialFileName("StrataExport.txt");

        File save = saveChooser.showSaveDialog(Framework.getInstance().getWindow());

        if(save == null)
            return;

        try {
            new SampleLithology().run(lasList, save.getAbsolutePath());
            new AlertBox(Alert.AlertType.INFORMATION, "Strata Export", "Strata Export Finished", "Success!").showAndWait();
        } catch(Exception e) {
            new AlertBox(Alert.AlertType.ERROR, "Strata Export", "Strata Export Finished", "Error! " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

}
