/**
 * Name: Tyler Fenske
 * Class: CS351
 * Date: 08/29/2018
 *
 * The Score class keeps track of the current score of the game. Anytime the
 * ball hovers over an orange tile, 10 points are added and kept track of in
 * this class. This class tells the display when to update the
 * score onto the GUI.
 */

package Pinball;

public class Score {

    private int score = 0;
    private Display display;

    /**
     * Constructor for score. Sends the display the starting score of 0.
     * Params: Display
     * Returns: Nothing
     */
    public Score(Display display){
        this.display = display;

        display.updateScore(score);
    }

    /**
     * Adds 10 points to the score. Tells the GUI to update the score Label.
     * Is called by GameCoordinator whenever the ball flies over an orange tile.
     * Params: None
     * Returns: Nothing
     */
    public void addPoints(){
        score+=10;
        display.updateScore(score);
    }
}
