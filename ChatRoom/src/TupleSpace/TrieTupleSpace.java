/**
 * Name: Tyler Fenske
 * Class: CS351
 * Date: 10/04/2018
 *
 * Tuple space implementation that uses a trie to store all of its tuples.
 * Uses an array list to store all tupleSets. Initializes a tupleSet of size 1
 * through N, where N is passed in the TTS constructor.
 *
 * The following methods are implemented:
 *
 * add(Tuple tuple) - adds a tuple to the tuple space by calling tupleSet add().
 * remove(Object...obj) - removes a tuple with matching pattern by calling
 * tupleSet remove().
 * read(Object...obj) - returns a tuple with matching pattern by calling
 * tupleSet read().
 */

package TupleSpace;
import java.util.ArrayList;
import java.util.List;
import Trie.*;

public class TrieTupleSpace implements TupleSpace{

    private List<TrieTupleSet> tupleSets;
    private int size;

    /**
     * TrieTupleSpace constructor.
     * @param size
     */
    public TrieTupleSpace(int size){
        this.size = size;
        tupleSets = new ArrayList<>();
        initializeTupleSets();
    }

    private void initializeTupleSets(){
        tupleSets.add(null);

        for(int i = 1; i < size+1; i++){
            TrieTupleSet ts = new TrieTupleSet(i);
            tupleSets.add(ts);
        }
    }

    /**
     * Looks for a tuple with a matching pattern to the objects passed in the
     * object array by calling remove() method in tupleSet.
     * @param obj
     * @return Tuple if pattern found, null if not.
     */
    @Override
    public Tuple remove(Object... obj) {
        int tupleSize = obj.length;

        if(tupleSize == 0){
            return null;
        }

        return tupleSets.get(tupleSize).remove(obj);
    }

    /**
     * Looks for a tuple with a matching pattern to the objects passed in the
     * object array by calling read() method in tupleSet.
     * @param obj
     * @return Tuple if pattern found, null if not.
     */
    @Override
    public Tuple read(Object... obj) {
        int tupleSize = obj.length;

        if(tupleSize == 0){
            return null;
        }

        return tupleSets.get(tupleSize).read(obj);
    }

    /**
     * Override method that adds a tuple to the tuple space by calling
     * tupleSet add() method.
     * @param tuple
     */
    @Override
    public void add(Tuple tuple) {
        int tupleSize = tuple.getSize();

        tupleSets.get(tupleSize).add(tuple);
    }

    private Trie getTrie(int index){
        return tupleSets.get(index).getTrie();
    }

    /**
     * debugging method.
     */
    public void printTupleSets(){
        for(int i = 1; i < size + 1; i++){
            System.out.println(i + " TUPLES");
            getTrie(i).printTrie();
            System.out.println();
        }
    }
}
