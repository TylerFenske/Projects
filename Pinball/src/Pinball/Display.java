/**
 * Name: Tyler Fenske
 * Class: CS351
 * Date: 08/29/2018
 *
 * The Display class creates JavaFX nodes to be added to the Stage. This class
 * is at the lowest level of the design tree, and is passed no references. This
 * class' main purpose is to display the GUI of the game.
 */

package Pinball;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class Display{

    private Stage window;
    private Canvas playArea;
    private BorderPane controlPane;
    private Button reset;
    private Button play;
    private Label scoreLabel;
    private double ballWidth;
    private double ballHeight;
    private int ballStartPosX;
    private int ballStartPosY;
    private double ballPosX;
    private double ballPosY;
    private int numRows;
    private int numCols;
    private boolean gameInPlay = false;
    private final int TILE_WIDTH = 50;
    private final int TILE_HEIGHT = 50;
    //List that helps build GUI tiles 2D list
    private List<Canvas> tilesGUIRows;
    //2D List of the game board's GUI tiles.
    private List<List<Canvas>> tilesGUI;

    /**
     * Constructor for Display object.
     * Params: Stage, Int, Int, Double, Double, Int, Int
     * When the display object is created, it needs the following information
     * in order to properly design the game board GUI.
     * Returns: Nothing
     */
    public Display(Stage window, int numRows, int numCols, double ballWidth,
            double ballHeight, int ballStartPosX, int ballStartPosY){
        this.window = window;
        this.numRows = numRows;
        this.numCols = numCols;
        this.ballWidth = ballWidth;
        this.ballHeight = ballHeight;
        this.ballStartPosX = ballStartPosX;
        this.ballStartPosY = ballStartPosY;
    }

    /**
     * Initializes the GUI for the game board by embedding several layouts. The
     * out-most layout is a border pane, with a black canvas behind, and a clear
     * canvas stacked on top of it with a stack pane (to draw the ball on).
     * Mouse events are also defined for any mouse events happening on the
     * canvas.
     * Params: 2D List of Characters
     * The 2D list is used to later pass on to the grid, which will draw each
     * tile according to the 2D list (which is passed form the board class).
     * Returns: Nothing
     */
    public void initializeBoard(List<List<Character>> tiles){

        window.setTitle("Pinball");

        //Restrict window resizing to maintain GUI components' placements
        window.setMaxHeight(509);
        window.setMinHeight(509);
        window.setMaxWidth(266);
        window.setMinWidth(266);

        //GridPane to hold the tiles
        GridPane grid = createGridPane(tiles);

        //BorderPane that holds the grid of tiles, and the grey starting area
        BorderPane innerBorderPane = createInnerBorderPane(grid);

        //BorderPane that pulls all the nodes into a coherent screen
        BorderPane outerBorderPane = new BorderPane();

        //Initializes the "play area" which is stacked on top of the
        // innerBorderPane
        playArea = new Canvas(250, 420);
        StackPane stackpane = createPlayArea(innerBorderPane, playArea);

        //Initializes the score label
        createLabel();

        //Initializes the play and reset buttons
        createButtons();

        //Holds the buttons and labels
        controlPane = createControlPane();

        outerBorderPane.setTop(stackpane);
        outerBorderPane.setCenter(controlPane);

        //StackPane to put a full black canvas in the background
        // to have a black score board
        StackPane finalPane = new StackPane();

        Canvas blackBackgroundCanvas = new Canvas(250, 470);
        GraphicsContext gc = blackBackgroundCanvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, 250, 470);

        finalPane.getChildren().addAll(blackBackgroundCanvas, outerBorderPane);

        resetBallPosition();

        playArea.addEventHandler(MouseEvent.MOUSE_PRESSED,
                e -> drawBallInStartArea(e.getX(), e.getY()));

        playArea.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                e-> drawBallInStartArea(e.getX(), e.getY()));


        window.setScene(new Scene(finalPane, 250, 470));

        window.show();

    }

    /**
     * Creates the grid pane that holds the board GUI. The GUI is generated by
     * creating a Tile object (which generates a small canvas for each tile
     * filled with either blue or orange with a dark blue border). These canvas'
     * are then stored in a 2D list of canvases, to allow for later changes to
     * be made to the tile colors.
     * Params: 2D List of Characters
     * Returns: GridPane
     */
    private GridPane createGridPane(List<List<Character>> tiles) {
        GridPane grid = new GridPane();
        tilesGUI = new ArrayList<>();


        for(int r = 0; r < numRows; r++){
            tilesGUIRows = new ArrayList<>();
            for(int c = 0; c < numCols; c++){

                //Creates a new tile, and gives it a
                // color matching the board's data
                Tile tile = new Tile(TILE_WIDTH, TILE_HEIGHT,
                        tiles.get(r).get(c));

                //Creates tile canvas to be added to the grid
                Canvas canvas = tile.getTile();

                //Systematically adds tiles to the grid's GUI
                // in the same order as the board.
                grid.add(canvas, c, r);

                tilesGUIRows.add(canvas);
            }
            tilesGUI.add(tilesGUIRows);
        }

        return grid;
    }

    /**
     * Accesses 2D List of canvases that holds tile colors and changes the
     * specified tile to blue.
     * Params: Int Int
     * The row and column and needed to access the correct spot in the grid.
     * Returns: Nothing
     */
    public void changeTileBlue (int row, int col){
        GraphicsContext gc = tilesGUI.get(row).get(col).getGraphicsContext2D();

        gc.setFill(Color.ROYALBLUE);
        gc.setStroke(Color.DARKBLUE);

        gc.fillRect(0, 0, TILE_WIDTH, TILE_HEIGHT);
        gc.strokeRect(0, 0, TILE_WIDTH, TILE_HEIGHT);
    }

    /**
     * Accesses 2D List of canvases that holds tile colors and changes the
     * specified tile to Orange.
     * Params: Int Int
     * The row and column and needed to access the correct spot in the grid.
     * Returns: Nothing
     */
    public void changeTileOrange (int row, int col){
        GraphicsContext gc = tilesGUI.get(row).get(col).getGraphicsContext2D();

        gc.setFill(Color.ORANGE);
        gc.setStroke(Color.DARKBLUE);

        gc.fillRect(0, 0, TILE_WIDTH, TILE_HEIGHT);
        gc.strokeRect(0, 0, TILE_WIDTH, TILE_HEIGHT);
    }

    /**
     * Inner BorderPane used to hold the grid and black starting bar
     * for the ball.
     * Params: GridPane
     * Uses the GridPane to add to the top of this BorderPane.
     * Returns: BorderPane
    */
    private BorderPane createInnerBorderPane(GridPane grid){
        BorderPane borderpane = new BorderPane();

        Canvas canvas = new Canvas(250, 20);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.GRAY);
        gc.setStroke(Color.DARKBLUE);
        gc.fillRect(0, 0, 250, 20);
        gc.strokeRect(0, 0, 250, 20);

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 20, 250, 50);

        borderpane.setTop(grid);
        borderpane.setCenter(canvas);

        return borderpane;
    }

    /**
     * StackPane that stacks the canvas "playArea" on top of the outer border
     * pane to allow for a spot for the ball to move, and be repainted
     * as needed.
     * Params: BorderPane, Canvas
     * Returns: StackPane
    */
    private StackPane createPlayArea(BorderPane borderpane, Canvas playArea){
        StackPane stackpane = new StackPane();

        stackpane.getChildren().addAll(borderpane, playArea);

        return stackpane;
    }

    /**
     * Border pane used to hold the buttons and score label.
     * Params: None
     * Returns: BorderPane
     */
    private BorderPane createControlPane(){
        BorderPane controlPane = new BorderPane();

        controlPane.setLeft(reset);
        controlPane.setCenter(scoreLabel);
        controlPane.setRight(play);

        return controlPane;
    }

    /**
     * Initializes the score label.
     * Params: None
     * Returns: Nothing
     */
    private void createLabel(){

        scoreLabel = new Label("###");

        scoreLabel.setTextFill(Color.RED);
    }

    /**
     * Initializes the reset and play buttons.
     * Params: None
     * Returns: Nothing
     */
    private void createButtons(){
        reset = new Button();
        play = new Button();
    }

    /**
     * Taking the buttons created by Controls, updates the buttons on the
     * screen.
     * Params: Button Button
     * Returns: Nothing
     */
    public void updateButtons(Button r, Button p){
        controlPane.setLeft(r);
        controlPane.setRight(p);
    }

    /**
     * Draws the ball in the play area at the defined starting position.
     * Params: None
     * Returns: Nothing
     */
    public void resetBallPosition(){
        drawBall(ballStartPosX, ballStartPosY);
    }

    /**
     * Draws the ball at the given x, y point.
     * Params: double double
     * Returns: Nothing
     */
    public void drawBall(double x, double y){

        GraphicsContext gc = playArea.getGraphicsContext2D();

        gc.clearRect(0, 0, playArea.getWidth(), playArea.getHeight());

        gc.setFill(Color.RED);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);

        gc.strokeOval(x, y, ballWidth, ballHeight);
        gc.fillOval(x, y, ballWidth, ballHeight);

        ballPosX = x;
        ballPosY = y;
    }

    /**
     * If the game is in reset mode, allows the user to move the ball left or
     * right along the gray bar in the GUI.
     * Params: double double
     * Returns: Nothing
     */
    private void drawBallInStartArea(double x, double y){

        //top of bar y=400, bottom y=420. Leftmost part of bar x=0
        // (+8 for ball height/2),
        //rightmost part of bar x=250 (-8 for ball height/2)

        if(!gameInPlay){
            if(x <= 240 && x>= 10) {
                drawBall(x - (ballHeight/2), 410 - (ballHeight / 2));
            }else if(x > 240){
                drawBall(240 - (ballHeight/2), 410 - (ballHeight /2));
            }else if(x < 10){
                drawBall(10 - (ballHeight/2), 410 - (ballHeight /2));
            }
        }
    }

    /**
     * Updates the score label with current score.
     * Params: int
     * Returns: Nothing
     */
    public void updateScore(int x){
        scoreLabel.setText(Integer.toString(x));
    }

    /**
     * Params: None
     * Returns: x coordinate of the ball's position on the GUI
     */
    public double getBallPosX(){
        return ballPosX;
    }

    /**
     * Params: None
     * Returns: y coordinate of the ball's position on the GUI
    */
    public double getBallPosY(){
        return ballPosY;
    }

    /**
     * Changes gameInPlay flag to true. Used to decide if user can move the ball
     * with the mouse or not.
     * Params: None
     * Returns: None
     */
    public void setGameInPlay(){
        gameInPlay = true;
    }

    /**
     * Changes gameInPlay flag to true. Used to decide if user can move the ball
     * with the mouse or not.
     * Params: None
     * Returns: None
    */
    public void setGameInReset(){
        gameInPlay = false;
    }
}
