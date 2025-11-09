package domxbg3s61029;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class DOMReadXBG3S61 {
    public static void main(String[] args) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringComments(false);
            factory.setNamespaceAware(false);
            factory.setValidating(false);

            DocumentBuilder builder = factory.newDocumentBuilder();
            File xmlFile = new File("orarendXBG3S6.xml");
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            System.out.println("=== Fastruktúra (orarendXBG3S6.xml) ===");
            printNode(doc, 0);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void printNode(Node node, int indent) {
        StringBuilder pad = new StringBuilder();
        for (int i = 0; i < indent; i++) pad.append("    ");

        switch (node.getNodeType()) {
            case Node.DOCUMENT_NODE: {
                System.out.println(pad + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                NodeList children = node.getChildNodes();
                for (int i = 0; i < children.getLength(); i++) {
                    printNode(children.item(i), indent);
                }
                break;
            }

            case Node.ELEMENT_NODE: {
                Element elem = (Element) node;
                StringBuilder startTag = new StringBuilder();
                startTag.append(pad).append("<").append(elem.getNodeName());

                NamedNodeMap attrs = elem.getAttributes();
                for (int i = 0; i < attrs.getLength(); i++) {
                    Node a = attrs.item(i);
                    startTag.append(" ").append(a.getNodeName()).append("=\"").append(a.getNodeValue()).append("\"");
                }
                startTag.append(">");

                System.out.println(startTag.toString());

                NodeList children = elem.getChildNodes();
                boolean onlyText = true;
                for (int i = 0; i < children.getLength(); i++) {
                    if (children.item(i).getNodeType() != Node.TEXT_NODE) {
                        onlyText = false;
                        break;
                    }
                }

                if (onlyText) {
                    String text = elem.getTextContent().trim();
                    if (!text.isEmpty()) System.out.println(pad + "    " + text);
                } else {
                    for (int i = 0; i < children.getLength(); i++) {
                        Node ch = children.item(i);
                        if (ch.getNodeType() == Node.TEXT_NODE) {
                            String text = ch.getNodeValue().trim();
                            if (!text.isEmpty()) System.out.println(pad + "    " + text);
                        } else {
                            printNode(ch, indent + 1);
                        }
                    }
                }

                System.out.println(pad + "</" + elem.getNodeName() + ">");
                break;
            }

            case Node.TEXT_NODE: {
                String text = node.getNodeValue().trim();
                if (!text.isEmpty()) System.out.println(pad + text);
                break;
            }

            case Node.COMMENT_NODE: {
                System.out.println(pad + "<!-- " + node.getNodeValue() + " -->");
                break;
            }

            default: {
                System.out.println(pad + node.getNodeName() + ": " + node.getNodeValue());
                break;
            }
        }
    }
}
