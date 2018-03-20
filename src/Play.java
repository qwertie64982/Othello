/**
 * Parses plays translated to XML
 * Supports simple analysis and replacing functionality
 *
 * @author Maxwell Sherman
 * @author Malik Al Ali
 */

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Play object
 * Stores a play, parsed using DOM from XML
 */
public class Play {
    private DocumentBuilderFactory factory;
    private DocumentBuilder builder;
    private String filename;
    private Document document;
    private Element root;

    /**
     * Default value constructor
     * Default file is othello.xml
     */
    public Play() throws ParserConfigurationException, IOException, SAXException {
        this.factory = DocumentBuilderFactory.newInstance();
        this.filename = "othello.xml";

        // This gets rid of a problem with <!DOCTYPE> tags in the XML files
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        builder = factory.newDocumentBuilder();
        document = builder.parse(new File(this.filename));
        root = document.getDocumentElement();
    }

    /**
     * Explicit value constructor
     * @param filename filename for the play's xml file
     */
    public Play(String filename) throws ParserConfigurationException, IOException, SAXException {
        this.factory = DocumentBuilderFactory.newInstance();
        this.filename = filename;

        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        builder = factory.newDocumentBuilder();
        document = builder.parse(new File(this.filename));
        root = document.getDocumentElement();
    }

    /**
     * Returns the number of personae that appear in the play
     * @return number of personae
     */
    public int countPersona() {
        // get PERSONAE node
        Node personae = this.root.getFirstChild();
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
     * Returns how many times a persona speaks - Default value, where speaker is Othello
     * If Othello does not exist or exists and does not speak, this returns 0
     * @return how many times Othello speaks
     */
    public int countSpeakerLines() {
        // All speakers are in all caps, so this makes it case-insensitive
        String persona = "OTHELLO";

        NodeList nodeList = this.root.getElementsByTagName("SPEAKER");
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
     * Returns how many times a persona speaks
     * If the persona does not exist or exists and does not speak, this returns 0
     * @param persona name of persona (case-insensitive)
     * @return how many times the persona speaks
     */
    public int countSpeakerLines(String persona) {
        // All speakers are in all caps, so this makes it case-insensitive
        persona = persona.toUpperCase();

        NodeList nodeList = this.root.getElementsByTagName("SPEAKER");
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
        NodeList nodeList = this.root.getElementsByTagName("LINE");
//        System.out.println(nodeList.getLength());

        int hits = 0;
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getFirstChild().getNodeName().compareTo("#text") == 0) { // cases where the line is just text
                if (nodeList.item(i).getFirstChild().getNodeValue().contains(searchFragment)) {
//                    System.out.println(nodeList.item(i).getFirstChild().getNodeValue());
                    hits++;
                }
            } else { // cases where the line has a STAGEDIR before its text
                if (nodeList.item(i).getFirstChild().getNextSibling() != null) { // cases where the line is only a STAGEDIR
                    if (nodeList.item(i).getFirstChild().getNextSibling().getNodeValue().contains(searchFragment)) {
//                    System.out.println(nodeList.item(i).getFirstChild().getNodeValue());
                        hits++;
                    }
                }
            }
        }
        return hits;
    }

    /**
     * Collects all the lines where the search fragment exists
     * Default version, where the fragment is "green"
     * (because green signifies jealousy, a major theme in Othello)
     * @return ArrayList of lines containing the search fragment
     */
    public ArrayList<String> findLinesFromFragment() {
        String searchFragment = "green";
        ArrayList<String> sentences = new ArrayList<>();
        NodeList nodeList = this.root.getElementsByTagName("LINE");

        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getFirstChild().getNodeName().compareTo("#text") == 0) { // cases where the line is just text
                if (nodeList.item(i).getFirstChild().getNodeValue().contains(searchFragment)) {
                    sentences.add(nodeList.item(i).getFirstChild().getNodeValue());
                }
            } else { // cases where the line has a STAGEDIR before its text
                if (nodeList.item(i).getFirstChild().getNextSibling() != null) { // cases where the line is only a STAGEDIR
                    if (nodeList.item(i).getFirstChild().getNextSibling().getNodeValue().contains(searchFragment)) {
                        sentences.add(nodeList.item(i).getFirstChild().getNextSibling().getNodeValue());
                    }
                }
            }
        }
        return sentences;
    }

    /**
     * Collects all the lines where the search fragment exists
     * @param searchFragment fragment to search
     * @return ArrayList of lines containing the search fragment
     */
    public ArrayList<String> findLinesFromFragment(String searchFragment) {
        ArrayList<String> sentences = new ArrayList<>();
        NodeList nodeList = this.root.getElementsByTagName("LINE");

        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getFirstChild().getNodeName().compareTo("#text") == 0) { // cases where the line is just text
                if (nodeList.item(i).getFirstChild().getNodeValue().contains(searchFragment)) {
                    sentences.add(nodeList.item(i).getFirstChild().getNodeValue());
                }
            } else { // cases where the line has a STAGEDIR before its text
                if (nodeList.item(i).getFirstChild().getNextSibling() != null) { // cases where the line is only a STAGEDIR
                    if (nodeList.item(i).getFirstChild().getNextSibling().getNodeValue().contains(searchFragment)) {
                        sentences.add(nodeList.item(i).getFirstChild().getNextSibling().getNodeValue());
                    }
                }
            }
        }
        return sentences;
    }

    /**
     * Replaces a line with another line
     * This is used within replaceFragment(), and is not available to the user
     * @param originalLine line to replace (should be unique)
     * @param editedLine what to replace the line with
     * @return whether or not the operation succeeded (ex. false if line doesn't exist)
     */
    private boolean replaceLine(String originalLine, String editedLine) {
        NodeList nodeList = this.root.getElementsByTagName("LINE");

        // getElementsByTagName returns a live collection NodeList (rather than static collect)
        // This means that changes made within the NodeList are reflected in the XML tree
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getFirstChild().getNodeName().compareTo("#text") == 0) { // cases where the line is just text
                if (nodeList.item(i).getFirstChild().getNodeValue().contains(originalLine)) {
                    nodeList.item(i).getFirstChild().setNodeValue(editedLine);
                    return true;
                }
            } else { // cases where the line has a STAGEDIR before its text
                if (nodeList.item(i).getFirstChild().getNextSibling() != null) { // cases where the line is only a STAGEDIR
                    if (nodeList.item(i).getFirstChild().getNextSibling().getNodeValue().contains(originalLine)) {
                        nodeList.item(i).getFirstChild().getNextSibling().setNodeValue(editedLine);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Replaces a user's fragment in a line with another fragment
     * @param originalFragment fragment of text the user wants to change
     * @param editedFragment fragment of text that will replace originalFragment
     * @param originalLine line that contains originalFragment (so we know which line to change)
     * @return true if success, false if failure
     */
    public boolean replaceFragment(String originalFragment, String editedFragment, String originalLine) {
        if (originalLine.contains(originalFragment)) {
            String editedLine = originalLine.replaceAll(originalFragment, editedFragment);

            return replaceLine(originalLine, editedLine);
        } else {
            return false;
        }
    }

    /**
     * Overwrites the pre-existing XML file with the modified XML tree
     * @return true if success, false if failure
     */
    public boolean saveFile() {
        boolean success = true;

        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Result output = new StreamResult(new File(this.filename));
            Source input = new DOMSource(this.document);

            transformer.transform(input, output);
        } catch (TransformerException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    /**
     * Saves the XML tree as a new XML file
     * Will overwrite the original file if it has the same name as this.filename
     * @param newFilename name of new XML file
     * @return true if success, false if failure
     */
    public boolean saveFile(String newFilename) {
        boolean success = true;

        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Result output = new StreamResult(new File(newFilename));
            Source input = new DOMSource(this.document);

            transformer.transform(input, output);
        } catch (TransformerException e) {
            e.printStackTrace();
            success = false;
        }

        // Change the current filename if Save As... works properly
        if (success) {
            this.filename = newFilename;
        }

        return success;
    }

    /**
     * Filename getter
     * @return the file's name
     */
    public String getFilename() {
        return filename;
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
