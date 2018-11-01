/**
 * Name: Tyler Fenske
 * Class: CS351
 * Date: 10/04/2018
 *
 * Basic tuple set implementation. Creates an array list to store each tuple
 * in. The following primary functions are called in the class:
 *
 * add(Tuple tuple) - adds a tuple to the tuples array list as long as there
 * is not already a matching tuple in the list.
 * readOrRemove(boolean remove, Object...obj) - searches array list for a
 * matching pattern (when compared to the passed object array). If a match isn't
 * found, the method returns null. If a match is found, the found tuple is
 * returned. If the remove flag is set to true, that same tuple will also
 * be removed from the tuples array list.
 */

package TupleSpace;
import java.util.ArrayList;
import java.util.List;
import static java.util.Collections.shuffle;

public class BasicTupleSet {

    private int setSize;
    private List<Tuple> tuples = new ArrayList<>();

    /**
     * Constructor for BasicTupleSet.
     * @param setSize
     */
    public BasicTupleSet(int setSize){
        this.setSize = setSize;
    }

    /**
     * Debugging method.
     */
    public void printSetSize(){
        System.out.println(setSize);
    }

    /**
     * First checks if there is a matching tuple already in the array list, if
     * not, adds that tuple to the list.
     * @param tuple
     */
    public void add(Tuple tuple){
        if(!foundMatch(tuple)){
            tuples.add(tuple);
        }
    }

    /**
     * Searches array list for a matching pattern
     * (when compared to the passed object array). If a match isn't
     * found, the method returns null. If a match is found, the found tuple is
     * returned. If the remove flag is set to true, that same tuple will also
     * be removed from the tuples array list.
     * @param remove
     * @param obj
     * @return
     */
    public Tuple readOrRemove(boolean remove, Object...obj){
        int match = 0;

        //method always returns first tuple match found, but tuples are shuffled
        //first so that it is non-deterministic
        shuffle(tuples);

        for(int i = 0; i < tuples.size(); i++){
            Tuple tuple = tuples.get(i);
            for(int j = 0; j < setSize; j++){
                if((obj[j].equals(tuple.get(j))) || obj[j].equals("*")){
                    match++;
                }
            }
            if(match == setSize){
                if(remove){
                    tuples.remove(i);
                }
                return tuple;
            }
            match = 0;
        }

        return null;
    }

    private boolean foundMatch(Tuple tup){
        int match = 0;

        for(int i = 0; i < tuples.size(); i++){
            Tuple tuple = tuples.get(i);
            for(int j = 0; j < setSize; j++){
                if(tup.get(j).equals(tuple.get(j))){
                    match++;
                }
            }
            if(match == setSize){
                return true;
            }
            match = 0;
        }

        return false;
    }

    /**
     * debugging method.
     */
    public void printTuples(){
        System.out.println(setSize + "-TUPLES:");
        for(Tuple t : tuples){
            t.printTuple();
        }
    }

    /**
     * debugging method.
     */
    public int getSize(){
        return tuples.size();
    }

}
