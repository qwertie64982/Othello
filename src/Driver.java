import java.util.ArrayList;

public class Driver {
    public static void main(String[] args) {
        Play othello = new Play();

//        System.out.println("Title: " + othello);
//        System.out.println();
//
//        System.out.println("Number of personae: " + othello.countPersona());
//        System.out.println();
//
//        System.out.println("Roderigo speaks " + othello.countSpeakerLines("RODERIGO") + " times");
//        System.out.println("Iago speaks " + othello.countSpeakerLines("IAGO") + " times");
//        System.out.println("Othello speaks " + othello.countSpeakerLines("OTHELLO") + " times");
//        System.out.println("Bianca speaks " + othello.countSpeakerLines("BIANCA") + " times");

//        System.out.println("'Despise me' exists " + othello.fragmentCount("Despise me") + " times");
//        System.out.println("'Othello' exists " + othello.fragmentCount("Othello") + " times");
//        System.out.println("'Dank memes' exists " + othello.fragmentCount("Dank memes") + " times");
//
//        System.out.println();
//        ArrayList<String> sentences = othello.fragmentLines("Despise me");
//        for (String sentence : sentences) {
//            System.out.println(sentence);
//        }
//
//        System.out.println();
//        sentences = othello.fragmentLines("Othello");
//        for (String sentence : sentences) {
//            System.out.println(sentence);
//        }
//
//        System.out.println();
//        sentences = othello.fragmentLines("Dank memes");
//        for (String sentence : sentences) {
//            System.out.println(sentence);
//        }

        System.out.println(othello.fragmentLines("Despise me"));
        System.out.println(othello.replaceFragment("Despise me, if I do not. Three great ones of the city,", "This sentence is altered, ASDF"));
        System.out.println(othello.fragmentLines("ASDF"));

        othello.saveFile("othello_new.xml");
    }
}