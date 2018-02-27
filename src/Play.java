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

// TODO: what to do with the methods if the play doesn't exist (the functions will crash otherwise)

public class Play {
    private Scanner keyboard;
    private DocumentBuilderFactory factory;
    private DocumentBuilder builder;
    private String filename;
    private Document document;
    private Element root;

    public Play() {
        this.keyboard = new Scanner(System.in);
        this.factory = DocumentBuilderFactory.newInstance();
        this.filename = "othello.xml";

        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(new File(this.filename));
            root = document.getDocumentElement();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    public Play(String filename) {
        this.keyboard = new Scanner(System.in);
        this.factory = DocumentBuilderFactory.newInstance();
        this.filename = filename;

        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(new File(this.filename));
            root = document.getDocumentElement();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the number of personae that appear in the play
     * @return number of personae
     */
    public int countPersona() {
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

    /**
     * Returns how many times a persona speaks
     * If the persona exists but speaks 0 times, this returns 0
     * If the persona doesn't exist, this returns -1
     * @param persona name of persona
     * @return how many times the persona speaks
     */
    public int countSpeakerLines(String persona) {
        // TODO: For more efficiency, first check if the speaker is within personae. If not, immediately return -1
        // TODO: thus, 0 means the persona exists with no lines, whereas -1 means the persona doesn't exist

        NodeList nodeList = root.getElementsByTagName("SPEAKER");
        // How many times the persona has acted (spoken a set of lines)
        int actCount = 0;
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getFirstChild().getNodeValue().compareTo(persona) == 0) {
                actCount++;
            }
//                System.out.print(nodeList.item(i).getNodeName() + " ");
//                System.out.print(nodeList.item(i).getFirstChild().getNodeValue() + " ");
        }
        return actCount;
    }
}
