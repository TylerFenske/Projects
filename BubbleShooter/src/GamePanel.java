/**
 * Class that contains all information for the GamePanel GUI to operate. 
 * Constructs lists of points to map out the GUI coordinates of bubbles,
 * and works with the BubbleManager class to constantly repaint the board
 * to match the state of the game.
 * 
 * @author Tyler Fenske
 * @version 2016-12-06
 */

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.net.URL;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.Timer;
import java.util.*;
import java.util.List;
import java.awt.Point;

public class GamePanel extends JPanel implements ActionListener {
    
    /** Class that keeps track of scores and state of the game*/
    public static class ScoreEvent{
        public final int points;
        public final int bubblesPopped;
        public final boolean isGameOver;
        public final boolean isGameWon;
        public ScoreEvent(int points, int bubblesPopped, boolean isGameOver, boolean isGameWon){
            this.points = points;
            this.bubblesPopped = bubblesPopped;
            this.isGameOver = isGameOver;
            this.isGameWon = isGameWon;
        }
    }
    
    /** Interface to implement score listener */
    public static interface ScoreListener{
        void updateScore(ScoreEvent ev);
    }
    
    /** Audioclips used when firing or bubbles touch */
    private AudioClip fire;
    private AudioClip touch;

    /** Size of a bubble image, halfsize, and 86% size to calculate placement on GUI */
    private int cellSize = 41;
    private int halfSize = cellSize/2;
    private double cellPos = cellSize * .86;
    
    /** Number of rows and cols on the board */
    private int rows;
    private int cols;
    
    /** X and Y coordinate of origin of the bubble pointer */
    private final int X = 185;
    private final int Y = 560;
    
    /** Constant x and y coordinate of where "next bubble" will show up */
    private final int bookPosX = 130;
    private final int bookPosY = 517;

    /** 
     * Boolean values to keep track of if the when the bubble has launched, 
     * when it's a new bubble, if game is playing, if a piece was placed,
     * and the state of the game.
     * */
    private boolean isPlaying = false;
    private boolean hasLaunched = false;
    private boolean newPiece = false;
    private boolean placed = false;
    private boolean gameOver = false;
    private boolean gameWon = false;
    
    /** Ints that keep track of the score of the game */
    private int bubblesPopped;
    private int points;

    /** Constantly changing value of where the launched bubble is moving */
    private int movingX;
    private int movingY;
    
    /** Values of shift in x and y to move the bubble in proper direction */
    private int xDir;
    private int yDir;
    
    /** Value of what angle the pointer is aiming */
    private int pointerAngle = 270;
    
    /** Timer used to make launched bubble move */
    private Timer timer = new Timer(16, this);
    
    /** List of bubble coordinates */
    private List<Point> bubblePoints;
    /** List of all points in the top row */
    private List<Point> topRowList;
    /** List of all points in a square around a bubble on the board */
    private List<Point> stickableBubbles;
    /** 
     * List of lists of stickable bubbles. 
     * Each list can be accessed to find the master point, 
     * aka the point where the bubble image is drawn on the board. 
     */
    private List<List<Point>> stickableBubblesList;
    
    /** Set of score listeners */
    private Set<ScoreListener> scoreListeners = new HashSet<ScoreListener>();
    
    /** 
     * Maps the correlate bubblemanager coordinates of 
     * bubbles to GUI coordinates and vice versa 
     */
    private Map<Point, Point> bubbleMap;
    private Map<Point, Point> reverseBubbleMap;

    /** Images of all bubbles */
    private BufferedImage blueBubble;
    private BufferedImage purpleBubble;
    private BufferedImage greenBubble;
    private BufferedImage orangeBubble;
    private BufferedImage redBubble;
    private BufferedImage gameBoard;
    
    /** String representation of the next random bubble */
    private static String randomBubble;

    /** Creates array of new bubbles */
    //@=blue #=red $=green *=purple %=orange
    public Bubble[] bubbles = {new Bubble("@"), new Bubble("#"), new Bubble("$"), new Bubble("*"), new Bubble("%")};
/*    public Bubble[] bubbles = {new Bubble("@")};*/
    
    /** BubbleManger object to be used to keep track of of the board */
    public BubbleManager newGame;
    
    /** Fills new board, loads images and sounds, and adds key listeners for Bubble pointer */
    public GamePanel(int rows, int cols){
        newGame = new BubbleManager(rows, cols, bubbles);
        newGame.fill();

        this.rows = rows;
        this.cols = cols;

        //System.out.println(newGame.toString());
        buildBubbleList();
        buildBubbleMaps();
        buildTopRowList();
        buildStickableBubbles();
        randomBubble = newGame.createRandomBubble();

        URL url = BubbleFrame.class.getResource("launch.wav");
        AudioClip clip = Applet.newAudioClip(url);
        fire = clip;
        
        URL url2 = BubbleFrame.class.getResource("pop.wav");
        AudioClip clip2 = Applet.newAudioClip(url2);
        touch = clip2;

        gameBoard = loadImage("gameboard.png", gameBoard);
        redBubble = loadImage("redbubble.png", redBubble);
        blueBubble = loadImage("bluebubble.png", blueBubble);
        purpleBubble = loadImage("purplebubble.png", purpleBubble);
        greenBubble = loadImage("greenbubble.png", greenBubble);
        orangeBubble = loadImage("orangebubble.png", orangeBubble);
        

        addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent ev){
                if(isPlaying){
                    switch (ev.getKeyCode()){
                    case KeyEvent.VK_LEFT:
                        movePointerLeft();
                        break;
                    case KeyEvent.VK_RIGHT:
                        movePointerRight();
                        break;
                    case KeyEvent.VK_SPACE:
                        launchBall();
                        break;
                }
                }
            }
        });
 
    }
    

    /** Override paintComponent to draw board, moving ball, and pointer */
    public void paintComponent(Graphics g){
        super.paintComponents(g);

        g.drawImage(gameBoard, 0, 0, null);
        
        
            
            
        for(int i=0; i<newGame.allPoints.size(); i++){
            
            int x = (int)newGame.allPoints.get(i).getX();
            int y = (int)newGame.allPoints.get(i).getY();
            
            int GUIx = (int) bubbleMap.get(new Point(x, y)).getX();
            int GUIy = (int) bubbleMap.get(new Point(x, y)).getY();
            
            g.drawImage(bubbleImage(x, y), GUIx, GUIy, cellSize, cellSize, null);
            
        }

        
        if(!hasLaunched){
            g.drawImage(bubbleImage(randomBubble), bookPosX, bookPosY, null);
        }
        
        g.setColor(new Color(183, 254, 174));
        

        int x;
        int y;
        int r = 110;
        double a = pointerAngle*Math.PI/180;
        x = (int) (X + r * Math.cos(a));
        y = (int) (Y + r * Math.sin(a));
        

        
        g.drawLine(X+1, Y, x+1, y);

        g.drawLine(X, Y, x, y);
        
        g.drawLine(X-1, Y, x-1, y);
        
        
        g.setColor(new Color(0, 255, 0));
        
        g.drawLine(X+2, Y, x+2, y);
        
        g.drawLine(X-2, Y, x-2, y);

        
        g.drawLine(X+3, Y, x+3, y);
        
        g.drawLine(X-3, Y, x-3, y);


        if(!hasLaunched){
            movingX = x-halfSize;
            movingY = y-halfSize;

            xDir = (x - X)/10;
            yDir = (y - Y)/10;
            
        }

        if(hasLaunched){

            g.drawImage(bubbleImage(randomBubble), movingX, movingY, null);
        }

    }
    
    /** Method that allows other class to add a score listener */
    public void addScoreListener(ScoreListener listener) {
        scoreListeners.add(listener);
    }
 
    /** Updates all score listeners */
    private void updateScoreListeners() {
        ScoreEvent event = new ScoreEvent(points, bubblesPopped, gameOver, gameWon);
        for(ScoreListener listener : scoreListeners) {
            listener.updateScore(event);
        }
    }
    
    /** Sets the state of playing */
    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }
    
    /** Method used to use an image that correlates to a bubble symbol provided by bubblemanager */
    private Image bubbleImage(String str){
        if(str.equals("$")){
            return greenBubble;
        }else if(str.equals("#")){
            return redBubble;
        }else if(str.equals("@")){
            return blueBubble;
        }else if(str.equals("*")){
            return purpleBubble;
        }else if(str.equals("%")){
            return orangeBubble;
        }
        return null;
    }
    
    /** Overloads bubbleImage if given a row and col to check the bubble type */
    private Image bubbleImage(int row, int col){
        if(newGame.cols.get(row).get(col).getBubble() == "$"){
            return greenBubble;
        }else if(newGame.cols.get(row).get(col).getBubble() == "#"){
            return redBubble;
        }else if(newGame.cols.get(row).get(col).getBubble() == "@"){
            return blueBubble;
        }else if(newGame.cols.get(row).get(col).getBubble() == "*"){
            return purpleBubble;
        }else if(newGame.cols.get(row).get(col).getBubble() == "%"){
            return orangeBubble;
        }
        return null;
    }
    
    
    /** Returns bubbles popped score */
    public int getBubblesPopped(){
        return bubblesPopped;
    }
    
    /** Returns points */
    public int getPoints(){
        return points;
    }
    
    /** Loads image */
    private BufferedImage loadImage (String imageName, BufferedImage image){
        ClassLoader cl = getClass().getClassLoader();
        InputStream in = cl.getResourceAsStream(imageName);
        try{
            image = ImageIO.read(in);
        }catch (IOException ex){
            System.err.println("Could not load: " + imageName);
        }
        return image;
    }

    /** Moves pointer left by a set amount of radians */
    private void movePointerLeft(){
        if(pointerAngle - 6 >= 211){
            pointerAngle-=6;
            repaint();
        }
    }
    
    /** Moves pointer right by a set amount of radians */
    private void movePointerRight(){
        if(pointerAngle + 6 <= 329){
            pointerAngle+=6;
            repaint();
        }
    }
    
    /** Starts timer and launches the next random bubble */
    private void launchBall(){
        fire.play();
        hasLaunched = true;
        newPiece = true;
        timer.setInitialDelay(0);
        timer.start();
    }
    
    /** Sets gameOver boolean value to true if bottom row has a bubble in it */
    private void isGameOver(){
        for(int i=0; i<cols; i++){
            if(newGame.cols.get(rows+BubbleManager.BLANK_SPACES-1).get(i).getBubble() != "-"){
                gameOver = true;
            }
        }
    }
    
    /** Returns true if all spaces on the board are blank */
    private boolean isGameWon(){
        for(int r=0; r<rows+BubbleManager.BLANK_SPACES; r++){
            if(r%2 == 0){
                for(int c=0; c<cols; c++){
                    if(newGame.cols.get(r).get(c).getBubble() != "-"){
                        return false;
                    }
                }
            }else{
                for(int c=0; c<cols-1; c++){
                    if(newGame.cols.get(r).get(c).getBubble() != "-"){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    

    /** 
     * Action listener that moves the ball, and calls lockin or sticktotop 
     * if moving bubble collides with a stickable bubble or top of board 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(movingX + xDir < 0 ||
                movingX + xDir + cellSize > getWidth()) {
                 xDir = -xDir;
             }

             if(movingY + yDir < 0 ||
                movingY + yDir + cellSize > getHeight()) {
                 
                 stickToTop(movingX);
                 touch.play();
                 points = newGame.getPoints();
                 bubblesPopped = newGame.getBubblesPopped();
                 isGameOver();
                 gameWon = isGameWon();
                 updateScoreListeners();
                 placed = true;
                 
             }
             
             
                 for(int i=5; i<cellSize-10; i++){
                     for(int a=5; a<cellSize-10; a++){
                         if(touchingBubble(a+movingX+xDir, i+movingY+yDir)){
                             if(newPiece){
                                 lockIn(a+movingX+xDir, i+movingY+yDir);
                                 touch.play();
                                 points = newGame.getPoints();
                                 bubblesPopped = newGame.getBubblesPopped();
                                 isGameOver();
                                 gameWon = isGameWon();
                                 updateScoreListeners();
                                 newPiece = false;
                                 placed = true;
                                 break;
                             }
                        
                         }
                     }
                 }
                 

             
             movingX += xDir;
             movingY += yDir;

             repaint();
             
             if(placed){
                 randomBubble = newGame.createRandomBubble();
                 buildStickableBubbles();
                 placed=false;
             }

        
    }
    
    
    /** 
     * If bubble would touch anywhere on the top of board, 
     * finds the closest open spot to lock ball in 
     * */
    private void stickToTop(int x){
        int check;
        timer.stop();
        hasLaunched = false;
        for(int i=0; i<topRowList.size(); i++){
            check = (int) topRowList.get(i).getX();
            if(x+halfSize>=check && x+halfSize<check+cellSize){
                if(newGame.cols.get(0).get(i).getBubble() == "-"){
                    newPiece = false;
                    newGame.addAndRemove(0, i, new Bubble(randomBubble));
                    break;
                }else if(newGame.cols.get(0).get(i+1).getBubble() == "-"){
                    newPiece = false;
                    newGame.addAndRemove(0, i+1, new Bubble(randomBubble));
                    break;
                }else if(newGame.cols.get(0).get(i-1).getBubble() == "-"){
                    newPiece = false;
                    newGame.addAndRemove(0, i-1, new Bubble(randomBubble));
                    break;
                }

            }
        }
    }
    
    /** 
     * Takes the coordinate of where the moving bubble first touched 
     * a stickable bubble, then adds it to the gameboard based on if it 
     * touched the NE, NW, SE, SW, W, or E side of the square of 
     * points around the stickable bubble.
     */
    private void lockIn (int x, int y){
        int mx, my;
        int nx, ny;
        String dir;
        
        timer.stop();
        hasLaunched = false;
        
        mx = (int) whatBubble(x, y).getX();
        my = (int) whatBubble(x, y).getY();

        dir = whatDirection(mx, my, x, y);

 
        ny = (int) reverseBubbleMap.get(new Point(mx, my)).getY();
        nx = (int) reverseBubbleMap.get(new Point(mx, my)).getX();
        

        switch (dir){
        case "NE": 
            newGame.placeNE(nx, ny);
            repaint();
            break;
        case"NW":
            newGame.placeNW(nx, ny);
            repaint();
            break;
        case"SE":
            newGame.placeSE(nx, ny);
            repaint();
            break;
        case"SW":
            newGame.placeSW(nx, ny);
            repaint();
            break;
        case"E":
            newGame.placeE(nx, ny);
            repaint();
            break;
        case"W":
            newGame.placeW(nx, ny);
            repaint();
            break;
        }
 
        
    }
    
    /** 
     * Returns a string that defines what cardinal direction 
     * a point touched on a stickable bubble's outline of points 
     */
    private String whatDirection(int mx, int my, int x, int y){
        String dir = "N";


        if(x==mx && y<=my+halfSize+12 && y>=my+halfSize-12){
            dir = "W";
        }
        if(x==mx+cellSize && y<=my+halfSize+12 && y>=my+halfSize-12){
            dir = "E";
        }
        

        if(y <= my +10){
            if(x<mx+10){
                dir = "NW";
            }
            if(x>=mx+10){
                dir = "NE";
            }
        }

        if(y >= my+cellSize-10){
            if(x<mx+10){
                dir = "SW";
            }
            if(x>=mx+10){
                dir = "SE";
            }
        }

        return dir;
    }
    
    /** Fills the topRowList */
    private void buildTopRowList(){
        topRowList = new ArrayList<>();
        Point p;
        int y;
        
        for(int i=0; i<cols; i++){
            y = i*cellSize;
            p = new Point(y, 0);
            topRowList.add(p);
        }
        
    }
    
    /** Fills the bubblePoints list */
    private void buildBubbleList(){
        bubblePoints = new ArrayList<>();
        Point p;
        
        for(int a=0; a<rows+BubbleManager.BLANK_SPACES; a++){
            for(int i = 0; i<cols; i++){
                if(a%2 == 0){
                    
                        p = new Point(cellSize*i, (int)cellPos*a);
                        bubblePoints.add(p);

                }

            }
            
            for(int i = 0; i<cols-1; i++){

                if(!(a%2 == 0)){

                    
                        p = new Point(halfSize+cellSize*i, (int)cellPos*a);
                        bubblePoints.add(p);
                }

            }
        }
    }
    
    /** Fills the BubbleMap and ReverseBubbleMap */
    private void buildBubbleMaps(){
        bubbleMap = new HashMap<>();
        reverseBubbleMap = new HashMap<>();
        
        for(int i=0; i<bubblePoints.size(); i++){
            bubbleMap.put(newGame.allPoints.get(i), bubblePoints.get(i));
            }
        
        for(int i=0; i<newGame.allPoints.size(); i++){
            reverseBubbleMap.put(bubblePoints.get(i), newGame.allPoints.get(i));
        }
        
    }
    
    /** Fills the stickableBubbles and stickableBubblesList lists */
    private void buildStickableBubbles(){
        int x;
        int y;

        stickableBubblesList = new ArrayList<>();
        
        for(int r=0; r<rows+BubbleManager.BLANK_SPACES; r++){
            if(r%2 == 0){
                for(int c = 0; c<cols; c++){
                    
                    if(newGame.aroundFreeSpace(r, c)){
                        
                        
                        x = (int) bubbleMap.get(new Point(r, c)).getX();
                        y = (int) bubbleMap.get(new Point(r, c)).getY();
                        
                        stickableBubbles = new ArrayList<>();
                        
                        for (int i = 0; i<=cellSize; i++){
                            if(!contains(stickableBubbles, x, y+i)){
                                Point p = new Point(x, y+i);
                                stickableBubbles.add(p);
                            }
                            if(!contains(stickableBubbles, x+i, y)){
                                Point p = new Point(x+i, y);
                                stickableBubbles.add(p);
                            }
                            if(!contains(stickableBubbles, x+i, y+cellSize)){
                                Point p = new Point(x+i, y+cellSize);
                                stickableBubbles.add(p);
                            }
                            if(!contains(stickableBubbles, x+cellSize, y+i)){
                                Point p = new Point(x+cellSize, y+i);
                                stickableBubbles.add(p);
                            }
                            
                        }
                        
                        stickableBubblesList.add(stickableBubbles);
                        }
                    }
            }
                if(!(r%2 == 0)){
                    for(int c = 0; c<cols-1; c++){
                        if(newGame.aroundFreeSpace(r, c)){
                            
                            
                            x = (int) bubbleMap.get(new Point(r, c)).getX();
                            y = (int) bubbleMap.get(new Point(r, c)).getY();
                            
                            stickableBubbles = new ArrayList<>();
                            
                            for (int i = 0; i<=cellSize-4; i++){
                                if(!contains(stickableBubbles, x, y+i)){
                                    Point p = new Point(x, y+i);
                                    stickableBubbles.add(p);
                                }
                                if(!contains(stickableBubbles, x+i, y)){
                                    Point p = new Point(x+i, y);
                                    stickableBubbles.add(p);
                                }
                                if(!contains(stickableBubbles, x+i, y+cellSize)){
                                    Point p = new Point(x+i, y+cellSize);
                                    stickableBubbles.add(p);
                                }
                                if(!contains(stickableBubbles, x+cellSize, y+i)){
                                    Point p = new Point(x+cellSize, y+i);
                                    stickableBubbles.add(p);
                                }
                                
                            }
                            
                            stickableBubblesList.add(stickableBubbles);
                            }
                    }
                }
            
        }
    }
    
    /** 
     * Returns true if given GUI coordinate is intersecting 
     * with any of the points surrounding a stickable bubble 
     */
    private boolean touchingBubble(int x, int y){
        for(int i = 0; i<stickableBubblesList.size(); i++){
            for(int s = 0; s<stickableBubblesList.get(i).size(); s++){
                if((stickableBubblesList.get(i).get(s).getX() == x) && (stickableBubblesList.get(i).get(s).getY() == y)){
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /** 
     * Returns the master point, aka the point where the bubble image 
     * is drawn, by searching through the list of points that surround a stickable bubble
     * for smallest coordinate values 
     */
    private Point whatBubble(int x, int y){
        
        for(int i=0; i<stickableBubblesList.size(); i++){
            for(int a=0; a<stickableBubblesList.get(i).size(); a++){
                if((stickableBubblesList.get(i).get(a).getX() == x) && (stickableBubblesList.get(i).get(a).getY() == y)){
                    return getMasterPoint(stickableBubblesList.get(i));
                }
            }
        }
        
        return null;
    }
    
    /** Given a list, finds the smallest value and returns it as a point */
    private Point getMasterPoint(List<Point> list){
        Point p;
        int mx = 5000;
        int my = 5000;
        
        for(int z=0; z<list.size(); z++){
            if(list.get(z).getX() < mx){
                mx = (int)list.get(z).getX();
            }
            if(list.get(z).getY() < my){
                my = (int)list.get(z).getY();
            }
        }
        
        return p = new Point(mx, my);
    }
    
    /** Returns true if list contains given point */
    private boolean contains(List<Point> list, int row, int col) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getX() == row && list.get(i).getY() == col) {
                return true;
            }
        }
        return false;
    }
    
    /** Prints a list to console for debugging */
    private void printList(List<Point> list){
        for(Point p : list){
            System.out.println(p.toString());
        }
        System.out.println(list.size());
        System.out.println("END OF LIST");
    }
    
    /** Prints a map to console for debugging */
    private void printMap(Map<Point, Point> map){
        for (Map.Entry<Point, Point> entry : map.entrySet()) {
            System.out.println(entry.getKey()+" : "+entry.getValue());
        }
    }
    
}
