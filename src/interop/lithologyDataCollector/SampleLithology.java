package interop.lithologyDataCollector;

import interop.lithoprototype.model.LithologyDatabase;
import interop.log.model.LASList;
import interop.log.model.ParsedLAS;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Eduardo Bassani
 */

public class SampleLithology {

    public static float nullValue;
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
        System.out.println("LASList Size: " + lasList.size());

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

        OrganizeSample organizer = new OrganizeSample(parsed, logTypesWanted);
        //FOR EVERY SAMPLE IN THE LAS FILE
        for (int i = 0; i < parsed.getLogsList().get(0).getLogValues().size(); i++) {
            if ((10 * i) / parsed.getLogsList().get(0).getLogValues().size() > (10 * (i - 1)) / parsed.getLogsList().get(0).getLogValues().size())
                System.out.print(".");
            //ORGANIZE SAMPLE IN ORDER OF logsTypeWanted
            List<String> organizedSample = organizer.organize(i);

            //SEARCH THE LITHOLOGY IN THE LIST OF XML, IF IT EXISTS
            DiscoverLithology discoverLithology = new DiscoverLithology(parsed, i, pathDescriptions);
            int lithology = discoverLithology.discover();
            //int lithology = discoverLithology.fast_discover();
            //System.out.println(lithology2 + " AND " + lithology);

            if (lithology != 0)
                db.feedDatabase(lithology, organizedSample);

            //AND GET THE PATH OF LAS AND XML TO IDENTIFY IN THE OUTPUT
            String lasPath = discoverLithology.getParsedLAS().getFullPath();
            String xml = discoverLithology.getXmlPath();
            organizedSample.add(lasPath);
            organizedSample.add(xml);
            organizedSample.add(discoverLithology.getLithologyName());
            //System.out.println("NAMO:" + discoverLithology.getLithologyName() );
            organizedSample.add(Integer.toString(lithology));
            organizedSample.add(String.valueOf(discoverLithology.getGrainSizeID()));
            organizedSample.add(String.valueOf(discoverLithology.getRoundnessID()));
            organizedSample.add(String.valueOf(discoverLithology.getSphericityID()));

            //if(lithology >= -1){//WITH UNUSED SAMPLES
            if (lithology != -1) {//WITHOUT UNUSED SAMPLES;
                //IF EXISTS JUST ADD THE SAMPLE

                archive.add(
                        parsed.getWellName(),
                        organizedSample);
            }
        }

    }

}