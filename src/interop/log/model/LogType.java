package interop.log.model;

public class LogType {

    private String logType;
    private String logDescription;
    private String logMeasureUnit;

    public LogType() {
        this("", "", "");
    }

    public LogType(String logType, String logDescription, String logMeasureUnit) {
        this.logType = logType;
        this.logDescription = logDescription;
        this.logMeasureUnit = logMeasureUnit;
    }
    
     /**
     * @return The log mnemonic.
     */
    public String getLogMnemonic() {
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

}
