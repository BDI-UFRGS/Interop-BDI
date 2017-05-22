/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interop.log.model;

/**
 * Represents a Well Log, containing its type mnemonic, descriptions, measure unit, null value and the list of depth / log values.
 *
 * @author Luan
 */
public class WellLog {

    private LogType logType;

    private float nullValue;

    private LogValues logValues;
    private LogValues changePoints;

    private LogConfiguration configuration;

    public WellLog() {
        this.logValues = new LogValues();
        this.configuration = new LogConfiguration();
    }

    /**
     * @return The LogType
     */
    public LogType getLogType() {
        if(this.logType == null)
            this.logType = new LogType();
        return logType;
    }

    /**
     * @param logType The LogType.
     */
    public void setLogType(LogType logType) {
        this.logType = logType;
    }

    /**
     * @return A TreeMap of depth/log value.
     */
    public LogValues getLogValues() {
        return logValues;
    }

    /**
     * @param logValues Set the entire TreeMap of depth/log value;
     */
    public void setLogValues(LogValues logValues) {
        this.logValues = logValues;

    }

    /**
     * Convenience method to add to the list of log value pairs a single pair.
     *
     * @param depth Depth
     * @param value Value
     */
    public void addLogValuePair(float depth, float value) {
        this.logValues.put(depth, value);
    }

    /**
     * @return the nullValue
     */
    public float getNullValue() {
        return nullValue;
    }

    /**
     * @param nullValue the nullValue to set
     */
    public void setNullValue(float nullValue) {
        this.nullValue = nullValue;
    }

    /**
     * TODO: Gets the Change Points from this log.
     *
     * @return LogValueList, where 0 means no change point, and values > 0 mark a change point.
     */
    public LogValues getChangePoints() {
        return null;
    }

    /**
     * Manually set change points
     *
     * @param changePoints LogValueList, where: 0 - no change point, > 0 - change point
     */
    public void setChangePoints(LogValues changePoints) {
        this.changePoints = changePoints;
    }


    /**
     * Getter for the LogConfiguration
     *
     * @return LogConfiguration
     */
    public LogConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Setter for the LogConfiguration
     */
    public void setConfiguration(LogConfiguration configuration) {
        this.configuration = configuration;
    }
}
