package interop.lithologyDataCollector;

import interop.framework.Framework;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucas Hagen.
 */
public class LithologyArchiveFormat {

    public static String TAB = "\t";

    static int counter = 0;
    static PrintWriter writer = null;
    int lithologyUID;
    String lithologyName = null;
    List<List<String>> specificLog;
    List<String> logTypesWanted;

    public LithologyArchiveFormat(int UID, String lithoName, List<String> sample, List<String> logTypes) {
        this.specificLog = new ArrayList<>();
        specificLog.add(sample);
        lithologyUID = UID;
        logTypesWanted = logTypes;
    }

    public void initializeWriter() {
        try {
            writer = new PrintWriter(Framework.getInstance().getExportPath(), "UTF-8");
            for (String types : logTypesWanted) {
                writer.print(types);
                writer.print(TAB);
            }
            writer.println("LAS" + TAB + "XML" + TAB + "LITHO_NAME" + TAB + "LITHO_ID" + TAB + "GRAIN_SIZE" + TAB + "ROUNDNESS_ID" + TAB + "SPHERICIRY_ID");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot create output file... ");
        }
    }

    public void add(List<String> sample) {
        specificLog.add(sample);
    }

    public static void closeWriter() {
        writer.close();
    }

    //ATENTION: clean the folder of results before resaving samples, or it will save at the end
    public void saveToArchive() {
        if (writer == null)
            return;

        for (List<String> sample : specificLog) {
            for (String data : sample) {
                writer.print(data);
                writer.print(TAB);
                counter++;
            }
            writer.println();
        }

    }
}