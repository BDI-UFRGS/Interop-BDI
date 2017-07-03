package interop.lithologyDataCollector;

import interop.log.model.ParsedLAS;
import interop.log.model.WellLog;

import java.util.ArrayList;
import java.util.List;


public class OrganizeSample {

    private ParsedLAS parsed;
    private List<String> types;

    public OrganizeSample(ParsedLAS psd, List<String> types) {
        this.parsed = psd;
        this.types = types;
    }

    public List<String> organize(int sampleIndex) {
        List<String> organizedSample = new ArrayList<>();

        for (String type : types) {
            organizedSample.add(Float.toString(getSpecificSampleValue(type, sampleIndex)));
        }

        return organizedSample;
    }

    public float getSpecificSampleValue(String type, int sampleIndex) {
        float nullValue = parsed.getLogsList().get(0).getNullValue();
        float value = nullValue;

        for (WellLog wl : parsed.getLogsList()) {
            if (type.equalsIgnoreCase("DEPT")) {
                value = wl.getLogValues().getPair(sampleIndex).getDepth();
            } else if (type.equalsIgnoreCase(wl.getLogType().getLogMnemonic()) && wl.getLogValues().getPair(sampleIndex).getLogValue() != nullValue) {
                value = wl.getLogValues().getPair(sampleIndex).getLogValue();
            }

        }
        return value;
    }

}