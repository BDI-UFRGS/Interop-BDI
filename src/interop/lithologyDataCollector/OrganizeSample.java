package interop.lithologyDataCollector;

import interop.log.model.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Lucas Hagen
 */
public class OrganizeSample {

    private static String nullValue = "NaN";

    private ParsedLAS parsedLAS;
    private List<LogTypeAlias> types;

    private List<Iterator<Map.Entry<Float, Float>>> organized;

    public OrganizeSample(ParsedLAS psd, List<LogTypeAlias> types) {
        this.parsedLAS = psd;
        this.types = types;

        resetIterator();
    }

    public void resetIterator() {
        this.organized = getOrganizeLogsIterators();
    }

    public boolean hasNextOrganizedSample() {
        if(organized == null)
            return false;

        for(Iterator i : organized)
            if(i.hasNext())
                return true;

        return false;
    }

    public List<String> getNextOrganizedSample() {
        List<String> organizedSample = new ArrayList<>();
        List<String> organizedValues = new ArrayList<>();
        boolean empty = true;

        for (Iterator<Map.Entry<Float, Float>> log : organized) {
            if(log == null || !log.hasNext()) {
                organizedValues.add(nullValue);
            } else {
                Map.Entry<Float, Float> value = log.next();
                if(value.getValue() == parsedLAS.getNullValue())
                    organizedValues.add(nullValue);
                else
                    organizedValues.add(Float.toString(value.getValue()));

                if(organizedSample.isEmpty())
                    organizedSample.add(Float.toString(value.getKey()));

                empty = false;
            }
        }

        organizedSample.addAll(organizedValues);

        return empty ? null : organizedSample;
    }

    public List<WellLog> getOrganizeLogs() {
        List<WellLog> logs = new ArrayList<>();

        for(LogTypeAlias alias : types) {
            if(alias != LogTypeAlias.DEPT)
                logs.add(alias.get(parsedLAS));
        }

        return logs;
    }

    public List<Iterator<Map.Entry<Float, Float>>> getOrganizeLogsIterators() {
        List<Iterator<Map.Entry<Float, Float>>> logs = new ArrayList<>();

        for(LogTypeAlias alias : types) {
            if(alias != LogTypeAlias.DEPT) {
                WellLog log = alias.get(parsedLAS);
                logs.add(log == null ? null : log.getLogValues().entrySet().iterator());
            }
        }

        return logs;
    }

}