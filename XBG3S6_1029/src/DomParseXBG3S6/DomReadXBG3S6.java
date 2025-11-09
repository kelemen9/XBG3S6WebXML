package domxbg3s61029;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class DomReadXBG3S6 {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        File input = new File("hallgatoXBG3S6.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(input);
        doc.getDocumentElement().normalize();

        System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
        System.out.println("-------------------------------------------");
        printBlock(doc.getDocumentElement(), 0);
    }

    private static void printBlock(Node node, int indent) {
        if (node.getNodeType() != Node.ELEMENT_NODE) return;
        Element elem = (Element) node;
        String ind = " ".repeat(Math.max(0, indent));

        NamedNodeMap attrs = elem.getAttributes();
        StringBuilder aStr = new StringBuilder();
        for (int i = 0; i < attrs.getLength(); i++) {
            Node a = attrs.item(i);
            if (i > 0) aStr.append(" ");
            aStr.append(a.getNodeName()).append("=\"").append(a.getNodeValue()).append("\"");
        }
        System.out.println(ind + elem.getNodeName() + (aStr.length() > 0 ? " " + aStr.toString() : ""));

        if ("hallgato".equals(elem.getNodeName())) {
            String id = elem.getAttribute("id");
            String keresztnev = firstNonEmpty(getTagValue(elem, "keresztnev"), getTagValue(elem, "firstname"));
            String vezeteknev = firstNonEmpty(getTagValue(elem, "vezeteknev"), getTagValue(elem, "lastname"));
            String foglalkozas = firstNonEmpty(getTagValue(elem, "foglalkozas"), getTagValue(elem, "profession"));

            System.out.println(ind + "  Hallgató id: " + id);
            if (!keresztnev.isEmpty()) System.out.println(ind + "  Keresztnév: " + keresztnev);
            if (!vezeteknev.isEmpty()) System.out.println(ind + "  Vezetéknév: " + vezeteknev);
            if (!foglalkozas.isEmpty()) System.out.println(ind + "  Foglalkozás: " + foglalkozas);
        } else {
            NodeList children = elem.getChildNodes();
            boolean hasElementChild = false;
            for (int i = 0; i < children.getLength(); i++) {
                if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    hasElementChild = true;
                    break;
                }
            }
            if (!hasElementChild) {
                String text = elem.getTextContent();
                if (text != null) {
                    text = text.trim();
                    if (!text.isEmpty()) System.out.println(ind + "  " + text);
                }
            } else {
                for (int i = 0; i < children.getLength(); i++) {
                    Node c = children.item(i);
                    if (c.getNodeType() == Node.ELEMENT_NODE) {
                        printBlock(c, indent + 2);
                    } else if (c.getNodeType() == Node.TEXT_NODE) {
                        String t = c.getTextContent().trim();
                        if (!t.isEmpty()) System.out.println(ind + "  " + t);
                    }
                }
            }
        }

        System.out.println(ind + "-------------------------------------------");
    }

    private static String getTagValue(Element parent, String tagName) {
        NodeList nl = parent.getElementsByTagName(tagName);
        if (nl != null && nl.getLength() > 0) {
            Node n = nl.item(0);
            if (n != null) return n.getTextContent().trim();
        }
        return "";
    }

    private static String firstNonEmpty(String... vals) {
        for (String s : vals) if (s != null && !s.isEmpty()) return s;
        return "";
    }
}
