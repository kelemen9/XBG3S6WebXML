package domxbg3s61105;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class DOMModify1XBG3S6 {
    public static void main(String[] args) {
        try {
            File input = new File("orarendXBG3S6.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringComments(false);
            factory.setNamespaceAware(false);
            factory.setValidating(false);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(input);
            doc.getDocumentElement().normalize();

            NodeList oras = doc.getElementsByTagName("ora");
            if (oras.getLength() > 0) {
                Element firstOra = (Element) oras.item(0);
                Element oraado = doc.createElement("oraado");
                oraado.setTextContent("Dr. XY");
                firstOra.appendChild(oraado);
            }

            //orarendModify1XBG3S6.xml -konzolba, és fájlba mentés
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            System.out.println("---- 1) Óraadó hozzáadva, módosított XML (konzol) ----");
            transformer.transform(new DOMSource(doc), new StreamResult(System.out));

            File outFile = new File("orarendModify1XBG3S6.xml");
            transformer.transform(new DOMSource(doc), new StreamResult(outFile));
            System.out.println("\nMódosított fájl elmentve: " + outFile.getAbsolutePath());

            //gyakorlat módosítása előadásra
            for (int i = 0; i < oras.getLength(); i++) {
                Node n = oras.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    Element oraElem = (Element) n;
                    String tipus = oraElem.getAttribute("tipus");
                    if ("gyakorlat".equalsIgnoreCase(tipus)) {
                        oraElem.setAttribute("tipus", "előadás");
                    }
                }
            }

            //blokk forma kiírás
            System.out.println("\n---- 2) Teljes fastruktúra (minden tipus: gyakorlat -> előadás) ----");
            printNode(doc, 0);

        } catch (ParserConfigurationException | SAXException | IOException | RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printNode(Node node, int indent) {
        StringBuilder pad = new StringBuilder();
        for (int i = 0; i < indent; i++) pad.append("    ");
        String prefix = pad.toString();

        switch (node.getNodeType()) {
            case Node.DOCUMENT_NODE:
                System.out.println(prefix + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                NodeList childrenDoc = node.getChildNodes();
                for (int i = 0; i < childrenDoc.getLength(); i++) printNode(childrenDoc.item(i), indent);
                break;

            case Node.ELEMENT_NODE:
                Element elem = (Element) node;
                StringBuilder startTag = new StringBuilder();
                startTag.append(prefix).append("<").append(elem.getNodeName());

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

            case Node.TEXT_NODE:
                String text = node.getTextContent().trim();
                if (!text.isEmpty()) System.out.println(prefix + text);
                break;

            case Node.COMMENT_NODE:
                System.out.println(prefix + "<!-- " + node.getNodeValue() + " -->");
                break;

            default:
                System.out.println(prefix + node.getNodeName() + ": " + node.getNodeValue());
                break;
        }
    }
}