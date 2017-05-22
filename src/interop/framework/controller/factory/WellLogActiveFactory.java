package interop.framework.controller.factory;

import interop.log.model.WellLog;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * @author Lucas Hagen
 */

public class WellLogActiveFactory implements Callback<TableColumn.CellDataFeatures<WellLog, CheckBox>, ObservableValue<CheckBox>> {

    @Override
    public ObservableValue<CheckBox> call(TableColumn.CellDataFeatures<WellLog, CheckBox> param) {
        WellLog log = param.getValue();
        CheckBox checkBox = new CheckBox();
        checkBox.selectedProperty().setValue(log.getConfiguration().isActive());
        checkBox.selectedProperty().addListener((ov, old_val, new_val) -> {
            log.getConfiguration().setActive(new_val);
        });
        return new SimpleObjectProperty<>(checkBox);
    }

}
