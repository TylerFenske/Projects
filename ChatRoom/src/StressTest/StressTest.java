/**
 * Name: Tyler Fenske
 * Class: CS351
 * Date: 10/04/2018
 *
 * Stress Test class used to benchmark the performance difference between
 * Basic Tuple Space and Trie Tuple Space. TEST_COUNT defines how many random
 * tuples will be generated, and TUPLE_SPACE_SIZE defines the max size of any
 * random tuple. Randomized tuples are generated with random objects, including
 * randomized integers, strings, characters, and floats.
 *
 * The test will first add all randomized tuples to both tuple spaces, then
 * remove all of the same tuples, printing the working-time to the console.
 *
 * Tests have proven that Trie Tuple Space far exceeds Basic Tuple Space in
 * performance.
 *
 * Trie Tuple Space time complexity for all operations = O(M) where M
 * is the size of the tuple.
 *
 * Basic Tuple Space time complexity for all operations = O(N * M) where N
 * is the number of tuples in the TupleSpace already and M is the size of
 * the tuple.
 */

package StressTest;
import TupleSpace.*;
import java.util.*;

public class StressTest {
    private final static int TEST_COUNT = 100000;
    private final static int TUPLE_SPACE_SIZE = 15;
    //Integer, String, Char, Float
    private final static int NUM_TUPLE_TYPES = 4;

    /**
     * Main method of StressTest. No command line args are used.
     * @param args
     */
    public static void main (String[] args){

        List<Tuple> testTuples = new ArrayList<>();

        TupleSpace bts = new BasicTupleSpace(TUPLE_SPACE_SIZE);
        TupleSpace tts = new TrieTupleSpace(TUPLE_SPACE_SIZE);

        long startTime;
        long endTime;


        //creates a set of random tuples
        for(int i = 0; i <TEST_COUNT; i++){
            testTuples.add(createRandomTuple());
        }



        //adds random tuples to BasicTupleSpace
        startTime = System.currentTimeMillis();
        for(int i = 0; i < TEST_COUNT; i++){
            bts.add(testTuples.get(i));
        }
        endTime = System.currentTimeMillis();
        System.out.println("BasicTupleSpace added " + TEST_COUNT + " tuples in "
                + (endTime - startTime) + " milliseconds.\n");



        //adds random tuples to TrieTupleSpace
        startTime = System.currentTimeMillis();
        for(int i = 0; i < TEST_COUNT; i++){
            tts.add(testTuples.get(i));
        }
        endTime = System.currentTimeMillis();
        System.out.println("TrieTupleSpace added " + TEST_COUNT + " tuples in "
                + (endTime - startTime) + " milliseconds.\n");



        //removes all the tuples previously added to BasicTupleSpace
        startTime = System.currentTimeMillis();
        for(int i = 0; i < TEST_COUNT; i++){
            bts.remove(testTuples.get(i).convertToObjArr());
        }
        endTime = System.currentTimeMillis();
        System.out.println("BasicTupleSpace removed " + TEST_COUNT + " tuples" +
                " in " + (endTime - startTime) + " milliseconds.\n");



        //removes all the tuples previously added to TrieTupleSpace
        startTime = System.currentTimeMillis();
        for(int i = 0; i < TEST_COUNT; i++){
            tts.remove(testTuples.get(i).convertToObjArr());
        }
        endTime = System.currentTimeMillis();
        System.out.println("TrieTupleSpace removed " + TEST_COUNT + " tuples" +
                " in " + (endTime - startTime) + " milliseconds.\n");


    }


    private static Tuple createRandomTuple (){
        List<Object> tuple = new ArrayList<>();
        int nextTupleObj;

        Random rand = new Random();
        int randomTupleSize = rand.nextInt(TUPLE_SPACE_SIZE)+1;

        for(int i = 0; i < randomTupleSize; i++){
            nextTupleObj = rand.nextInt(NUM_TUPLE_TYPES);

            switch(nextTupleObj){
                case 0:
                    int intObj = createRandomInt();
                    tuple.add(intObj);
                    break;
                case 1:
                    float floatObj = createRandomFloat();
                    tuple.add(floatObj);
                    break;
                case 2:
                    char charObj = createRandomCharacter();
                    tuple.add(charObj);
                    break;
                default:
                    String stringObj = createRandomString();
                    tuple.add(stringObj);
            }

        }

        return new Tuple(tuple);
    }

    private static char createRandomCharacter(){
        Random rand = new Random();
        //AsciiTable starts with letter 'A' dec value 65 and ends with
        //letter 'z' dec value 122 (122-65 = 57)
        return (char)(rand.nextInt(57)+65);
    }

    private static String createRandomString(){
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        int stringSize = rand.nextInt(10)+1;

        for(int i = 0; i < stringSize; i++){
            sb.append(createRandomCharacter());
        }
        return sb.toString();
    }

    private static int createRandomInt(){
        Random rand = new Random();
        return rand.nextInt(1000);
    }

    private static float createRandomFloat(){
        Random rand = new Random();
        return rand.nextFloat()+createRandomInt();
    }

}

