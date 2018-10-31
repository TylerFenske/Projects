/**
 * Name: Tyler Fenske
 * Class: CS351
 * Date: 09/13/2018
 *
 * Pool initializes, shuffles, and manages the tile pool.
 * The pool is stored with a Stack of Tiles.
 */

package Remi_Tile;

import java.util.Stack;
import static java.util.Collections.shuffle;

public class Pool {

    private Stack<Tile> pool = new Stack<>();

    private short numRanks;
    private short numDupColorSets;
    private short numJokers;
    private int numTiles = 0;


    /**
     * Constructor for pool. Creates a deck based off of number of ranks,
     * jokers, and duplicate color sets.
     * Params: short, short, short
     * Returns: Nothing
     */
    public Pool(short numOfRanks, short numOfDupColorSets, short numOfJokers){
        this.numRanks = numOfRanks;
        this.numDupColorSets = numOfDupColorSets;
        this.numJokers = numOfJokers;

        initializePool();
    }

    /**
     * Initializes the pool with 2 sets of 54 cards (2 Jokers, Ranks 1-13 in 4
     * different colors {blue, green, red, and yellow}). Then shuffles the
     * deck.
     * Params: None
     * Returns: Nothing
     */
    private void initializePool(){
        for(int i = 1; i < numRanks+1; i++){
            for(int j = 0; j < numDupColorSets; j++){
                Tile blue = new Tile(Color.BLUE, i);
                Tile green = new Tile(Color.GREEN, i);
                Tile red = new Tile(Color.RED,  i);
                Tile yellow = new Tile(Color.YELLOW, i);

                pool.push(blue);
                pool.push(green);
                pool.push(red);
                pool.push(yellow);

                numTiles+=4;
            }
        }

        for(int i = 0; i < numJokers; i++){
            Tile joker = new Tile(Color.JOKER, 0);

            pool.push(joker);
            numTiles++;
        }

        shuffle(pool);
    }

    /**
     * Prints the deck from the top of the deck to the bottom. Debugging
     * method.
     * Params: None
     * Returns: Nothing
     */
    public void printPool(){
        System.out.println("POOL: " +  numTiles);

        for(int i = numTiles-1; i >= 0; i--){
            System.out.println(pool.elementAt(i).getColor() + " " +
                    pool.elementAt(i).getRank());

        }
    }

    /**
     * Prints the number of tiles in the pool. Debugging method.
     * Params: None
     * Returns: Nothing
     */
    public void printPoolCount(){
        System.out.println("POOL: " +  numTiles);
    }

    /**
     * Returns the top tile of the deck and decrements the pool count.
     * Params: None
     * Returns: Tile
     */
    public Tile drawTile(){
        Tile topTile = pool.peek();
        pool.pop();
        numTiles--;

        return topTile;
    }

    /**
     * Returns true if the pool is empty.
     * Params: None
     * Returns: Boolean
     */
    public boolean isEmpty(){
        return pool.isEmpty();
    }

    /**
     * Called when pool is empty and a player tries to draw from it. Merges
     * all cards from both discard piles (except the top card) into the pool,
     * then shuffles the deck.
     * Params: Stack<Tile>, Stack<Tile>
     * Returns: Nothing
     */
    public void mergeWithDiscardPiles(Stack<Tile> discard1,
                                      Stack<Tile> discard2){

        for(int i = 0; i < discard1.size()-1; i++){
            pool.push(discard1.get(i));
            numTiles++;
            discard1.remove(i);
            i--;
        }

        for(int i = 0; i < discard2.size()-1; i++){
            pool.push(discard2.get(i));
            numTiles++;
            discard2.remove(i);
            i--;
        }
        shuffle(pool);
    }
}
