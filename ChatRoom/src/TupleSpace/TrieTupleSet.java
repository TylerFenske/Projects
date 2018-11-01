/**
 * Name: Tyler Fenske
 * Class: CS351
 * Date: 10/04/2018
 *
 * The main tuple space is partitioned into tuple sets. A trie data structure
 * is initialized for each tuple set. Each tuple set
 * simply calls the add, remove, and read methods in the trie.
 */

package TupleSpace;
import Trie.*;

public class TrieTupleSet {

    private Trie trie;

    /**
     * Constructor for TrieTupleSet. Initialized a trie of passed size.
     * @param setSize
     */
    public TrieTupleSet(int setSize){
        trie = new Trie(setSize);
    }

    /**
     * Calls remove method from trie.
     * @param obj
     * @return Tuple.
     */
    public Tuple remove(Object... obj) {
        return trie.remove(obj);
    }

    /**
     * Calls read method from trie.
     * @param obj
     * @return Tuple.
     */
    public Tuple read(Object... obj) {
        return trie.read(obj);
    }

    /**
     * Calls add method from trie.
     * @param tuple
     */
    public void add(Tuple tuple) {
        trie.add(tuple);
    }

    /**
     * debugging method.
     */
    public Trie getTrie(){
        return trie;
    }
}
