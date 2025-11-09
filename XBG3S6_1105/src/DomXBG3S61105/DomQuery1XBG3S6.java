package domxbg3s61105;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class DOMQuery1XBG3S6 {
    public static void main(String[] args) {
        File input = new File("orarendXBG3S6.xml");
        if (!input.exists()) {
            System.err.println("Hiba: az XBG3S6Neptunkod.xml fájl nem található");
            return;
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringComments(false);
            factory.setNamespaceAware(false);
            factory.setValidating(false);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(input);
            doc.getDocumentElement().normalize();

            NodeList oras = doc.getElementsByTagName("ora");

            System.out.println("1) Kurzusok nevei:");
            StringBuilder kurzusLista = new StringBuilder();
            kurzusLista.append("[");
            for (int i = 0; i < oras.getLength(); i++) {
                Element ora = (Element) oras.item(i);
                Node targyNode = ora.getElementsByTagName("targy").item(0);
                String targy = targyNode != null ? targyNode.getTextContent().trim() : "";
                System.out.println(" - " + targy);
                kurzusLista.append(targy);
                if (i < oras.getLength() - 1) kurzusLista.append(", ");
            }
            kurzusLista.append("]");
            System.out.println("Kurzusnév lista: " + kurzusLista.toString());

            if (oras.getLength() > 0) {
                Node first = oras.item(0);

                System.out.println("\n2) Az első <ora> strukturált kiírása (konzol):");
                printStructured((Element) first, 0);

                Document newDoc = builder.newDocument();
                Node imported = newDoc.importNode(first, true);
                newDoc.appendChild(imported);

                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer tr = tf.newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

                File outFirst = new File("orarendFirstXBG3S6.xml");
                tr.transform(new DOMSource(newDoc), new StreamResult(outFirst));
                System.out.println("Első <ora> elmentve: " + outFirst.getAbsolutePath());
            } else {
                System.out.println("\n2) Nincs <ora> elem az XML-ben.");
            }

            System.out.println("\n3) Oktatók listája (egyedi):");
            NodeList oktatoNodes = doc.getElementsByTagName("oktato");
            Set<String> oktatok = new LinkedHashSet<>();
            for (int i = 0; i < oktatoNodes.getLength(); i++) {
                String okt = oktatoNodes.item(i).getTextContent().trim();
                if (!okt.isEmpty()) oktatok.add(okt);
            }
            for (String o : oktatok) System.out.println(" - " + o);

            System.out.println("\n4) Összetett lekérdezés: kurzusok, ahol nap = 'szerda':");
            for (int i = 0; i < oras.getLength(); i++) {
                Element ora = (Element) oras.item(i);
                Element ido = (Element) ora.getElementsByTagName("idopont").item(0);
                if (ido != null) {
                    Node napNode = ido.getElementsByTagName("nap").item(0);
                    if (napNode != null) {
                        String nap = napNode.getTextContent().trim();
                        if ("szerda".equalsIgnoreCase(nap)) {
                            Node targyNode = ora.getElementsByTagName("targy").item(0);
                            String targy = targyNode != null ? targyNode.getTextContent().trim() : "(ismeretlen)";
                            Node tol = ido.getElementsByTagName("tol").item(0);
                            Node ig = ido.getElementsByTagName("ig").item(0);
                            String tolS = tol != null ? tol.getTextContent().trim() : "?";
                            String igS = ig != null ? ig.getTextContent().trim() : "?";
                            Node hely = ora.getElementsByTagName("helyszin").item(0);
                            String helyS = hely != null ? hely.getTextContent().trim() : "-";
                            System.out.println(" - " + targy + " (" + tolS + "-" + igS + ") @ " + helyS);
                        }
                    }
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.err.println("XML feldolgozási hiba: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printStructured(Element elem, int indent) {
        StringBuilder pad = new StringBuilder();
        for (int i = 0; i < indent; i++) pad.append("    ");
        String p = pad.toString();

        System.out.print(p + "<" + elem.getNodeName());
        NamedNodeMap attrs = elem.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            Node a = attrs.item(i);
            System.out.print(" " + a.getNodeName() + "=\"" + a.getNodeValue() + "\"");
        }
        System.out.println(">");

        NodeList children = elem.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node ch = children.item(i);
            if (ch.getNodeType() == Node.ELEMENT_NODE) {
                Element ce = (Element) ch;
                NodeList grand = ce.getChildNodes();
                boolean onlyText = true;
                for (int j = 0; j < grand.getLength(); j++) {
                    if (grand.item(j).getNodeType() != Node.TEXT_NODE) {
                        onlyText = false;
                        break;
                    }
                }
                if (onlyText) {
                    String text = ce.getTextContent().trim();
                    System.out.println(p + "    <" + ce.getNodeName() + ">" + text + "</" + ce.getNodeName() + ">");
                } else {
                    printStructured(ce, indent + 1);
                }
            } else if (ch.getNodeType() == Node.TEXT_NODE) {
                String t = ch.getTextContent().trim();
                if (!t.isEmpty()) System.out.println(p + "    " + t);
            }
        }

        System.out.println(p + "</" + elem.getNodeName() + ">");
    }
}
