package interop.log.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Lucas Hagen
 */

public class LASRetriever {

    private Pattern section;
    private Pattern comment;
    private Pattern info;
    private Pattern data;
    private Pattern dataExtract;

    /**
     * Generates a LASRetriever and compiles all patterns.
     */
    public LASRetriever() {
        section = Pattern.compile("^~(([a-zA-Z])(?:.*[^ \\t])?)[ \\t]*$");      // COLON + DESCRIPTION (may not exist)

        comment = Pattern.compile("^#(.*)$");                                   // COLON + DESCRIPTION (may not exist)

        info = Pattern.compile("^[ \t]*([a-zA-Z]+)[ ]*" +                       // MNEMONIC
                "\\.([^ \\t]+)?[ \\t]+" +                                       // DOT (SEPARATOR) + measurement unit (may not exist)
                "([^ \\t](?:.*[^ \\t])?)?[ \\t]*" +                             // DATA
                ":(?:[ \\t]*([^ \\t](?:.*[^ \\t])?))?[ \\t]*$");                // COLON + DESCRIPTION (may not exist)

        data = Pattern.compile("^[ \\t]*(?:-?\\d+(?:\\.\\d+)?)(?:[\\t ]+(?:-?\\d+(?:\\.\\d+)?))*[ \\t]*$");
        dataExtract = Pattern.compile("(?:-?\\d+(?:\\.\\d+)?)");
    }

    /**
     * Gets Section Pattern
     * @return Section Pattern
     */
    public Pattern getSectionPattern() {
        return section;
    }

    /**
     * Gets Comment Pattern
     * @return Comment Pattern
     */
    public Pattern getCommentPattern() {
        return comment;
    }

    /**
     * Gets Info Pattern
     * @return Info Pattern
     */
    public Pattern getInfoPattern() {
        return info;
    }

    /**
     * Gets Data Pattern
     * @return Data Pattern
     */
    public Pattern getDataPattern() {
        return data;
    }

    /**
     * Gets Data Extract Pattern (Float Pattern)
     * @return Data Extract Pattern (Float Pattern)
     */
    public Pattern getDataExtractPattern() {
        return dataExtract;
    }

    /**
     * Checks if a string is a Section Divider
     *
     * @param line Line to be checked
     * @return If line is a Section Divider
     */
    public boolean isSection(String line) {
        Matcher matcher = section.matcher(line);

        return matcher.matches();
    }

    /**
     * Checks if a string is a Comment
     *
     * @param line Line to be checked
     * @return If line is a Comment
     */
    public boolean isComment(String line) {
        Matcher matcher = comment.matcher(line);

        return matcher.matches();
    }

    /**
     * Checks if a string is a Info Line
     *
     * @param line Line to be checked
     * @return If line is a Section Divider
     */
    public boolean isInfo(String line) {
        Matcher matcher = info.matcher(line);

        return matcher.matches();
    }

    /**
     * Checks if a string is a Data Line
     *
     * @param line Line to be checked
     * @return If line is a Section Divider
     */
    public boolean isData(String line) {
        Matcher matcher = data.matcher(line);

        return matcher.matches();
    }

    /**
     * Gets section from a Section Divider Line
     *
     * @param line Line to be parsed
     * @return LASSection if found, or null if not found
     */
    public LASSection getSection(String line) {
        Matcher matcher = section.matcher(line);

        return matcher.find() ? LASSection.fromString(matcher.group(1)) : null;
    }

    /**
     * Gets information from a line
     *
     * @param line Line to be parsed
     * @return null if not in the right format, or:
     *          data[0] -> Mnemonic
     *          data[1] -> Unit
     *          data[2] -> Value
     *          data[3] -> Description
     */
    public String[] getInfo(String line) {
        Matcher matcher = info.matcher(line);

        if(matcher.find()) {
            return new String[] {
                    matcher.group(1),
                    matcher.group(2),
                    matcher.group(3),
                    matcher.group(4),
            };
        } else {
            return null;
        }
    }

    /**
     * Gets a list of log values and converts them to floats.
     *
     * @param line Line to be parsed
     * @return Values list or null if not in the right format
     */
    public List<Float> getData(String line) {
        if(!isData(line))
            return null;

        List<Float> data = new ArrayList<>();
        Matcher matcher = dataExtract.matcher(line);

        while(matcher.find())
            data.add(Float.valueOf(matcher.group()));

        return data;
    }
}
