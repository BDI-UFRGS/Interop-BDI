package interop.anp;

import interop.anp.exceptions.WrongFormatException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
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

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        Pattern pattern = Pattern.compile("(\\d+(?:[.]\\d+)?)\t(\\d+)[ \\t]*");
        while ((line = br.readLine()) != null) {
            System.out.print("'" + line + "': ");
            Matcher m = pattern.matcher(line);

            if(m.find()) {
                lines.put(Float.valueOf(m.group(1)), Integer.valueOf(m.group(2)));
            }
        }

        if(lines.isEmpty())
            throw new WrongFormatException("Wrong ANP format!");
    }

    public void saveToXML(String path) throws ParserConfigurationException, TransformerException {

        String VALUE = "";

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // Root Element (StrataledgeExport)
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("StrataledgeExport");
        doc.appendChild(rootElement);

        // StrataledgeDescriptions Element
        Element descriptions = doc.createElement("StrataledgeDescriptions");
        rootElement.appendChild(descriptions);

        // Well Identification
        Element identification = doc.createElement("WellDescriptionIdentification");
        identification.appendChild(getItem(doc, "WellDescriptionIdentificationName", VALUE));
        identification.appendChild(getItem(doc, "WellDescriptionIdentificationTopMeasure", VALUE));
        identification.appendChild(getItem(doc, "WellDescriptionIdentificationBottomMeasure", VALUE));
        identification.appendChild(getItem(doc, "WellDescriptionIdentificationAuthorName", VALUE));
        identification.appendChild(getItem(doc, "WellDescriptionIdentificationObservation", VALUE));
        identification.appendChild(getItem(doc, "WellDescriptionIdentificationCreationDate", VALUE));
        identification.appendChild(getItem(doc, "WellDescriptionIdentificationLastEditionDate", VALUE));
        descriptions.appendChild(identification);

        // Well Element
        Element well = doc.createElement("Well");
        well.appendChild(getItem(doc, "WellName", VALUE));
        well.appendChild(getItem(doc, "WellLocation", VALUE));
        well.appendChild(getItem(doc, "WellId", VALUE));
        well.appendChild(getItem(doc, "WellState", VALUE));
        well.appendChild(getItem(doc, "WellBasinName", VALUE));
        well.appendChild(getItem(doc, "WellAdditionalObs", VALUE));
        well.appendChild(getItem(doc, "WellDrillingMethod", VALUE));
        well.appendChild(getItem(doc, "WellResponsibleDriller", VALUE));
        well.appendChild(getItem(doc, "WellFieldName", VALUE));
        well.appendChild(getItem(doc, "WellOrientation", VALUE));
        well.appendChild(getItem(doc, "WellType", VALUE));
        descriptions.appendChild(well);

        // BoxColumn
        Element boxColumn = doc.createElement("BoxColumn");
        descriptions.appendChild(boxColumn);

        // FaciesColumn
        Element facies = doc.createElement("");

        Map.Entry<Float, Integer> previous = null;
        for(Map.Entry<Float, Integer> f : lines.entrySet()) {
            if(previous != null) {
                facies.appendChild(getFacies(previous.getKey(), f.getKey(), previous.getValue(), doc));



            }
            previous = f;
        }



        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(path));

        transformer.transform(source, result);
    }

    private Element getItem(Document doc, String tag, String value) {
        Element element = doc.createElement(tag);
        element.appendChild(doc.createTextNode(value));
        return element;
    }

    private Element getFacies(float topMeasure, float bottomMeasure, int lithologyCode, Document doc) {
        Element facies = doc.createElement("Facies");

        facies.appendChild(getItem(doc, "FaciesTopMeasure", Integer.toString((int) (topMeasure * 1000))));
        facies.appendChild(getItem(doc, "FaciesBottomMeasure", Integer.toString((int) (bottomMeasure * 1000))));
        facies.appendChild(getItem(doc, "FaciesRockClass", "VALUE"));
        facies.appendChild(getItem(doc, "FaciesRockClassUID", "VALUE"));
        facies.appendChild(getItem(doc, "GrainSize", "VALUE"));
        facies.appendChild(getItem(doc, "GrainSizeUID", "VALUE"));
        facies.appendChild(getItem(doc, "GrainSizePosIndex", "VALUE"));
        facies.appendChild(getItem(doc, "Lithology", "VALUE"));
        facies.appendChild(getItem(doc, "LithologyUID", "VALUE"));




        facies.setAttribute("FaciesEmpty", "false");

        return facies;
    }

}
