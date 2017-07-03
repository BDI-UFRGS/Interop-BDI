package interop.lithologyDataCollector;

import interop.framework.Framework;
import interop.lithoprototype.model.LithologyDatabase;
import interop.log.model.LASList;
import interop.log.model.ParsedLAS;
import interop.log.util.LASParser;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Eduardo Bassani
 */
public class SampleLithology {

    private List<String> logTypesWanted;
    private LithologyDatabase db;
    private float nullValue;

    public void run(LASList lasList) {
        logTypesWanted = Arrays.asList("DEPT",
                "DEPT",
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
        );

        db = new LithologyDatabase(logTypesWanted);

        for (ParsedLAS las : lasList)
            processWell(las, las.getXMLPaths());

        LithologyArchiveFormat archive = new LithologyArchiveFormat(Framework.getInstance().getExportPath(), , );

        try {
            archive.initializeWriter();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        archive.saveToArchive();

        archive.closeWriter();
    }

    public void processWell(ParsedLAS las, List<String> pathDescriptions) {

        ParsedLAS parsed = las;
        nullValue = parsed.getNullValue();
        System.out.println();
        System.out.print("Processing Well");
        //FOR EVERY SAMPLE IN THE LAS FILE
        for (int i = 0; i < parsed.getLogsList().get(0).getLogValues().size(); i++) {
            if ((10 * i) / parsed.getLogsList().get(0).getLogValues().size() > (10 * (i - 1)) / parsed.getLogsList().get(0).getLogValues().size())
                System.out.print(".");
            //ORGANIZE SAMPLE IN ORDER OF logsTypeWanted
            List<String> organizedSample = new OrganizeSample(parsed, i).organize();

            //SEARCH THE LITHOLOGY IN THE LIST OF XML, IF IT EXISTS
            DiscoverLithology discoverLithology = new DiscoverLithology(las, i, pathDescriptions);
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
            if (lithology != -1) {//WITHOUT UNUSED SAMPLES

                //SEE IF LITHOLOGY ALREADY EXISTS ON THE LIST lithologies AND GET ITS INDEX
                int lithologiesIndex = indexOfLithologyArchiveFormatList(lithologies, lithology);

                //IF EXISTS JUST ADD THE SAMPLE
                if (lithologiesIndex != -1) {
                    lithologies.get(lithologiesIndex).add(organizedSample);
                }
                //OTHERWISE CREATE THE LITHOLOGY .txt AND ADD THE SAMPLE
                else {
                    LithologyArchiveFormat lit = new LithologyArchiveFormat(lithology, discoverLithology.LithologyName, organizedSample);
                    lithologies.add(lit);
                }
            }
        }

    }

    private int indexOfLithologyArchiveFormatList(List<LithologyArchiveFormat> list, int UID) {

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).lithologyUID == UID) {
                return i;
            }
        }
        return -1;
    }

}