/**
 * BubbleManager class that has useful methods for BubbleShooter game.
 * Contains methods that allow filling the board, adding bubbles, 
 * removing matching bubbles, and removing 
 * any floating bubbles remaining. Added functionality that allows
 * placing bubbles in cardinal directions in relation to other
 * already existing bubbles.
 * 
 * @author Tyler Fenske
 * @version 2016-12-06
 */

import java.awt.Point;
import java.util.*;

public class BubbleManager {

    /** List of bubble objects */
    private List<Bubble> bubbles = new ArrayList<>();

    /** List of Lists of rows, used as column index */
    public List<List<Bubble>> cols;
    /** List of rows, indicating what bubble is in each row index */
    private List<Bubble> rows;

    /** List of points that haven't fully been checked yet */
    private List<Point> uncheckedPoints = new ArrayList<>();
    /** List of points that have been checked for matching neighbors */
    private List<Point> checkedPoints = new ArrayList<>();
    /** List of points that are connected to the top of game board */
    private List<Point> isConnected = new ArrayList<>();
    /**
     * List of points that are not connected to the top of game board (floaters)
     */
    private List<Point> isNotConnected = new ArrayList<>();
    /** List of all points on the board in a (row,col) format */
    public List<Point> allPoints;

    /** String representation of a random bubble */
    private String randomBubble;
    
    /** Number of rows passed in constructor */
    private int numRows;
    /** Number of columns passed in constructor */
    private int numCols;
    /**
     * Number of blank spaces on the game board to allow room for new bubbles to
     * come in
     */
    public static final int BLANK_SPACES = 6;
    /** Player will gain points multiplied by this int value for each bubble popped */
    private static final int POP_BUBBLES_POINTS_MULTIPLIER = 6;
    /** Player will gain points multiplied by this int value for each bubble that drops */
    private static final int DROP_BUBBLES_POINTS_MULTIPLIER = 2;
    /** Total amount of bubbles popped during each game session */
    private int bubblesPopped = 0;
    /** Total amount of points accumulated during each game session */
    private int points = 0;
    
    /** Number of bubbles needed in a row to cause them to pop */
    private final int POP_MATCHING = 3;
    /** Symbol that represents a blank space on board */
    private final Bubble BLANK_SPACE = new Bubble("-");

    

    
    
    /**
     * Main constructor for BubbleManager that will set up rows, cols, and
     * bubbles
     */
    public BubbleManager(int row, int col, Bubble[] list) {
        numRows = row;
        numCols = col;

        for (Bubble x : list) {
            bubbles.add(x);
        }
        
        buildList();
    }
    

    /** Fills the board with random bubbles */
    public void fill() {
        Random rand = new Random();
        cols = new ArrayList<>();

        for (int r = 0; r < numRows; r++) {
            rows = new ArrayList<>();
            if(r%2 == 0){
                for (int c = 0; c < numCols; c++) {
                    rows.add(bubbles.get(rand.nextInt(bubbles.size())));
                }
            }else{
                for (int c = 0; c < numCols-1; c++) {
                    rows.add(bubbles.get(rand.nextInt(bubbles.size())));
                } 
            }

            cols.add(rows);
        }

        for (int r = 0; r < BLANK_SPACES; r++) {
            rows = new ArrayList<>();
            if(r%2 ==0){
                for (int c = 0; c < numCols-1; c++) {
                    rows.add(BLANK_SPACE);
                }
            }else{
                for (int c = 0; c < numCols; c++) {
                    rows.add(BLANK_SPACE);
                }
            }

            cols.add(rows);
        }
    }

    /** Returns string representation of board */
    public String toString() {

        StringBuilder sb = new StringBuilder();


        for (int r = 0; r < numRows + BLANK_SPACES; r++) {
            if (!(r%2 == 0)) {
                sb.append(" ");
                for (int c = 0; c < numCols-1; c++) {
                    sb.append(cols.get(r).get(c).getBubble());
                    sb.append(" ");
                }
            }else{
                for (int c = 0; c < numCols; c++) {
                    sb.append(cols.get(r).get(c).getBubble());
                    sb.append(" ");
                }
            }

            sb.append("\n");

        }
        return sb.toString();
    }

    /** Removes bubble at param row/col and adds a blank space */
    private void removeBubble(int row, int col) {
        cols.get(row).remove(col);
        cols.get(row).add(col, BLANK_SPACE);
    }

    /** Adds a designated bubble to designated point on board */
    private void addBubble(int row, int col, Bubble bubble) {
        if(cols.get(row).get(col).getBubble() == BLANK_SPACE.getBubble()){
            cols.get(row).remove(col);
            cols.get(row).add(col, bubble);
            //System.out.println("Adding " + bubble.getBubble()+  " bubble to point [" + row + ", " + col + "].");
            //System.out.println(toString());
        }
    }
    
    /** Returns current points */
    public int getPoints(){
        return points;
    }
    
    /** Returns current amount of bubbles popped */
    public int getBubblesPopped(){
        return bubblesPopped;
    }

    /**
     * Removes all bubbles that match if there are (POP_MATCHING's value) or
     * more bubbles touching, then clears helper lists
     */
    private void removeMatching(int row, int col) {
        if (isGroup(row, col)) {
            while (uncheckedPoints.size() != checkedPoints.size()) {
                for (int i = 0; i < uncheckedPoints.size(); i++) {
                    isGroup((int) uncheckedPoints.get(i).getX(),
                            (int) uncheckedPoints.get(i).getY());
                }

            }

        }

        //System.out.println("There are " + checkedPoints.size() + " bubbles that match.");
        if (checkedPoints.size() < POP_MATCHING) {
            //System.out.println("Not enough match to remove.");
        }
        if (checkedPoints.size() >= POP_MATCHING) {
            points += checkedPoints.size()*POP_BUBBLES_POINTS_MULTIPLIER;
            bubblesPopped += checkedPoints.size();
            //System.out.println("Removing " + checkedPoints.size() + " matching [" + getBubble(row, col) + "] " + "bubbles \nconnected to space [" + row + ", " + col + "].");
            for (int i = 0; i < checkedPoints.size(); i++) {
                removeBubble((int) checkedPoints.get(i).getX(),
                        (int) checkedPoints.get(i).getY());
            }
        }

        checkedPoints.clear();
        uncheckedPoints.clear();

    }

    /**
     * Takes into account if row is offset or not, then looks in appropriate
     * directions for any neighbor matching bubbles, then adds them to a list
     */
    private boolean isGroup(int row, int col) {
        Point p;

        p = new Point(checkOffset(row, col, -1, 0));
        if (p.getX() != 500) {
            if (!contains(uncheckedPoints, row - 1, col)) {
                uncheckedPoints.add(p);
            }
        }

        p = new Point(checkOffset(row, col, 1, 0));
        if (p.getX() != 500) {
            if (!contains(uncheckedPoints, row + 1, col)) {
                uncheckedPoints.add(p);
            }
        }

        p = new Point(checkOffset(row, col, 0, -1));
        if (p.getX() != 500) {
            if (!contains(uncheckedPoints, row, col - 1)) {
                uncheckedPoints.add(p);
            }
        }

        p = new Point(checkOffset(row, col, 0, 1));
        if (p.getX() != 500) {
            if (!contains(uncheckedPoints, row, col + 1)) {
                uncheckedPoints.add(p);
            }
        }

        if (isOffsetRow(row)) {
            p = new Point(checkOffset(row, col, -1, 1));
            if (p.getX() != 500) {
                if (!contains(uncheckedPoints, row - 1, col + 1)) {
                    uncheckedPoints.add(p);
                }
            }

            p = new Point(checkOffset(row, col, 1, 1));
            if (p.getX() != 500) {
                if (!contains(uncheckedPoints, row + 1, col + 1)) {
                    uncheckedPoints.add(p);
                }
            }

        } else {
            p = new Point(checkOffset(row, col, -1, -1));
            if (p.getX() != 500) {
                if (!contains(uncheckedPoints, row - 1, col - 1)) {
                    uncheckedPoints.add(p);
                }
            }

            p = new Point(checkOffset(row, col, 1, -1));
            if (p.getX() != 500) {
                if (!contains(uncheckedPoints, row + 1, col - 1)) {
                    uncheckedPoints.add(p);
                }
            }
        }

        if (uncheckedPoints.size() > 0) {
            p = new Point(row, col);
            if (!contains(uncheckedPoints, row, col)) {
                uncheckedPoints.add(p);
            }
            if (!contains(checkedPoints, row, col)) {
                checkedPoints.add(p);
            }
            return true;
        }

        return false;
    }
    
    /** Returns true if there is a free space around bubble being checked */
    public boolean aroundFreeSpace(int row, int col){
        if(!(cols.get(row).get(col).getBubble() == BLANK_SPACE.getBubble())){
            if(checkFreeSpace(row, col, -1, 0)){
                return true;
            }
            if(checkFreeSpace(row, col, 1, 0)){
                return true;
            }
            if(checkFreeSpace(row, col, 0, -1)){
                return true;
            }
            if(checkFreeSpace(row, col, 0, 1)){
                return true;
            }
            
            if(isOffsetRow(row)){
                if(checkFreeSpace(row, col, -1, 1)){
                    return true;
                }
                if(checkFreeSpace(row, col, 1, 1)){
                    return true;
                }
            }else{
                if(checkFreeSpace(row, col, -1, -1)){
                    return true;
                }
                if(checkFreeSpace(row, col, 1, -1)){
                    return true;
                }
            }
        }
   
        return false;
    }
    
    /** Returns true if the offSet row/col has a free space */
    private boolean checkFreeSpace(int row, int col, int rowOffset, int colOffset){

        if(isOffsetRow(row+rowOffset)){
            if ((row + rowOffset >= 0 && col + colOffset >= 0)
                    && (row + rowOffset < numRows + BLANK_SPACES
                            && col + colOffset < numCols-1)) {
                if (cols.get(row + rowOffset).get(col + colOffset).getBubble() ==  BLANK_SPACE.getBubble()){
                    return true;
                }
            }
        }else{
            if ((row + rowOffset >= 0 && col + colOffset >= 0)
                    && (row + rowOffset < numRows + BLANK_SPACES
                            && col + colOffset < numCols)) {
                if (cols.get(row + rowOffset).get(col + colOffset).getBubble() ==  BLANK_SPACE.getBubble()){
                    return true;
                }
            }
        }
        return false;
    }

    /** Returns true if list param contains a point with row and col param */
    private boolean contains(List<Point> list, int row, int col) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getX() == row && list.get(i).getY() == col) {
                return true;
            }
        }
        return false;
    }

    /**
     * If original point has the same bubble symbol as offset point, returns
     * offset point. If not, returns point 500 x 500 as this is larger than any
     * game board that is planned to be used.
     */
    private Point checkOffset(int row, int col, int rowOffset, int colOffset) {
        Point p = new Point(500, 500);

        if(isOffsetRow(row+rowOffset)){
            if ((row + rowOffset >= 0 && col + colOffset >= 0)
                    && (row + rowOffset < numRows + BLANK_SPACES
                            && col + colOffset < numCols-1)) {
                if (cols.get(row).get(col).getBubble() == cols.get(row + rowOffset)
                        .get(col + colOffset).getBubble()) {
                    p = new Point(row + rowOffset, col + colOffset);
                }
            }
        }else{
            if ((row + rowOffset >= 0 && col + colOffset >= 0)
                    && (row + rowOffset < numRows + BLANK_SPACES
                            && col + colOffset < numCols)) {
                if (cols.get(row).get(col).getBubble() == cols.get(row + rowOffset)
                        .get(col + colOffset).getBubble()) {
                    p = new Point(row + rowOffset, col + colOffset);
                }
            }
        }
        return p;
    }

    /**
     * Returns true if offset point is connected to the top of the game board by
     * any means
     */
    private boolean touchingConnectedPoint(int row, int col, int rowOffset,
            int colOffset) {

        if ((row + rowOffset >= 0 && col + colOffset >= 0)
                && (row + rowOffset < numRows + BLANK_SPACES
                        && col + colOffset < numCols)) {
            if (contains(isConnected, row + rowOffset, col + colOffset)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if row is considered offset (every other row, starting at
     * row index 1)
     */
    private boolean isOffsetRow(int rowIndex) {
        if (rowIndex % 2 == 0) {
            return false;
        }
        return true;
    }

    /**
     * Looks in all appropriate directions (depending if row is an offset row)
     * to see if current point is touching any other connected point
     */
    private void checkFloating(int row, int col) {
        Point p = new Point(row, col);

        if (touchingConnectedPoint(row, col, -1, 0)) {
            if (!contains(isConnected, row, col)) {
                isConnected.add(p);
            }
        }

        if (touchingConnectedPoint(row, col, 1, 0)) {
            if (!contains(isConnected, row, col)) {
                isConnected.add(p);
            }
        }

        if (touchingConnectedPoint(row, col, 0, -1)) {
            if (!contains(isConnected, row, col)) {
                isConnected.add(p);
            }
        }

        if (touchingConnectedPoint(row, col, 0, 1)) {
            if (!contains(isConnected, row, col)) {
                isConnected.add(p);
            }
        }

        if (isOffsetRow(row)) {

            if (touchingConnectedPoint(row, col, -1, 1)) {
                if (!contains(isConnected, row, col)) {
                    isConnected.add(p);
                }
            }

            if (touchingConnectedPoint(row, col, 1, 1)) {
                if (!contains(isConnected, row, col)) {
                    isConnected.add(p);
                }
            }
        } else {

            if (touchingConnectedPoint(row, col, -1, -1)) {
                if (!contains(isConnected, row, col)) {
                    isConnected.add(p);
                }
            }

            if (touchingConnectedPoint(row, col, 1, -1)) {
                if (!contains(isConnected, row, col)) {
                    isConnected.add(p);
                }
            }
        }
    }

    /**
     * Checks board rigorously for connected points by looping over the game
     * board in 4 different directions, then removes all floating bubbles
     */
    private void removeFloating() {
        Point p;

        for (int i = 0; i < cols.get(0).size(); i++) {
            if (cols.get(0).get(i) != BLANK_SPACE) {
                p = new Point(0, i);
                isConnected.add(p);
            }
        }

        for (int r = 1; r < numRows + BLANK_SPACES; r++) {
            if(isOffsetRow(r)){
                for (int c = 0; c < numCols-1; c++) {
                    if (cols.get(r).get(c) != BLANK_SPACE) {
                        checkFloating(r, c);
                    }
                }
            }else{
                for (int c = 0; c < numCols; c++) {
                    if (cols.get(r).get(c) != BLANK_SPACE) {
                        checkFloating(r, c);
                    }
                }
            }

        }

        for (int r = 1; r < numRows + BLANK_SPACES; r++) {
            if(isOffsetRow(r)){
                for (int c = numCols - 2; c >= 0; c--) {
                    if (cols.get(r).get(c) != BLANK_SPACE) {
                        checkFloating(r, c);
                    }
                }
            }else{
                for (int c = numCols - 1; c >= 0; c--) {
                    if (cols.get(r).get(c) != BLANK_SPACE) {
                        checkFloating(r, c);
                    }
                }
            }
        }

        for (int r = numRows + BLANK_SPACES - 1; r >= 0; r--) {
            if(isOffsetRow(r)){
                for (int c = 0; c < numCols-1; c++) {
                    if (cols.get(r).get(c) != BLANK_SPACE) {
                        checkFloating(r, c);
                    }
                }
            }else{
                for (int c = 0; c < numCols; c++) {
                    if (cols.get(r).get(c) != BLANK_SPACE) {
                        checkFloating(r, c);
                    }
                }
            }
        }

        for (int r = numRows + BLANK_SPACES - 1; r >= 0; r--) {
            if(isOffsetRow(r)){
                for (int c = numCols - 2; c >= 0; c--) {
                    if (cols.get(r).get(c) != BLANK_SPACE) {
                        checkFloating(r, c);
                        if (!contains(isConnected, r, c)) {
                            if (!contains(isNotConnected, r, c)) {
                                p = new Point(r, c);
                                isNotConnected.add(p);
                            }
                        }
                    }
                }
            }else{
                for (int c = numCols - 1; c >= 0; c--) {
                    if (cols.get(r).get(c) != BLANK_SPACE) {
                        checkFloating(r, c);
                        if (!contains(isConnected, r, c)) {
                            if (!contains(isNotConnected, r, c)) {
                                p = new Point(r, c);
                                isNotConnected.add(p);
                            }
                        }
                    }
                }
            }

        }

        if (isNotConnected.size() == 1) {
            points += isNotConnected.size() * DROP_BUBBLES_POINTS_MULTIPLIER;
            bubblesPopped += isNotConnected.size();
            //System.out.println("There is " + isNotConnected.size() + " floating bubble.");
            //System.out.println("\n" + toString());
        } else {
            points += isNotConnected.size() * DROP_BUBBLES_POINTS_MULTIPLIER;
            bubblesPopped += isNotConnected.size();
            //System.out.println("There are " + isNotConnected.size() + " floating bubbles.");
            //System.out.println("\n" + toString());
        }
        for (int i = 0; i < isNotConnected.size(); i++) {
            removeBubble((int) isNotConnected.get(i).getX(),
                    (int) isNotConnected.get(i).getY());
        }

        isNotConnected.clear();
        isConnected.clear();
    }

    /** Method that allows an easy way to test BubbleManager */
    public void addAndRemove(int row, int col, Bubble bubble) {
        addBubble(row, col, bubble);
        removeMatching(row, col);
        removeFloating();
    }
    
    /** Creates a random bubble */
    public String createRandomBubble(){
        Random rand = new Random();
        String[] randombubs = {"@","#", "*", "$", "%"};
        String str;
        
        str = randombubs[rand.nextInt(randombubs.length)];
        randomBubble = str;
        return str;
    }
    
    /** 
     * This method will place the bubble NE of the given point if possible, and will default to 
     * placing the bubble to the E point if attempted action isn't possible.
     */
    public void placeNE (int row, int col){
        if(isOffsetRow(row) && contains(allPoints, row-1, col+1)){
            if(cols.get(row-1).get(col+1).getBubble() == BLANK_SPACE.getBubble()){
                addBubble(row-1, col+1, new Bubble(randomBubble));
                removeMatching(row-1, col+1);
                removeFloating();
            }else{
                placeE(row, col);
            }
        }else{
            if(contains(allPoints, row-1, col)){
                if(cols.get(row-1).get(col).getBubble() == BLANK_SPACE.getBubble() && contains(allPoints, row-1, col)){
                    addBubble(row-1, col, new Bubble(randomBubble));
                    removeMatching(row-1, col);
                    removeFloating();
            
            }else{
                placeE(row, col);
            }
        }
            else{
                placeE(row, col);
            }
        }
    }
    
    /** 
     * This method will place the bubble NW of the given point if possible, and will default to 
     * placing the bubble to the W point if attempted action isn't possible.
     */
    public void placeNW (int row, int col){
        if(isOffsetRow(row) && contains(allPoints, row-1, col)){
            if(cols.get(row-1).get(col).getBubble() == BLANK_SPACE.getBubble()){
                addBubble(row-1, col, new Bubble(randomBubble));
                removeMatching(row-1, col);
                removeFloating();
            }else{
                placeW(row, col);
            }
        }else{
            if(contains(allPoints, row-1, col-1)){
                if(cols.get(row-1).get(col-1).getBubble() == BLANK_SPACE.getBubble()){
                    addBubble(row-1, col-1, new Bubble(randomBubble));
                    removeMatching(row-1, col-1);
                    removeFloating();
            

            }else{
                placeW(row, col);
            }
        }else{
            placeW(row, col);
        }
        }
    }
    
    /** 
     * This method will place the bubble SE of the given point if possible, and will default to 
     * placing the bubble to the SW point if attempted action isn't possible.
     */
    public void placeSE (int row, int col){
        if(isOffsetRow(row) && contains(allPoints, row+1, col+1)){
            if(cols.get(row+1).get(col+1).getBubble() == BLANK_SPACE.getBubble()){
                addBubble(row+1, col+1, new Bubble(randomBubble));
                removeMatching(row+1, col+1);
                removeFloating();
            }else{
                placeSW(row, col);
            }
        }else{
            if(contains(allPoints, row+1, col)){
                if(cols.get(row+1).get(col).getBubble() == BLANK_SPACE.getBubble()){
                    addBubble(row+1, col, new Bubble(randomBubble));
                    removeMatching(row+1, col);
                    removeFloating();
                }else{
                    placeSW(row, col);
                }
            }else{
                placeSW(row, col);
            }
        }
    }
    
    /** 
     * This method will place the bubble SW of the given point if possible, and will default to 
     * placing the bubble to the SE point if attempted action isn't possible.
     */
    public void placeSW (int row, int col){
        if(isOffsetRow(row) && contains(allPoints, row+1, col)){
            if(cols.get(row+1).get(col).getBubble() == BLANK_SPACE.getBubble()){
                addBubble(row+1, col, new Bubble(randomBubble));
                removeMatching(row+1, col);
                removeFloating();
            }else{
                placeSE(row, col);
            }

        }else{
            if(contains(allPoints, row+1, col-1)){

                if(cols.get(row+1).get(col-1).getBubble() == BLANK_SPACE.getBubble()){

                    addBubble(row+1, col-1, new Bubble(randomBubble));
                    removeMatching(row+1, col-1);
                    removeFloating();
            }else{
                placeSE(row, col);
            }
            }else{
                placeSE(row, col);
            }
        }

    }
    
    /** 
     * This method will place the bubble W of the given point if possible, and will default to 
     * placing the bubble to the SW point if attempted action isn't possible.
     */
    public void placeW (int row, int col){
        if(contains(allPoints, row, col-1)){
            if(cols.get(row).get(col-1).getBubble() == BLANK_SPACE.getBubble()){
                addBubble(row, col-1, new Bubble(randomBubble));
                removeMatching(row, col-1);
                removeFloating();
        
        }else{
            placeSW(row, col);
        }
        }
        else{
            placeSW(row, col);
        }

    }
    
    /** 
     * This method will place the bubble E of the given point if possible, and will default to 
     * placing the bubble to the SE point if attempted action isn't possible.
     */
    public void placeE (int row, int col){
        if(contains(allPoints, row, col+1)){
            if(cols.get(row).get(col+1).getBubble() == BLANK_SPACE.getBubble()){
                addBubble(row, col+1, new Bubble(randomBubble));
                removeMatching(row, col+1);
                removeFloating();
        
        }else{
            placeSE(row, col);
        }
        }
        else{
            placeSE(row, col);
        }
    }
    
    /** Builds a list of all points on the board in an (row,col) format */
    private void buildList(){
        allPoints = new ArrayList<>();
        Point p;
        for (int r = 0; r < numRows + BLANK_SPACES; r++) {
            if(isOffsetRow(r)){
                for (int c = 0; c < numCols-1; c++) {
                    p = new Point(r, c);
                    allPoints.add(p);
                }
            }else{
                for (int c = 0; c < numCols; c++) {
                    p = new Point(r, c);
                    allPoints.add(p);
                }
            }

        }
        
    }

}
