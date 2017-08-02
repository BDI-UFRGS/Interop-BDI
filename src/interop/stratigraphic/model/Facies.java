package interop.stratigraphic.model;

import interop.stratigraphic.control.SQLite;

/**
 * @author Lucas Hagen
 */

public class Facies extends DepositionalFacies {

    public Facies(int lithology, float top, float bottom) {
        super();
        getLithology().setId(lithology);
        setTopMeasure(top);
        setBottomMeasure(bottom);
    }

    public void updateInfo() {
        String rockClassID = new SQLite().readValue(AttributeType.Lithology, "NR_ROCK_CLASS_ID", getLithology().getId());

        StratigraphicRockClass rockClass = getRockClass();
        rockClass.setId(Integer.valueOf(rockClassID));
        rockClass.getValue();
    }

}
