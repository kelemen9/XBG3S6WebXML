package xpathxbg3s6;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

import java.io.IOException;

public class xPathQueryXBG3S6 {

    public static void main(String[] args) {

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse("studentXBG3S6.xml");
            document.getDocumentElement().normalize();

            XPath xPath = XPathFactory.newInstance().newXPath();

            NodeList students = (NodeList) xPath.evaluate("/class/student", document, XPathConstants.NODESET);

            for (int i = 0; i < students.getLength(); i++) {

                Node node = students.item(i);
                System.out.println("\nAktuális elem: " + node.getNodeName());

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) node;

                    System.out.println("Hallgató ID: " + element.getAttribute("id"));
                    System.out.println("Vezetéknév: " + element.getElementsByTagName("vezeteknev").item(0).getTextContent());
                    System.out.println("Keresztnév: " + element.getElementsByTagName("keresztnev").item(0).getTextContent());
                    System.out.println("Becenév: " + element.getElementsByTagName("becenev").item(0).getTextContent());
                    System.out.println("Kor: " + element.getElementsByTagName("kor").item(0).getTextContent());
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
            e.printStackTrace();
        }
    }
}