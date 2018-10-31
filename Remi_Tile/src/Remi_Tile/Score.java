/**
 * Name: Tyler Fenske
 * Class: CS351
 * Date: 09/13/2018
 *
 * Score keeps track of the int score of each player. Managed by the
 * GameCoordinator.
 */

package Remi_Tile;

public class Score {

    private int score;
    private Display display;

    private static int scoreIDGen = 0;
    private int scoreID;

    /**
     * Constructor for Score. Initially tells display to update score labels
     * to 0 for each player for a new game.
     * Params: None
     * Returns: Nothing
     */
    public Score(Display display){
        this.display = display;

        score = 0;
        scoreIDGen++;
        scoreID = scoreIDGen;

        display.updateScore(score, scoreID);
    }

    /**
     * Adds passed value to score value.
     * Params: Int
     * Returns: Nothing
     */
    public void addScore(int score){
        this.score += score;
        display.updateScore(this.score, scoreID);
    }

    /**
     * Returns the score value.
     * Params: None
     * Returns: Int
     */
    public int getScore(){
        return score;
    }

    /**
     * scoreIDGen is static, and needs to be reset to 0 for each new game.
     * Params: None
     * Returns: Nothing
     */
    public void startNewGame(){
        scoreIDGen = 0;
    }

}
