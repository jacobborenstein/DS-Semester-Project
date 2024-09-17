//                                                                                          בס"ד
package edu.yu.cs.com1320.project.impl;


import edu.yu.cs.com1320.project.Stack;

public class StackImpl<T> implements Stack<T> {

    private class linkedList<E> {
        node head;
    }
    private class node {
        T data;
        node next;
        node(T v) {
            this.data = v;
        }
    }
    private linkedList<T> stack = new linkedList<T>();

    /**
     * @param element object to add to the Stack
     */
    @Override
    public void push(T element) {
        //Stack is empty, add 1st elemnt;
        if (this.stack.head == null) {
            this.stack.head = new node(element);
            return;
        }
        node pushed = new node(element);
        pushed.next =this.stack.head;
        this.stack.head = pushed;
//        //Stack is not empty
//        node current = this.stack.head;
//        while (current.next != null) {  //find the next open spot to store the elemnt
//            current = current.next;
//        }
//        current.next = new node(element);
    }
    /**
     * removes and returns element at the top of the stack
     * @return element at the top of the stack, null if the stack is empty
     */
    @Override
    public T pop() {
        if (this.stack.head == null){
            return null;
        }
        T top = this.stack.head.data;
        this.stack.head = this.stack.head.next;
        return top;
    }
    /**
     *
     * @return the element at the top of the stack without removing it
     */
    @Override
    public T peek() {
        if (this.stack.head == null){
            return null;
        }
        return this.stack.head.data;
    }
    /**
     *
     * @return how many elements are currently in the stack
     */
    @Override
    public int size() {
        int size = 0;
//        if (this.stack.head == null){
//            return size;
//        }
        node current = this.stack.head;
        while (current != null){
            size ++;
            current = current.next;
        }
        return size;
    }
}
