package xpathXBG3S6;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

public class xPathModifyXBG3S6 {

    public static void main(String[] args) {
        try {
            File inputFile = new File("studentXBG3S6.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputFile);
            doc.getDocumentElement().normalize();

            XPath xPath = XPathFactory.newInstance().newXPath();

            String expression = "/class/student[@id='01']";
            Element studentElement = (Element) xPath.compile(expression)
                    .evaluate(doc, XPathConstants.NODE);

            if (studentElement == null) {
                System.out.println("Nincs id=\"01\" hallgató a doksiban");
                return;
            }

            System.out.println("Korábbi keresztnév: " +
                    studentElement.getElementsByTagName("keresztnev").item(0).getTextContent()
            );

            Node keresztnevNode = studentElement
                    .getElementsByTagName("keresztnev")
                    .item(0);

            if (keresztnevNode != null) {
                keresztnevNode.setTextContent("XBG3S6");
            }

            System.out.println("\nMódosított hallgató:");
            System.out.println("ID: " + studentElement.getAttribute("id"));
            System.out.println("Vezetéknév: "
                    + studentElement.getElementsByTagName("vezeteknev")
                                     .item(0).getTextContent());
            System.out.println("Keresztnév: "
                    + studentElement.getElementsByTagName("keresztnev")
                                     .item(0).getTextContent());
            System.out.println("Becenév: "
                    + studentElement.getElementsByTagName("becenev")
                                     .item(0).getTextContent());
            System.out.println("Kor: "
                    + studentElement.getElementsByTagName("kor")
                                     .item(0).getTextContent());

        } catch (ParserConfigurationException | SAXException |
                 IOException | XPathExpressionException e) {
            e.printStackTrace();
        }
    }
}