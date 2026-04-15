package com.ashid;

import java.util.Comparator;
import java.util.Iterator;

public class MyList<E extends Comparable<E>> implements Iterable<E> {
    private Object[] arr;
    private int length;
    private int index;
    private int modCount;

    public MyList() {
        this.arr = new Object[10];
        this.length = 10;
        this.index = 0;
        this.modCount = 0;
    }

    public void add(E addMe) {
        if (index < length) {
            arr[index] = addMe;
            index++;
        } else {
            Object[] newArr = new Object[arr.length * 2];
            for (int i = 0; i < arr.length; i++) {
                newArr[i] = arr[i];
            }
            newArr[index] = addMe;
            index++;
            length = length * 2;
            arr = newArr;
        }
        this.modCount++;
    }

    public String toString() {
        StringBuilder toReturn = new StringBuilder("[");
        for (int i = 0; i < index - 1; i++) {
            toReturn.append(arr[i]).append(", ");
        }
        if (index > 0) {
            toReturn.append(arr[index - 1]);
        }
        toReturn.append("]");
        return toReturn.toString();
    }

    public boolean contains(E findMe) {
        boolean present = false;
        for (int i = 0; i < index; i++) {
            if (this.arr[i].equals(findMe)) {
                present = true;
                break;
            }
        }
        return present;
    }

    public E get(int i) {
        if (i < 0 || i >= index) {
            throw new IndexOutOfBoundsException();
        }
        return (E) arr[i];
    }

    public E set(int i, E element) {
        if (i < 0 || i > this.arr.length) {
            throw new IndexOutOfBoundsException();
        }
        E prev = (E) this.arr[i];
        this.arr[i] = element;
        return prev;
    }

    @Override
    public Iterator<E> iterator() {
        return new MyListIterator<>(this);
    }

    public E remove(int i) {
        if (i < 0 || i >= this.index) {
            throw new IndexOutOfBoundsException();
        }
        E toReturn = (E) arr[i];
        for (int j = i + 1; j < this.index; j++) {
            arr[j - 1] = arr[j];
        }
        this.index--;
        this.modCount++;
        return toReturn;
    }

    public int getModCount() {
        return this.modCount;
    }

    public void sort() {
        for (int i = 0; i < this.index; i++) {
            for (int j = 0; j < this.index - 1; j++) {
                if (((E) this.arr[j]).compareTo((E) this.arr[j + 1]) > 0) {
                    E temp = (E) this.arr[j];
                    this.arr[j] = this.arr[j + 1];
                    this.arr[j + 1] = temp;
                }
            }
        }
    }

    public void sort(Comparator<E> comparator) {
        for (int i = 0; i < this.index; i++) {
            for (int j = 0; j < this.index - 1; j++) {
                if (comparator.compare(((E) this.arr[j]), (E) this.arr[j + 1]) > 0) {
                    E temp = (E) this.arr[j];
                    this.arr[j] = this.arr[j + 1];
                    this.arr[j + 1] = temp;
                }
            }
        }
    }
}
