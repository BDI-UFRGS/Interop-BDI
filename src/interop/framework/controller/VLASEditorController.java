package interop.framework.controller;

import interop.log.model.ParsedLAS;
import interop.log.model.WellLog;
import interop.log.util.ConfigurableLog;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Controller for the Validation LAS Editing screen
 *
 * @author Lucas Hagen
 */
public class VLASEditorController implements Controller, Initializable {

    @FXML Label lasID;
    @FXML Label lasStartDepth;
    @FXML Label lasStopDepth;
    @FXML Label lasStep;


    @FXML TextField lasName;
    @FXML Button saveChanges;
    @FXML Button cancel;

    @FXML TableView<ConfigurableLog> logsTable;
    @FXML TableColumn<ConfigurableLog, Boolean> logActive;
    @FXML TableColumn<ConfigurableLog, String> logName;
    @FXML TableColumn<ConfigurableLog, Integer> logWeight;
    @FXML TableColumn<ConfigurableLog, String> logSmallW;
    @FXML TableColumn<ConfigurableLog, String> logBigW;

    public ParsedLAS las = null;

    public VLASEditorController(ParsedLAS las) {
        this.las = las;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setupTableColumns();
        this.updateLAS();
    }

    public void updateLAS() {
        lasID.setText("'" + las.getWellName() + "'");
        lasStartDepth.setText(las.getStartDepth() + " (" + las.getStartDepthMeasureUnit() + ")");
        lasStopDepth.setText(las.getStopDepth() + " (" + las.getStopDepthMeasureUnit() + ")");
        lasStep.setText(las.getStepValue() + " (" + las.getStepValueMeasureUnit() + ")");

        for(WellLog log : las.getLogsList())
            logsTable.getItems().add(new ConfigurableLog(log));
    }

    /**
     * Function to set Table View PropertyValueFactories
     */
    public void setupTableColumns() {
        this.logActive.setCellValueFactory(new PropertyValueFactory<>("active"));
        this.logName.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.logWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        this.logSmallW.setCellValueFactory(new PropertyValueFactory<>("smallW"));
        this.logBigW.setCellValueFactory(new PropertyValueFactory<>("bigW"));
    }

}
