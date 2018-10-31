/**
 * This class constructs the GUI JFrame and ties all GUI components together. 
 * It determines when the game is won, or lost, and updates the players
 * points on the screen. Also contains text and audioclips to help 
 * tell the story of the game.
 * 
 * @author Tyler Fenske
 * @version 2016-12-06
 */

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.*;


public class BubbleFrame extends JFrame implements GamePanel.ScoreListener{
    
    /** 
     * Constant strings used to fill the JButton at 
     * different instances of the game. 
     * */
    private static final String START = "If I gotta... [BEGIN DESTRUCTION]";
    private static final String PAUSE1 = "You mean this? [INSPECT PLAY THING]";
    private static final String PAUSE2 = "But it's fun... [INSPECT PLAY THING]";
    private static final String UNPAUSE = "Maybe later. [GET BACK TO WORK]";
    private static final String WIN = "YOU WIN?";
    private static final String LOSE = "YOU DIED";
    
    /** JLabels used to display score */
    private JLabel pointsLabel = new JLabel();
    private JLabel crystalBallsLabel = new JLabel();
    private JLabel shatteredLabel = new JLabel();
    
    /** JLabels used to display story text */
    private JLabel topLabel = new JLabel();
    private JLabel botLabel = new JLabel();
    
    /** The game panel */
    private JPanel boardPanel;
    
    /** The button used to Start, Pause, and Resume game */
    private JButton startButton;
    
    /** Keeps track of whether or not this is a new game */
    private boolean isNewGame = true;
    /** Keeps track of whether or not the game is paused */
    private boolean isPaused = true;
    
    /** Audioclips used during the game */
    private AudioClip start;
    private AudioClip lose;
    private AudioClip background;
    private AudioClip win;
    private AudioClip pause;
    
    /** 
     * Creates all JComponents for the game and adds them to the JFrame. 
     * Also loads up sounds, and creates action listener for the JButton.
     */
    public BubbleFrame() {
        super("Tish Busts-a-Village");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        start = loadSound("Start.wav");
        lose = loadSound("lose.wav");
        background = loadSound("background2.wav");
        win = loadSound("win.wav");
        pause = loadSound("pause.wav");
        

        boardPanel = new GamePanel(7, 9);
        ((GamePanel) boardPanel).addScoreListener(this);
        updatePoints(0, 0);

        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        startButton = new JButton(START);
        startButton.setToolTipText("Starts the game");
        startButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ev){
                isPaused = !isPaused;
                ((GamePanel) boardPanel).setPlaying(!isPaused);
                if(!isPaused && isNewGame){
                    startButton.setText(PAUSE1);
                    startButton.setToolTipText("Pauses the game");
                    topLabel.setBounds(133, 0, 550, 25);
                    topLabel.setText("[Tish]: Oh my, you're easy! Now get to work, and don't even");
                    botLabel.setBounds(172, 12, 500, 25);
                    botLabel.setText("think about touching my little play thing over there.");
                    start.play();
                    background.loop();
                    boardPanel.requestFocusInWindow();
                    isNewGame = false;
                }else if(!isPaused){
                    startButton.setText(PAUSE2);
                    startButton.setToolTipText("Pauses the game");
                    topLabel.setBounds(115, 0, 550, 25);
                    topLabel.setText("[Tish]: Oh my, you're bad at following instructions. Focus imbecile!");
                    start.play();
                    background.loop();
                    boardPanel.requestFocusInWindow();
                }else{
                    startButton.setText(UNPAUSE);
                    startButton.setToolTipText("Resumes the game");
                    topLabel.setBounds(95, 0, 550, 25);
                    topLabel.setText("[Play Thing]: Help me! She uses me as a bath toy! You've gotta get me out!");
                    botLabel.setBounds(50, 12, 0, 0);
                    botLabel.setText("");
                    background.stop();
                    pause.play();
                }
            }
        });
        
        
        topLabel = new JLabel();
        topLabel.setText("[Tish]: Yes! The summoning was successful! Wow, humans are ugly... Well, if you want to live,");
        
        botLabel = new JLabel();
        botLabel.setText("help me wreck this village of cuddly critters. They're too cute not to destroy!");
        
        topLabel.setBounds(39, 0, 550, 25);
        topLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        
        botLabel.setBounds(78, 12, 500, 25);
        botLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        
        
        startButton.setBounds(173, 37, 255, 25);
        startButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        buttonPanel.add(topLabel);
        buttonPanel.add(botLabel);
        buttonPanel.add(startButton);
        setSizes(buttonPanel, 0, 65);
        buttonPanel.setBorder(BorderFactory.createEtchedBorder());


        JPanel leftPanel = new LeftPanel();
        setSizes(leftPanel, 113, 600);
        

        JPanel rightPanel = new RightPanel();
        rightPanel.setLayout(null);
        setSizes(rightPanel, 113, 600);
        shatteredLabel.setBounds(9, 523, 100, 100);
        shatteredLabel.setForeground(Color.WHITE);
        shatteredLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        crystalBallsLabel.setBounds(10, 512, 100, 100);
        crystalBallsLabel.setForeground(Color.WHITE);
        crystalBallsLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        pointsLabel.setBounds(10, 533, 100, 100);
        pointsLabel.setForeground(Color.WHITE);
        pointsLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        

        rightPanel.add(shatteredLabel);
        rightPanel.add(pointsLabel);
        rightPanel.add(crystalBallsLabel);
        
        
        getContentPane().add(leftPanel, BorderLayout.LINE_START);
        getContentPane().add(boardPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.PAGE_END);
        getContentPane().add(rightPanel, BorderLayout.LINE_END);

        pack();
    }
    
    /** 
     * Updates the points of game, color coding 
     * them depending on the points accumulated 
     * */
    private void updatePoints(final int points, final int bubblesPopped){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                if(points >= 650){
                    pointsLabel.setForeground(Color.RED);
                    pointsLabel.setText("Debt: $" + points);
                    crystalBallsLabel.setText("Crystal Balls ");
                    shatteredLabel.setText("Shattered: " + bubblesPopped);
                }else if(points >= 500){
                    pointsLabel.setForeground(Color.YELLOW);
                    pointsLabel.setText("Debt: $" + points);
                    crystalBallsLabel.setText("Crystal Balls ");
                    shatteredLabel.setText("Shattered: " + bubblesPopped);
                }else{
                    pointsLabel.setText("Debt: $" + points);
                    crystalBallsLabel.setText("Crystal Balls ");
                    shatteredLabel.setText("Shattered: " + bubblesPopped);
                }
            }
        });
    }
   
        
  
    /** Method to allows an easy way to set all sizes */
    private void setSizes(JPanel p, int x, int y){
        p.setPreferredSize(new Dimension(x, y));
        p.setMinimumSize(new Dimension(x, y));
        p.setMaximumSize(new Dimension(x, y));
    }
    
    /** Updates scores and executes appropriate code when player wins/loses */
    @Override
    public void updateScore(GamePanel.ScoreEvent ev) {
        updatePoints(ev.points, ev.bubblesPopped);
        if(ev.isGameOver){
            background.stop();
            lose.play();
            topLabel.setBounds(222, 0, 550, 25);
            topLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
            topLabel.setText("YOU LOSE");
            botLabel.setBounds(0, 0, 0, 0);
            botLabel.setText("");
            isPaused = true;
            ((GamePanel) boardPanel).setPlaying(false);
            startButton.setText(LOSE);
            startButton.setEnabled(false);
        }
        if(ev.isGameWon){
            background.stop();
            win.play();
            topLabel.setBounds(55, 0, 550, 25);
            topLabel.setText("[Tish]: Ahahahaha! Great job! Guess who gets to live! I'll summon you again if I need you.");
            botLabel.setBounds(93, 12, 500, 25);
            botLabel.setText("Expect a bill in the amount of $" + ev.points +  " for the " + ev.bubblesPopped + " crystal balls you shattered.");
            isPaused = true;
            ((GamePanel) boardPanel).setPlaying(false);
            startButton.setText(WIN);
            startButton.setEnabled(false);
        }
        if(ev.points >= 750 && !(ev.isGameWon)){
            background.stop();
            start.play();
            topLabel.setBounds(65, 0, 550, 25);
            topLabel.setText("[Tish]: Oh my, you're bad at this. You've already exceeded $750 worth of crystal balls.");
            botLabel.setBounds(104, 12, 500, 25);
            botLabel.setText("I'm a witch, not a king! That's ok, I could always use more bath toys.");
            isPaused = true;
            ((GamePanel) boardPanel).setPlaying(false);
            startButton.setText("YOU LOSE");
            startButton.setEnabled(false);
        }
    }

    /** Loads audio clip */
    private AudioClip loadSound(String soundFile){
        URL url = BubbleFrame.class.getResource(soundFile);
        AudioClip clip = Applet.newAudioClip(url);
        return clip;
    }
    
    
    

}
