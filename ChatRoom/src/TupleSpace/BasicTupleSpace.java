/**
 * Name: Tyler Fenske
 * Class: CS351
 * Date: 10/04/2018
 *
 * Basic tuple space implementation. Uses an array list to store all tupleSets.
 * Initializes a tupleSet of size 1 through N, where N is passed in the BTS
 * constructor.
 *
 * The following methods are implemented:
 *
 * add(Tuple tuple) - adds a tuple to the tuple space by calling tupleSet add().
 * remove(Object...obj) - removes a tuple with matching pattern by calling
 * tupleSet readOrRemove() with remove flag on.
 * read(Object...obj) - returns a tuple with matching pattern by calling
 * tupleSet readOrRemove() with remove flag off.
 */

package TupleSpace;
import java.util.ArrayList;
import java.util.List;

public class BasicTupleSpace implements TupleSpace {

    private int size;
    private List<BasicTupleSet> tupleSets = new ArrayList<>();

    /**
     * Constructor for Basic Tuple Space.
     * @param size
     */
    public BasicTupleSpace(int size){
        this.size = size;
        initializeTupleSets();
    }

    private void initializeTupleSets(){

        tupleSets.add(null);

        for(int i = 1; i < size + 1; i++){
            BasicTupleSet ts = new BasicTupleSet(i);
            tupleSets.add(ts);
        }
    }

    /**
     * Debugging method
     */
    public int getSize(){

        for(int i = 0; i < tupleSets.size(); i++){
            if(tupleSets.get(i) != null){
                tupleSets.get(i).printSetSize();
            }
        }
        return size;
    }

    /**
     * Debugging method
     */
    public void printTupleSets(){
        for(int i = 1; i < size + 1; i++){
            tupleSets.get(i).printTuples();
        }
    }

    /**
     * Override method that adds a tuple to the tuple space by calling
     * tupleSet add() method.
     * @param tuple
     */
    @Override
    public void add(Tuple tuple) {
        tupleSets.get(tuple.getSize()).add(tuple);
    }

    /**
     * Override method that removes a tuple from the tuple space by calling
     * tupleSet readOrRemove() method with remove flag turn on.
     * Returns null if tuple size is equal to 0.
     * @param obj
     * @return Tuple of matching pattern, or null.
     */
    @Override
    public Tuple remove(Object...obj) {
        int setSize = obj.length;
        BasicTupleSet tSet = tupleSets.get(setSize);

        if(setSize == 0){
            return null;
        }

        Tuple tuple = tSet.readOrRemove(true, obj);

        return tuple;
    }

    /**
     * Override method that returns a tuple from the tuple space by calling
     * tupleSet readOrRemove() method with remove flag turn off.
     * Returns null if tuple size is equal to 0.
     * @param obj
     * @return Tuple of matching pattern, or null.
     */
    @Override
    public Tuple read(Object...obj) {
        int setSize = obj.length;
        BasicTupleSet tSet = tupleSets.get(setSize);

        if(setSize == 0){
            return null;
        }

        Tuple tuple = tSet.readOrRemove(false, obj);

        return tuple;
    }
}
