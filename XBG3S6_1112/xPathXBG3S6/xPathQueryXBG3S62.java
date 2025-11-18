package xpathxbg3s6;

import org.w3c.dom.*;
import java.io.*;
import javax.xml.parsers.*;
import javax.xml.xpath.*;

public class xPathQueryXBG3S6 {

    public static void main(String[] args) {
        try {
            File inputFile = new File("orarendXBG3S62.xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputFile);
            doc.getDocumentElement().normalize();

            XPath xPath = XPathFactory.newInstance().newXPath();

            System.out.println("1) Összes előadás:");
            String q1 = "//ora[@tipus='előadás']";
            NodeList list1 = (NodeList) xPath.compile(q1).evaluate(doc, XPathConstants.NODESET);
            printOraList(list1);

            System.out.println("\n2) Dr. Sasvári Péter órái:");
            String q2 = "//ora[oktato='Dr. Sasvári Péter']";
            NodeList list2 = (NodeList) xPath.compile(q2).evaluate(doc, XPathConstants.NODESET);
            printOraList(list2);

            System.out.println("\n3) L103-ban tartott órák:");
            String q3 = "//ora[helyszin='L103']";
            NodeList list3 = (NodeList) xPath.compile(q3).evaluate(doc, XPathConstants.NODESET);
            printOraList(list3);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printOraList(NodeList list) {
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element ora = (Element) node;
                String id = ora.getAttribute("id");
                String tipus = ora.getAttribute("tipus");
                String targy = ora.getElementsByTagName("targy").item(0).getTextContent();
                String nap = ((Element) ora.getElementsByTagName("idopont").item(0))
                        .getElementsByTagName("nap").item(0).getTextContent();
                String tol = ((Element) ora.getElementsByTagName("idopont").item(0))
                        .getElementsByTagName("tol").item(0).getTextContent();
                String ig = ((Element) ora.getElementsByTagName("idopont").item(0))
                        .getElementsByTagName("ig").item(0).getTextContent();
                String helyszin = ora.getElementsByTagName("helyszin").item(0).getTextContent();

                System.out.println("  óra id=" + id + " (" + tipus + "): " + targy +
                        " | " + nap + " " + tol + "-" + ig + " | " + helyszin);
            }
        }
    }
}