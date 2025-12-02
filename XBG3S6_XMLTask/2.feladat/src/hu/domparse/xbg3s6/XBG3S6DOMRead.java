package hu.domparse.xbg3s6;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;
import java.util.StringJoiner;

public class XBG3S6DOMRead {

    public static void main(String[] args) {
    ReadXMLDocument("XBG3S6_XMLTask/src/hu/domparse/xbg3s6/XBG3S6XML.xml");
    } 

    //XML beolvasása
    public static void ReadXMLDocument(String filePath) {
        try {
            //fájlbeolvasás
            File xmlFile = new File(filePath);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            //példányosítás
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            //kiírás konzolra és fájlba
            printDocument(doc);

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    //dokumentum kiírása és mentése
    private static void printDocument(Document doc) {
        PrintWriter writer = null;
        try {
            //mentés fájlba
            File outputFile = new File("XBG3S6XML_output.xml");
            writer = new PrintWriter(new FileWriter(outputFile, false));

            //deklaráció kiírása
            System.out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");

            //gyökérelem
            Element rootElement = doc.getDocumentElement();
            String rootName = rootElement.getTagName();

            //gyökérelem attribútumai
            StringJoiner rootAttributes = new StringJoiner(" ");
            NamedNodeMap rootAttributeMap = rootElement.getAttributes();
            for (int i = 0; i < rootAttributeMap.getLength(); i++) {
                Node attribute = rootAttributeMap.item(i);
                rootAttributes.add(attribute.getNodeName() + "=\""
                        + attribute.getNodeValue() + "\"");
            }

            System.out.print("<" + rootName + " " + rootAttributes.toString() + ">\n");
            writer.print("<" + rootName + " " + rootAttributes.toString() + ">\n");

            //gyökérelem alatti gyermek elem 
            NodeList children = rootElement.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                printNode(children.item(i), 1, writer);
            }

            System.out.println("</" + rootName + ">");
            writer.println("</" + rootName + ">");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    //node 
    private static void printNode(Node node, int indent, PrintWriter writer) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;

            String nodeName = element.getTagName();
            StringJoiner attributes = new StringJoiner(" ");
            NamedNodeMap attributeMap = element.getAttributes();

            //Attribútumok
            for (int i = 0; i < attributeMap.getLength(); i++) {
                Node attribute = attributeMap.item(i);
                attributes.add(attribute.getNodeName() + "=\""
                        + attribute.getNodeValue() + "\"");
            }

            //nyitó tag
            System.out.print(getIndentString(indent));
            writer.print(getIndentString(indent));

            if (attributes.length() > 0) {
                System.out.print("<" + nodeName + " " + attributes.toString() + ">");
                writer.print("<" + nodeName + " " + attributes.toString() + ">");
            } else {
                System.out.print("<" + nodeName + ">");
                writer.print("<" + nodeName + ">");
            }

            NodeList children = element.getChildNodes();

            if (children.getLength() == 1 && children.item(0).getNodeType() == Node.TEXT_NODE) {
                System.out.print(children.item(0).getNodeValue());
                writer.print(children.item(0).getNodeValue());
                System.out.println("</" + nodeName + ">");
                writer.println("</" + nodeName + ">");
            } else {
                System.out.println();
                writer.println();

                //Gyermek elemek rekurzív bejárása
                for (int i = 0; i < children.getLength(); i++) {
                    printNode(children.item(i), indent + 1, writer);
                }

                //Záró tag
                System.out.print(getIndentString(indent));
                writer.print(getIndentString(indent));
                System.out.println("</" + nodeName + ">");
                writer.println("</" + nodeName + ">");
            }
        }
    }

    //behúzás szóközökkel
    private static String getIndentString(int indent) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            sb.append("    "); 
        }
        return sb.toString();
    }
}