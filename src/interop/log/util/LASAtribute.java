package interop.log.util;

import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Lucas Hagen
 */

public class LASAtribute {

    private String line;

    private String tag;
    private String unit;
    private String data;
    private String description;

    public LASAtribute(String line) {
        this.line = line;

        Pattern section = Pattern.compile("^~(([a-zA-Z])(?:.*[^ \\t])?)[ \\t]*$");    // COLON + DESCRIPTION (may not exist)

        Pattern comment = Pattern.compile("^#(.*)$");    // COLON + DESCRIPTION (may not exist)

        Pattern data = Pattern.compile("^[ \t]*([a-zA-Z]+)[ ]*" +      // MNEMONIC
                "\\.([^ \\t]+)?[ \\t]+" +                           // DOT (SEPARATOR) + measurement unit (may not exist)
                "([^ \\t](?:.*[^ \\t])?)[ \\t]*" +                  // DATA
                ":(?:[ \\t]*([^ \\t](?:.*[^ \\t])?))?[ \\t]*$");    // COLON + DESCRIPTION (may not exist)

    }



}
