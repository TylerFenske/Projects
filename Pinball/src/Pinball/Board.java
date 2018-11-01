/**
 * Name: Tyler Fenske
 * Class: CS351
 * Date: 08/29/2018
 *
 * The Board class creates a 2D List of characters that represents the tiles on
 * the board. Anytime a tile is changed, the 2D List is updated. This list is
 * passed to display to have a proper GUI representation.
 */

package Pinball;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private Display display;
    private int numCols, numRows;
    private final char BLUE_TILE = 'b';
    private final char ORANGE_TILE = 'o';

    //A list containing tile color codes for each row
    private List<Character> tileRows;
    //A list of lists of rows containing tile color codes.
    // Acts as 2D representation  of game board.
    public List<List<Character>> tiles = new ArrayList<>();

    /**
     * Constructor for the Board. Initializes a new board and sends it to
     * display.
     * Params: Display, Int, Int
     * numRows and numCols are used to define the size of the 2D List
     * Returns: Nothing
     */
    public Board(Display display, int numRows, int numCols) {
        this.display = display;
        this.numCols = numCols;
        this.numRows = numRows;

        initializeBoard();
        display.initializeBoard(tiles);
    }

    /**
     * For each row, creates a new List of characters. Then adds a blue tile
     * to that index. After each row is complete, it is added to a larger, 2D
     * List that will hold the state of the entire board.
     * Params: None
     * Returns: Nothing
     */
    private void initializeBoard(){

        for(int r = 0; r < numRows; r++){
            tileRows = new ArrayList<>();
            for(int c = 0; c < numCols; c++){
                tileRows.add(BLUE_TILE);
            }
            //Add each row list to the game board
            tiles.add(tileRows);
        }

    }

    /**
     * Sets all tiles to blue.
     * Params: None
     * Returns: Nothing
     */
    public void resetBoard(){
        for(int r = 0; r < numRows; r++){
            for(int c = 0; c < numCols; c++){
                changeTileColorToBlue(r, c);
            }
        }
    }

    /**
     * Params: Int Int
     * Uses row and col as indexes to find the color of the tile in the 2D
     * List.
     * Returns: Color of tile at index [row][col] as a char 'o' for Orange or
     * 'b' for Blue.
     */
    public char getTileColor(int row, int col){
        return tiles.get(row).get(col);
    }

    /**
     * Changes a tiles color to blue or orange (whatever it is not already).
     * Params: Int Int
     * Uses row and col as indexes to search the 2D List.
     * Returns: Nothing
     */
    public void changeTileColor(int row, int col){
        char tileColor = tiles.get(row).get(col);

        if(tileColor == ORANGE_TILE){
            tiles.get(row).set(col, BLUE_TILE);
            display.changeTileBlue(row, col);
        }else{
            tiles.get(row).set(col, ORANGE_TILE);
            display.changeTileOrange(row, col);
        }
    }

    /**
     * Changes a tile color to blue by updating the 2D List.
     * Params: Int Int
     * Uses row and col as indexes to search the 2D List.
     * Returns: Nothing
     */
    private void changeTileColorToBlue(int row, int col){
        tiles.get(row).set(col, BLUE_TILE);
        display.changeTileBlue(row, col);
    }

    /**
     * Debugging tool - prints a string representation of the board.
     * Params: None
     * Returns: Nothing
     */
    public void printBoard(){
        for(int r = 0; r < numRows; r++){
            for(int c = 0; c < numCols; c++){
                System.out.print(tiles.get(r).get(c) + " ");
            }
            System.out.println();
        }
    }
}
