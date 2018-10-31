/**
 * Name: Tyler Fenske
 * Class: CS351
 * Date: 09/13/2018
 *
 * The Display class creates JavaFX nodes to be added to the Stage. This class
 * is at the lowest level of the design tree, and is passed no references. This
 * class' main purpose is to display the GUI of the game.
 *
 * Included in this class are event handlers for all buttons and mouse controls.
 */

package Remi_Tile;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;


public class Display {

    private Stage window;
    private Scene mainScene;
    private Canvas playArea;
    private Canvas poolArea;
    private Canvas discardPile1;
    private Canvas discardPile2;
    private Button newGame;
    private Button gameRules;
    private Button sortHand;
    private Label score1;
    private Label score2;
    private TileGUI blankTile;

    private List<Canvas> hand1;
    private List<Canvas> hand2;

    private List<TileGUI> handInfo1;

    private int pressedTile;
    private int releasedTile;
    private boolean showComputerCards = false;
    private int swapX;
    private int swapY;

    private boolean swapMade = false;
    private boolean sortMade = false;
    private boolean tileDiscarded = false;
    private boolean tileDrawn = false;
    private boolean discardDrawn = false;
    private boolean isNewGame = true;

    private final double TILE_WIDTH = 56;
    private final double TILE_HEIGHT = 84;
    private final double POOL_WIDTH = 100;
    private final double POOL_HEIGHT = 115;
    private final double TILE_SPACING = 5;
    private final double POOLX = 25;
    private final double POOLY = 200;
    private final int DISCARD1ID = 16;
    private final int DISCARD2ID = 17;
    private final int POOLID = 18;
    private final int MAX_HAND_SIZE = 15;


    /**
     * Constructor for Display object.
     * Params: Stage
     * When the display object is created, it needs the following information
     * in order to properly design the game board GUI.
     * Returns: Nothing
     */
    public Display(Stage stage) throws Exception{
        this.window = stage;
        initializeBoardGUI();
    }

    /**
     * Initializes the GUI for the game board by embedding several layouts. The
     * out-most layout is a stackPane, which holds the background image in back,
     * and the main borderPane on top. The borderPane holds two Vbox's on it's
     * left and right sides, a gridPane on the top and bottom (to hold the hands
     * of each player, and finally a borderPane in the center. The center pane
     * holds a canvas for the pool on the left, and a grid in the center, for
     * possible future updates to the game requiring the center of the board.
     * Params: None
     * Returns: Nothing
     */
    private void initializeBoardGUI() throws Exception{
        window.setTitle("Remi Tile");

        window.setMaxHeight(730);
        window.setMinHeight(730);
        window.setMaxWidth(1300);
        window.setMinWidth(1300);

        initializeComponents();
        initializePoolArea();

        GridPane topGrid = createTopGridPane();
        GridPane midGrid = createMiddleGridPane();
        GridPane botGrid = createBottomGridPane();

        VBox leftPanel = createLeftPanel();
        VBox rightPanel = createRightPanel();

        BorderPane innerBorder = createInnerBorderPane(topGrid, midGrid,
                botGrid);

        StackPane centerStack = createStackPane(innerBorder);

        BorderPane outerBorder = createOuterBorderPane(leftPanel, rightPanel,
                centerStack);

        playArea.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                e -> {
                    if((pressedTile >= 0 && pressedTile <= MAX_HAND_SIZE-1)
                            && (handInfo1.get(pressedTile).getRank() != -1)){
                        paintCard(e.getX(), e.getY());
                    }
                });

        playArea.addEventHandler(MouseEvent.MOUSE_PRESSED,
                e -> {
                    pressedTile = findNodeIndex(e.getX(), e.getY());
                    if((pressedTile >= 0 && pressedTile <= MAX_HAND_SIZE-1)
                            && (handInfo1.get(pressedTile).getRank() != -1)){
                        makeTileBlank();
                        paintCard(e.getX(), e.getY());
                    }

                    if(pressedTile == POOLID){
                        tileDrawn = true;
                    }

                    if(pressedTile == DISCARD2ID){
                        discardDrawn = true;
                    }
                });

        playArea.addEventHandler(MouseEvent.MOUSE_RELEASED,
                e -> {
                    if((pressedTile >= 0 && pressedTile <= MAX_HAND_SIZE-1)
                            && (handInfo1.get(pressedTile).getRank() != -1)){
                        releasedTile = findNodeIndex(e.getX(), e.getY());
                        if((releasedTile >= 0
                                && releasedTile <= MAX_HAND_SIZE-1)
                                && (pressedTile != releasedTile)){
                            swapTiles();
                        }else if(releasedTile == DISCARD1ID){
                            tileDiscarded = true;
                            clearPlayArea();
                        }else{
                            returnTileToPosition();
                            clearPlayArea();
                        }
                    }
                });

        StackPane stackPane = new StackPane();

        TileGUI background = new TileGUI(Color.BACKGROUND, -1);


        stackPane.getChildren().addAll(background.getImgView(), outerBorder);

        mainScene = new Scene(stackPane, 1300, 660);

        window.setScene(mainScene);
        window.show();
    }

    /**
     * Initializes the components of the board. Both score labels, newGame,
     * sortHand, and gameRules buttons. Blank tiles are initialized here as
     * well.
     * Params: None
     * Returns: Nothing
     */
    private void initializeComponents(){
        createNewGameButton();


        sortHand = createSortHandButton();

        score1 = createScoreLabel();
        score2 = createScoreLabel();


        score1.setTranslateX(13);
        score1.setTranslateY(605);

        score2.setTranslateX(13);
        score2.setTranslateY(33);

        initializeGameRules();


        blankTile = new TileGUI(Color.BLANK, -1);
    }

    /**
     * Initializes the components of the board. Both score labels, newGame,
     * sortHand, and gameRules buttons. Blank tiles are initialized here as
     * well.
     * Params: None
     * Returns: Nothing
     */
    private void createNewGameButton(){
        newGame = new Button("New Game");

        newGame.setTranslateX(2);
        newGame.setTranslateY(10);
        newGame.setStyle("-fx-font-weight: bold;" +
                " -fx-font-size: .9em;");
        newGame.setMinSize(82, 25);
        newGame.setMaxSize(82, 25);

        newGame.setOnAction( e-> isNewGame = true);

    }

    /**
     * Initializes the sort hand button, formatting it to specifications listed.
     * Params: None
     * Returns: Button
     */
    private Button createSortHandButton(){
        Button button = new Button("Sort \nHand");

        button.setStyle("-fx-background-color: #E9E9E9;" +
                " -fx-font-size: 1.75em; -fx-font-weight: bold; " +
                "-fx-text-fill: #000000; ");

        button.setOnAction( e-> sortMade = true);


        button.setMinSize(86, 50);
        button.setTextAlignment(TextAlignment.CENTER);


        button.setTranslateY(500);
        button.setTranslateX(0);

        return button;
    }

    /**
     * Initializes a score label, formatting it to specifications listed.
     * Params: None
     * Returns: Label
     */
    private Label createScoreLabel(){
        Label score = new Label("XXXXX");
        score.setAlignment(Pos.CENTER);

        score.setStyle("-fx-background-color: #676767;" +
                " -fx-font-size: 1.55em; -fx-font-weight: bold; " +
                "-fx-text-fill: #000000; ");

        score.setMinSize(49, 25);

        return score;
    }

    /**
     * Initializes the left VBox panel.
     * Params: None
     * Returns: VBox
     */
    private VBox createLeftPanel(){
        VBox leftPane = new VBox();

        leftPane.setMinWidth(75);
        leftPane.setMaxWidth(75);

        leftPane.getChildren().addAll(score2, score1);

        return leftPane;
    }

    /**
     * Initializes the right VBox panel.
     * Params: None
     * Returns: VBox
     */
    private VBox createRightPanel(){
        VBox rightPane = new VBox();

        rightPane.getChildren().addAll(sortHand, newGame, gameRules);

        return rightPane;
    }

    /**
     * Initializes the inner borderPane.
     * Params: GridPane, GridPane, GridPane
     * Returns: BorderPane
     */
    private BorderPane createInnerBorderPane(GridPane top, GridPane middle,
                                             GridPane bottom){
        BorderPane borderPane = new BorderPane();
        BorderPane innerBorderPane = new BorderPane();

        innerBorderPane.setLeft(poolArea);
        innerBorderPane.setRight(middle);

        borderPane.setTop(top);
        borderPane.setCenter(innerBorderPane);
        borderPane.setBottom(bottom);

        return borderPane;

    }

    /**
     * Initializes the main borderPane.
     * Params: VBox, VBox, StackPane
     * Returns: BorderPane
     */
    private BorderPane createOuterBorderPane(VBox left, VBox right,
                                             StackPane center){

        BorderPane borderPane = new BorderPane();

        borderPane.setLeft(left);
        borderPane.setRight(right);
        borderPane.setCenter(center);

        return borderPane;
    }

    /**
     * Initializes top gridPane. Draws tiles initially as blank tiles in the
     * hand of the computer player (since this is the top hand on the screen).
     * An empty discard image is also manually placed here. The list of tiles
     * in each player's hand are saved in a list of canvas' to be repainted
     * later when changes occur.
     * Params: None
     * Returns: GridPane
     */
    private GridPane createTopGridPane(){
        GridPane gridPane = new GridPane();

        hand2 = new ArrayList<>();

        //discard pile
        TileGUI tile0 = new TileGUI(Color.DISCARD, -1);
        discardPile2 = new Canvas(TILE_WIDTH, TILE_HEIGHT);
        GraphicsContext gc0 = discardPile2.getGraphicsContext2D();
        gc0.drawImage(tile0.getTileImage(), 0, 0);
        gridPane.add(discardPile2, 0, 0);
        GridPane.setMargin(discardPile2, new Insets(TILE_SPACING));

        //blank space between discard and hand
        Canvas canvas1 = new Canvas(TILE_WIDTH, TILE_HEIGHT);
        gridPane.add(canvas1, 1, 0);
        GridPane.setMargin(canvas1, new Insets(TILE_SPACING));

        for(int i = 2; i < MAX_HAND_SIZE+2; i++){
            Canvas canvas = new Canvas(TILE_WIDTH, TILE_HEIGHT);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.drawImage(blankTile.getTileImage(), 0, 0);
            gridPane.add(canvas, i, 0);
            GridPane.setMargin(canvas, new Insets(TILE_SPACING));

            hand2.add(i-2, canvas);
        }

        return gridPane;
    }

    /**
     * Initializes middle gridPane. Currently just exists to take up space, and
     * allow for future additions in this spot of the board.
     * Params: None
     * Returns: GridPane
     */
    private GridPane createMiddleGridPane(){
        GridPane gridPane = new GridPane();

        return gridPane;
    }

    /**
     * Initializes bottom gridPane. Draws tiles initially as blank tiles in the
     * hand of the human player (since this is the bottom hand on the screen).
     * An empty discard image is also manually placed here. The list of tiles
     * in each player's hand are saved in a list of canvas' to be repainted
     * later when changes occur.
     * Params: None
     * Returns: GridPane
     */
    private GridPane createBottomGridPane(){
        GridPane gridPane = new GridPane();

        hand1 = new ArrayList<>();
        handInfo1 = new ArrayList<>();

        for(int i = 0; i < MAX_HAND_SIZE; i++){
            Canvas canvas = new Canvas(TILE_WIDTH, TILE_HEIGHT);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.drawImage(blankTile.getTileImage(), 0, 0);
            gridPane.add(canvas, i, 0);
            GridPane.setMargin(canvas, new Insets(TILE_SPACING));

            hand1.add(i, canvas);
            handInfo1.add(i, blankTile);
        }

        //blank space between discard and hand
        Canvas canvas1 = new Canvas(TILE_WIDTH, TILE_HEIGHT);
        gridPane.add(canvas1, 15, 0);
        GridPane.setMargin(canvas1, new Insets(TILE_SPACING));

        //discard pile
        TileGUI tile0 = new TileGUI(Color.DISCARD, -1);
        discardPile1 = new Canvas(TILE_WIDTH, TILE_HEIGHT);
        GraphicsContext gc0 = discardPile1.getGraphicsContext2D();
        gc0.drawImage(tile0.getTileImage(), 0, 0);
        gridPane.add(discardPile1, 16, 0);
        GridPane.setMargin(discardPile1, new Insets(TILE_SPACING));

        return gridPane;
    }

    /**
     * Initializes the center stackPane. This holds the borderPane that holds
     * the grids to display the hands, and stacks a canvas on top to allow for
     * moving images.
     * Params: BorderPane
     * Returns: StackPane
     */
    private StackPane createStackPane(BorderPane borderPane){
        StackPane stackPane = new StackPane();

        playArea = new Canvas(1125, 685);

        stackPane.getChildren().addAll(borderPane, playArea);

        return stackPane;
    }

    /**
     * Initializes canvas poolArea, which is where all moving cards are drawn.
     * Params: None
     * Returns: Nothing
     */
    private void initializePoolArea(){
        poolArea = new Canvas(125,500);

        TileGUI tile = new TileGUI(Color.POOL, -1);

        Image tileImg = tile.getTileImage();

        GraphicsContext gc = poolArea.getGraphicsContext2D();

        gc.drawImage(tileImg, POOLX, POOLY);
    }

    /**
     * Draws a tile on the screen at the given x, y values provided by the
     * mouse event handler.
     * Params: Double, Double
     * Returns: Nothing
     */
    private void paintCard(double x, double y){
        Image tileImg = handInfo1.get(pressedTile).getTileImage();

        GraphicsContext gc = playArea.getGraphicsContext2D();

        gc.clearRect(0, 0, playArea.getWidth(), playArea.getHeight());

        gc.drawImage(tileImg, x - TILE_WIDTH/2, y - TILE_HEIGHT/2);
    }

    /**
     * Draws a player's entire hand to their side of the screen. This is done
     * by looking at a list of the player's hand, passed from Hand class.
     * The computer's tiles are drawn face down, showing just the tile back
     * image, unless flagged for debugging. When paint hand is called, many
     * boolean values are changed in order to tell the Animation Timer in Player
     * class that an event has occurred in the display.
     * Params: List<Tile>, Int
     * Returns: Nothing
     */
    public void paintHand(List<Tile> hand, int playerID){
        TileGUI tileGUI = null;

        for(int i = 0; i < MAX_HAND_SIZE; i++){
            GraphicsContext gc;

            if(playerID == 1){
                gc = hand1.get(i).getGraphicsContext2D();
            }else{
                gc = hand2.get(i).getGraphicsContext2D();
            }

            if(!showComputerCards && playerID == 2){
                if(hand.get(i).getColor() == Color.BLANK){
                    tileGUI = new TileGUI(Color.BLANK, -1);
                }else{
                    tileGUI = new TileGUI(Color.BACK, -1);
                }

                gc.clearRect(0, 0, TILE_WIDTH, TILE_HEIGHT);
                gc.drawImage(tileGUI.getTileImage(), 0, 0);

            }else{
                tileGUI = new TileGUI(hand.get(i).getColor(),
                        hand.get(i).getRank());

                gc.clearRect(0, 0, TILE_WIDTH, TILE_HEIGHT);
                gc.drawImage(tileGUI.getTileImage(), 0, 0);
            }

            if(playerID == 1){
                handInfo1.set(i, tileGUI);
            }
        }

        if(playerID == 1){
            swapMade = false;
            tileDrawn = false;
            sortMade = false;
        }
        isNewGame = false;
    }

    /**
     * Systematically assigns an index to each card on the board GUI. This
     * method is called anytime a spot on the board is clicked. A return value
     * of [0, 14] indicates the user is clicking on one of the 15 human tile
     * spaces. A return value of 16 means the human player's discard is being
     * clicked on. A return value of 17 means the computer player's discard is
     * being clicked on. A return value of 18 means the pool is being clicked
     * on. The double's passed in this function are from the mouse x and y
     * positions on the screen.
     * Params: Double, Double
     * Returns: Int
     */
    private int findNodeIndex(double x, double y){
        int gridIdx = -1;
        int upperBound = (int) TILE_WIDTH;
        int lowerBound = 0;

        if(y >= 599 && y <= 683){
            for(int i = 0; i < MAX_HAND_SIZE+2; i++){
                if(x >= lowerBound && x <= upperBound){
                    gridIdx = i;
                }

                upperBound += (TILE_WIDTH + (TILE_SPACING*2));
                lowerBound = upperBound - (int) TILE_WIDTH;
            }

        }

        if(y >= 291 && y <= 291+POOL_HEIGHT){
            if(x >= 20 && x <= 20+POOL_WIDTH){
                gridIdx = POOLID;
            }
        }

        if(y >= 2 && y <= 2+TILE_HEIGHT){
            if(x >= 0 && x <= TILE_WIDTH){
                gridIdx = DISCARD2ID;
            }
        }

        return gridIdx;
    }

    /**
     * Finds the last tile clicked on the screen, and changes it to a blank
     * space.
     * Params: None
     * Returns: Nothing
     */
    private void makeTileBlank(){
        GraphicsContext gc = hand1.get(pressedTile).getGraphicsContext2D();

        gc.clearRect(0, 0, TILE_WIDTH, TILE_HEIGHT);
        gc.drawImage(blankTile.getTileImage(), 0, 0);
    }

    /**
     * Draws a tile back to where it was before it was dragged away from it's
     * original position. Used when player doesn't drag a card over a valid
     * area.
     * Params: None
     * Returns: Nothing
     */
    private void returnTileToPosition(){
        TileGUI tileGUI = handInfo1.get(pressedTile);
        GraphicsContext gc = hand1.get(pressedTile).getGraphicsContext2D();

        gc.clearRect(0, 0, TILE_WIDTH, TILE_HEIGHT);
        gc.drawImage(tileGUI.getTileImage(), 0, 0);
    }

    /**
     * Cleans the play area of any graphics.
     * Params: None
     * Returns: Nothing
     */
    private void clearPlayArea(){
        GraphicsContext gc = playArea.getGraphicsContext2D();

        gc.clearRect(0, 0, playArea.getWidth(), playArea.getHeight());
    }


    /**
     * Returns true when two cards were swapped on the GUI by the user.
     * Params: None
     * Returns: Boolean
     */
    public boolean swapMade(){
        return swapMade;
    }

    /**
     * Returns true when sort hand button is clicked.
     * Params: None
     * Returns: Boolean
     */
    public boolean sortMade(){
        return sortMade;
    }

    /**
     * Returns true when game first starts, and when newGame button is pressed.
     * Params: None
     * Returns: Boolean
     */
    public boolean isNewGame(){
        return isNewGame;
    }

    /**
     * Returns true when tile has been discarded in the GUI by user.
     * Params: None
     * Returns: Boolean
     */
    public boolean tileDiscarded(){
        return tileDiscarded;
    }

    /**
     * Returns true when pool has been clicked in the GUI by user.
     * Params: None
     * Returns: Boolean
     */
    public boolean tileDrawn(){
        return tileDrawn;
    }

    /**
     * Returns true when computer discard pile has been clicked in the GUI by
     * user.
     * Params: None
     * Returns: Boolean
     */
    public boolean discardDrawn(){
        return discardDrawn;
    }

    /**
     * Returns the index of one of the tiles involved in a GUI swap operation.
     * Params: None
     * Returns: Int
     */
    public int getSwapX(){
        return swapX;
    }

    /**
     * Returns the index of one of the tiles involved in a GUI swap operation.
     * Params: None
     * Returns: Int
     */
    public int getSwapY(){
        return swapY;
    }

    /**
     * Returns the index of the tile discarded in a GUI discard operation.
     * Params: None
     * Returns: Int
     */
    public int getTileDiscarded(){
        return pressedTile;
    }

    /**
     * Updates score label based on playerID.
     * Params: Int, Int
     * Returns: Nothing
     */
    public void updateScore(int score, int scoreID){
        if(scoreID == 1){
            score1.setText(Integer.toString(score));
        }else{
            score2.setText(Integer.toString(score));
        }
    }

    /**
     * Updates relevant variables when swap is made in GUI by user.
     * Params: None
     * Returns: Nothing
     */
    private void swapTiles(){
        swapX = pressedTile;
        swapY = releasedTile;
        clearPlayArea();
        swapMade = true;
    }

    /**
     * Paints the top tile of the passed playerID's discard pile to the discard
     * pile area on the board for the respective player.
     * Params: Tile, Int
     * Returns: Nothing
     */
    public void paintDiscardPile(Tile tile, int playerID){
        GraphicsContext gc;

        if(playerID == 1){
            gc = discardPile1.getGraphicsContext2D();
        }else{
            gc = discardPile2.getGraphicsContext2D();
        }

        gc.clearRect(0, 0, TILE_WIDTH, TILE_HEIGHT);

        TileGUI tileGUI = new TileGUI(tile.getColor(), tile.getRank());

        gc.drawImage(tileGUI.getTileImage(), 0, 0);

        tileDiscarded = false;
        discardDrawn = false;
    }

    /**
     * Creates a pop window (that cannot be closed by any means other than
     * pressing the newGame button) that lets the user know that the game has
     * ended, and notifies them of the winner (calculated by passing the
     * method both player's scores).
     * Params: Int, Int
     * Returns: Nothing
     */
    public void gameOver(int score1, int score2){
        BorderPane borderPane = new BorderPane();
        Label gameOver = new Label("GAME OVER!");
        Label humanWins = new Label("You Win!");
        Label computerWins = new Label("You Lose.");
        Button newGame = new Button("New Game");
        Scene scene2 = new Scene(borderPane, 300, 300);
        Stage stage = new Stage();

        borderPane.setTop(gameOver);
        borderPane.setBottom(newGame);

        newGame.setTranslateX(115);
        newGame.setTranslateY(0);

        gameOver.setTranslateX(115);

        newGame.setOnAction(e -> {
            isNewGame = true;
            stage.close();
        });

        if(score1 > score2){
            borderPane.setCenter(computerWins);
        }else{
            borderPane.setCenter(humanWins);
        }

        stage.setOnCloseRequest(e -> e.consume());
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setScene(scene2);

        stage.show();
    }

    /**
     * initializes the gameRules button. When pressed, pops up with a separate
     * window (that can be closed and resized at any time). The window displays
     * an image that explains the rules of the game.
     * Params: None
     * Returns: Nothing
     */
    private void initializeGameRules(){
        gameRules = new Button("Game Rules");

        gameRules.setTranslateX(2);
        gameRules.setTranslateY(40);
        gameRules.setStyle("-fx-font-weight: bold;" +
                " -fx-font-size: .9em;");
        gameRules.setMinSize(82, 25);
        gameRules.setMaxSize(82, 25);

        TileGUI rulesImg = new TileGUI(Color.RULES, -1);

        StackPane stackPane = new StackPane();

        stackPane.getChildren().addAll(rulesImg.getImgView());

        Scene scene = new Scene(stackPane, 1300, 660);

        Stage stage = new Stage();

        stage.setScene(scene);

        gameRules.setOnAction(e -> stage.show());
    }
}
