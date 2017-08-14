package interop.log.util;

import java.util.HashMap;

/**
 * @author Lucas Hagen
 */

public enum LASAttributeType {

    VERS,
    WRAP,
    STRT,
    STOP,
    STEP,
    NULL,
    COMP,
    WELL,
    FLD,
    LOC,
    PROV,
    CNTY,
    STAT,
    CTRY,
    SRVC,
    DATE,
    UWI,
    API,
    LIC,
    OTHER;

    private static HashMap<String, LASAttributeType> byMnemonic;

    static {
        byMnemonic = new HashMap<>();

        for(LASAttributeType type : values())
            byMnemonic.put(type.toString(), type);

    }

    public static LASAttributeType get(String s) {
        LASAttributeType type;

        try {
            type = valueOf(s.toUpperCase());
        } catch (Exception e) {
            type = OTHER;
        }

        return type;
    }


    /*public static LASAttributeType valueOf(String string) {
        return byMnemonic.getOrDefault(string.toUpperCase(), OTHER);
    }*/

}
