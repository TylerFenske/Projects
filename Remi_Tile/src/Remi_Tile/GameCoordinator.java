/**
 * Name: Tyler Fenske
 * Class: CS351
 * Date: 09/13/2018
 *
 * The GameCoordinator is the top level class of the design tree. The main
 * program loop, and game logic is located in this class. This class uses an
 * AnimationTimer as it's source for the main program loop. This class has a
 * reference to every object in the game, and passes the Display object to all
 * relevant classes. New Games, and new rounds are controlled in this class.
 */

package Remi_Tile;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

public class GameCoordinator extends Application {

    private Display display;
    private Board board;
    private Score score1;
    private Score score2;
    private boolean firstGame = true;
    private boolean gameOver = false;
    private MediaPlayer background = null;
    private Player player1;
    private Player player2;

    /**
     * Mandatory override method for JavaFX. This method contains the Animation
     * timer as the main loop of the program. This method also creates all
     * objects that will be used during the duration of the game.
     * Params: Stage
     * primaryStage is the stage that will be used to show the entire game, and
     * is initially passed to display.
     * Returns: Nothing
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        display = new Display(primaryStage);


        AnimationTimer a = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(display.isNewGame()){
                    gameOver = false;

                    if(!firstGame){
                        player1.setNewGame();
                        board.startNewGame();
                        score1.startNewGame();
                    }

                    board = new Board(display);
                    score1 = new Score(display);
                    score2 = new Score(display);


                    player1 = new Player(board.getHand1(), board.getPool(),
                            board.getDiscard1(), board.getDiscard2());

                    player2 = new Player(board.getHand2(), board.getPool(),
                            board.getDiscard2(), board.getDiscard1());

                    player1.drawInitialHand();
                    player2.drawInitialHand();
                    player1.startPlaying();


                    if(background != null){
                        background.stop();
                    }

                    Media mediaBackground = new Media(getClass().
                            getResource("/resources/background.wav").
                            toString());
                    background = new MediaPlayer(mediaBackground);
                    background.setVolume(.25);
                    background.play();

                    firstGame = false;

                }else if((player1.hasWon() || player2.hasWon()) && !gameOver){

                    if(player1.hasWon()){
                        score1.addScore(-50);
                        score2.addScore(board.getHand2().calculateValue());
                        background.stop();
                        background.play();

                    }else if(player2.hasWon()){
                        score2.addScore(-50);
                        score1.addScore((board.getHand1().calculateValue()));
                        background.stop();
                        background.play();
                    }

                    if(score1.getScore() >= 200 || score2.getScore() >= 200){
                        display.gameOver(score1.getScore(), score2.getScore());
                        gameOver = true;
                    }else{
                        player1.setNewGame();
                        board.startNewGame();

                        board = new Board(display);

                        player1 = new Player(board.getHand1(), board.getPool(),
                                board.getDiscard1(), board.getDiscard2());

                        player2 = new Player(board.getHand2(), board.getPool(),
                                board.getDiscard2(), board.getDiscard1());

                        player1.drawInitialHand();
                        player2.drawInitialHand();
                        player1.startPlaying();
                    }

                }else if(player1.turnFinished()){
                    player2.newTurn();
                    player2.automateTurn();
                    player1.newTurn();
                }
            }
        };
        a.start();


        /*//debugging code
        Board board = new Board(display);

        board.getHand1().drawTest(new Tile(Color.BLUE, 5), 0);
        board.getHand1().drawTest(new Tile(Color.BLUE, 6), 1);
        board.getHand1().drawTest(new Tile(Color.BLUE, 7), 2);
        board.getHand1().drawTest(new Tile(Color.JOKER, 0), 3);
        board.getHand1().drawTest(new Tile(Color.BLUE, 9), 4);
        board.getHand1().drawTest(new Tile(Color.BLANK, -1), 5);
        board.getHand1().drawTest(new Tile(Color.GREEN, 5), 6);
        board.getHand1().drawTest(new Tile(Color.GREEN, 6), 7);
        board.getHand1().drawTest(new Tile(Color.GREEN, 7), 8);
        board.getHand1().drawTest(new Tile(Color.RED, 6), 9);
        board.getHand1().drawTest(new Tile(Color.RED, 7), 10);
        board.getHand1().drawTest(new Tile(Color.RED, 8), 11);
        board.getHand1().drawTest(new Tile(Color.RED, 9), 12);
        board.getHand1().drawTest(new Tile(Color.YELLOW, 9), 13);
        board.getHand1().drawTest(new Tile(Color.BLUE, 9), 14);


        System.out.println("WIN = " + board.getHand1().isWin());

        System.out.println("RUN = " + board.getHand1().isRun(10, 13));
        System.out.println("SET = " + board.getHand1().isSet(10, 13));*/


    }
}
