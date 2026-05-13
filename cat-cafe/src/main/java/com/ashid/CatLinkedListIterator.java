package com.ashid;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class CatLinkedListIterator implements Iterator<Cat> {

  private final CatLinkedList list;
  private Node<Cat> prev;
  private Node<Cat> lastReturned;
  private Node<Cat> current;
  private int modCount;

  public CatLinkedListIterator(CatLinkedList list) {
    this.list = list;
    this.prev = null;
    this.lastReturned = null;
    this.current = list.getHead();
    this.modCount = list.getModCount();
  }

  @Override
  public boolean hasNext() {
    return current != null;
  }

  @Override
  public Cat next() {
    if (
      modCount != list.getModCount()
    ) throw new ConcurrentModificationException();
    if (current == null) throw new NoSuchElementException();
    if (lastReturned != null) prev = lastReturned;
    lastReturned = current;
    current = current.getNext();
    return lastReturned.getValue();
  }

  @Override
  public void remove() {
    if (lastReturned == null) throw new IllegalStateException();
    if (
      modCount != list.getModCount()
    ) throw new ConcurrentModificationException();
    list.removeNode(prev, lastReturned);
    modCount = list.getModCount();
    lastReturned = null;
  }
}
