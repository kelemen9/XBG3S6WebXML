package domxbg3s61029;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class DOMWriteXBG3S61 {
    public static void main(String[] args) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringComments(false);
            factory.setNamespaceAware(false);
            factory.setValidating(false);

            DocumentBuilder builder = factory.newDocumentBuilder();
            File input = new File("orarendXBG3S6.xml");
            Document doc = builder.parse(input);
            doc.getDocumentElement().normalize();

            System.out.println("=== Fastruktúra (orarendXBG3S6.xml) ===");
            printNode(doc, 0);

            File output = new File("orarend1XBG3S6.xml");
            writeDocumentToFile(doc, output);
            System.out.println("\nA dokumentum ki lett írva: " + output.getName());
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void printNode(Node node, int indent) {
        StringBuilder pad = new StringBuilder();
        for (int i = 0; i < indent; i++) pad.append("    ");
        String prefix = pad.toString();

        switch (node.getNodeType()) {
            case Node.DOCUMENT_NODE: {
                System.out.println(prefix + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                NodeList children = node.getChildNodes();
                for (int i = 0; i < children.getLength(); i++) printNode(children.item(i), indent);
                break;
            }
            case Node.ELEMENT_NODE: {
                Element elem = (Element) node;
                StringBuilder startTag = new StringBuilder();
                startTag.append(prefix).append("<").append(elem.getNodeName());

                NamedNodeMap attrs = elem.getAttributes();
                for (int i = 0; i < attrs.getLength(); i++) {
                    Node a = attrs.item(i);
                    startTag.append(" ").append(a.getNodeName()).append("=\"").append(a.getNodeValue()).append("\"");
                }
                startTag.append(">");
                System.out.println(startTag);

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
                    if (!text.isEmpty()) System.out.println(prefix + "    " + text);
                } else {
                    for (int i = 0; i < children.getLength(); i++) {
                        Node ch = children.item(i);
                        if (ch.getNodeType() == Node.TEXT_NODE) {
                            String text = ch.getTextContent().trim();
                            if (!text.isEmpty()) System.out.println(prefix + "    " + text);
                        } else {
                            printNode(ch, indent + 1);
                        }
                    }
                }

                System.out.println(prefix + "</" + elem.getNodeName() + ">");
                break;
            }
            case Node.TEXT_NODE: {
                String text = node.getTextContent().trim();
                if (!text.isEmpty()) System.out.println(prefix + text);
                break;
            }
            case Node.COMMENT_NODE: {
                System.out.println(prefix + "<!-- " + node.getNodeValue() + " -->");
                break;
            }
            default: {
                System.out.println(prefix + node.getNodeName() + ": " + node.getNodeValue());
                break;
            }
        }
    }

    private static void writeDocumentToFile(Document doc, File outFile) {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            DOMSource source = new DOMSource(doc);
            StreamResult console = new StreamResult(System.out);
            StreamResult file = new StreamResult(outFile);

            transformer.transform(source, console);
            transformer.transform(source, file);
        } catch (Exception e) {
            System.err.println("Nem sikerült kiírni: " + outFile.getName());
            e.printStackTrace();
        }
    }
}
