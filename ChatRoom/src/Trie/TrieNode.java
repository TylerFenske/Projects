/**
 * Name: Tyler Fenske
 * Class: CS351
 * Date: 10/04/2018
 *
 * Each trie node has a hashmap that holds all of their child nodes,
 * a boolean isTuple field that is marked true if the node is the last node
 * in an added tuple, and an Object that defines the value of a certain
 * Tuple element.
 *
 * Some important public methods include:
 * addChild(TrieNode child) - which adds a new node to this node's set of
 * children.
 * removeChild(TrieNode child) - which removes a node from this node's set of
 * children.
 * getSetOfKeys() - returns all keys of children as a set.
 */

package Trie;
import java.util.HashMap;
import java.util.Set;

public class TrieNode {

    private HashMap<Object, TrieNode> children;
    private boolean isTuple;
    private Object obj;

    /**
     * No argument TrieNode constructor. Used to create the root.
     */
    public TrieNode(){
        children = new HashMap<>();
        isTuple = false;
    }

    /**
     * TrieNode constructor used to create new nodes for non-root nodes.
     * @param obj
     */
    public TrieNode(Object obj){
        this();
        this.obj = obj;
    }

    /**
     * @return number of children this node has.
     */
    public int getNumChildren(){
        return children.size();
    }

    /**
     * @return tuple element represented in this node.
     */
    public Object getObj(){
        return obj;
    }

    /**
     * Returns true if this node is the final node in a tuple.
     * @return isTuple.
     */
    public boolean isTuple(){
        return isTuple;
    }

    /**
     * Changes value of isTuple.
     * @param isTuple
     */
    public void setIsTuple(boolean isTuple){
        this.isTuple = isTuple;
    }

    /**
     * Returns the child with a matching key. Returns null if no child
     * with matching key found.
     * @param obj
     * @return TrieNode
     */
    public TrieNode getChild(Object obj){
        return children.get(obj);
    }

    /**
     * Adds child node to children hashmap.
     * @param child
     */
    public void addChild(TrieNode child){
        children.put(child.getObj(), child);
    }

    /**
     * Removes child node from children hashmap.
     * @param child
     */
    public void removeChild(TrieNode child){
        children.remove(child.getObj());
    }

    /**
     * Debugging method.
     */
    public void printChildren(){
        System.out.println(children.entrySet());
    }

    /**
     * @return Set data structure with all children keys.
     */
    public Set getSetOfKeys(){
        return children.keySet();
    }
}
