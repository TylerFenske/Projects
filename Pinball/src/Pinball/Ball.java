/**
 * Name: Tyler Fenske
 * Class: CS351
 * Date: 08/29/2018
 *
 * The Ball class keeps track of the ball object created by GameCoordinator. The
 * ball always keeps track of it's position on the board, and remembers where to
 * go if the game is in reset mode.
 */

package Pinball;

public class Ball {

    private Display display;
    private double ballPosX;
    private double ballPosY;
    private int ballStartPosX;
    private int ballStartPosY;

    /**
     * Constructor for Ball. Receives and notes it's starting position, and
     * updates it's current position with that information.
     * Params: Display, Int, Int
     * Tells display where to draw it on the screen.
     * Returns: Nothing
     */
    public Ball(Display display, int ballStartPosX, int ballStartPosY) {
        this.ballStartPosX = ballStartPosX;
        this.ballStartPosY = ballStartPosY;
        this.display = display;

        reset();
    }

    /**
     * Updates ball position to starting position.
     * Params: None
     * Returns: Nothing
     */
    private void reset(){
        ballPosX = ballStartPosX;
        ballPosY = ballStartPosY;
    }

    /**
     * Moves ball to specified location passed as arguments.
     * Params: Double, Double
     * Returns: Nothing
     */
    public void moveBall(double x, double y){
        ballPosX = x;
        ballPosY = y;
    }

    /**
     * Gets the ball's position based on where it is at on the GUI
     * Manipulated during the reset mode when mouse events
     * can redraw the ball without the ball requesting it.
     * Params: None
     * Returns: Nothing
     */
    public void updateBallPosition(){
        ballPosX = display.getBallPosX();
        ballPosY = display.getBallPosY();
    }

    /**
     * Param: None
     * Returns: X coordinate of ball position
     */
    public double getBallPosX(){
        return ballPosX;
    }

    /**
     * Param: None
     * Returns: Y coordinate of ball position
     */
    public double getBallPosY(){
        return ballPosY;
    }

}
