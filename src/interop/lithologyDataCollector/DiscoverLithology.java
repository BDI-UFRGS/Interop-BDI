package interop.lithologyDataCollector;

import interop.log.model.ParsedLAS;
import interop.stratigraphic.model.DepositionalFacies;
import interop.stratigraphic.model.StratigraphicDescription;
import interop.stratigraphic.util.XMLReader;

import java.util.ArrayList;
import java.util.List;

public class DiscoverLithology {

    private List<String> xmlPaths = new ArrayList<>();
    private ParsedLAS parsedLAS;


    private String xmlFound;
    private String lithologyName;
    private String grainSize;
    private int grainSizeID;
    private int roundnessID;
    private int sortingID;
    private int sphericityID;

    private List<StratigraphicDescription> stratigraphicDescriptions;
    private StratigraphicDescription stratigraphicDescription;
    private DepositionalFacies facie;
    private int i = 0; //indexOfWhichXmlFileWeAreLooking;
    private int j = 0;//indexOfStratigraphicDescriptionList;
    private int k = 0;//indexOfDepositionalFaciesList;


    public DiscoverLithology(ParsedLAS las, List<String> xmls) {
        this.xmlPaths = xmls;
        this.parsedLAS = las;

        /*
            stratigraphicDescriptions = XMLReader.readStratigraphicDescriptionXML(xmls.get(0));
            facie = stratigraphicDescriptions.get(0).getFaciesList().get(0);
            mmTOm(facie);*/
    }


    private DepositionalFacies nextDepositionalFacie() {
        if (k < stratigraphicDescriptions.get(j).getFaciesList().size() - 1) {
            return stratigraphicDescriptions.get(j).getFaciesList().get(++k);
        } else {
            k = 0;
            if (j < stratigraphicDescriptions.size() - 1) {
                return stratigraphicDescriptions.get(++j).getFaciesList().get(k);
            } else {
                k = 0;
                j = 0;
                if (i < xmlPaths.size() - 1) {
                    stratigraphicDescriptions = XMLReader.readStratigraphicDescriptionXML(xmlPaths.get(++i));
                    return stratigraphicDescriptions.get(j).getFaciesList().get(k);
                } else {
                    return null;
                }
            }
        }
    }

    public ParsedLAS getParsedLAS() {
        return parsedLAS;
    }

    public String getXmlPath() {
        return xmlFound;
    }

    public String getLithologyName() {
        return this.lithologyName;
    }


    public int fast_discover(int index) {
        float depthLAS = parsedLAS.getLogsList().get(0).getLogValues().getPair(index).getDepth();
        //same as the discover() method but with the ideia that each xml file is readed only once, but the results are the same

        if (facie == null) {
            return 0;
        } if (facie.getBottomMeasure() >= depthLAS && facie.getTopMeasure() < depthLAS) {
            //GET THE LITHOLOGY AND THE XML IT WAS FOUND
            xmlFound = xmlPaths.get(i);
            lithologyName = facie.getLithology().getValue();
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
            return fast_discover(index);
        }

        return 0;
    }

    public int discover(int index) {
        float depthLAS = parsedLAS.getLogsList().get(0).getLogValues().getPair(index).getDepth();

        //FOR EVERY XML FILE
        for (String path : xmlPaths) {
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
                        lithologyName = facie.getLithology().getValue();
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

    private List<DepositionalFacies> mmTOm(List<DepositionalFacies> depositionalFacies) {
        for (DepositionalFacies dp : depositionalFacies)
            mmTOm(dp);

        return depositionalFacies;
    }

    private DepositionalFacies mmTOm(DepositionalFacies dp) {
        dp.setBottomMeasure(dp.getBottomMeasure() / 1000);
        dp.setTopMeasure(dp.getTopMeasure() / 1000);
        return dp;
    }

    public int getGrainSizeID() {
        return grainSizeID;
    }

    public int getRoundnessID() {
        return roundnessID;
    }

    public int getSphericityID() {
        return sphericityID;
    }
}