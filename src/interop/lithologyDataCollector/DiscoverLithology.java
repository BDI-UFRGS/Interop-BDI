package interop.lithologyDataCollector;

import interop.log.model.ParsedLAS;
import interop.stratigraphic.model.DepositionalFacies;
import interop.stratigraphic.model.StratigraphicDescription;
import interop.stratigraphic.util.XMLReader;

import java.util.ArrayList;
import java.util.List;

public class DiscoverLithology {

    private List<String> pathDescriptions = new ArrayList<>();
    private float depthLAS;
    private ParsedLAS las;
    private String xmlFound;
    private String LithologyName;
    private String grainSize;
    private int grainSizeID;
    private int roundnessID;
    private int sortingID;
    private int sphericityID;

    //testing the fast_discover
    private List<StratigraphicDescription> stratigraphicDescriptions = null;
    private StratigraphicDescription stratigraphicDescription;
    private DepositionalFacies facie;
    private int i = 0; //indexOfWhichXmlFileWeAreLooking;
    private int j = 0;//indexOfStratigraphicDescriptionList;
    private int k = 0;//indexOfDepositionalFaciesList;


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

    public int getGrainSizeID() {
        return grainSizeID;
    }

    public int getRoundnessID() {
        return roundnessID;
    }

    public int getSphericityID() {
        return sphericityID;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
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

    private List<DepositionalFacies> mmTOm(List<DepositionalFacies> depositionalFacies) {
        for (DepositionalFacies dp : depositionalFacies) {
            dp.setBottomMeasure(dp.getBottomMeasure() / 1000);
            dp.setTopMeasure(dp.getTopMeasure() / 1000);
        }
        return depositionalFacies;
    }

    private DepositionalFacies mmTOm(DepositionalFacies dp) {
        dp.setBottomMeasure(dp.getBottomMeasure() / 1000);
        dp.setTopMeasure(dp.getTopMeasure() / 1000);
        return dp;
    }

}