/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interop.log.model;

import interop.log.util.LogValueList;

/**
 * Represents a Well Log, containing its type mnemonic, descriptions, measure unit, null value and the list of depth / log values.
 * @author Luan
 */
public class WellLog 
{
    private String logType;
    private String logDescription;
    private String logMeasureUnit;
    private float nullValue;
    private LogValueList logValues;
    private LogValueList changePoints;

    public WellLog() {
        this.logValues = new LogValueList();
    }
    
    /**
     * @return The log mnemonic.
     */
    public String getLogType() {
        return logType;
    }

    /**
     * @param logType The log mnemonic.
     */
    public void setLogType(String logType) {
        this.logType = logType;
    }

    /**
     * @return The description of the log.
     */
    public String getLogDescription() {
        return logDescription;
    }

    /**
     * @param logDescription the logDescription to set
     */
    public void setLogDescription(String logDescription) {
        this.logDescription = logDescription;
    }

    /**
     * @return the logMeasureUnit
     */
    public String getLogMeasureUnit() {
        return logMeasureUnit;
    }

    /**
     * @param logMeasureUnit the logMeasureUnit to set
     */
    public void setLogMeasureUnit(String logMeasureUnit) {
        this.logMeasureUnit = logMeasureUnit;
    }

    /**
     * @return A list of pairs of depth/log value.
     */
    public LogValueList getLogValues() {
        return logValues;
    }

    /**
     * @param logValues Set the entire list of pairs of depth/log value;
     */
    public void setLogValues(LogValueList logValues) {
        this.logValues = logValues;

    }
    
    /**
     * Convenience method to add to the list of log value pairs a single pair.
     * @param logValuePair A pair of depth/log value.
     */
    public void addLogValuePair(LogValuePair logValuePair)
    {
        this.logValues.add(logValuePair);
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
     * TODO: Gets log weight, should be used to integrate change points
     * @author: Lucas Hagen
     */
    public int getLogWeight() {
        return 1;
    }
    
    /**
     * Gets the Change Points from this log.
     * @return LogValueList, where 0 means no change point, and values > 0 mark a change point.
     */
    public LogValueList getChangePoints() {
        //if(this.changePoints == null)
            //this.changePoints = BeddingDiscriminator.findChangePoints(this.logValues);
        
        return this.changePoints;
    }
    
    /**
     * Manually set change points
     * @param changePoints LogValueList, where: 0 - no change point, > 0 - change point
     */
    public void setChangePoints(LogValueList changePoints) {
        this.changePoints = changePoints;
    }
    
    
}
