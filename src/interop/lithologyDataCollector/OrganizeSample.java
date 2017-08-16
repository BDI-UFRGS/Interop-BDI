package interop.lithologyDataCollector;

import interop.log.model.LogValue;
import interop.log.model.ParsedLAS;
import interop.log.model.WellLog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucas Hagen
 */
public class OrganizeSample {

    private static String nullValue = "NaN";

    private ParsedLAS parsedLAS;
    private List<LogTypeAlias> types;

    private List<WellLog> organized;

    public OrganizeSample(ParsedLAS psd, List<LogTypeAlias> types) {
        this.parsedLAS = psd;
        this.types = types;

        this.organized = getOrganizeLogs();
    }

    public List<String> getOrganizedSample(int index) {
        List<String> organizedSample = new ArrayList<>();
        List<String> organizedValues = new ArrayList<>();

        for (WellLog log : organized) {
            if(log == null) {
                organizedValues.add(nullValue);
            } else {
                LogValue value = log.getLogValues().getPair(index);
                organizedValues.add(Float.toString(value.getLogValue()));

                if(organizedSample.isEmpty())
                    organizedSample.add(Float.toString(value.getDepth()));
            }
        }

        organizedSample.addAll(organizedValues);

        return organizedSample;
    }

    public List<WellLog> getOrganizeLogs() {
        List<WellLog> logs = new ArrayList<>();

        for(LogTypeAlias alias : types) {
            if(alias != LogTypeAlias.DEPT)
                logs.add(alias.get(parsedLAS));
        }

        return logs;
    }

}