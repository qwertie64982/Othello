public class Driver {
    public static void main(String[] args) {
        Play othello = new Play();

        System.out.println("Title: " + othello);
        System.out.println();

        System.out.println("Number of personae: " + othello.countPersona());
        System.out.println();

        System.out.println("Roderigo speaks " + othello.countSpeakerLines("RODERIGO") + " times");
        System.out.println("Iago speaks " + othello.countSpeakerLines("IAGO") + " times");
        System.out.println("Othello speaks " + othello.countSpeakerLines("OTHELLO") + " times");
        System.out.println("Bianca speaks " + othello.countSpeakerLines("BIANCA") + " times");
    }
}