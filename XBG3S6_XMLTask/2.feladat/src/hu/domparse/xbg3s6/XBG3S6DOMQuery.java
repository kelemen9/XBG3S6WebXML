package hu.domparse.xbg3s6;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class XBG3S6DOMQuery {

    public static void main(String[] args) {
          QueryPrescribedDetails("XBG3S6_XMLTask/src/hu/domparse/xbg3s6/XBG3S6XML.xml");
    }

    public static void QueryPrescribedDetails(String filePath) {

        Document doc = null;

        try {
            //fájlbeolvasás
            File inputFile = new File(filePath);

            //példányosítás
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            doc = dBuilder.parse(inputFile);

            //normalizálása
            doc.getDocumentElement().normalize();

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        //1. lekérdezés, összes vevő adatai
        System.out.println();
        System.out.println("1. Lekérdezés:");
        System.out.println("Összes vevő adatainak kiírása:");
        System.out.println();

        NodeList vevoList = doc.getElementsByTagName("vevo");

        for (int i = 0; i < vevoList.getLength(); i++) {
            Node node = vevoList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element vevo = (Element) node;

                String vevoID = vevo.getAttribute("VevoID");
                String nev = vevo.getElementsByTagName("nev").item(0).getTextContent();

                Element cimElem = (Element) vevo.getElementsByTagName("cim").item(0);
                String varos = cimElem.getElementsByTagName("varos").item(0).getTextContent();
                String irsz = cimElem.getElementsByTagName("iranyitoszam").item(0).getTextContent();
                String utca = cimElem.getElementsByTagName("utca").item(0).getTextContent();
                String hazszam = cimElem.getElementsByTagName("hazszam").item(0).getTextContent();

                NodeList telefonok = vevo.getElementsByTagName("telefonszam");
                String email = vevo.getElementsByTagName("email").item(0).getTextContent();

                System.out.println("VevoID: " + vevoID);
                System.out.println("Név: " + nev);
                System.out.println("Cím: " + varos + " " + irsz + ", " + utca + " " + hazszam);

                for (int j = 0; j < telefonok.getLength(); j++) {
                    System.out.println("Telefonszám: " + telefonok.item(j).getTextContent());
                }

                System.out.println("E-mail: " + email);
                System.out.println("----------------------------------------");
            }
        }

        //2. lekérdezés- rendelésekhez tartozó vevők, sütemények
        System.out.println();
        System.out.println("2. Lekérdezés:");
        System.out.println("Rendelések vevőkkel és rendelt süteményekkel:");
        System.out.println();

        NodeList rendelesList = doc.getElementsByTagName("rendeles");

        for (int i = 0; i < rendelesList.getLength(); i++) {
            Element rendeles = (Element) rendelesList.item(i);

            String rendelesID = rendeles.getAttribute("RendelesID");
            String vevoID = rendeles.getAttribute("VevoID");
            String datum = rendeles.getElementsByTagName("datum").item(0).getTextContent();

            //VevoID alapján vevők keresése
            Element vevo = findElementByAttribute(doc, "vevo", "VevoID", vevoID);
            String vevoNev = vevo != null
                    ? vevo.getElementsByTagName("nev").item(0).getTextContent()
                    : "ismeretlen";

            //Sütemény megkeresése RendelesID alapján
            Element sutemeny = findElementByAttribute(doc, "sutemeny", "RendelesID", rendelesID);

            String termek = "-";
            String mennyiseg = "-";
            String ar = "-";
            String cukraszdaID = "-";
            String cukraszdaNev = "-";

            if (sutemeny != null) {
                termek = sutemeny.getElementsByTagName("termek").item(0).getTextContent();
                mennyiseg = sutemeny.getElementsByTagName("mennyiseg").item(0).getTextContent();
                ar = sutemeny.getElementsByTagName("ar").item(0).getTextContent();
                cukraszdaID = sutemeny.getAttribute("CukraszdaID");

                //Cukrászda megkeresése
                Element cukraszda = findElementByAttribute(doc, "cukraszda", "CukraszdaID", cukraszdaID);
                if (cukraszda != null) {
                    cukraszdaNev = cukraszda.getElementsByTagName("nev").item(0).getTextContent();
                }
            }

            System.out.println("Rendelés ID: " + rendelesID + " (" + datum + ")");
            System.out.println("Vevő: " + vevoNev + " (VevoID=" + vevoID + ")");
            System.out.println("Cukrászda: " + cukraszdaNev + " (CukraszdaID=" + cukraszdaID + ")");
            System.out.println("Termék: " + termek + ", mennyiség: " + mennyiseg + ", ár: " + ar + " Ft");
            System.out.println("----------------------------------------");
        }

        //3. lekérdezés - azok a házhozszállítások, ahol drágább a szállítási ktg., mint 1600
        System.out.println();
        System.out.println("3. Lekérdezés:");
        System.out.println("Házhoz szállítások, ahol a szállítási költség nagyobb, mint 1600 Ft:");
        System.out.println();

        NodeList szallitasList = doc.getElementsByTagName("szallitas");

        for (int i = 0; i < szallitasList.getLength(); i++) {
            Element szallitas = (Element) szallitasList.item(i);

            String mod = szallitas.getElementsByTagName("mod").item(0).getTextContent();
            int koltseg = Integer.parseInt(
                    szallitas.getElementsByTagName("szallitasi_koltseg").item(0).getTextContent()
            );

            if (!"Házhoz szállítás".equals(mod) || koltseg <= 1600) {
                continue;
            }

            String rendelesID = szallitas.getAttribute("RendelesID");
            String futarID = szallitas.getAttribute("FutarID");

            Element rendeles = findElementByAttribute(doc, "rendeles", "RendelesID", rendelesID);
            String vevoNev = "ismeretlen";
            String futarNev = "ismeretlen";

            if (rendeles != null) {
                String vevoID = rendeles.getAttribute("VevoID");
                Element vevo = findElementByAttribute(doc, "vevo", "VevoID", vevoID);
                if (vevo != null) {
                    vevoNev = vevo.getElementsByTagName("nev").item(0).getTextContent();
                }
            }

            //Futár
            Element futar = findElementByAttribute(doc, "futar", "FutarID", futarID);
            if (futar != null) {
                futarNev = futar.getElementsByTagName("nev").item(0).getTextContent();
            }

            System.out.println("Szállítás ID: " + szallitas.getAttribute("SzallitasID"));
            System.out.println("Rendelés ID: " + rendelesID);
            System.out.println("Vevő: " + vevoNev);
            System.out.println("Futár: " + futarNev + " (FutarID=" + futarID + ")");
            System.out.println("Szállítási költség: " + koltseg + " Ft");
            System.out.println("----------------------------------------");
        }


        //4. lekérdezés - bankkártyák és ahhoz tartozó vevő

        System.out.println();
        System.out.println("4. Lekérdezés:");
        System.out.println("Bankkártyák tulajdonosaival együtt:");
        System.out.println();

        NodeList kartyaList = doc.getElementsByTagName("bankkartya");

        for (int i = 0; i < kartyaList.getLength(); i++) {
            Element kartya = (Element) kartyaList.item(i);

            String kartyaszam = kartya.getAttribute("Kartyaszam");
            String vevoID = kartya.getAttribute("VevoID");
            String lejarat = kartya.getElementsByTagName("lejarat").item(0).getTextContent();
            String bank = kartya.getElementsByTagName("bank").item(0).getTextContent();

            Element vevo = findElementByAttribute(doc, "vevo", "VevoID", vevoID);
            String vevoNev = vevo != null
                    ? vevo.getElementsByTagName("nev").item(0).getTextContent()
                    : "ismeretlen";

            System.out.println("Kártyaszám: " + kartyaszam);
            System.out.println("Tulajdonos: " + vevoNev + " (VevoID=" + vevoID + ")");
            System.out.println("Lejárat: " + lejarat);
            System.out.println("Bank: " + bank);
            System.out.println("----------------------------------------");
        }
    }

    private static Element findElementByAttribute(Document doc,
                                                  String tagName,
                                                  String attrName,
                                                  String attrValue) {

        NodeList list = doc.getElementsByTagName(tagName);

        for (int i = 0; i < list.getLength(); i++) {
            Element elem = (Element) list.item(i);
            if (attrValue.equals(elem.getAttribute(attrName))) {
                return elem;
            }
        }
        return null;
    }
}
