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
import java.util.ArrayList;
import java.util.Scanner;

// TODO: what to do with the methods if the play doesn't exist (the functions will crash otherwise)
// TODO: default value for some functions

public class Play {
    private Scanner keyboard;
    private DocumentBuilderFactory factory;
    private DocumentBuilder builder;
    private String filename;
    private Document document;
    private Element root;

    /**
     * Default value constructor
     * Default file is othello.xml
     */
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

    /**
     * Explicit value constructor
     * @param filename filename for the play's xml file
     */
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
     * If the persona does not exist or exists and does not speak, this returns 0
     * @param persona name of persona (case-insensitive)
     * @return how many times the persona speaks
     */
    public int countSpeakerLines(String persona) {
        // All speakers are in all caps, so this makes it case-insensitive
        persona = persona.toUpperCase();

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

    /**
     * Determines how many times a fragment exists in lines in the play
     * @param searchFragment fragment to search
     * @return number of times the fragment was found in the play
     */
    public int fragmentCount(String searchFragment) {
        NodeList nodeList = root.getElementsByTagName("LINE");
//        System.out.println(nodeList.getLength());

        int hits = 0;
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getFirstChild().getNodeName().compareTo("#text") == 0) { // cases where the line is just text
                if (nodeList.item(i).getFirstChild().getNodeValue().contains(searchFragment)) {
//                    System.out.println(nodeList.item(i).getFirstChild().getNodeValue());
                    hits++;
                }
            } else { // cases where the line has a STAGEDIR before its text
                if (nodeList.item(i).getFirstChild().getNextSibling().getNodeValue().contains(searchFragment)) {
//                    System.out.println(nodeList.item(i).getFirstChild().getNodeValue());
                    hits++;
                }
            }
        }
        return hits;
    }

    // TODO: time it takes to search

    /**
     * Collects all the lines where the search fragment exists
     * @param searchFragment fragment to search
     * @return ArrayList of lines containing the search fragment
     */
    public ArrayList<String> fragmentLines(String searchFragment) {
        ArrayList<String> sentences = new ArrayList<>();
        NodeList nodeList = root.getElementsByTagName("LINE");

        for (int i = 0; i < nodeList.getLength(); i++) {

            if (nodeList.item(i).getFirstChild().getNodeName().compareTo("#text") == 0) { // cases where the line is just text
                if (nodeList.item(i).getFirstChild().getNodeValue().contains(searchFragment)) {
                    sentences.add(nodeList.item(i).getFirstChild().getNodeValue());
                }
            } else { // cases where the line has a STAGEDIR before its text
                if (nodeList.item(i).getFirstChild().getNextSibling().getNodeValue().contains(searchFragment)) {
                    sentences.add(nodeList.item(i).getFirstChild().getNextSibling().getNodeValue());
                }
            }
        }
        return sentences;
    }

    /**
     * toString override
     * @return play's full title, according to its XML file
     */
    @Override
    public String toString() {
        // Assumes TITLE is the second child of PLAY (the first being whitespace/#text), and PLAY is the root element
        Node titleNode = root.getFirstChild().getNextSibling();
        return titleNode.getFirstChild().getNodeValue();
    }
}
