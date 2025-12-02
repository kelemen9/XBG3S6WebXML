package hu.domparse.xbg3s6;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.io.StringWriter;

import org.w3c.dom.*;

public class XBG3S6DOMModify {

    public static void main(String[] args) {
        //XML fájl helye
        ModifyElement("XBG3S6_XMLTask/src/hu/domparse/xbg3s6/XBG3S6XML.xml");
    }

    //XML elemek módosítása
    public static void ModifyElement(String filePath) {

        try {
            //fájlbeolvasás
            File inputFile = new File(filePath);

            if (!inputFile.exists()) {
                System.out.println("Nem található XML fájlt: " + inputFile.getAbsolutePath());
                return;
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

            //példányosítás
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            //beolvasás
            Document doc = dBuilder.parse(inputFile);

            //normalizálás
            doc.getDocumentElement().normalize();

            //módosítások elvégzése
            ModifyPrescribedElements(doc);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //módosítások
    private static void ModifyPrescribedElements(Document doc) throws TransformerException {

        //Gyökérelem
        Element root = doc.getDocumentElement();
        if (root == null) {
            System.out.println("Nincs gyökérelem a dokumentumban");
            return;
        }

        //az első beszállító nevének módosítása
        NodeList beszallitoList = root.getElementsByTagName("beszallito");
        if (beszallitoList.getLength() > 0) {
            Element beszallito = (Element) beszallitoList.item(0);
            beszallito.getElementsByTagName("nev")
                    .item(0)
                    .setTextContent("Prémium Liszt Kft.");
        }

        //az első cukrászda nyitvatartásának módosítása
        NodeList cukraszdaList = root.getElementsByTagName("cukraszda");
        if (cukraszdaList.getLength() > 0) {
            Element cukraszda = (Element) cukraszdaList.item(0);
            cukraszda.getElementsByTagName("nyitvatartas")
                    .item(0)
                    .setTextContent("H–V 8:00–21:00");
        }

        //az első vevő városának és irányítószámának módosítása
        NodeList vevoList = root.getElementsByTagName("vevo");
        if (vevoList.getLength() > 0) {
            Element vevo = (Element) vevoList.item(0);
            Element cimElem = (Element) vevo.getElementsByTagName("cim").item(0);

            if (cimElem != null) {
                cimElem.getElementsByTagName("varos")
                        .item(0)
                        .setTextContent("Budapest");
                cimElem.getElementsByTagName("iranyitoszam")
                        .item(0)
                        .setTextContent("1111");
            }
        }

        //az első futár telefonszámának módosítása
        NodeList futarList = root.getElementsByTagName("futar");
        if (futarList.getLength() > 0) {
            Element futar = (Element) futarList.item(0);
            futar.getElementsByTagName("telefonszam")
                    .item(0)
                    .setTextContent("36709998877");
        }

        //az első sütemény árának módosítása
        NodeList sutemenyList = root.getElementsByTagName("sutemeny");
        if (sutemenyList.getLength() > 0) {
            Element sutemeny = (Element) sutemenyList.item(0);
            sutemeny.getElementsByTagName("ar")
                    .item(0)
                     // új ár
                    .setTextContent("1600");   
        }

        //első fizetés összegének módosítása
        NodeList fizetesList = root.getElementsByTagName("fizetes");
        if (fizetesList.getLength() > 0) {
            Element fizetes = (Element) fizetesList.item(0);
            fizetes.getElementsByTagName("osszeg")
                    .item(0)
                    .setTextContent("4000");
        }

        //a módosított kiírása konzolra
        printDocument(doc);
    }

    //teljes, módosított XML dokumentum kiírása
    private static void printDocument(Document doc) throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();

        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));

        String output = writer.getBuffer().toString();
        System.out.println(output);
    }
}