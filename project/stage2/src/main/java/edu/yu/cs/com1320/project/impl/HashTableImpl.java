package edu.yu.cs.com1320.project.impl;


import edu.yu.cs.com1320.project.HashTable;

import java.lang.instrument.UnmodifiableClassException;
import java.util.*;

/**
 * Instances of HashTable should be constructed with two type parameters, one for the type of the keys in the table and one for the type of the values
 *
 * @param "<Key>"
 * @param <"Value">
 */

public class HashTableImpl<Key, Value> implements HashTable<Key, Value> {
   //inner class
    private class linkedList<E> {
        node head;
    }
   private class node {
       Key key;
        Value data;
       node next;
       node(Key k, Value v) {
           this.data = v;
           this.key = k;

       }
   }
    public linkedList[] array = new linkedList[5];
    @SuppressWarnings("unchecked")
    private linkedList add (linkedList list , Key k, Value data) {
       node newNode = new node(k,data);
        if (list.head == null) {
          list.head = newNode;
          return list;
       }
       node last = list.head;
       while (last.next != null) {
           last = last.next;
       }
       last.next = newNode;
       return list;
   }
   public HashTableImpl(){
   }
   private linkedList delete (linkedList list, Key k) {
       if (list == null || list.head == null) {
           return list;
       }
       if (list.head.key.equals(k)) { // if key is in first slot of  linked list
           list.head = list.head.next;
           return list;
           }
           node last = list.head;
           node current = last.next;
           while (current != null && current.next != null && !current.next.key.equals(k)) {
               last = current;
               current = current.next;
           }

           if(current == null){
               return list;

           }
       last.next = current.next;
           return list;
       }

    /**
     * @param k the key whose value should be returned
     * @return the value that is stored in the HashTable for k, or null if there is no such key in the table
     */

    @Override
    public Value get(Key k) {
        int index = hashFunction(k);
        if (this.array[index] == null||this.array[index].head == null){
            return null;
        }
        node last = this.array[index].head;
        if (last.key.equals(k)){
            return last.data;
        }
        if (last.next == null){
            return null;
        }
        while (last.next != null){
            if (last.key.equals(k)){
                return last.data;
            }
            last = last.next;
        }
        return last.data;
    }
    /**
     * @param k the key at which to store the value
     * @param v the value to store
     *          To delete an entry, put a null value.
     * @return if the key was already present in the HashTable, return the previous value stored for the key. If the key was not already present, return null.
     */



    @Override
    public Value put(Key k, Value v) {
        int index = hashFunction(k);
        if (v==null){
            Value old = get(k);
            this.array[index] = delete(this.array[index],k);
            return old;
        }
        if(this.array[index] == null){
                this.array[index] = new linkedList();
                this.array[index] = add(this.array[index], k, v);
                return null;
        }
        node last = this.array[index].head;
        Value og = last.data;
            if(last.key.equals(k)){
                this.array[index].head.data = v;
                return og;
            }
            while (last.next != null) {
                if (last.key.equals(k)) {
                    Value old = last.data;
                    last.data = v;
                    return old;
                }
                last = last.next;
            }
            if (last == null || last.next == null) { // not found
                        //cud be just add(this.array[index],v);
//                this.array[index] = add(this.array[index], k, v);
                last.next = new node(k,v);
                return null;
                }
            return last.next.data;
    }
    /**
     * @param "key the key whose presence in the hashtabe we are inquiring about
     * @return true if the given key is present in the hashtable as a key, false if not
     * @throws NullPointerException if the specified key is null
     */
    @Override
    public boolean containsKey(Key k) {
        if (k == null){
            throw new IllegalArgumentException();
        }
        if (get(k) == null){
            return false;
        }
        return true;
    }

    /**
     * @return an unmodifiable set of all the keys in this HashTable
     * @see java.util.Collections#unmodifiableSet(Set)
     */
    @Override
    public Set<Key> keySet() {
       HashSet<Key> set = new HashSet<>();
        for (int i=0; i < this.array.length; i++){
            if (this.array[i] != null && this.array[i].head != null) {
                node last = this.array[i].head;
                set.add(last.key);
                while (last.next != null && last.next.key != null){
                    set.add(last.next.key);
                    last = last.next;
                }
            }
        }
        return Collections.unmodifiableSet(set);
    }
    /**
     * @return an unmodifiable collection of all the values in this HashTable
     * @see java.util.Collections#unmodifiableCollection(Collection)
     */
    @Override
    public Collection<Value> values() {
        List<Value> list = new ArrayList<>();
        for (int i=0; i < this.array.length; i++){
            if (this.array[i] != null && this.array[i].head != null){
                node last = this.array[i].head;
                list.add(last.data);
                while (last.next != null && last.next.data != null){
                    list.add(last.next.data);
                    last = last.next;
                }
            }
        }
        return Collections.unmodifiableCollection(list);
    }
    /**
     * @return how entries there currently are in the HashTable
     */
    @Override
    public int size() {
        int size = 0;
        for (int i=0; i < this.array.length; i++){
            if (this.array[i] != null && this.array[i].head != null){
                size++;
                node last = this.array[i].head;
                while (last.next != null){
                   size++;
                   last = last.next;
                }
            }
        }
        return size;
    }
    private int hashFunction(Key k){
        return (k.hashCode() & 0x7fffffff) % this.array.length;
    }
}
