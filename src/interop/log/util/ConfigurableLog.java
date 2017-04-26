package interop.log.util;

import interop.log.model.WellLog;

public class ConfigurableLog {

    private WellLog log;

    private boolean active;
    private String name;
    private float weight;
    private float smallW;
    private float bigW;

    public ConfigurableLog(WellLog log) {
        this.log = log;
        this.active = true;
        this.name = log.getLogType() + " - " + log.getLogDescription();
        this.weight = 1;
        this.smallW = 1;
        this.bigW = 1;
    }

    public WellLog getLog() {
        return log;
    }

    public boolean getActive() {
        return active;
    }

    public boolean isActive() {
        return active;
    }

    public String getName() {
        return name;
    }

    public float getWeight() {
        return weight;
    }

    public double getSmallW() {
        return smallW;
    }

    public double getBigW() {
        return bigW;
    }

}
