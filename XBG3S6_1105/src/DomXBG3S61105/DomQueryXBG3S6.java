package domxbg3s61105;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class DOMQueryXBG3S6 {
    public static void main(String[] args) {
        File xmlFile = new File("hallgatoXBG3S6.xml");
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringComments(false);
            factory.setNamespaceAware(false);
            factory.setValidating(false);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            System.out.println("Gyökér elem: " + doc.getDocumentElement().getNodeName());
            System.out.println("-------------------------------------------");

            NodeList hallgatok = doc.getElementsByTagName("hallgato");
            for (int i = 0; i < hallgatok.getLength(); i++) {
                Node n = hallgatok.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    Element h = (Element) n;
                    String id = h.hasAttribute("id") ? h.getAttribute("id") : "(no id)";
                    String vezeteknev = getChildText(h, "vezeteknev");
                    System.out.println("Aktuális elem: hallgato");
                    System.out.println("  id: " + id);
                    System.out.println("  vezeteknev: " + (vezeteknev.isEmpty() ? "(nincs adat)" : vezeteknev));
                    System.out.println("-------------------------------------------");
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.err.println("Hiba az XML feldolgozásakor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String getChildText(Element parent, String tagName) {
        NodeList nl = parent.getElementsByTagName(tagName);
        if (nl == null || nl.getLength() == 0) return "";
        Node n = nl.item(0);
        if (n == null) return "";
        return n.getTextContent().trim();
    }
}
