/**
 * Parses plays translated to XML
 * Supports simple analysis and replacing functionality
 *
 * @author Maxwell Sherman
 * @author Malik Al Ali
 */

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Command-line driver class
 */
public class Driver {
    public static void main(String[] args) {
        Play mPlay = null;
        Scanner keyboard = new Scanner(System.in);
        String filename;
        try {
            System.out.print("Enter filename (leave blank for othello.xml): ");
            filename = keyboard.nextLine();
            if (filename.compareTo("") == 0) {
                System.out.println("Opening othello.xml");
                mPlay = new Play();
            } else {
                System.out.println("Opening " + filename);
                mPlay = new Play(filename);
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            System.out.println("File error. Aborting.");
        }

        if (mPlay != null) {
            runMainMenu(mPlay, keyboard);
        }
    }

    /**
     * Runs the main menu loop
     * @param mPlay Play object
     * @param keyboard keyboard Scanner
     */
    private static void runMainMenu(Play mPlay, Scanner keyboard) {
        // Main loop
        boolean exit = false;
        while (!exit) {
            // Loop ensuring the user picks a proper menu option
            int selection;
            do {
                selection = 0; // If the user puts in something other than a number, this will trigger a redo
                System.out.println();
                System.out.println("MAIN MENU");
                System.out.println("1: Calculate how many persona in play");
                System.out.println("2: Calculate how many times a character acts");
                System.out.println("3: Fragment search/replace");
                System.out.println("4: Quit");
                System.out.println();
                System.out.print("Enter selection: ");
                try {
                    selection = Integer.parseInt(keyboard.nextLine());
                } catch (NumberFormatException e) {} // Prevent it from crashing if the user enters a non-number
            } while (selection <= 0 || selection > 4);

            // Handle selection
            switch (selection) {
                case 1:
                    System.out.println();
                    System.out.println("There are " + mPlay.countPersona() + " personae in this play.");
                    break;
                case 2:
                    characterLineCount(mPlay, keyboard);
                    break;
                case 3:
                    fragmentSearch(mPlay, keyboard);
                    break;
                case 4:
                    System.out.println("Goodbye.");
                    exit = true;
                    break;
                default:
                    System.out.println("Error, quitting");
                    exit = true;
                    break;
            }
        }
    }

    /**
     * Runs menu option 2 - counts how many times a character counts
     * @param mPlay Play object
     * @param keyboard keyboard Scanner
     */
    private static void characterLineCount(Play mPlay, Scanner keyboard) {
        System.out.print("Enter character name (leave blank for Othello): ");
        String speaker = keyboard.nextLine();

        int count;
        if (speaker.compareTo("") == 0) {
            speaker = "Othello";
            count = mPlay.countSpeakerLines();
        } else {
            count = mPlay.countSpeakerLines(speaker);
        }

        if (count <= 0) {
            System.out.println("That character does not speak in this play.");
        } else {
            System.out.println("That character speaks " + count + " times in this play.");
        }
    }

    /**
     * Runs menu option 3 - searches for a fragment in the play
     * @param mPlay Play object
     * @param keyboard keyboard Scanner
     */
    private static void fragmentSearch(Play mPlay, Scanner keyboard) {
        System.out.print("Enter a fragment from the play (leave blank for 'green'): ");
        String searchFragment = keyboard.nextLine();

        long startTime = System.currentTimeMillis();
        long elapsedTime;
        ArrayList<String> foundLines;
        if (searchFragment.compareTo("") == 0) {
            searchFragment = "green";
            foundLines = mPlay.findLinesFromFragment();
        } else {
            foundLines = mPlay.findLinesFromFragment(searchFragment);
        }
        elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("Search completed in " + elapsedTime + " ms");

        System.out.println();
        if (foundLines.size() > 0) {
            System.out.println("Found lines:");
            for (int i = 0; i < foundLines.size(); i++) {
                System.out.println((i + 1) + ": " + foundLines.get(i));
            }

            String response;
            do { // loop to make sure the user enters a valid answer
                System.out.println();
                System.out.print("Do you want to replace this fragment? Y/n: ");
                response = keyboard.nextLine();
            } while (response.toLowerCase().compareTo("y") != 0 && response.toLowerCase().compareTo("n") != 0);

            if (response.toLowerCase().compareTo("y") == 0) { // If the user wants to replace the fragment
                int whichLine;
                do {
                    whichLine = 0;
                    System.out.print("Enter the number of the line where you wish to replace it: ");
                    try {
                        whichLine = Integer.parseInt(keyboard.nextLine());
                    } catch (NumberFormatException e) {}
                } while (whichLine <= 0 || whichLine > foundLines.size());
                fragmentReplacement(searchFragment, foundLines.get(whichLine - 1), mPlay, keyboard);
            }
        } else {
            System.out.println("Did not find any lines containing that fragment.");
        }
    }

    /**
     * Fragment replacement functionality
     * @param searchFragment fragment the user searched for
     * @param line line where the fragment is found
     * @param mPlay Play object
     * @param keyboard keyboard Scanner
     */
    private static void fragmentReplacement(String searchFragment, String line, Play mPlay, Scanner keyboard) {
        System.out.println();
        System.out.print("Enter replacement text: ");
        String editedFragment = keyboard.nextLine();

        String alteredLine = line.replaceAll(searchFragment, editedFragment);

        System.out.println();
        if (mPlay.replaceFragment(searchFragment, editedFragment, line)) {
            System.out.println("Success!");
            System.out.println("The line has been replaced as follows:");
            System.out.println(mPlay.findLinesFromFragment(alteredLine));

            String response;
            do { // loop to make sure the user enters a valid answer
                System.out.println();
                System.out.print("Do you want to save? Y/n: ");
                response = keyboard.nextLine();
            } while (response.toLowerCase().compareTo("y") != 0 && response.toLowerCase().compareTo("n") != 0);

            boolean isValidPath;
            String path;
            if (response.toLowerCase().compareTo("y") == 0) { // If the user wants to save
                do {
                    isValidPath = true;

                    System.out.println();
                    System.out.print("Enter filename (leave blank to leave as-is): ");
                    path = keyboard.nextLine();

                    if (path.compareTo("") != 0) {
                        try { // check if valid file name
                            new File(path).getCanonicalPath();
                        } catch (IOException e) {
                            isValidPath = false;
                        }
                    } else {
                        path = mPlay.getFilename();
                    }
                } while (!isValidPath);

                System.out.println();
                if (path.compareTo(mPlay.getFilename()) == 0) {
                    String overwriteResponse;
                    do {
                        System.out.println();
                        System.out.print("Overwrite previous file? Y/n: ");
                        overwriteResponse = keyboard.nextLine();
                    } while (overwriteResponse.toLowerCase().compareTo("y") != 0
                            && overwriteResponse.toLowerCase().compareTo("n") != 0);

                    if (overwriteResponse.compareTo("y") == 0) { // if the user wants to overwrite
                        if (mPlay.saveFile()) {
                            System.out.println("Success!");
                        } else {
                            System.out.println("Operation failed.");
                        }
                    }
                } else {
                    if (mPlay.saveFile(path)) {
                        System.out.println("Success!");
                    } else {
                        System.out.println("Operation failed.");
                    }
                }
            }
        } else {
            System.out.println("Operation failed.");
        }
    }
}