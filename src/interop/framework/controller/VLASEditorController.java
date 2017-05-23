package interop.framework.controller;

import interop.framework.controller.factory.WellLogActiveFactory;
import interop.framework.controller.factory.WellLogBigWindowFactory;
import interop.framework.controller.factory.WellLogSmallWindowFactory;
import interop.framework.controller.factory.WellLogWeightFactory;
import interop.log.model.ParsedLAS;
import interop.log.model.WellLog;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

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

    @FXML TableView<WellLog> logsTable;
    @FXML TableColumn<WellLog, CheckBox> logActive;
    @FXML TableColumn<WellLog, String> logName;
    @FXML TableColumn<WellLog, TextField> logWeight;
    @FXML TableColumn<WellLog, TextField> logSmallW;
    @FXML TableColumn<WellLog, TextField> logBigW;

    public ParsedLAS las = null;

    public VLASEditorController(Object las) {
        if(las instanceof ParsedLAS) {
            this.las = (ParsedLAS)las;
        } else {

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setupTableColumns();
        if(las != null) {
            this.updateLAS();
        }
    }

    /**
     * TODO
     */
    public void updateLAS() {
        lasID.setText("'" + las.getWellName() + "'");
        lasStartDepth.setText(las.getStartDepth() + " (" + las.getStartDepthMeasureUnit() + ")");
        lasStopDepth.setText(las.getStopDepth() + " (" + las.getStopDepthMeasureUnit() + ")");
        lasStep.setText(las.getStepValue() + " (" + las.getStepValueMeasureUnit() + ")");

        for(WellLog log : las.getLogsList())
            logsTable.getItems().add(log);
    }

    /**
     * Function to set Table View PropertyValueFactories
     */
    public void setupTableColumns() {
        this.logActive.setCellValueFactory(new WellLogActiveFactory());

        this.logName.setCellValueFactory(cellValue -> {
            WellLog log = cellValue.getValue();
            return new SimpleObjectProperty(log.getLogType().getLogType() + " (" + log.getLogType().getLogMeasureUnit() + ")");
        });


        this.logWeight.setCellValueFactory(new WellLogWeightFactory());
        this.logSmallW.setCellValueFactory(new WellLogSmallWindowFactory());
        this.logBigW.setCellValueFactory(new WellLogBigWindowFactory());
    }

    public void openLASFile() {

    }

}
