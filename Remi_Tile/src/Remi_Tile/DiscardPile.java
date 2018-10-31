/**
 * Name: Tyler Fenske
 * Class: CS351
 * Date: 09/13/2018
 *
 * Creates a discardPile. When a new game starts, a discardPile is created for
 * both players. A discardPile is stored with a Stack of Tiles.
 */

package Remi_Tile;

import java.util.Stack;

public class DiscardPile {

    private Stack<Tile> discardPile;
    private Display display;

    private static int discardIDGen = 0;
    private int discardID;

    private int numTiles = 0;

    /**
     * Constructor for discardPile. Creates a stack for the discard pile.
     * Params: None
     * Returns: Nothing
     */
    public DiscardPile(Display display){
        discardPile = new Stack<>();

        this.display = display;

        discardIDGen++;
        discardID = discardIDGen;
    }

    /**
     * Returns the top tile of the discard pile. Used to pain the GUI.
     * Params: None
     * Returns: Nothing
     */
    public Tile viewTopTile(){
        if(!isEmpty()){
            return discardPile.peek();
        }
        return new Tile(Color.DISCARD, -1);
    }

    /**
     * discardIDGen is static, and a new discard pile is created when a
     * new game is launched. So the variable is set back to 0 at the beginning
     * of a new game.
     * Params: None
     * Returns: Nothing
     */
    public void startNewGame(){
        discardIDGen = 0;
    }

    /**
     * Returns the discard pile.
     * Params: None
     * Returns: Stack<Tile>
     */
    public Stack<Tile> getDiscardPile(){
        return discardPile;
    }

    /**
     * Returns the top card of the discard pile, and removes it from the stack.
     * Params: None
     * Returns: Tile
     */
    public Tile drawTopTile(){
        Tile topTile = discardPile.peek();

        discardPile.pop();
        numTiles--;

        return topTile;
    }

    /**
     * Adds passed tile to the top of the stack.
     * Params: Tile
     * Returns: Nothing
     */
    public void discardTile(Tile discardedTile){
        discardPile.push(discardedTile);
        numTiles++;
    }

    /**
     * Returns true if the discard pile is empty.
     * Params: None
     * Returns: Boolean
     */
    public boolean isEmpty(){
        return discardPile.isEmpty();
    }

    /**
     * Debugging method
     * Params: None
     * Returns: Nothing
     */
    public void printPile(){
        System.out.println("PLAYER " + discardID + "'s DP: " + numTiles);

        for(int i = numTiles-1; i >= 0; i--){
            System.out.println(discardPile.elementAt(i).getColor() + " " +
                    discardPile.elementAt(i).getRank());

        }
    }

    /**
     * Paints the discard pile in the display GUI
     * Params: None
     * Returns: Nothing
     */
    public void paintDiscardInDisplay(){
        display.paintDiscardPile(viewTopTile(), discardID);
    }

    /**
     * When discard pile is shuffled into the pool, numTiles needs to be
     * reassigned to 1, since there is only 1 tile remaining in the pile.
     * Params: None
     * Returns: Nothing
     */
    public void shuffleIntoDeck(){
        numTiles = 1;
    }


}
