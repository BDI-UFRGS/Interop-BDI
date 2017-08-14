package interop.log.util;

import java.util.HashMap;

/**
 * @author Lucas Hagen
 */

public enum LASSection {

    VERSION,
    WELL,
    CURVE,
    PARAMETER,
    OTHER,
    ASCII;

    public Character getChar() {
        return this.toString().charAt(0);
    }

    // STATIC METHODS

    private static HashMap<Character, LASSection> byChar;

    static {
        byChar = new HashMap<>();

        for(LASSection section : values())
            byChar.put(section.toString().charAt(0), section);
    }

    public static LASSection fromString(String s) {
        return byChar.getOrDefault(s.toUpperCase().charAt(0), OTHER);
    }

}
