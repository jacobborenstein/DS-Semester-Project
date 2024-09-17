//                                                                                       בס"ד
package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.MinHeap;

import java.util.NoSuchElementException;

public class MinHeapImpl <E extends Comparable<E>> extends MinHeap<E> {

    public MinHeapImpl() {
         this.elements = (E[]) new Comparable[5];
         this.count = 0;
    }

    @Override
    public void reHeapify(E element) {
        int index = getArrayIndex(element);
        //if new value is greater than its parent's value, down-heap
        if (this.isGreater(index,index/2)) {
            this.downHeap(index);
        } else{
            //if new value is less than parent's value, up heap
            this.upHeap(index);
        }
    }
    @Override
    protected int getArrayIndex(E element) throws NoSuchElementException{
        int i = 1;
        while (this.elements[i] != null){
            if (this.elements[i].equals(element)){

                return i;
            }
            i++;
        }
        throw new NoSuchElementException();
    }

    @Override
    protected void doubleArraySize() {
        E[] copy = (E[]) new Comparable[this.elements.length*2];
        for (int i = 0; i < this.elements.length; i++){
            copy[i] = this.elements[i];
        }
        this.elements = copy;
    }
}
