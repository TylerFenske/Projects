/**
 * Name: Tyler Fenske
 * Class: CS351
 * Date: 09/13/2018
 *
 * Player has two instances. One for the human and one for the computer.
 * Human player (playerID = 1), uses an animation timer, and constantly
 * asks the display class what the user is doing
 * (communicated through booleans). The game flow logic is determined
 * primarily in this class.
 */

package Remi_Tile;

import javafx.animation.AnimationTimer;
import javafx.scene.media.*;

import java.io.File;
import java.io.InputStream;
import java.util.Random;

public class Player {

    private Hand hand;
    private Pool pool;
    private DiscardPile myDiscardPile;
    private DiscardPile enemyDiscardPile;
    private boolean turnFinished;
    private boolean drawPhase;
    private boolean newGame;
    private boolean hasWon = false;
    private int playerID;
    private static int playerIDGen = 0;

    private MediaPlayer draw;
    private MediaPlayer sort;
    private MediaPlayer swap;
    private MediaPlayer discard;


    /**
     * Constructor for a player instance. The player knows about their own hand,
     * discard pile, enemy discard pile, and the pool.
     * Params: Hand, Pool, DiscardPile, DiscardPile
     * Returns: Nothing
     */
    public Player(Hand hand, Pool pool, DiscardPile myDiscardPile,
                  DiscardPile enemyDiscardPile){
        this.hand = hand;
        this.pool = pool;
        this.myDiscardPile = myDiscardPile;
        this.enemyDiscardPile = enemyDiscardPile;

        playerIDGen++;
        playerID = playerIDGen;

        if(playerID == 1){
            turnFinished = false;
            drawPhase = true;
            initializeSounds();
        }else if(playerID == 2){
            turnFinished = true;
            drawPhase = false;
        }
    }


    /**
     * Computer's turn AI. The computer starts their turn by deciding to draw
     * between the player's discard pile and the deck (if player discard pile
     * isn't empty, there is a 25% chance the computer will decide to draw
     * from it. This low percentage was chosen as the computer blindly makes
     * this decision, and may just end up discarding the card it chose). Next,
     * the computer will sort their hand in the best possible way, then discard
     * the card of least significance.
     * Params: None
     * Returns: Nothing
     */
    public void automateTurn(){

        int r;
        Random rand = new Random();
        r = rand.nextInt(4);

        if(!enemyDiscardPile.isEmpty()){
            if(r == 0){
                drawFromDiscard();
                enemyDiscardPile.paintDiscardInDisplay();

            }else{
                drawFromPool();
            }
        }else{
            drawFromPool();
        }

        hand.sortHandComp();
        hand.paintHandInDisplay();

        discardTile();
        myDiscardPile.paintDiscardInDisplay();
        hand.paintHandInDisplay();

        if(hand.isWin()){
            hasWon = true;
        }

        turnFinished = true;
    }

    /**
     * Human player's turn procedure. Communicates with display via booleans,
     * and controls game states (draw phase, turn finished, etc)
     * Params: None
     * Returns: Nothing
     */
    public void startPlaying(){

        newGame = false;

        AnimationTimer a = new AnimationTimer() {
            @Override
            public void handle(long now) {
                boolean swapMade = hand.getSwapMade();
                boolean sortMade = hand.getSortMade();
                boolean tileDiscarded = hand.getTileDiscarded();
                boolean tileDrawn = hand.getTileDrawn();
                boolean discardDrawn = hand.getDiscardDrawn();

                if(swapMade){
                    hand.swapTiles();
                    hand.paintHandInDisplay();
                    swap.stop();
                    swap.play();
                }
                if(tileDiscarded && !turnFinished && !drawPhase){
                    discardTile();
                    hand.paintHandInDisplay();
                    myDiscardPile.paintDiscardInDisplay();
                    discard.stop();
                    discard.play();

                    if(hand.isWin()){
                        hasWon = true;
                    }

                    turnFinished = true;

                }else if(tileDiscarded){
                    hand.paintHandInDisplay();
                    myDiscardPile.paintDiscardInDisplay();
                }
                if(tileDrawn && drawPhase){
                    drawFromPool();
                    hand.paintHandInDisplay();
                    draw.stop();
                    draw.play();

                    drawPhase = false;
                }
                if(discardDrawn && drawPhase && !enemyDiscardPile.isEmpty()){
                    drawFromDiscard();
                    hand.paintHandInDisplay();
                    enemyDiscardPile.paintDiscardInDisplay();
                    drawPhase = false;
                    discard.stop();
                    discard.play();
                }
                if(sortMade){
                    hand.sortHand();
                    hand.paintHandInDisplay();
                    sort.stop();
                    sort.play();
                }
                if(newGame){
                    this.stop();
                }
            }
        };
        a.start();
    }

    /**
     * Returns if true if player's turn is over.
     * Params: None
     * Returns: Boolean
     */
    public boolean turnFinished(){
        return turnFinished;
    }

    /**
     * Returns if true if player's hand contains nothing but winning formations.
     * Params: None
     * Returns: Boolean
     */
    public boolean hasWon(){
        return hasWon;
    }


    /**
     * Draws one card from the pool. If the deck is empty, calls pool method
     * to fuse the discard piles (excluding the top cards) back into the deck,
     * then shuffle the deck.
     * Params: None
     * Returns: Nothing
     */
    public void drawFromPool(){
        if(!pool.isEmpty() && !hand.isFull()){
            hand.draw(pool.drawTile());
        }else if(pool.isEmpty() && !hand.isFull()){
            pool.mergeWithDiscardPiles(myDiscardPile.getDiscardPile(),
                    enemyDiscardPile.getDiscardPile());
            myDiscardPile.shuffleIntoDeck();
            enemyDiscardPile.shuffleIntoDeck();
            myDiscardPile.printPile();
            enemyDiscardPile.printPile();
            pool.printPool();
            hand.draw(pool.drawTile());
        }
    }

    /**
     * Draws the initial 14 cards at the start of a game or round.
     * Params: None
     * Returns: Nothing
     */
    public void drawInitialHand(){
        for(int i = 0; i < 14; i++){
            drawFromPool();
        }

        hand.paintHandInDisplay();
        myDiscardPile.paintDiscardInDisplay();
    }

    /**
     * Draws a card from the enemies discard pile.
     * Params: None
     * Returns: Nothing
     */
    public void drawFromDiscard(){
        if(!enemyDiscardPile.isEmpty() && !hand.isFull()){
            hand.draw(enemyDiscardPile.drawTopTile());
        }
    }

    /**
     * Discards a tile. Human's discarded card is determined by display, and
     * computer's is always MAX_HAND_SIZE-1.
     * Params: None
     * Returns: Nothing
     */
    public void discardTile(){
        Tile tile = hand.discard();
        myDiscardPile.discardTile(tile);
    }


    /**
     * Sets turnFinished to false. Called by GameCoordinator.
     * Params: None
     * Returns: Nothing
     */
    private void setTurnFinished(){
        turnFinished = false;
    }

    /**
     * Sets drawPhase to true. Called by GameCoordinator.
     * Params: None
     * Returns: Nothing
     */
    private void setDrawPhase(){
        drawPhase = true;
    }

    /**
     * In new games, player instances are recreated. Since playerIDGen is static
     * it must be set back to 0 during a new game.
     * Params: None
     * Returns: Nothing
     */
    public void setNewGame(){
        playerIDGen = 0;
        newGame = true;
    }

    /**
     * Calls listed functions that were described above.
     * Params: None
     * Returns: Nothing
     */
    public void newTurn(){
        setDrawPhase();
        setTurnFinished();
    }

    /**
     * Debugging method.
     * Params: None
     * Returns: Nothing
     */
    private void printBoardInfo(){
        myDiscardPile.printPile();
        hand.printHand();
        pool.printPool();

    }

    private void initializeSounds(){


        Media mediaDraw = new Media(getClass().
                getResource("/resources/draw.mp3").toExternalForm());
        draw = new MediaPlayer(mediaDraw);
        draw.setVolume(.5);

        Media mediaSort = new Media(getClass().
                getResource("/resources/sort.mp3").toExternalForm());
        sort = new MediaPlayer(mediaSort);
        sort.setVolume(.18);

        Media mediaSwap = new Media(getClass().
                getResource("/resources/swap.mp3").toExternalForm());
        swap = new MediaPlayer(mediaSwap);
        swap.setVolume(.185);

        Media mediaDiscard = new Media(getClass().
                getResource("/resources/discard.mp3").toExternalForm());
        discard = new MediaPlayer(mediaDiscard);
        discard.setVolume(.7);


    }

}
