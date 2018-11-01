/**
 * Name: Tyler Fenske
 * Class: CS351
 * Date: 10/04/2018
 *
 * TupleSpace interface that creates a contract with all implementations of
 * tuplespace to assure the following methods are used:
 *
 * add(Tuple tuple)
 * remove(Object...obj)
 * read(Object...obj)
 */

package TupleSpace;

public interface TupleSpace {

    /**
     * Will add a tuple to the tuple space.
     * @param tuple
     */
    public void add(Tuple tuple);

    /**
     * Will remove a tuple with matching pattern from the tuple space.
     * @param obj
     * @return Tuple
     */
    public Tuple remove(Object...obj);

    /**
     * Will return tuple with matching pattern from tuple space if found. Else
     * will return null.
     * @param obj
     * @return Tuple
     */
    public Tuple read(Object...obj);
}


