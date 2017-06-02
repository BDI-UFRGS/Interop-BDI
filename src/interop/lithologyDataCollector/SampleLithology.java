/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interop.lithologyDataCollector;

import interop.framework.Framework;
import interop.lithoprototype.model.LithologyDatabase;
import interop.log.model.LASList;
import interop.log.model.ParsedLAS;
import interop.log.model.WellLog;
import interop.log.util.LASParser;
import interop.stratigraphic.model.DepositionalFacies;
import interop.stratigraphic.model.StratigraphicDescription;
import interop.stratigraphic.util.XMLReader;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author eduardo
 */
public class SampleLithology {

    static List<String> logTypesWanted;
    private static String TAB = "\t";
    private static List<LithologyArchiveFormat> lithologies = new ArrayList<>();
    private static LithologyDatabase db;
    public static float nullValue;

    public static void run(LASList lasList) {
        //SELECIONA QUAIS TIPOS DE LOG QUERES PROCESSAR
        logTypesWanted = new ArrayList<>();
        logTypesWanted.add("DEPT");
        logTypesWanted.add("DT");
        logTypesWanted.add("GR");
        logTypesWanted.add("ILD");
        logTypesWanted.add("NPHI");
        logTypesWanted.add("RHOB");
        logTypesWanted.add("DRHO");
        logTypesWanted.add("CALI");
        logTypesWanted.add("SP");
        logTypesWanted.add("SN");
        logTypesWanted.add("MSFL");

        db = new LithologyDatabase(logTypesWanted);

        for(ParsedLAS las : lasList)
            SampleLithology.processWell(las, las.getXMLPaths());

        //COMENTAR PARA GRAVAR EM ARQUIVO .TXT
        LithologyArchiveFormat.initializeWriter();
        for (LithologyArchiveFormat laf : lithologies) {
            laf.saveToArchive();
        }
        LithologyArchiveFormat.closeWriter();


        //return db;

    }

    public static List<Double> getSample(int index, String pathLog) {
        LASParser parser = new LASParser();
        ParsedLAS parsed = parser.parseLAS(pathLog);
        List<String> organized = new OrganizeSample(parsed, index).Organize();
        return parseDouble(organized);
    }

    public static List<Double> getSample(int index, ParsedLAS parsedlas) {
        List<String> organized = new OrganizeSample(parsedlas, index).Organize();
        return parseDouble(organized);
    }

    private static List<Double> parseDouble(List<String> strings) {
        List<Double> doubles = new ArrayList<>();
        for (String string : strings) {
            doubles.add(Double.parseDouble(string));
        }
        return doubles;
    }

    static void processWell(ParsedLAS las, List<String> pathDescriptions) {

        ParsedLAS parsed = las;
        nullValue = parsed.getNullValue();
        System.out.println();
        System.out.print("Processing Well");
        //FOR EVERY SAMPLE IN THE LAS FILE
        for (int i = 0; i < parsed.getLogsList().get(0).getLogValues().size(); i++) {
            if ((10 * i) / parsed.getLogsList().get(0).getLogValues().size() > (10 * (i - 1)) / parsed.getLogsList().get(0).getLogValues().size())
                System.out.print(".");
            //ORGANIZE SAMPLE IN ORDER OF logsTypeWanted
            List<String> organizedSample = new OrganizeSample(parsed, i).Organize();

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
            organizedSample.add(String.valueOf(discoverLithology.grainSizeID));
            organizedSample.add(String.valueOf(discoverLithology.roundnessID));
            organizedSample.add(String.valueOf(discoverLithology.sphericityID));

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

    static int indexOfLithologyArchiveFormatList(List<LithologyArchiveFormat> list, int UID) {

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).lithologyUID == UID) {
                return i;
            }
        }
        return -1;
    }

    public static int getNumberOfSamples(String las) {
        LASParser parser = new LASParser();
        ParsedLAS parsed = parser.parseLAS(las);
        return parsed.getLogsList().get(0).getLogValues().size();
    }


    static class DiscoverLithology {

        List<String> pathDescriptions = new ArrayList<>();
        float depthLAS;
        ParsedLAS las;
        String xmlFound;
        String LithologyName;
        String grainSize;
        int grainSizeID;
        int roundnessID;
        int sortingID;
        int sphericityID;

        //testing the fast_discover
        static List<StratigraphicDescription> stratigraphicDescriptions = null;
        static StratigraphicDescription stratigraphicDescription;
        static DepositionalFacies facie;
        static int i = 0; //indexOfWhichXmlFileWeAreLooking;
        static int j = 0;//indexOfStratigraphicDescriptionList;
        static int k = 0;//indexOfDepositionalFaciesList;


        public DiscoverLithology(ParsedLAS las, int index, List<String> path) {
            this.pathDescriptions = path;
            this.depthLAS = las.getLogsList().get(0).getLogValues().getPair(index).getDepth();
            this.las = las;

            if (stratigraphicDescriptions == null) {
                stratigraphicDescriptions = XMLReader.readStratigraphicDescriptionXML(path.get(0));
                facie = stratigraphicDescriptions.get(0).getFaciesList().get(0);
                mmTOm(facie);
            }
        }


        private DepositionalFacies nextDepositionalFacie() {
            if (k < stratigraphicDescriptions.get(j).getFaciesList().size() - 1)
                return stratigraphicDescriptions.get(j).getFaciesList().get(++k);
            else {
                k = 0;
                if (j < stratigraphicDescriptions.size() - 1)
                    return stratigraphicDescriptions.get(++j).getFaciesList().get(k);
                else {
                    k = 0;
                    j = 0;
                    if (i < pathDescriptions.size() - 1) {
                        stratigraphicDescriptions = XMLReader.readStratigraphicDescriptionXML(pathDescriptions.get(++i));
                        return stratigraphicDescriptions.get(j).getFaciesList().get(k);
                    } else return null;
                }
            }
        }

        public ParsedLAS getParsedLAS() {
            return las;
        }

        public String getXmlPath() {
            return xmlFound;
        }

        public String getLithologyName() {
            return this.LithologyName;
        }


        public int fast_discover()
        //same as the discover() method but with the ideia that each xml file is readed only once, but the results are the same
        {
            if (facie == null)
                return 0;
            if (facie.getBottomMeasure() >= depthLAS && facie.getTopMeasure() < depthLAS) {
                //GET THE LITHOLOGY AND THE XML IT WAS FOUND
                xmlFound = pathDescriptions.get(i);
                LithologyName = facie.getLithology().getValue();
                grainSize = facie.getGrainSize().getValue();
                grainSizeID = facie.getGrainSize().getId();
                roundnessID = facie.getRoundness().getId();
                sortingID = facie.getSorting().getId();
                sphericityID = facie.getSphericity().getId();
                return facie.getLithology().getId();
            } else if (depthLAS > facie.getBottomMeasure()) {
                facie = this.nextDepositionalFacie();
                if (facie == null)
                    return 0;
                mmTOm(facie);
                return fast_discover();
            }

            return 0;
        }

        public int discover() {

            //FOR EVERY XML FILE
            for (String path : pathDescriptions) {
                List<StratigraphicDescription> listCore = XMLReader.readStratigraphicDescriptionXML(path);
                //FOR EVERY CORE IN A XML FILE
                for (StratigraphicDescription core : listCore) {
                    List<DepositionalFacies> faciesList = core.getFaciesList();
                    faciesList = mmTOm(faciesList);
                    //FOR EVERY FACIES
                    for (DepositionalFacies facie : faciesList) {
                        //IF THE DEPTH OF SAMPLE IS IN BETWEEN THE FACIE
                        if (facie.getBottomMeasure() >= depthLAS && facie.getTopMeasure() < depthLAS) {
                            //GET THE LITHOLOGY AND THE XML IT WAS FOUND
                            xmlFound = path;
                            LithologyName = facie.getLithology().getValue();
                            grainSize = facie.getGrainSize().getValue();
                            grainSizeID = facie.getGrainSize().getId();
                            roundnessID = facie.getRoundness().getId();
                            sortingID = facie.getSorting().getId();
                            sphericityID = facie.getSphericity().getId();

                            return facie.getLithology().getId();
                        }
                    }
                }
            }
            return 0;
        }

        private static List<DepositionalFacies> mmTOm(List<DepositionalFacies> depositionalFacies) {
            for (DepositionalFacies dp : depositionalFacies) {
                dp.setBottomMeasure(dp.getBottomMeasure() / 1000);
                dp.setTopMeasure(dp.getTopMeasure() / 1000);
            }
            return depositionalFacies;
        }

        private static DepositionalFacies mmTOm(DepositionalFacies dp) {
            dp.setBottomMeasure(dp.getBottomMeasure() / 1000);
            dp.setTopMeasure(dp.getTopMeasure() / 1000);
            return dp;
        }

    }

    static class OrganizeSample {
        ParsedLAS parsed;
        int sampleIndex;

        public OrganizeSample(ParsedLAS psd, int index) {
            this.parsed = psd;
            this.sampleIndex = index;
        }


        public List<String> Organize() {
            List<String> organizedSample = new ArrayList<>();

            for (String type : logTypesWanted) {
                organizedSample.add(Float.toString(getSpecificSampleValue(type)));
            }

            return organizedSample;
        }

        public float getSpecificSampleValue(String type) {
            float nullValue = parsed.getLogsList().get(0).getNullValue();
            float value = nullValue;

            for (WellLog wl : parsed.getLogsList()) {
                if (type.equalsIgnoreCase("DEPT"))
                    value = wl.getLogValues().getPair(sampleIndex).getDepth();
                else if (type.equalsIgnoreCase(wl.getLogType().getLogType())
                        && wl.getLogValues().getPair(sampleIndex).getLogValue() != nullValue)//IN CASE THERE IS MORE THAN ONE LOG OF THE SAME MEASURE KIND
                    value = wl.getLogValues().getPair(sampleIndex).getLogValue();

            }
            return value;
        }

    }

    static class LithologyArchiveFormat {
        static int counter = 0;
        static PrintWriter writer = null;
        int lithologyUID;
        String lithologyName = null;
        List<List<String>> specificLog;

        public LithologyArchiveFormat(int UID, String lithoName, List<String> sample) {
            this.specificLog = new ArrayList<>();
            specificLog.add(sample);
            lithologyUID = UID;
        }

        public static void initializeWriter() {
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
            if(writer == null)
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
}