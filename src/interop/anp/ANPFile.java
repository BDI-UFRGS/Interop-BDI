package interop.anp;

import interop.anp.exceptions.WrongFormatException;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Lucas Hagen
 */

public class ANPFile {

    private Map<Float, Integer> lines;

    public ANPFile(File file) throws IOException, WrongFormatException {
        lines = new TreeMap<>();

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        Pattern pattern = Pattern.compile("(\\d+(?:[.]\\d+)?)\t(\\d+)[ \\t]*");
        while ((line = br.readLine()) != null) {
            System.out.print("'" + line + "': ");
            Matcher m = pattern.matcher(line);

            if(m.find()) {
                lines.put(Float.valueOf(m.group(1)), Integer.valueOf(m.group(2)));
            }
        }

        if(lines.isEmpty())
            throw new WrongFormatException("Wrong ANP format!");
    }

}
