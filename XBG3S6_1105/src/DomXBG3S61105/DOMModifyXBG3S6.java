package domxbg3s61105;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class DOMModify1XBG3S6 {
    public static void main(String[] args) {
        try {
            File input = new File("XBG3S6hallgato.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(input);
            doc.getDocumentElement().normalize();

            NodeList hallgatok = doc.getElementsByTagName("hallgato");
            for (int i = 0; i < hallgatok.getLength(); i++) {
                Element hallgato = (Element) hallgatok.item(i);
                if ("01".equals(hallgato.getAttribute("id"))) {
                    Node kereszt = hallgato.getElementsByTagName("keresztnev").item(0);
                    Node vezetek = hallgato.getElementsByTagName("vezeteknev").item(0);
                    //új keresztnév, vezetéknév
                    if (kereszt != null) kereszt.setTextContent("János");    
                    if (vezetek != null) vezetek.setTextContent("Kovács");   
                    break;
                }
            }

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            DOMSource source = new DOMSource(doc);
            StreamResult console = new StreamResult(System.out);
            t.transform(source, console);

        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
            e.printStackTrace();
        }
    }
}
