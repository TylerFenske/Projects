/**
 * Name: Tyler Fenske
 * Class: CS351
 * Date: 09/13/2018
 *
 * The Hand class is used to store, upkeep, and sort each player's individual
 * hand. Also contains methods for checking if valid sets or runs exists in
 * the hand. An instance of this class is created for each player.
 */

package Remi_Tile;

import java.util.*;

public class Hand {

    private List<Tile> hand;
    private Display display;
    private Tile nullTile;

    private final int MAX_HAND_SIZE = 15;
    private int handSize = 0;

    private static int handIDGen = 0;
    private int handID;

    private List<Tile> blue = new ArrayList<>();
    private List<Tile> green = new ArrayList<>();
    private List<Tile> red = new ArrayList<>();
    private List<Tile> yellow = new ArrayList<>();
    private List<Tile> jokers = new ArrayList<>();


    /**
     * Constructor for Hand object.
     * Params: Display
     * When the hand object is created, a handID is created, and the List that
     * holds the Tiles in hand are initialized.
     * Returns: Nothing
     */
    public Hand(Display display){
        hand = new ArrayList<>();
        nullTile = new Tile(Color.BLANK, -1);

        handIDGen++;
        handID = handIDGen;

        this.display = display;

        initializeHand();
    }

    /**
     * Initializes a hand to be 15 blank tiles.
     * Params: None
     * Returns: Nothing
     */
    private void initializeHand(){
        for(int i = 0; i < MAX_HAND_SIZE; i++){
            hand.add(i, nullTile);
        }
    }

    /**
     * Starting a new game, new hands are generated. Since handIDGen is static,
     * it needs to be set back to 0.
     * Params: None
     * Returns: Nothing
     */
    public void startNewGame(){
        handIDGen = 0;
    }

    /**
     * Print function for debugging
     * Params: None
     * Returns: Nothing
     */
    public void printHand(){
        System.out.println("PLAYER " + handID + "'s HAND: " + handSize);

        for(int i = 0; i < MAX_HAND_SIZE; i++){
            System.out.println(hand.get(i).getColor() + " "
                    + hand.get(i).getRank());
        }
    }



    /**
     * Places the tile in the hand at the first open spot (starting from the
     * left) indicated by the first card with a rank of -1
     * Params: Tile (being drawn)
     * Returns: Nothing
     */
    public void draw(Tile tile){
        boolean foundSpot = false;

        for(int i = 0; i < MAX_HAND_SIZE; i++){
            if(!foundSpot && hand.get(i).getRank() == -1){
                hand.set(i, tile);
                foundSpot = true;
                handSize++;
            }
        }
    }

    /**
     * Draw function for debugging
     * Params: None
     * Returns: Nothing
     */
    public void drawTest(Tile tile, int index){
        hand.set(index, tile);
        handSize++;
    }

    /**
     * Discards the card indicated by the display GUI for human player, or the
     * last spot in the hand always for the computer (since the computer always
     * organizes it's hand to have the least useful card at the last position).
     * Params: None
     * Returns: Tile (being discarded)
     */
    public Tile discard(){
        int index;

        if(handID == 1){
            index = display.getTileDiscarded();
        }else{
            index = MAX_HAND_SIZE-1;
        }

        Tile discardedTile = hand.get(index);
        hand.set(index, nullTile);
        handSize--;

        return discardedTile;
    }

    /**
     * Swaps two tiles indicated by the display class.
     * Params: None
     * Returns: Nothing
     */
    public void swapTiles(){
        int x = display.getSwapX();
        int y = display.getSwapY();

        Tile temp = hand.get(x);

        hand.set(x, hand.get(y));
        hand.set(y, temp);
    }

    /**
     * Method called by player, which tells display to draw the current player's
     * hand to the GUI.
     * Params: None
     * Returns: Nothing
     */
    public void paintHandInDisplay(){
        display.paintHand(hand, handID);
    }

    /**
     * Checks if the hand is full. Returns true if it is.
     * Params: None
     * Returns: Boolean
     */
    public boolean isFull(){
        if(handSize == MAX_HAND_SIZE){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Checks if swap was made by asking display.
     * Params: None
     * Returns: Boolean
     */
    public boolean getSwapMade(){
        return display.swapMade();
    }

    /**
     * Checks if sort was made by asking display.
     * Params: None
     * Returns: Boolean
     */
    public boolean getSortMade(){
        return display.sortMade();
    }

    /**
     * Checks if tile was discarded by asking display.
     * Params: None
     * Returns: Boolean
     */
    public boolean getTileDiscarded(){
        return display.tileDiscarded();
    }

    /**
     * Checks if tile was drawn from deck by asking display.
     * Params: None
     * Returns: Boolean
     */
    public boolean getTileDrawn(){
        return display.tileDrawn();
    }

    /**
     * Checks if tile was drawn from discard pile by asking display.
     * Params: None
     * Returns: Boolean
     */
    public boolean getDiscardDrawn(){
        return display.discardDrawn();
    }

    /**
     * Simple sort hand algorithm that first organizes cards by color, then
     * by rank. Used for player to sort hand with sort hand button. Not
     * intended to solve the game for the player, just to assist them with
     * understanding what cards are in their hand, faster than normal.
     * Params: None
     * Returns: Nothing
     */
    public void sortHand(){

        for(int i = 0; i < MAX_HAND_SIZE; i++){
            Tile tile = hand.get(i);
            Color check = tile.getColor();
            hand.set(i, nullTile);
            handSize--;

            if(check == Color.BLUE){
                blue.add(tile);
            }else if(check == Color.GREEN){
                green.add(tile);
            }else if(check == Color.RED){
                red.add(tile);
            }else if(check == Color.YELLOW){
                yellow.add(tile);
            }else if(check == Color.JOKER){
                jokers.add(tile);
            }
        }

        blue.sort(new SortByRank());
        green.sort(new SortByRank());
        red.sort(new SortByRank());
        yellow.sort(new SortByRank());

        addBackToHand(blue);
        addBackToHand(green);
        addBackToHand(red);
        addBackToHand(yellow);
        addBackToHand(jokers);
    }


    /**
     * Very complex sort algorithm used for the computer to sort their hand.
     * The algorithm partitions the computer's hand into different lists of
     * colors (similar to human player's sort algorithm). Then sorts each list
     * by rank. Next it moves all duplicate cards to another list to be followed
     * up with later. The algorithm then finds any runs it can in any of the
     * lists. Any runs identified are immediately placed back in the hand. Next,
     * jokers (that were partitioned by color in the start), are placed back in
     * the hand. Next the algorithm puts all cards (outside the hand) back into
     * a single list, then partitions into 13 lists, all representing
     * a rank 1-13. These lists are then sorted based on the size of each list.
     * The list with the largest size is placed back in the hand first,
     * followed by the rest.
     * Finally, any remaining duplicate cards are placed at the end of the
     * hand as the "lease significant cards". The computer will always discard
     * the card at the last index of the hand, as it is usually a duplicate or
     * card that is not part of a set or run.
     * Params: None
     * Returns: Nothing
     */
    public void sortHandComp(){

        for(int i = 0; i < MAX_HAND_SIZE; i++){
            Tile tile = hand.get(i);
            Color check = tile.getColor();
            hand.set(i, nullTile);
            handSize--;

            if(check == Color.BLUE){
                blue.add(tile);
            }else if(check == Color.GREEN){
                green.add(tile);
            }else if(check == Color.RED){
                red.add(tile);
            }else if(check == Color.YELLOW){
                yellow.add(tile);
            }else if(check == Color.JOKER){
                jokers.add(tile);
            }
        }

        blue.sort(new SortByRank());
        green.sort(new SortByRank());
        red.sort(new SortByRank());
        yellow.sort(new SortByRank());


        blue = findRuns(blue);
        green = findRuns(green);
        red = findRuns(red);
        yellow = findRuns(yellow);

        addBackToHand(jokers);

        findSets();
    }

    /**
     * Helper method to computer sort method. Logic explained there.
     * Params: List<Tile>
     * Returns: List<Tile>
     */
    private List<Tile> findRuns(List<Tile> list){

        Tile previousTile = nullTile;
        List<Tile> duplicates = removeDuplicates(list);
        List<Tile> runs = new ArrayList<>();
        List<Tile> nonRuns = new ArrayList<>();
        boolean wasMatch = false;

        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getRank() - previousTile.getRank() == 1){
                if(nonRuns.size() > 0 && !wasMatch){
                    nonRuns.remove(nonRuns.size()-1);
                }
                runs.add(previousTile);
                runs.add(list.get(i));

                removeDuplicates(runs);
                wasMatch = true;
            }else{
                if(runs.size() >= 3){
                    for(int r = 0; r < runs.size(); r++){
                        draw(runs.get(r));
                        runs.remove(r);
                        r--;
                    }
                }else if(runs.size() > 0){
                    for(int r = 0; r < runs.size(); r++){
                        nonRuns.add(runs.get(r));
                        runs.remove(r);
                        r--;
                    }
                }

                nonRuns.add(list.get(i));
                wasMatch = false;

            }

            previousTile = list.get(i);
        }


        if(runs.size() >= 3){
            for(int r = 0; r < runs.size(); r++){
                draw(runs.get(r));
                runs.remove(r);
                r--;
            }
        }else if(runs.size() > 0){
            for(int r = 0; r < runs.size(); r++){
                nonRuns.add(runs.get(r));
                runs.remove(r);
                r--;
            }
        }


        for(int i = 0; i < duplicates.size(); i++){
            nonRuns.add(duplicates.get(i));
        }

        nonRuns.sort(new SortByRank());

        return nonRuns;
    }


    /**
     * Helper method to computer sort method. Logic explained there.
     * Params: None
     * Returns: Nothing
     */
    private void findSets(){
        List<Tile> blueDups = removeDuplicates(blue);
        List<Tile> greenDups = removeDuplicates(green);
        List<Tile> redDups = removeDuplicates(red);
        List<Tile> yellowDups = removeDuplicates(yellow);

        List<Tile> remainingTiles = new ArrayList<>();

        for(int b = 0; b < blue.size(); b++){
            remainingTiles.add(blue.get(b));
            blue.remove(b);
            b--;
        }

        for(int g = 0; g < green.size(); g++){
            remainingTiles.add(green.get(g));
            green.remove(g);
            g--;
        }

        for(int r = 0; r < red.size(); r++){
            remainingTiles.add(red.get(r));
            red.remove(r);
            r--;
        }

        for(int y = 0; y < yellow.size(); y++){
            remainingTiles.add(yellow.get(y));
            yellow.remove(y);
            y--;
        }

        remainingTiles.sort(new SortByRank());

        organizeRuns(remainingTiles);

        //addBackToHand(jokers);

        addBackToHand(blueDups);
        addBackToHand(greenDups);
        addBackToHand(redDups);
        addBackToHand(yellowDups);

    }

    /**
     * Helper method to computer sort method. Logic explained there.
     * Params: List<Tile>
     * Returns: Nothing
     */
    private void organizeRuns(List<Tile> tiles){
        List<Tile> ones = new ArrayList<>();
        List<Tile> twos = new ArrayList<>();
        List<Tile> threes = new ArrayList<>();
        List<Tile> fours = new ArrayList<>();
        List<Tile> fives = new ArrayList<>();
        List<Tile> sixes = new ArrayList<>();
        List<Tile> sevens = new ArrayList<>();
        List<Tile> eights = new ArrayList<>();
        List<Tile> nines = new ArrayList<>();
        List<Tile> tens = new ArrayList<>();
        List<Tile> elevens = new ArrayList<>();
        List<Tile> twelves = new ArrayList<>();
        List<Tile> thirteens = new ArrayList<>();

        List<List<Tile>> lists = new ArrayList<>();


        for(int i = 0; i < tiles.size(); i++){
            switch(tiles.get(i).getRank()){
                case 1:
                    ones.add(tiles.get(i));
                    tiles.remove(i);
                    i--;
                    break;
                case 2:
                    twos.add(tiles.get(i));
                    tiles.remove(i);
                    i--;
                    break;
                case 3:
                    threes.add(tiles.get(i));
                    tiles.remove(i);
                    i--;
                    break;
                case 4:
                    fours.add(tiles.get(i));
                    tiles.remove(i);
                    i--;
                    break;
                case 5:
                    fives.add(tiles.get(i));
                    tiles.remove(i);
                    i--;
                    break;
                case 6:
                    sixes.add(tiles.get(i));
                    tiles.remove(i);
                    i--;
                    break;
                case 7:
                    sevens.add(tiles.get(i));
                    tiles.remove(i);
                    i--;
                    break;
                case 8:
                    eights.add(tiles.get(i));
                    tiles.remove(i);
                    i--;
                    break;
                case 9:
                    nines.add(tiles.get(i));
                    tiles.remove(i);
                    i--;
                    break;
                case 10:
                    tens.add(tiles.get(i));
                    tiles.remove(i);
                    i--;
                    break;
                case 11:
                    elevens.add(tiles.get(i));
                    tiles.remove(i);
                    i--;
                    break;
                case 12:
                    twelves.add(tiles.get(i));
                    tiles.remove(i);
                    i--;
                    break;
                case 13:
                    thirteens.add(tiles.get(i));
                    tiles.remove(i);
                    i--;
                    break;
            }
        }

        lists.add(ones);
        lists.add(twos);
        lists.add(threes);
        lists.add(fours);
        lists.add(fives);
        lists.add(sixes);
        lists.add(sevens);
        lists.add(eights);
        lists.add(nines);
        lists.add(tens);
        lists.add(elevens);
        lists.add(twelves);
        lists.add(thirteens);

        lists.sort(new SortBySize());

        for(int i = 0; i < 13; i++){
            addBackToHand(lists.get(i));
        }
    }

    /**
     * Helper method to computer sort method. Logic explained there.
     * Params: List<Tile>
     * Returns: Nothing
     */
    private void addBackToHand(List<Tile> tiles){
        for(int i = 0; i < tiles.size(); i++){
            draw(tiles.get(i));
            tiles.remove(i);
            i--;
        }
    }

    /**
     * Helper method to computer sort method. Logic explained there.
     * Params: List<Tile>
     * Returns: List<Tile>
     */
    private List<Tile> removeDuplicates(List<Tile> list){
        Tile previousTile = nullTile;
        List<Tile> duplicates = new ArrayList<>();

        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getRank() == previousTile.getRank()){
                duplicates.add(list.get(i));
                list.remove(i);
                i--;
            }
            previousTile = list.get(i);
        }
        return duplicates;
    }

    /**
     * Debugging method.
     * Params: None
     * Returns: Nothing
     */
    private void printLists(){

        System.out.println("BLUE: SIZE=" +  blue.size());
        for(Tile x : blue){
            System.out.println(x.getColor() + " " + x.getRank());
        }

        System.out.println("GREEN: SIZE=" + green.size());
        for(Tile x : green){
            System.out.println(x.getColor() + " " + x.getRank());
        }

        System.out.println("RED: SIZE=" + red.size());
        for(Tile x : red){
            System.out.println(x.getColor() + " " + x.getRank());
        }

        System.out.println("YELLOW: SIZE=" + yellow.size());
        for(Tile x : yellow){
            System.out.println(x.getColor() + " " + x.getRank());
        }

        System.out.println("JOKER: SIZE=" + jokers.size());
        for(Tile x : jokers){
            System.out.println(x.getColor() + " " + x.getRank());
        }

    }

    /**
     * Checks the hand for a win. Returns true if every tile in the hand
     * belongs to a set or a run. Sets and runs cannot contain blank tiles.
     * They can however be separated by blank tiles. This algorithm keeps two
     * pointers to indices of the hand, a head and a tail. It systematically
     * searching the hand for sets and runs with different indices. Starting
     * with head = 0, and tail = 3 (unless there are blank spaces, tail++ for
     * each). Since a set and run both require at least 3 cards to be true.
     * If there is a set or run in the first 3 cards observed, the tail
     * increments by one, and the head stays in the same spot. This continues
     * till a false set AND run is returned. When this happens, the head is
     * set equal to the tail, and the algorithm begins the search anew. If a
     * joker is part of the tail end of a formation > 3 cards, the head and
     * tail will be pushed back by one, to assure the joker wasn't intended to
     * be a part of the following formation. If the algorithm returns false if
     * the check to set AND run returns false two times in a row.
     * Params: None
     * Returns: Boolean
     */
    public boolean isWin(){
        int head = 0;
        int tail = 0;

        int traversal = 1;
        Color previousTileColor = null;


        while(head <= MAX_HAND_SIZE-1){


            if((previousTileColor == Color.JOKER) && traversal > 3){


                if(head <= MAX_HAND_SIZE-4){

                    int rank = 0;

                    for(int i = 0; i < 4; i++){
                        if(hand.get(tail+i).getColor() != Color.JOKER){
                            rank = hand.get(tail+i).getRank();
                            break;
                        }
                    }

                    if(hand.get(tail).getRank() == 1 &&
                        hand.get(tail+1).getRank() == 2){

                    }else if(rank != 0){
                        if((hand.get(tail).getRank() == rank ||
                                hand.get(tail).getColor() == Color.JOKER) &&
                            (hand.get(tail+1).getRank() == rank ||
                                hand.get(tail+1).getColor() == Color.JOKER) &&
                        (hand.get(tail+2).getRank() == rank ||
                                hand.get(tail+2).getColor() == Color.JOKER) &&
                        (hand.get(tail+3).getRank() == rank ||
                                hand.get(tail+3).getColor() == Color.JOKER)){
                        }else{
                            head--;
                            tail--;
                        }
                    }else{

                        head--;
                        tail--;
                    }
                }else{
                    head--;
                    tail--;
                }
            }else if(traversal > 3 && tail-1 <= MAX_HAND_SIZE-3){
                if(isSet(head-1, tail+1) ||
                        isRun(head-1, tail+1)){
                    head--;
                    tail--;
                }
            }

            traversal = 1;

            if(hand.get(tail).getColor() == Color.BLANK){
                tail++;
                head++;
            }else{

                //No more possible formations at this point
                if(head >= MAX_HAND_SIZE-2){
                    return false;
                }

                tail+=2;

                if(isSet(head, tail) || isRun(head, tail)){
                    while( tail < MAX_HAND_SIZE &&
                            (isSet(head, tail) || isRun(head, tail))){
                        tail++;
                    }
                    traversal = tail - head;
                    head = tail;
                    previousTileColor = hand.get(tail-1).getColor();

                }else{

                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Returns false if there are less than 3 or more than 4 cards being
     * evaluated, as a set much only contain 3 or 4 cards. The algorithm
     * checks if there is any cards of the same color (excluding jokers), in
     * which case it returns false. Finally, the algorithm assures that all
     * cards are of the same rank. If all these tests are passed, returns
     * true.
     * Params: Int, Int
     * Returns: Boolean
     */
    public boolean isSet(int head, int tail){

        int foundBlue = 0;
        int foundGreen = 0;
        int foundRed = 0;
        int foundYellow = 0;
        int numJokers = 0;

        int numTiles = tail - head + 1;

        int rankToCompare = -1;
        boolean foundFirstNonJoker = false;

        //run consists of 3-4 different color tiles with the same rank. There
        //are only 4 different colored tiles in the game.
        if(numTiles > 4){
            return false;
        }


        for(int i = head; i <= tail; i++){
            switch(hand.get(i).getColor()){
                case BLUE:
                    foundBlue++;
                    break;
                case GREEN:
                    foundGreen++;
                    break;
                case RED:
                    foundRed++;
                    break;
                case YELLOW:
                    foundYellow++;
                    break;
                case BLANK:
                    return false;
                default:
                    numJokers++;
            }
        }

        //all tiles are jokers, so it's a run
        if(numTiles == numJokers){
            return true;
        }

        //only one of each color may exist
        if(foundBlue > 1 || foundGreen > 1 || foundRed  > 1 || foundYellow > 1){
            return false;
        }

        for(int i = head; i <= tail; i++){
            if(hand.get(i).getColor() != Color.JOKER){
                if(!foundFirstNonJoker){
                    rankToCompare = hand.get(i).getRank();
                    foundFirstNonJoker = true;
                }else{
                    if(hand.get(i).getRank() != rankToCompare){
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Extremely, extremely, EXTREMELY complicated algorithm that returns
     * true if there is a run and false if not. The reason for the algorithms
     * complexity is the number of possible edge cases with 1 counting as 1
     * or 14, Jokers counting as anything, and the possibility of having up to 4
     * Jokers in one hand.
     * Params: Int, Int
     * Returns: Boolean
     */
    public boolean isRun(int head, int tail){

        Tile previousTile = null;
        Tile firstNonJokerTile = null;
        Color color = null;
        boolean foundFirstNonBlank = false;
        boolean foundFirstNonJoker = false;
        int padding = 1;
        int lastTileIdx = tail;
        int jokerValue = 0;
        int numConsecutiveJokers = 0;
        int numJokers = 0;
        int jokerSkips = 0;



        for(int i = head; i <= tail; i++){
            if(hand.get(i).getRank() != -1){
                lastTileIdx = i;
                if(!foundFirstNonBlank){
                    previousTile = hand.get(i);
                    foundFirstNonBlank = true;
                }
                if(hand.get(i).getColor() != Color.JOKER){
                    if(numJokers >= numConsecutiveJokers){
                        numConsecutiveJokers = numJokers;
                        numJokers = 0;
                    }
                    if(!foundFirstNonJoker){
                        color = hand.get(i).getColor();
                        firstNonJokerTile = hand.get(i);
                        foundFirstNonJoker = true;
                    }
                }else{
                    numJokers++;
                }

            }else{
                //changed design decision, will now always return false if blank
                //spaces are included.
                return false;
            }
        }

        if(numJokers > numConsecutiveJokers){
            numConsecutiveJokers = numJokers;
        }


        //if reaching this point, all tiles are jokers, therefore a set
        if(color == null){
            return true;
        }


        for(int i = head; i <= tail; i++){
            if(hand.get(i).getRank() != -1){
                if((hand.get(i).getColor() != color) &&
                        (hand.get(i).getColor() != Color.JOKER)){
                    return false;
                }
            }
        }




        for(int i = head+padding; i <= tail; i++){
            if(hand.get(i).getRank() != -1){
                if(previousTile.getColor() == Color.JOKER){
                    if(jokerValue == 0){
                        if(hand.get(i).getRank() == 1){
                            return false;
                        }else if(hand.get(i).getColor() == Color.JOKER){
                            if(firstNonJokerTile.getRank() -
                                    numConsecutiveJokers < 1 &&
                                    firstNonJokerTile.getRank() != 1){
                                return false;
                            }else if(firstNonJokerTile.getRank() == 1){
                                if(hand.get(lastTileIdx).getRank() == 1){
                                    return true;
                                }else{
                                    return false;
                                }
                            }
                            jokerSkips += numConsecutiveJokers-2;
                            jokerValue = firstNonJokerTile.getRank()-
                                    (jokerSkips+1);
                        }else{
                            jokerValue = hand.get(i).getRank()-1;
                            previousTile = hand.get(i);
                        }
                    }else if(hand.get(i).getColor() == Color.JOKER &&
                            jokerSkips == 0){
                        jokerSkips += numConsecutiveJokers-2;
                        jokerValue++;
                    }else if(jokerSkips > 0){
                        jokerSkips--;
                        jokerValue++;
                    }else if(hand.get(i).getRank() - jokerValue == 1) {
                        previousTile = hand.get(i);
                    }else if((hand.get(i).getRank() -
                            jokerValue == -12)
                            && (i == lastTileIdx)){
                        return true;
                    }else{
                        return false;
                    }
                }else if(hand.get(i).getColor() == Color.JOKER){
                    //edge cases
                    if(previousTile.getRank() == 13 && i != lastTileIdx){
                        return false;
                    }else if(previousTile.getRank() == 12
                            && lastTileIdx > i+1){
                        return false;
                    }else if(previousTile.getRank() == 11
                            && lastTileIdx > i+2){
                        return false;
                    }else{
                        jokerValue = previousTile.getRank()+1;
                    }
                    previousTile = hand.get(i);
                }else if(hand.get(i).getRank() - previousTile.getRank() == 1){

                    previousTile = hand.get(i);
                    //if 1 is followed by 13 (1 - 13 = -12)
                }else if((hand.get(i).getRank() - previousTile.getRank() == -12)
                        && (i == lastTileIdx)){
                    return true;
                }else{
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Returns the value of a player's hand. Calculated by adding up the rank
     * of each card in the hand, with Joker's value = 25.
     * Params: None
     * Returns: Int
     */
    public int calculateValue(){
        int value = 0;

        for(int i = 0; i < MAX_HAND_SIZE; i++){
            if(hand.get(i).getColor() != Color.BLANK){
                if(hand.get(i).getColor() == Color.JOKER){
                    value += 25;
                }else{
                    value += hand.get(i).getRank();
                }
            }
        }

        return value;
    }


}

/**
 * Inner class that implements Comparator, so that sort method can be used on
 * List<Tile>
 */

class SortByRank implements Comparator<Tile>{

    public int compare(Tile a, Tile b){
        return a.getRank() - b.getRank();
    }
}

/**
 * Inner class that implements Comparator, so that sort method can be used on
 * List<List<Tile>>
 */
class SortBySize implements Comparator<List<Tile>>{

    public int compare(List<Tile> a, List<Tile> b){
        return b.size() - a.size();
    }
}
