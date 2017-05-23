package interop.framework.controller.factory;

import interop.log.model.WellLog;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.util.Callback;

/**
 * @author Lucas Hagen
 */

public class WellLogBigWindowFactory implements Callback<TableColumn.CellDataFeatures<WellLog, TextField>, ObservableValue<TextField>> {

    @Override
    public ObservableValue<TextField> call(TableColumn.CellDataFeatures<WellLog, TextField> param) {
        WellLog log = param.getValue();
        TextField input = new TextField();
        input.setText(Float.toString(log.getConfiguration().getBigWindow()));

        input.textProperty().addListener((o, oldValue, newValue) -> {
            String value = newValue;
            if(!newValue.matches("[0-9]+([.,][0-9]*)?")) {
                value = oldValue;
            }
            input.setText(value.replaceAll("[,]", "."));
        });
        return new SimpleObjectProperty<>(input);
    }
}
