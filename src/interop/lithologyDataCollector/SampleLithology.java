package interop.lithologyDataCollector;

import interop.lithoprototype.model.LithologyDatabase;
import interop.log.model.LASList;
import interop.log.model.ParsedLAS;
import interop.log.model.WellLog;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Eduardo Bassani
 */

public class SampleLithology {

    private float nullValue;
    private List<String> logTypesWanted;
    private LithologyDatabase db;
    private LithologyArchiveFormat archive;

    public SampleLithology(List<String> types) {
        this.logTypesWanted = types;
    }

    public SampleLithology() {
        this(Arrays.asList("DEPT",
                "DT",
                "GR",
                "ILD",
                "NPHI",
                "RHOB",
                "DRHO",
                "CALI",
                "SP",
                "SN",
                "MSFL"
        ));
    }

    public void run(LASList lasList, String path) {
        db = new LithologyDatabase(logTypesWanted);
        archive = new LithologyArchiveFormat(path, logTypesWanted);

        for (ParsedLAS las : lasList)
            processWell(las, las.getXMLPaths());

        try {
            archive.initializeWriter();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        archive.saveToArchive();

        archive.closeWriter();
    }

    public void processWell(ParsedLAS parsed, List<String> pathDescriptions) {
        System.out.println();

        System.out.print("Processing Well");
        DiscoverLithology discoverLithology = new DiscoverLithology(parsed, pathDescriptions);
        OrganizeSample organizer = new OrganizeSample(parsed, logTypesWanted);

        //FOR EVERY SAMPLE IN THE LAS FILE
        for (int i = 0; i < parsed.getLogsList().get(0).getLogValues().size(); i++) {
            if ((10 * i) / parsed.getLogsList().get(0).getLogValues().size() > (10 * (i - 1)) / parsed.getLogsList().get(0).getLogValues().size())
                System.out.print(".");

            // Get depth + organized samples from a specific index
            List<String> organizedSample = organizer.getOrganizedSample(i);

            // Searches for the lithology
            DiscoverLithology.Result result = discoverLithology.discover(i, Float.valueOf(organizedSample.get(0)));


            if (result != null)
                db.feedDatabase(result.getLithologyID(), organizedSample);


            organizedSample.add(parsed.getFullPath());
            organizedSample.add(result.getXml());
            organizedSample.add(result.getLithologyName());
            organizedSample.add(Integer.toString(result.getLithologyID()));
            organizedSample.add(String.valueOf(result.getGrainSizeID()));
            organizedSample.add(String.valueOf(result.getRoundnessID()));
            organizedSample.add(String.valueOf(result.getSphericityID()));

            //if(lithology >= -1){//WITH UNUSED SAMPLES
            if () {//WITHOUT UNUSED SAMPLES;
                //IF EXISTS JUST ADD THE SAMPLE

                archive.add(
                        parsed.getWellName(),
                        organizedSample);
            }
        }

    }

}