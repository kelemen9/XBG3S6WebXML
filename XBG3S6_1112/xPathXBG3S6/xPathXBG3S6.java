package xpathxbg3s6;

import org.w3c.dom.*;
import java.io.*;
import javax.xml.parsers.*;
import javax.xml.xpath.*;

public class xPathXBG3S6 {

    public static void main(String[] args) {
        try {
            File inputFile = new File("studentXBG3S6.xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputFile);
            doc.getDocumentElement().normalize();

            XPath xPath = XPathFactory.newInstance().newXPath();

            //1)Válassza ki az összes student elemet, amely a class gyermekei!
            System.out.println("1.");
            String XBG3S6_1 = "/class/student";
            NodeList nodeList1 = (NodeList) xPath.compile(XBG3S6_1).evaluate(doc, XPathConstants.NODESET);
            writeData(nodeList1);

            //2)Válassza ki azt a student elemet, amely rendelkezik "id" attribútummal és értéke "02"!
            System.out.println("\n2.");
            String XBG3S6_2 = "/class/student[@id='02']";
            NodeList nodeList2 = (NodeList) xPath.compile(XBG3S6_2).evaluate(doc, XPathConstants.NODESET);
            writeData(nodeList2);

            //3)Kiválasztja az összes student elemet, függetlenül attól, hogy hol vannak a dokumentumban!
            System.out.println("\n3.");
            String XBG3S6_3 = "//student";
            NodeList nodeList3 = (NodeList) xPath.compile(XBG3S6_3).evaluate(doc, XPathConstants.NODESET);
            writeData(nodeList3);

            //4)Válassza ki a második student elemet, amely a class root element gyermeke!
            System.out.println("\n4.");
            String XBG3S6_4 = "/class/student[2]";
            NodeList nodeList4 = (NodeList) xPath.compile(XBG3S6_4).evaluate(doc, XPathConstants.NODESET);
            writeData(nodeList4);

            //5)Válassza ki az utolsó student elemet, amely a class root element gyermeke!
            System.out.println("\n5.");
            String XBG3S6_5 = "/class/student[last()]";
            NodeList nodeList5 = (NodeList) xPath.compile(XBG3S6_5).evaluate(doc, XPathConstants.NODESET);
            writeData(nodeList5);

            //6)Válassza ki az utolsó előtti student elemet, amely a class root element gyermeke!
            System.out.println("\n6.");
            String XBG3S6_6 = "/class/student[last()-1]";
            NodeList nodeList6 = (NodeList) xPath.compile(XBG3S6_6).evaluate(doc, XPathConstants.NODESET);
            writeData(nodeList6);

            //7)Válassza ki az első két student elemet, amelyek a root element gyermekei!
            System.out.println("\n7.");
            String XBG3S6_7 = "/class/student[position()<=2]";
            NodeList nodeList7 = (NodeList) xPath.compile(XBG3S6_7).evaluate(doc, XPathConstants.NODESET);
            writeData(nodeList7);

            //8)Válassza ki class root element összes gyermek elemét!
            System.out.println("\n8.");
            String XBG3S6_8 = "/class/*";
            NodeList nodeList8 = (NodeList) xPath.compile(XBG3S6_8).evaluate(doc, XPathConstants.NODESET);
            writeData(nodeList8);

            //9)Válassza ki az összes student elemet, amely rendelkezik legalább egy bármilyen attribútummal!
            System.out.println("\n9.");
            String XBG3S6_9 = "/class/student[@*]";
            NodeList nodeList9 = (NodeList) xPath.compile(XBG3S6_9).evaluate(doc, XPathConstants.NODESET);
            writeData(nodeList9);

            //10)Válassza ki a dokumentum összes elemét!
            System.out.println("\n10.");
            String XBG3S6_10 = "//*";
            NodeList nodeList10 = (NodeList) xPath.compile(XBG3S6_10).evaluate(doc, XPathConstants.NODESET);
            writeData(nodeList10);

            //11)Válassza ki a class root element összes student elemét, amelynél a kor>20!
            System.out.println("\n11.");
            String XBG3S6_11 = "/class/student[kor > 20]";
            NodeList nodeList11 = (NodeList) xPath.compile(XBG3S6_11).evaluate(doc, XPathConstants.NODESET);
            writeData(nodeList11);

            //12)Válassza ki az összes student elem összes keresztnev or vezeteknev csomópontot!
            System.out.println("\n12.");
            String XBG3S6_12 = "/class/student[keresztnev or vezeteknev]";
            NodeList nodeList12 = (NodeList) xPath.compile(XBG3S6_12).evaluate(doc, XPathConstants.NODESET);
            writeData(nodeList12);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeData(NodeList nodeList) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            System.out.println("\nAktuális elem: " + node.getNodeName());

            if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("student")) {
                Element element = (Element) node;

                System.out.println("Hallgató ID: " + element.getAttribute("id"));

                System.out.println("Vezetéknév: "
                        + element.getElementsByTagName("vezeteknev").item(0).getTextContent());
                System.out.println("Keresztnév: "
                        + element.getElementsByTagName("keresztnev").item(0).getTextContent());

                System.out.println("Becenév : "
                        + element.getElementsByTagName("becenev").item(0).getTextContent());

                System.out.println("Kor : "
                        + element.getElementsByTagName("kor").item(0).getTextContent());
            }
        }
    }
}