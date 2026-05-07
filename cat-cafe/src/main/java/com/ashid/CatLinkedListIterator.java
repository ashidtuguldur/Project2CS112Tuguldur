package com.ashid;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class CatLinkedListIterator implements Iterator<Cat> {

  private final CatLinkedList list;
  private Node<Cat> current;
  private int modCountCheck;

  public CatLinkedListIterator(CatLinkedList list) {
    this.list = list;
    this.current = list.getHead();
    this.modCountCheck = list.getModCount();
  }

  @Override
  public boolean hasNext() {
    return current != null;
  }

  @Override
  public Cat next() {
    if (modCountCheck != list.getModCount()) {
      throw new ConcurrentModificationException();
    }
    if (current == null) throw new NoSuchElementException();
    Cat value = current.getValue();
    current = current.getNext();
    return value;
  }
}
