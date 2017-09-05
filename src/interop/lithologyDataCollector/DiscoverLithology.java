package interop.lithologyDataCollector;

import interop.stratigraphic.model.DepositionalFacies;
import interop.stratigraphic.model.StratigraphicDescription;
import interop.stratigraphic.util.XMLReader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

/**
 * @author Lucas Hagen
 */
public class DiscoverLithology {

    private List<String> xmlPaths = new ArrayList<>();

    private Iterator<StratigraphicDescription> descriptionsIterator;
    private Iterator<DepositionalFacies> faciesIterator;

    private StratigraphicDescription currentDescription;
    private DepositionalFacies facies;

    public DiscoverLithology(List<String> xmls) {
        this.xmlPaths = xmls;

        restartFacies();
    }

    /**
     * Restart all iterators and loads all xml files to memory.
     */
    public void restartFacies() {
        TreeSet<StratigraphicDescription> descriptions = new TreeSet<>();

        for(String path : xmlPaths) {
            descriptions.addAll(XMLReader.readStratigraphicDescriptionXML(path));
        }

        descriptionsIterator = descriptions.iterator();
        facies = getNextFacies();
    }

    /**
     * Gets next facies if needed, or returns the current facies.
     *
     * @param depth Depth to get lithology, must be increasing.
     * @return
     */
    private DepositionalFacies getNextFacies(float depth) {
        if (facies == null)
            return null;

        int pos;
        do {
            pos = isIn(depth, facies);

            if (pos == 0) {
                return facies;
            } else if (pos == 1) {
                facies = getNextFacies();
            }
        } while (pos == 1 && facies != null);

        return null;
    }

    private DepositionalFacies getNextFacies() {
        if (faciesIterator != null && faciesIterator.hasNext()) {
            return faciesIterator.next();
        } else if (descriptionsIterator != null && descriptionsIterator.hasNext()) {
            currentDescription = descriptionsIterator.next();
            faciesIterator = currentDescription.getFaciesList().iterator();

            return getNextFacies();
        } else {
            return null;
        }
    }

    public Result discover(float depth) {
        DepositionalFacies currentFacies = getNextFacies(depth);

        return currentFacies == null ? null : new Result(currentDescription, currentFacies);
    }

    private int isIn(float depth, DepositionalFacies facies) {
        if ((depth * 1000) < facies.getTopMeasure()) {
            return -1;
        } else if (facies.getBottomMeasure() < (depth * 1000)) {
            return 1;
        }
        return 0;
    }


    public class Result {

        private StratigraphicDescription description;
        private DepositionalFacies facies;

        protected Result(StratigraphicDescription description, DepositionalFacies facies) {
            this.description = description;
            this.facies = facies;
        }

        public StratigraphicDescription getDescription() {
            return description;
        }

        public DepositionalFacies getFacies() {
            return facies;
        }
    }
}