package com.ashid;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class ListIterator<E extends Comparable<E>> implements Iterator<E> {

    private final List<E> list;
    private E next;
    private int index;
    private int modCountCheck;

    public ListIterator(List<E> list) {
        this.list = list;
        this.index = 0;
        this.modCountCheck = list.getModCount();
        try {
            this.next = list.get(this.index);
        } catch (IndexOutOfBoundsException e) {
            this.next = null;
        }
    }

    @Override
    public boolean hasNext() {
        return this.next != null;
    }

    @Override
    public E next() {
        if (this.modCountCheck != this.list.getModCount()) {
            throw new ConcurrentModificationException();
        }
        E element = this.next;
        this.index++;
        try {
            this.next = list.get(this.index);
        } catch (IndexOutOfBoundsException e) {
            this.next = null;
        }
        return element;
    }

    public void remove() {
        this.list.remove(this.index - 1);
        this.modCountCheck++;
        this.index--;
    }
}
