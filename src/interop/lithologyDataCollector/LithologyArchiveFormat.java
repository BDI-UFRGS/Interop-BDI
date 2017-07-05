package interop.lithologyDataCollector;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

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
        if(specificLog.containsKey(lasID))
            specificLog.get(lasID).add(sample);

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
            writer.print(types);
        writer.println(TAB + "LAS" + TAB + "XML" + TAB + "LITHO_NAME" + TAB + "LITHO_ID" + TAB + "GRAIN_SIZE" + TAB + "ROUNDNESS_ID" + TAB + "SPHERICIRY_ID");

        for (List<List<String>> well : specificLog.values()) {
            for(List<String> sample : well) {
                for (String data : sample) {
                    writer.print(data + TAB);
                }
                writer.println();
            }
        }
    }
}