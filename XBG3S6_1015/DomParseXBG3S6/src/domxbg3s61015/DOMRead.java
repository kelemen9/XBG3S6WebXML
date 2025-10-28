package domxbg3s61015;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class DOMRead {

    public static void main(String[] args) {
        try {
            File xmlFile = new File("orarendNeptunkod.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringComments(true);
            factory.setCoalescing(true);
            factory.setNamespaceAware(false);
            factory.setIgnoringElementContentWhitespace(true);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            printDocument(document);

        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void printDocument(Document document) {
        final String indentStr = "   ";
        int indent = 0;
        String ver = document.getXmlVersion() != null ? document.getXmlVersion() : "1.0";
        String enc = document.getXmlEncoding() != null ? document.getXmlEncoding() : "UTF-8";
        System.out.println("<?xml version=\"" + ver + "\" encoding=\"" + enc + "\"?>");

        Element root = document.getDocumentElement();
        System.out.println("<" + root.getNodeName() + ">");
        indent++;

        NodeList lessons = root.getElementsByTagName("ora");
        for (int i = 0; i < lessons.getLength(); i++) {
            Element lesson = (Element) lessons.item(i);
            System.out.print(indentStr.repeat(indent) + "<ora");
            NamedNodeMap attributes = lesson.getAttributes();
            printAttributes(attributes);

            Element subject = firstChild(lesson, "targy");
            indent++;
            if (subject != null)
                System.out.println(indentStr.repeat(indent) + "<targy>" + subject.getTextContent() + "</targy>");

            Element time = firstChild(lesson, "idopont");
            printTime(time, indent, indentStr);

            printSimpleTag(lesson, "helyszin", indent, indentStr);
            printSimpleTag(lesson, "oktato", indent, indentStr);
            printSimpleTag(lesson, "szak", indent, indentStr);

            indent--;
            System.out.println(indentStr.repeat(indent) + "</ora>");
        }

        indent--;
        System.out.println("</" + root.getNodeName() + ">");
    }

    private static void printSimpleTag(Element parent, String tagName, int indent, String indentStr) {
        Element el = firstChild(parent, tagName);
        if (el != null) {
            System.out.println(indentStr.repeat(indent) + "<" + tagName + ">" +
                               el.getTextContent() + "</" + tagName + ">");
        }
    }

    private static void printTime(Element time, int indent, String indentStr) {
        if (time == null) return;
        System.out.println(indentStr.repeat(indent) + "<idopont>");
        Element day = firstChild(time, "nap");
        Element from = firstChild(time, "tol");
        Element to = firstChild(time, "ig");
        indent++;
        if (day != null) System.out.println(indentStr.repeat(indent) + "<nap>" + day.getTextContent() + "</nap>");
        if (from != null) System.out.println(indentStr.repeat(indent) + "<tol>" + from.getTextContent() + "</tol>");
        if (to != null) System.out.println(indentStr.repeat(indent) + "<ig>" + to.getTextContent() + "</ig>");
        indent--;
        System.out.println(indentStr.repeat(indent) + "</idopont>");
    }

    private static void printAttributes(NamedNodeMap attributes) {
        if (attributes == null || attributes.getLength() == 0) {
            System.out.println(">");
        } else {
            System.out.print(" ");
            for (int i = 0; i < attributes.getLength(); i++) {
                Node a = attributes.item(i);
                System.out.print(a.getNodeName() + "=\"" + a.getNodeValue() + "\"");
                if (i != attributes.getLength() - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println(">");
        }
    }

    private static Element firstChild(Element parent, String name) {
        NodeList nl = parent.getElementsByTagName(name);
        if (nl.getLength() > 0 && nl.item(0).getNodeType() == Node.ELEMENT_NODE) {
            return (Element) nl.item(0);
        }
        return null;
    }
}
