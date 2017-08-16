package interop.lithologyDataCollector;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Lucas Hagen
 */
public class LithologyArchiveFormat {

    public static String TAB = "\t";

    private String fullPath;
    private PrintWriter writer = null;
    private HashMap<String, List<List<String>>> specificLog;
    private List<String> logTypesWanted;

    public LithologyArchiveFormat(String fullPath, List<String> logTypes) {
        this.fullPath = fullPath;
        this.specificLog = new HashMap<>();
        logTypesWanted = logTypes;
    }

    public void initializeWriter() throws FileNotFoundException, UnsupportedEncodingException {
        writer = new PrintWriter(this.fullPath, "UTF-8");
    }

    public PrintWriter getWriter() {
        if(this.writer == null) {
            try {
                initializeWriter();
            } catch(FileNotFoundException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return this.writer;
    }

    public void add(String lasID, List<String> sample) {
        if(specificLog.containsKey(lasID)) {
            specificLog.get(lasID).add(sample);
        } else {
            List<List<String>> list = new ArrayList<>();
            list.add(sample);
            specificLog.put(lasID, list);
        }
    }

    public void closeWriter() {
        writer.close();
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public void saveToArchive() {
        if (getWriter() == null)
            return;

        for (String types : logTypesWanted)
            writer.print(types + TAB);
        writer.println("LAS" + TAB + "XML" + TAB + "LITHO_NAME" + TAB + "LITHO_ID" + TAB + "GRAIN_SIZE" + TAB + "ROUNDNESS_ID" + TAB + "SPHERICIRY_ID");

        for (List<List<String>> well : specificLog.values()) {
            for(List<String> sample : well) {
                if(!sample.isEmpty()) {
                    StringBuilder builder = new StringBuilder(sample.get(0));
                    for (int i = 1; i < sample.size(); i++) {
                        builder.append(TAB + sample.get(i));
                    }
                    writer.println(builder);
                }
            }
        }
    }
}