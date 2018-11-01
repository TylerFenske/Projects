/**
 * Name: Tyler Fenske
 * Class: CS351
 * Date: 08/29/2018
 *
 * The GameCoordinator the top level class of the design tree. The main program
 * loop, and game logic is located in this class. This class uses an
 * AnimationTimer as it's source for the main program loop. This class has a
 * reference to every object in the game, and passes the Display object to Ball,
 * Score, Controls, and Board.
 */

package Pinball;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import java.util.Random;

public class GameCoordinator extends Application{

    private final int BOARD_WIDTH = 250;
    private final int BOARD_HEIGHT = 470;
    private final int NUM_ROWS = 8;
    private final int NUM_COLS = 5;
    private final int BALL_WIDTH = 16;
    private final int BALL_HEIGHT = 16;
    private final int BALL_STARTING_POS_X = (BOARD_WIDTH/2)-(BALL_WIDTH/2);
    private final int BALL_STARTING_POS_Y = 402;
    private final char ORANGE_TILE = 'o';
    private final char BLUE_TILE = 'b';

    private double ballPosX;
    private double ballPosY;
    private double ballSpeed = 3;
    private double xDir;
    private double yDir = -ballSpeed;
    private int wallCount = 0;

    private boolean checkList = false;
    private boolean aboveGreyBar = false;

    private Board board;
    private Ball ball;
    private Score score;
    private Controls controls;
    private Display display;


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
    public void start(Stage primaryStage) throws Exception {
        display = new Display(primaryStage, NUM_ROWS, NUM_COLS,
                BALL_WIDTH, BALL_HEIGHT, BALL_STARTING_POS_X,
                BALL_STARTING_POS_Y);
        board = new Board(display, NUM_ROWS, NUM_COLS);
        ball = new Ball(display, BALL_STARTING_POS_X, BALL_STARTING_POS_Y);
        score = new Score(display);
        controls = new Controls(display);

        AnimationTimer a = new AnimationTimer () {

            @Override
            public void handle ( long now ) {
                if(controls.gameInPlayStatus()){
                    if(checkList){
                        checkList = false;
                    }

                    if(wallCount < 3){
                        shootBall();
                        updateBoard();
                    }

                }else{
                    if(!checkList){
                        generateOrangeTiles();
                        xDir = generateRandomDirection();
                        wallCount = 0;
                        aboveGreyBar = false;
                        checkList = true;
                    }
                    ball.updateBallPosition();
                }
            }
        };
        a. start ();
    }

    /**
     * Figures out the balls' position by asking the ball object where it is.
     * If it is about to hit a wall, changes the balls' direction,
     * and adds to the wall count.
     * Params: None
     * Returns: Nothing
     */
    private void shootBall(){

        ballPosX = ball.getBallPosX();
        ballPosY = ball.getBallPosY();

        if(ballPosY < 350){
            aboveGreyBar = true;
        }

        if(ballPosY + yDir < 0){
            yDir = ballSpeed;
            wallCount++;
        }else if (ballPosY + yDir + BALL_HEIGHT > 400){
            yDir = -ballSpeed;

            if(aboveGreyBar){
                wallCount++;
            }
        }

        if(ballPosX + xDir < 0){
            xDir = ballSpeed;
            wallCount++;
        }else if(ballPosX + xDir + BALL_WIDTH > 250){
            xDir = -ballSpeed;
            wallCount++;
        }

        display.drawBall(ballPosX, ballPosY);
        ball.moveBall(ballPosX+xDir, ballPosY+yDir);
    }

    /**
     * Using two random number generators, decides a random direction for the
     * ball to travel (10 possible values total).
     * Params: None
     * Returns: Randomly generated integer between [-5, 5] excluding 0
     */
    private double generateRandomDirection(){
        double num;
        int sign;
        Random rand = new Random();
        Random decideSign = new Random();

        num = (double) rand.nextInt(5) + 1;
        sign = decideSign.nextInt(2);

        if(sign == 0){
            num *= -1;
        }

        return num;
    }

    /**
     * Checks if the ball is over an orange tile. If it is, changes the tile to
     * blue, adds points to the score, and resets the wall count.
     * Params: None
     * Returns: Nothing
     */
    private void updateBoard(){
        int col = (int) (ballPosX + (BALL_WIDTH/2))/50;
        int row = (int) (ballPosY + (BALL_HEIGHT/2))/50;

        if(col < NUM_COLS && row < NUM_ROWS){
            if(board.getTileColor(row, col) == ORANGE_TILE){
                board.changeTileColor(row, col);
                score.addPoints();
                wallCount = 0;
            }
        }
    }

    /**
     * Generates 3 random orange tiles, by creating 3 pairs of unique
     * coordinates for the grid. The method will rerun until 3 unique
     * pairs are generated to assure 3 orange tiles are created.
     * Params: None
     * Returns: Nothing
     */
    private void generateOrangeTiles(){
        boolean possibleDuplicates = true;
        int numR1, numR2, numR3, numC1, numC2, numC3;

        board.resetBoard();

        do{
            Random rand = new Random();

            numR1 = rand.nextInt(NUM_ROWS);
            numR2 = rand.nextInt(NUM_ROWS);
            numR3 = rand.nextInt(NUM_ROWS);
            numC1 = rand.nextInt(NUM_COLS);
            numC2 = rand.nextInt(NUM_COLS);
            numC3 = rand.nextInt(NUM_COLS);

            if(!((numR1 == numR2)&&(numC1 == numC2) ||
                    (numR1 == numR3)&&(numC1 == numC3) ||
                    (numR2 == numR3)&&(numC2 == numC3))){
                possibleDuplicates = false;
            }
        }while(possibleDuplicates);

        board.changeTileColor(numR1, numC1);
        board.changeTileColor(numR2, numC2);
        board.changeTileColor(numR3, numC3);
    }
}
