package interop.stratigraphic.xml;

import interop.stratigraphic.xml.exceptions.WrongFormatException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * @author Lucas Hagen
 */

public class ParsedXML {

    public static void main(String[] args) {
        ParsedXML xml = null;
        try {
            xml = new ParsedXML("C:\\Users\\lucas\\Documents\\PocosVinicius\\9-CP-0674-SE\\9_CP_0674_SE_20161003211554.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(xml == null)
            return;

        xml.test();
    }




    public static String DEFAULT_ROOT = "StrataledgeExport";
    public static String WELL_DESCRIPTION = "WellDescription";

    private Document doc;

    public ParsedXML(String fullPath) throws ParserConfigurationException, IOException, SAXException, WrongFormatException {
        File fXmlFile = new File(fullPath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();

        if(!doc.getDocumentElement().getNodeName().equals(DEFAULT_ROOT))
            throw new WrongFormatException("\"" + DEFAULT_ROOT + "\" expected as root tag, found: \"" + doc.getDocumentElement().getNodeName() + "\"!");
    }

    public void test() {
        NodeList descriptions = doc.getElementsByTagName(WELL_DESCRIPTION);
        for(int i = 0; i < descriptions.getLength(); i++) {
            Node d = descriptions.item(i);

            System.out.println(d.getNodeName());
        }
    }
}
