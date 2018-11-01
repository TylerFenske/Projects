/**
 * Name: Tyler Fenske
 * Class: CS351
 * Date: 10/04/2018
 *
 * Tuple objects hold a list of Objects. These can range from Strings, to ints,
 * to booleans, to newly defined types. A tuple's size is fixed, may contain
 * duplicates, but is unchangeable once created.
 *
 * Three important public methods are used in this class:
 * getSize() - returns k where the tuple in question is a k-tuple.
 * get(int index) - returns the Object in the array list, at the passed index.
 * convertToObjArr() - returns an Object[] of the array list tuple.
 */

package TupleSpace;
import java.util.*;

public class Tuple {

    private int size;
    private List<Object> tuple;


    /**
     * Tuple constructor.
     * @param tuple
     */
    public Tuple (List<Object> tuple){
        this.tuple = tuple;
        size = tuple.size();
    }

    /**
     * debugging method.
     */
    public void printTuple(){
        System.out.println("---" + size + "-tuple---");
        for(Object o : tuple){
            System.out.println(o.getClass().getName() + " = " + o);
        }
    }

    /**
     * @return the number of elements in the tuple.
     */
    public int getSize(){
        return size;
    }

    /**
     * @param index
     * @return Object located in tuple array list at index passed.
     */
    public Object get(int index){
        return tuple.get(index);
    }

    /**
     * Converts array list tuple to Object array.
     * @return Object array.
     */
    public Object[] convertToObjArr(){
        Object[] objects = new Object[size];

        for(int i = 0; i < size; i++){
            objects[i] = tuple.get(i);
        }

        return objects;
    }
}
