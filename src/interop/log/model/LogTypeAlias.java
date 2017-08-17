package interop.log.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Lucas Hagen
 */

public enum LogTypeAlias {

    DEPT,
    DT("DTC"),
    GR,
    ILD,
    NPHI("NEUT"),
    RHOB,
    DRHO,
    CALI,
    SP,
    SN,
    MSFL;

    private List<String> aliases;

    LogTypeAlias() {
        this.aliases = null;
    }

    LogTypeAlias(String... alias) {
        this.aliases = Arrays.asList(alias);
    }

    public WellLog get(ParsedLAS parsedLAS) {
        if (this == DEPT)
            return null;

        WellLog log = parsedLAS.getLog(this.toString());

        if (log == null && aliases != null) {
            for (String alias : aliases) {
                WellLog aliasLog = parsedLAS.getLog(alias);
                if (aliasLog != null) {
                    return aliasLog;
                }
            }
        }

        return log;
    }

    public static List<String> toStringList(List<LogTypeAlias> logTypeAliases) {
        List<String> list = new ArrayList<>();

        for (LogTypeAlias alias : logTypeAliases)
            list.add(alias.toString());

        return list;
    }

}
