/**
 * Name: Tyler Fenske
 * Class: CS351
 * Date: 09/13/2018
 *
 * Tile objects are simply a Color and a Rank. This object is used to
 * represent the data of each tile in the pool, hands, and discard piles.
 */

package Remi_Tile;

public class Tile {

    private Color color;
    private int rank;

    /**
     * Constructor for Tile.
     * Params: None
     * Returns: Nothing
     */
    public Tile(Color color, int rank){
        this.color = color;
        this.rank = rank;
    }

    /**
     * Returns the color of the tile.
     * Params: None
     * Returns: Color
     */
    public Color getColor(){
        return color;
    }

    /**
     * Returns the rank of the tile.
     * Params: None
     * Returns: Int
     */
    public int getRank(){
        return rank;
    }

}
