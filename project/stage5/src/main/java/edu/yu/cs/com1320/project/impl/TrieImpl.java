//                                                                             בס"ד
package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.Trie;

import java.util.*;

public class TrieImpl <Value> implements Trie<Value> {
    //node class
    private class Node<Value>{
        private Node[] links = new Node[TrieImpl.alphabetSize];
        private ArrayList<Value> value = new ArrayList<>();
    }
    private static final int alphabetSize = 256;
    private Node<Value> root; // root of trie



    /**
     * add the given value at the given key
     * @param key
     * @param val
     */
    @Override
    public void put(String key, Value val) {
        this.root = putHelp(this.root, key, val, 0);
    }
    private Node putHelp(Node node,String key, Value val, int d){
        //make new node
        if (node == null) {
            node = new Node();
        }
        //reached last node in key (reached last letter of word)
        if (d == key.length()){
            node.value.add(val);
            return node;
        }
        char c = key.charAt(d);
        node.links[c] = this.putHelp(node.links[c], key, val, d + 1);
        return node;
    }

    /**
     * Get all exact matches for the given key, sorted in descending order, where "descending" is defined by the comparator.
     * NOTE FOR COM1320 PROJECT: FOR PURPOSES OF A *KEYWORD* SEARCH, THE COMPARATOR SHOULD DEFINE ORDER AS HOW MANY TIMES THE KEYWORD APPEARS IN THE DOCUMENT.
     * Search is CASE SENSITIVE.
     * @param key
     * @param comparator used to sort values
     * @return a List of matching Values. Empty List if no matches.
     */
    @Override
    public List<Value> getSorted(String key, Comparator<Value> comparator) {
        Node node = this.get(this.root, key, 0);
        //no matches, return empty list
        if (node == null){
            return Collections.emptyList();
        }
        //there is a value, get it then, sort it, then return it
        ArrayList<Value> list = node.value;
        list.sort(comparator);
        return list;
    }
    /**
     * get all exact matches for the given key.
     * Search is CASE SENSITIVE.
     * @param key
     * @return a Set of matching Values. Empty set if no matches.
     */
    @Override
    public Set<Value> get(String key) {
        Node node = this.get(this.root, key, 0);
        if (node == null) {
            return Collections.emptySet();
        }

        Set <Value> set = new HashSet<>(node.value);
        return set;
    }
    private Node get(Node node, String key, int d) {
        //link was null - return null, indicating a miss
        if (node == null) {
            return null;
        }
        //we've reached the last node in the key,
        //return the node
        if (d == key.length()) {
            return node;
        }
        //proceed to the next node in the chain of nodes that
        //forms the desired key
        char c = key.charAt(d);
        return this.get(node.links[c], key, d + 1);
    }

    /**
     * get all matches which contain a String with the given prefix, sorted in descending order, where "descending" is defined by the comparator.
     * NOTE FOR COM1320 PROJECT: FOR PURPOSES OF A *KEYWORD* SEARCH, THE COMPARATOR SHOULD DEFINE ORDER AS HOW MANY TIMES THE KEYWORD APPEARS IN THE DOCUMENT.
     * For example, if the key is "Too", you would return any value that contains "Tool", "Too", "Tooth", "Toodle", etc.
     * Search is CASE SENSITIVE.
     * @param prefix
     * @param comparator used to sort values
     * @return a List of all matching Values containing the given prefix, in descending order. Empty List if no matches.
     */
    @Override
    public List<Value> getAllWithPrefixSorted(String prefix, Comparator<Value> comparator) {
        HashSet<Value> set = new HashSet<>();
        // find node which represents the prefix
        Node node = this.get(this.root, prefix, 0);
        if (node != null) {
            this.prefixHelp(node,new StringBuilder(prefix),set);
        }
        ArrayList<Value> list = new ArrayList<>(set);
        list.sort(comparator);
        return list;
    }
    private void prefixHelp(Node node, StringBuilder prefix, HashSet<Value> set){

        if (node.value != null){
            for (Object v : node.value){
                set.add((Value) v);
            }
        }
        //visit each non-null child/link
        for (char c = 0; c < alphabetSize; c++){
            if (node.links[c]!= null){
                for (Object v : node.links[c].value){
                    set.add((Value) v);
                }
                prefix.append(c);
                this.prefixHelp(node.links[c],prefix,set);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
    }

    /**
     * Delete the subtree rooted at the last character of the prefix.
     * Search is CASE SENSITIVE.
     * @param prefix
     * @return a Set of all Values that were deleted.
     */
    @Override
    public Set<Value> deleteAllWithPrefix(String prefix) {
        HashSet<Value> set = new HashSet<>();
        Node node = this.get(this.root, prefix, 0);
        if (node != null) {
            this.prefixDeleteHelp(node,new StringBuilder(prefix),set);

        }
//        prefixDeleteHelp(node,new StringBuilder(prefix),set);
        return set;
    }

    private void prefixDeleteHelp(Node node, StringBuilder prefix,  Set<Value> set) {
        if (node == null) {
            return;
        }

        // Recursively delete values in child nodes
        for (char c = 0; c < alphabetSize; c++) {
            if (node.links[c] != null) {
                prefix.append(c);
                this.prefixDeleteHelp(node.links[c], prefix, set);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }

        // Remove the values associated with the current node
        if (!node.value.isEmpty()) {
            set.addAll(node.value); // Add values to the set of deleted values
            node.value.clear(); // Clear the list of values associated with the node
        }
    }


    /**
     * Delete all values from the node of the given key (do not remove the values from other nodes in the Trie)
     * @param key
     * @return a Set of all Values that were deleted.
     */
    @Override
    public Set<Value> deleteAll(String key) {
        Node node = this.get(this.root,key,0);
        if (node == null){
            return new HashSet<Value>();
        }
        Set<Value> set = new HashSet<Value>(node.value);
        node.value.clear();
        return set;
    }
    /**
     * Remove the given value from the node of the given key (do not remove the value from other nodes in the Trie)
     * @param key
     * @param val
     * @return the value which was deleted. If the key did not contain the given value, return null.
     */
    @Override
    public Value delete(String key, Value val) {
        Node node = get(this.root,key,0);
        if (node == null){
            return null;
        }
        //going through each value in the node's ArrayList and seeing if its equal to the given parameter
        for (Object v : node.value){
            if (v == val){
                Value a = (Value) v;
                node.value.remove(v);
                return a;
            }
        }
        //no matching value was found, return null
        return null;
    }
}

