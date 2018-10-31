/**
 * BubbleManagerTest class that contains a main method to demonstrate
 * BubbleManager's capabilities.
 * 
 * @author Tyler Fenske
 * @version 2016-12-06
 */

public class BubbleManagerTest {

    public static void main(String[] args) {
        /** Build an array of symbols to use on first bubble manager. */
        Bubble[] bubArray = { new Bubble("@"), new Bubble("*"), new Bubble("~"),
                new Bubble("+") };
        /**
         * Create new BubbleManager with 7 rows and 10 cols, then tests
         * functionality of several methods
         */
        BubbleManager bm = new BubbleManager(7, 9, bubArray);
/*        System.out.println("First Bubble Manager Test");
        bm.fill();
        System.out.println("Filling new board with random bubbles.");
        System.out.println("\n" + bm.toString());

        bm.addAndRemove(7, 7, new Bubble("*"));
        bm.addAndRemove(7, 5, new Bubble("~"));
        bm.addAndRemove(7, 3, new Bubble("+"));
        bm.addAndRemove(2, 7, new Bubble("~"));

        *//** Build a new array of different symbols from the first bm. *//*
        Bubble[] bubArray2 = { new Bubble("$"), new Bubble("#"),
                new Bubble("-"), new Bubble("&") };
        *//**
         * Create different BubbleManager with 6 rows and 8 cols, then tests
         * functionality of several methods
         *//*
        BubbleManager bm2 = new BubbleManager(6, 8, bubArray2);
        System.out.println("Second Bubble Manager Test");
        bm2.fill();
        System.out.println("Filling new board with random bubbles.");
        System.out.println("\n" + bm2.toString());

        bm2.addAndRemove(6, 1, new Bubble("&"));
        bm2.addAndRemove(2, 0, new Bubble("-"));
        bm2.addAndRemove(2, 2, new Bubble("&"));
        bm2.addAndRemove(3, 3, new Bubble("#"));

        *//** Performs a couple more methods on the first bm created *//*
        System.out.println("Back to the First Bubble Manager");
        System.out.println("\n" + bm.toString());
        bm.addAndRemove(2, 3, new Bubble("@"));
        bm.addAndRemove(7, 0, new Bubble("~"));*/
        
        bm.fill();
        bm.addAndRemove(11, 7, new Bubble("@"));
        
    }

}
