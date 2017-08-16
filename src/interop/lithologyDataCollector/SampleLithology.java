package interop.lithologyDataCollector;

import interop.lithoprototype.model.LithologyDatabase;
import interop.log.model.LASList;
import interop.log.model.ParsedLAS;
import interop.stratigraphic.model.DepositionalFacies;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Eduardo Bassani
 */

public class SampleLithology {

    private List<LogTypeAlias> logTypesWanted;
    private LithologyDatabase db;
    private LithologyArchiveFormat archive;

    public SampleLithology(List<LogTypeAlias> types) {
        this.logTypesWanted = types;
    }

    public SampleLithology() {
        this(Arrays.asList(LogTypeAlias.DEPT,
                LogTypeAlias.DT,   // DTC
                LogTypeAlias.GR,
                LogTypeAlias.ILD,
                LogTypeAlias.NPHI, // NEUT
                LogTypeAlias.RHOB,
                LogTypeAlias.DRHO,
                LogTypeAlias.CALI,
                LogTypeAlias.SP,
                LogTypeAlias.SN,
                LogTypeAlias.MSFL
        ));
    }

    public void run(LASList lasList, String path) {
        db = new LithologyDatabase(LogTypeAlias.toStringList(logTypesWanted));
        archive = new LithologyArchiveFormat(path, LogTypeAlias.toStringList(logTypesWanted));

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

    public void processWell(ParsedLAS parsedLAS, List<String> pathDescriptions) {

        System.out.print("Processing Well");
        DiscoverLithology discoverLithology = new DiscoverLithology(pathDescriptions);
        OrganizeSample organizer = new OrganizeSample(parsedLAS, logTypesWanted);

        //FOR EVERY SAMPLE IN THE LAS FILE
        for (int i = 0; i < parsedLAS.getLogsList().get(0).getLogValues().size(); i++) {
            if((10*i)/parsedLAS.getLogsList().get(0).getLogValues().size() > (10*(i-1))/parsedLAS.getLogsList().get(0).getLogValues().size())
                System.out.print(".");
            // Get depth + organized samples from a specific index
            List<String> organizedSample = organizer.getOrganizedSample(i);
            organizedSample.add(parsedLAS.getFullPath());

            // Searches for the lithology
            DiscoverLithology.Result result = discoverLithology.discover(Float.valueOf(organizedSample.get(0)));


            if (result == null) {

                organizedSample.add("null");
                organizedSample.add("null");
                organizedSample.add("0");
                organizedSample.add("0");
                organizedSample.add("0");
                organizedSample.add("0");

            } else {
                db.feedDatabase(result.getFacies().getLithology().getId(), organizedSample);

                DepositionalFacies facies = result.getFacies();

                organizedSample.add(result.getDescription().getFilePath());
                organizedSample.add(facies.getLithology().getValue());
                organizedSample.add(String.valueOf(facies.getLithology().getId()));
                organizedSample.add(String.valueOf(facies.getGrainSize() != null ? facies.getGrainSize().getId() : 0));
                organizedSample.add(String.valueOf(facies.getRoundness() != null ? facies.getRoundness().getId() : 0));
                organizedSample.add(String.valueOf(facies.getSphericity() != null ? facies.getSphericity().getId() : 0));
            }

            archive.add(
                    parsedLAS.getWellName(),
                    organizedSample);

        }
        System.out.println();

    }

}