package interop.anp;

import java.util.HashMap;

/**
 * @author Lucas Hagen
 */

public enum ANPLithology {

    CALCAREO(2, 16),
    COQUINA(4, null),
    CALCILUTITO(6, 361),
    CALCISSILTITO(7, 363),
    CALCARENITO(8, 360),
    CALCARENITO_OOLITICO(9, null),
    CALCIRUDITO(10, 362),
    HIBRIDO(11, null),
    DOLOMITA(30, 11),
    BRECHA(40, null),
    CONGLOMERADO(42, 53),
    DIAMICTITO(44, 54),
    TILITO(46, null),
    AREIA(48, 58),
    ARENITO(49, 58),
    SILTITO(54, 61),
    ARGILA(55, 52),
    ARGILITO(56, 52),
    FOLHELHO(57, 59),
    MARGA(58, 17),
    TUFO_VULCANICO(61, null),
    IGNEAS(64, null),
    DIABASIO(65, 317),
    BASALTO(66, 321),
    GRANITO(67, 307),
    METAMORFICAS(70, null),
    GNAISSE(71, 205),
    FILITO(72, 220),
    XISTO(73, 222),
    QUARTZITO(74, 221),
    META_ARENITO(75, 215),
    METASSILTITO(76, 216),
    ARDOSIA(77, 225),
    ULTRABASICA(78, null),
    TAQUIDRITA(81, 359),
    ANIDRITA(82, 28),
    GIPSITA(83, 35),
    SAL_NAO_IDENTIFICADO(84, 365),
    HALITA(85, 36),
    SILVINITA(86, 40),
    CARNALITA(87, 364),
    CARVAO(92, 47),
    SILEXITO(94, null);


    private int anpID;
    private Integer petroID;

    ANPLithology(int anpID, Integer petroID) {
        this.anpID = anpID;
        this.petroID = petroID;
    }

    public int getAnpID() {
        return anpID;
    }

    public Integer getPetroID() {
        return petroID;
    }

    private static HashMap<Integer, ANPLithology> ids;

    static {
        ids = new HashMap<>();
        for(ANPLithology lithology : ANPLithology.values())
            ids.put(lithology.anpID, lithology);
    }

    public static ANPLithology fromID(int id) {
        return ids.get(id);
    }



}
