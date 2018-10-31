/**
 * Name: Tyler Fenske
 * Class: CS351
 * Date: 09/13/2018
 *
 * Board initializes all board objects at once. This includes the Pool, both
 * hands, and discard piles. It is used as a directory for GameCoordinator to
 * find all board objects in the same place.
 */

package Remi_Tile;

public class Board {

    private Pool pool;
    private Hand hand1;
    private Hand hand2;
    private DiscardPile discard1;
    private DiscardPile discard2;

    /**
     * Constructor for Board.
     * Params: None
     * Returns: Nothing
     */
    public Board(Display display){
        pool = new Pool((short) 13, (short) 2, (short) 4);
        hand1 = new Hand(display);
        hand2 = new Hand(display);
        discard1 = new DiscardPile(display);
        discard2 = new DiscardPile(display);
    }


    /**
     * Debugging method
     * Params: None
     * Returns: Nothing
     */
    public void printBoard(){
        pool.printPool();
        hand1.printHand();
        hand2.printHand();
        discard1.printPile();
        discard2.printPile();
    }

    /**
     * Calls start newGame methods on appropriate objects.
     * Params: None
     * Returns: Nothing
     */
    public void startNewGame(){
        hand1.startNewGame();
        discard1.startNewGame();
    }

    /**
     * Returns the pool.
     * Params: None
     * Returns: Nothing
     */
    public Pool getPool(){
        return pool;
    }

    /**
     * Returns human player's hand.
     * Params: None
     * Returns: Nothing
     */
    public Hand getHand1(){
        return hand1;
    }

    /**
     * Returns computer player's hand.
     * Params: None
     * Returns: Nothing
     */
    public Hand getHand2(){
        return hand2;
    }

    /**
     * Returns human player's discard pile.
     * Params: None
     * Returns: Nothing
     */
    public DiscardPile getDiscard1(){
        return discard1;
    }

    /**
     * Returns computer player's discard pile.
     * Params: None
     * Returns: Nothing
     */
    public DiscardPile getDiscard2(){
        return discard2;
    }
}
