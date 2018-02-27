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

            // TODO: Make UI for file selection
            String filename = "othello.xml";

            Document document = builder.parse(new File(filename));

            Element root = document.getDocumentElement();
//            System.out.println(root.getTagName());

            NodeList nodeList = root.getChildNodes();
//            for (int i = 0; i < nodeList.getLength(); i++) {
//                System.out.print(nodeList.item(i).getNodeName() + " ");
//            }

            System.out.println("Number of personae: " + countPersona(root));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the number of personae that appear in a play
     * @param root root element of play's XML tree
     * @return number of personae
     */
    private static int countPersona(Element root) {
        // get PERSONAE node
        Node personae = root.getFirstChild();
        while (personae.getNextSibling() != null && personae.getNodeName().compareTo("PERSONAE") != 0) {
            personae = personae.getNextSibling();
//                System.out.println(personae.getNodeName());
        }

        // find names of all children of PERSONAE with tag PERSONA
        Node persona = personae.getFirstChild();
        int personaCount = 0;
        while (persona.getNextSibling() != null) {
            persona = persona.getNextSibling();
            if (persona.getNodeName().compareTo("PERSONA") == 0) {
                // Prints all names and their roles - only for debug
//                    System.out.println(persona.getFirstChild().getNodeValue());

                personaCount++;
            }
        }
        return personaCount;
    }
}