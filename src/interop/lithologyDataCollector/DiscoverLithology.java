package interop.lithologyDataCollector;

import interop.log.model.ParsedLAS;
import interop.stratigraphic.model.DepositionalFacies;
import interop.stratigraphic.model.StratigraphicDescription;
import interop.stratigraphic.util.XMLHandler;
import interop.stratigraphic.util.XMLReader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DiscoverLithology {

    private List<String> xmlPaths = new ArrayList<>();
    private ParsedLAS parsedLAS;

    private Iterator<String> xmlIterator;
    private Iterator<StratigraphicDescription> stratIterator;
    private Iterator<DepositionalFacies> faciesIterator;
    private DepositionalFacies facies;


    public DiscoverLithology(ParsedLAS las, List<String> xmls) {
        this.xmlPaths = xmls;
        this.parsedLAS = las;

        xmlIterator = xmlPaths.iterator();

    }

    public DepositionalFacies getFacies(float depth) {
        if (facies == null)
            facies = getNextFacies();

        if(facies == null)
            return null;

        if(facies.getTopMeasure())
    }

    private DepositionalFacies getNextFacies() {
        if(faciesIterator != null && faciesIterator.hasNext()) {
            return faciesIterator.next();
        } else if(stratIterator != null && stratIterator.hasNext()) {
            faciesIterator = stratIterator.next().getFaciesList().iterator();

            return getNextFacies();
        } else if(xmlIterator != null && xmlIterator.hasNext()) {
            stratIterator = XMLReader.readStratigraphicDescriptionXML(xmlIterator.next()).iterator();

            return getNextFacies();
        } else {
            return null;
        }
    }

    public ParsedLAS getParsedLAS() {
        return parsedLAS;
    }


    public Result fast_discover(int index) {
        float depthLAS = parsedLAS.getLogsList().get(0).getLogValues().getPair(index).getDepth();
        //same as the discover() method but with the ideia that each xml file is readed only once, but the results are the same

        if (facie == null) {
            return null;
        }
        if (facie.getBottomMeasure() >= depthLAS && facie.getTopMeasure() < depthLAS) {

            //GET THE LITHOLOGY AND THE XML IT WAS FOUND
            String xmlFound = xmlPaths.get(i);
            String lithologyName = facie.getLithology().getValue();
            String grainSize = facie.getGrainSize().getValue();
            int grainSizeID = facie.getGrainSize().getId();
            int roundnessID = facie.getRoundness().getId();
            int sortingID = facie.getSorting().getId();
            int sphericityID = facie.getSphericity().getId();

            return new Result(xmlFound, lithologyName, grainSize, grainSizeID, roundnessID, sortingID, sphericityID);
        } else if (depthLAS > facie.getBottomMeasure()) {
            facie = this.nextDepositionalFacie();

            if (facie == null)
                return null;

            mmTOm(facie);
            return fast_discover(index);
        }

        return null;
    }

    public Result discover(int index, float depth) {

        //FOR EVERY XML FILE


        for (StratigraphicDescription core : listCore) {
            List<DepositionalFacies> faciesList = core.getFaciesList();
            faciesList = mmTOm(faciesList);

            //FOR EVERY FACIES
            for (DepositionalFacies facie : faciesList) {
                //IF THE DEPTH OF SAMPLE IS IN BETWEEN THE FACIE
                if (facie.getBottomMeasure() >= depthLAS && facie.getTopMeasure() < depthLAS) {
                    //GET THE LITHOLOGY AND THE XML IT WAS FOUND

                    String xml = path;
                    String lithologyName = facie.getLithology().getValue();
                    String grainSize = facie.getGrainSize().getValue();
                    int lithologyID = facie.getLithology().getId();
                    int grainSizeID = facie.getGrainSize().getId();
                    int roundnessID = facie.getRoundness().getId();
                    int sortingID = facie.getSorting().getId();
                    int sphericityID = facie.getSphericity().getId();

                    return new Result(xml, lithologyName, grainSize, lithologyID, grainSizeID, roundnessID, sortingID, sphericityID);
                }
            }
        }


        return null;
    }

    private List<DepositionalFacies> toMeters(List<DepositionalFacies> depositionalFacies) {
        for (DepositionalFacies dp : depositionalFacies)
            toMeters(dp);

        return depositionalFacies;
    }

    private DepositionalFacies toMeters(DepositionalFacies dp) {
        dp.setBottomMeasure(dp.getBottomMeasure() / 1000);
        dp.setTopMeasure(dp.getTopMeasure() / 1000);
        return dp;
    }


    public class Result {

        private String xml;
        private String lithologyName;
        private String grainSize;
        private int lithologyID;
        private int grainSizeID;
        private int roundnessID;
        private int sortingID;
        private int sphericityID;

        protected Result(String xml, String lithologyName, String grainSize, int lithologyID, int grainSizeID, int roundnessID, int sortingID, int sphericityID) {
            this.xml = xml;
            this.lithologyName = lithologyName;
            this.grainSize = grainSize;
            this.lithologyID = lithologyID;
            this.grainSizeID = grainSizeID;
            this.roundnessID = roundnessID;
            this.sortingID = sortingID;
            this.sphericityID = sphericityID;
        }

        public String getXml() {
            return xml;
        }

        public String getLithologyName() {
            return lithologyName;
        }

        public String getGrainSize() {
            return grainSize;
        }

        public int getLithologyID() {
            return lithologyID;
        }

        public int getGrainSizeID() {
            return grainSizeID;
        }

        public int getRoundnessID() {
            return roundnessID;
        }

        public int getSortingID() {
            return sortingID;
        }

        public int getSphericityID() {
            return sphericityID;
        }
    }
}