/**
 * Name: Tyler Fenske
 * Class: CS351
 * Date: 08/29/2018
 *
 * The Controls class holds the two buttons used in the game. Reset and Play.
 * After creation, it passes the buttons to display to be placed on the stage.
 * This class also keeps track of the state of the game, as the game state is
 * controlled by the two buttons.
 */

package Pinball;

import javafx.scene.control.Button;


public class Controls {

    private boolean inPlay;
    private Display display;
    private Button play;
    private Button reset;

    /**
     * Constructor for Controls object. Game starts in reset mode.
     * Params: display
     * Display is used to pass play and reset buttons to.
     * Returns: Nothing
     */
    public Controls(Display display){
        this.display = display;

        initializeButtons();

        inPlay = false;
    }

    /**
     * Params: None
     * Returns: Status of what mode the game is in (Reset | Play).
     */
    public boolean gameInPlayStatus(){
        return inPlay;
    }

    /**
     * Changes the game mode to Play.
     * Params: None
     * Returns: Nothing
     */
    private void setInPlay(){
        inPlay = true;
    }

    /**
     * Changes the game mode to Reset.
     * Params: None
     * Returns: Nothing
     */
    private void setInReset(){
        inPlay = false;
    }

    /**
     * Creates Play and Reset buttons, and gives them event handlers. When
     * play is pressed, the game status is updated, the play button changes from
     * yellow to grey, and the reset button is changed from grey to yellow.
     * Vice-Versa when the Reset button is pressed. If a button is grey, it is
     * inactive and cannot be pressed. After the buttons are created, they are
     * passed over to display to place on the GUI.
     * Params: None
     * Returns: Nothing
     */
    private void initializeButtons(){
        reset = new Button("reset");
        play = new Button("play");

        reset.setStyle("-fx-background-color: #E9E9E9;" +
                " -fx-font-size: 1.75em; -fx-font-weight: bold; " +
                "-fx-text-fill: #000000; ");
        /*        reset.setDisable(true);*/
        reset.setMouseTransparent(true);
        reset.setFocusTraversable(false);

        play.setStyle("-fx-background-color: #FFFF00;" +
                " -fx-font-size: 1.75em; -fx-font-weight: bold; " +
                "-fx-text-fill: #000000; ");

        reset.setMinSize(100, 50);
        play.setMinSize(100, 50);



        play.setOnAction(e -> {
            play.setStyle("-fx-background-color: #E9E9E9;" +
                    " -fx-font-size: 1.75em; -fx-font-weight: bold; " +
                    "-fx-text-fill: #000000; ");
            /*        reset.setDisable(true);*/
            play.setMouseTransparent(true);
            play.setFocusTraversable(false);

            reset.setMouseTransparent(false);
            reset.setFocusTraversable(true);
            reset.setStyle("-fx-background-color: #FFFF00;" +
                    " -fx-font-size: 1.75em; -fx-font-weight: bold; " +
                    "-fx-text-fill: #000000; ");
            setInPlay();
            display.setGameInPlay();
        });


        reset.setOnAction(e -> {
            reset.setStyle("-fx-background-color: #E9E9E9;" +
                    " -fx-font-size: 1.75em; -fx-font-weight: bold; " +
                    "-fx-text-fill: #000000; ");
            /*        reset.setDisable(true);*/
            reset.setMouseTransparent(true);
            reset.setFocusTraversable(false);

            play.setMouseTransparent(false);
            play.setFocusTraversable(true);
            play.setStyle("-fx-background-color: #FFFF00;" +
                    " -fx-font-size: 1.75em; -fx-font-weight: bold; " +
                    "-fx-text-fill: #000000; ");
            setInReset();
            display.resetBallPosition();
            display.setGameInReset();

        });

        display.updateButtons(reset, play);
    }
}
