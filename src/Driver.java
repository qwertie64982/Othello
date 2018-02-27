import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

// TODO: Step 4 / whole program within try/catch?

public class Driver {
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            // todo: make better UI for file input
//            System.out.print("Enter filename: ");
//            String filename = keyboard.nextLine();
            String filename = "othello.xml";

            Document document = builder.parse(new File(filename));

            Element root = document.getDocumentElement();
//            System.out.println(root.getTagName());

            NodeList nodeList = root.getChildNodes();
//            for (int i = 0; i < nodeList.getLength(); i++) {
//                System.out.print(nodeList.item(i).getNodeName() + " ");
//            }

            // get PERSONAE node
            Node personae = root.getFirstChild();
            while (personae.getNextSibling() != null && personae.getNodeName().compareTo("PERSONAE") != 0) {
                personae = personae.getNextSibling();
//                System.out.println(personae.getNodeName());
            }

            // print names of all children of PERSONAE with tag PERSONA
            Node persona = personae.getFirstChild();
            while (persona.getNextSibling() != null) {
                persona = persona.getNextSibling();
                if (persona.getNodeName().compareTo("PERSONA") == 0) {
                    System.out.println(persona.getFirstChild().getNodeValue());
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }
}