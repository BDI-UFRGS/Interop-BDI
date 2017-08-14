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

    public Pattern getSectionPattern() {
        return section;
    }

    public Pattern getCommentPattern() {
        return comment;
    }

    public Pattern getInfoPattern() {
        return info;
    }

    public Pattern getDataPattern() {
        return data;
    }

    public Pattern getDataExtractPattern() {
        return dataExtract;
    }

    public boolean isSection(String line) {
        Matcher matcher = section.matcher(line);

        return matcher.matches();
    }

    public boolean isComment(String line) {
        Matcher matcher = comment.matcher(line);

        return matcher.matches();
    }

    public boolean isInfo(String line) {
        Matcher matcher = info.matcher(line);

        return matcher.matches();
    }

    public boolean isData(String line) {
        Matcher matcher = data.matcher(line);

        return matcher.matches();
    }

    public LASSection getSection(String line) {
        Matcher matcher = section.matcher(line);

        return matcher.find() ? LASSection.fromString(matcher.group(1)) : null;
    }

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
