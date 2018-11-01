/**
 * Name: Tyler Fenske
 * Class: CS351
 * Date: 10/04/2018
 *
 * This class defines a Trie (retrieval tree) structure. The trie consists of
 * trie nodes (defined by a hashmap with key = tuple object and value =
 * hashmap of children).
 *
 * The trie data structure has the follow methods:
 * add(Tuple tuple) - adds each element of the passed tuple to the trie.
 * remove(Object[]) - removes pattern from trie if found, then returns the
 * pattern as a Tuple object (or null if no matching pattern found).
 * read(Object[]) - looks for pattern passed through Object array, if found
 * returns the pattern as a Tuple object. (or null if no matching pattern is
 * found).
 */

package Trie;
import TupleSpace.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Trie {

    private TrieNode root;
    private int setSize;
    private final String WILD = "*";
    private List<Tuple> tuples = new ArrayList<>();

    /**
     * Constructor for trie object.
     * @param setSize
     */
    public Trie(int setSize){
        root = new TrieNode();
        this.setSize = setSize;
    }

    /**
     * Loops over each tuple element. Starting with the root node, checks if
     * node has children with the same key as tuple element being evaluated.
     * If a match is found, nothing is added. If no match is found, a new
     * trie node is created for each element.
     *
     * The last node of the tuple pattern (if found) has filed "isTuple"
     * changed to true.
     * @param tuple
     */
    public void add(Tuple tuple){
        TrieNode currentNode = root;
        boolean addedToList = false;

        for(int i = 0; i < tuple.getSize(); i++){

            TrieNode child = currentNode.getChild(tuple.get(i));

            if(child == null){
                child = new TrieNode(tuple.get(i));
                currentNode.addChild(child);
                if(!addedToList){
                    tuples.add(tuple);
                    addedToList = true;
                }
            }

            currentNode = child;

            if(i == tuple.getSize()-1){
                currentNode.setIsTuple(true);
            }
        }
    }


    /**
     * Traverses the trie, comparing the passed pattern to each node during
     * traversal. If the pattern is found in the trie, returns a Tuple created
     * with the same pattern passed. If no matching pattern is found, returns
     * null.
     * @param obj
     * @return Tuple with matching pattern
     */
    public Tuple read(Object...obj){
        List<Object> tuple = new ArrayList<>();
        TrieNode currentNode = root;
        boolean containsWild = checkForWild(obj);

        if(containsWild){
            Tuple tup = findMatchingTuple(false, obj);

            if(tup == null){
                return null;
            }else{
                obj = toArray(tup);
            }
        }

        for(int i = 0; i < obj.length; i++){

            TrieNode child = currentNode.getChild(obj[i]);

            if(child == null){
                return null;
            }

            currentNode = child;
            tuple.add(currentNode.getObj());

            if(i == obj.length-1){
                if(currentNode.isTuple()){
                    return new Tuple(tuple);
                }
            }
        }
        return null;
    }


    /**
     * Traverses trie looking for matching pattern. If matching pattern is
     * found, removes pattern from trie and returns a Tuple object with same
     * pattern. If no matching pattern is found, return null.
     *
     * If tuple pattern is found, but last node still has children,
     * just changes node value "isTuple" to false.
     * @param obj
     * @return Tuple with matching pattern
     */
    public Tuple remove(Object...obj){
        List<Object> tuple = new ArrayList<>();
        Stack<TrieNode> ancestors = new Stack<>();
        TrieNode currentNode = root;
        Tuple result;
        boolean containsWild = checkForWild(obj);

        if(containsWild){
            Tuple tup = findMatchingTuple(true, obj);

            if(tup == null){
                return null;
            }else{
                obj = toArray(tup);
            }
        }

        for(int i = 0; i < obj.length; i++){

            TrieNode child = currentNode.getChild(obj[i]);

            if(child == null){
                return null;
            }

            currentNode = child;
            ancestors.push(currentNode);
            tuple.add(currentNode.getObj());

            if(i == obj.length-1){
                if(currentNode.isTuple()){
                    result = new Tuple(tuple);
                    currentNode.setIsTuple(false);
                    if(currentNode.getNumChildren() == 0){
                        removeAncestors(ancestors);
                    }
                    return result;
                }
            }
        }

        return null;
    }

    private void removeAncestors(Stack<TrieNode> ancestors){
        TrieNode child;
        int size = ancestors.size();

        for(int i = 0; i < size; i++){
            child = ancestors.peek();
            if(!(child.isTuple() || child.getNumChildren() > 0)){
                ancestors.pop();
                if(ancestors.size() == 0){
                    root.removeChild(child);
                }else{
                    ancestors.peek().removeChild(child);
                }
            }else{
                return;
            }
        }
    }

    private boolean checkForWild(Object... obj){
        for(int i = 0; i < obj.length; i++){
            if(obj[i].equals(WILD)){
                return true;
            }
        }
        return false;
    }

    private Tuple findMatchingTuple(boolean remove, Object... obj){
        int match = 0;

        for(int i = 0; i < tuples.size(); i++){
            Tuple tuple = tuples.get(i);
            for(int j = 0; j < setSize; j++){
                if((obj[j].equals(tuple.get(j))) || obj[j].equals(WILD)){
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

    private Object[] toArray(Tuple tuple){
        Object[] obj = new Object[setSize];

        for(int i = 0; i < setSize; i++){
            obj[i] = tuple.get(i);
        }

        return obj;
    }

    /**
     * debugging method.
     */
    public void printTrie(){
        if(root.getNumChildren() == 0){
            System.out.println("Empty Trie");
        }else{
            printTrieRec(root, true);
        }
    }

    private void printTrieRec(TrieNode currentNode, boolean firstRun){
        if(currentNode.getNumChildren() == 0){
            System.out.println("end of tuple");
            return;
        }

        Object[] children = currentNode.getSetOfKeys().toArray();

        for(int i = 0; i < children.length; i++){
            if(firstRun){
                System.out.println("start of tuple");
            }

            TrieNode next = currentNode.getChild(children[i]);

            System.out.println("[" + children[i] + "]");

            if(next.getNumChildren() > 1){
                System.out.println("branch");
            }

            printTrieRec(next, false);
        }
    }
}
