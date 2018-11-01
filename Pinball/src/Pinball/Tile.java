/**
 * Name: Tyler Fenske
 * Class: CS351
 * Date: 08/29/2018
 *
 * The Tile class is used to create new canvases to be used in the JavaFX GUI
 * GridPane.
 */

package Pinball;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Tile {

    //Sizes and colors of this tile object.
    private int width;
    private int height;
    private char color;

    /**
     * Constructor for Tile Object.
     * Params: Int Int Char
     * Width and Height are used to determine the size of a tile, and color, to
     * determine what color to fill the canvas with.
     * Returns: Nothing
     */
    public Tile(int width, int height, char color) {
        this.width = width;
        this.height = height;
        this.color = color;
    }

    /**
     * Creates a canvas with specified tile width and height and color.
     * Params: None
     * Returns: Canvas
     */
    public Canvas getTile (){
        Canvas canvas = new Canvas(width, height);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        if(color == 'o')
        {
            gc.setFill(Color.ORANGE);
        }else{
            gc.setFill(Color.ROYALBLUE);
        }

        gc.setStroke(Color.DARKBLUE);

        gc.fillRect(0, 0, width, height);
        gc.strokeRect(0, 0, width, height);

        return canvas;
    }
}
