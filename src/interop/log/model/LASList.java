package interop.log.model;

import java.util.ArrayList;

/**
 * @author Lucas Hagen
 */

public class LASList extends ArrayList<ParsedLAS> {

    public ParsedLAS getLAS(String name) {
        for(ParsedLAS las : this)
            if(las.getWellName().equalsIgnoreCase(name))
                return las;
        return null;
    }

    public void removeLAS(String name) {
        ParsedLAS las = getLAS(name);

        if(las != null)
            remove(las);
    }

    public boolean containsLAS(String name) {
        return getLAS(name) != null;
    }

}
