package interop.anp;

import interop.anp.exceptions.WrongFormatException;
import interop.stratigraphic.model.Facies;
import interop.stratigraphic.model.StratigraphicContact;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Lucas Hagen
 */

public class ANPFile {

    private String wellName;
    private Map<Float, Integer> lines;
    private Float top;
    private Float bottom;

    public ANPFile(File file) throws IOException, WrongFormatException {
        lines = new TreeMap<>();
        this.wellName = file.getName().replaceAll("\\.txt$", "");

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        Pattern pattern = Pattern.compile("(\\d+(?:[.]\\d+)?)\t(\\d+)[ \\t]*");
        while ((line = br.readLine()) != null) {
            Matcher m = pattern.matcher(line);

            if(m.find()) {
                lines.put(Float.valueOf(m.group(1)), Integer.valueOf(m.group(2)));

                bottom = Float.valueOf(m.group(1));
                if (lines.size() == 1)
                    top = bottom;
            }
        }

        if(lines.isEmpty())
            throw new WrongFormatException("Wrong ANP format!");
    }

    public void saveToXML(File saveTo) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // Root Element (StrataledgeExport)
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("StrataledgeExport");
        doc.appendChild(rootElement);

        // StrataledgeDescriptions Element
        Element descriptions = doc.createElement("StrataledgeDescriptions");
        rootElement.appendChild(descriptions);

        // WellDescription element
        Element wellDescription = doc.createElement("WellDescription");
        descriptions.appendChild(wellDescription);

        // Well Identification
        Element identification = doc.createElement("WellDescriptionIdentification");
        addItem(identification, "WellDescriptionIdentificationName", wellName);
        addItem(identification, "WellDescriptionIdentificationTopMeasure", getDepthString(top));
        addItem(identification, "WellDescriptionIdentificationBottomMeasure", getDepthString(bottom));
        addItem(identification, "WellDescriptionIdentificationAuthorName", null);
        addItem(identification, "WellDescriptionIdentificationObservation", null);
        addItem(identification, "WellDescriptionIdentificationCreationDate", null);
        addItem(identification, "WellDescriptionIdentificationLastEditionDate", null);
        wellDescription.appendChild(identification);

        // Well Element
        Element well = doc.createElement("Well");
        addItem(well, "WellName", wellName);
        addItem(well, "WellLocation", null);
        addItem(well, "WellId", null);
        addItem(well, "WellState", null);
        addItem(well, "WellBasinName", null);
        addItem(well, "WellAdditionalObs", null);
        addItem(well, "WellDrillingMethod", null);
        addItem(well, "WellResponsibleDriller", null);
        addItem(well, "WellFieldName", null);
        addItem(well, "WellOrientation", null);
        addItem(well, "WellType", null);
        wellDescription.appendChild(well);

        // BoxColumn
        Element boxColumn = doc.createElement("BoxColumn");
        wellDescription.appendChild(boxColumn);

        // FaciesColumn
        Element facies = doc.createElement("FaciesColumn");

        insertUndefinedContact(facies);
        Map.Entry<Float, Integer> previous = null;
        for(Map.Entry<Float, Integer> f : lines.entrySet()) {
            if(previous != null) {
                //System.out.println(f.getKey() + " -> " + f.getValue());
                facies.appendChild(getFacies(previous.getKey(), f.getKey(), previous.getValue(), doc));
                insertUndefinedContact(facies);
            }
            previous = f;
        }

        wellDescription.appendChild(facies);

        // write the content into xml file
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        Result output = new StreamResult(saveTo);
        Source input = new DOMSource(doc);

        transformer.transform(input, output);
    }

    private Element addItem(Element parent, String tag, String value) {
        Element element = parent.getOwnerDocument().createElement(tag);
        if(value != null)
            element.appendChild(parent.getOwnerDocument().createTextNode(value));
        parent.appendChild(element);
        return element;
    }

    private Element getFacies(float topMeasure, float bottomMeasure, int lithologyCode, Document doc) {
        ANPLithology lit = ANPLithology.fromID(lithologyCode);
        Element element = doc.createElement("Facies");

        addItem(element, "FaciesTopMeasure", getDepthString(topMeasure));
        addItem(element, "FaciesBottomMeasure", getDepthString(bottomMeasure));

        if(lit == null || lit.getPetroID() == null) {
            element.setAttribute("FaciesEmpty", "true");
        } else {
            Facies f = new Facies(lit.getPetroID(), topMeasure, bottomMeasure);
            f.updateInfo();

            element.setAttribute("FaciesEmpty", "false");
            addItem(element, "FaciesRockClass", f.getRockClass().getValue());
            addItem(element, "FaciesRockClassUID", Integer.toString(f.getRockClass().getId()));
            addItem(element, "GrainSize", null);
            addItem(element, "GrainSizeUID", null);
            addItem(element, "GrainSizePosIndex", null);
            addItem(element, "Lithology", f.getLithology().getValue());
            addItem(element, "LithologyUID", Integer.toString(f.getLithology().getId()));
        }

        return element;
    }

    private String getDepthString(Float depth) {
        return Integer.toString((int) (depth * 1000));
    }

    private Element insertUndefinedContact(Element parent) {
        StratigraphicContact contact = new StratigraphicContact();
        contact.setId(1);

        Element element = parent.getOwnerDocument().createElement("Contact");
        addItem(element, "ContactType", contact.getValue());
        addItem(element, "ContactTypeUID", Integer.toString(contact.getId()));

        parent.appendChild(element);

        return element;
    }

}
