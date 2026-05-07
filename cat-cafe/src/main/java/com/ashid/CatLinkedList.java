package com.ashid;

import java.util.Iterator;

public class CatLinkedList implements Iterable<Cat> {

  private Node<Cat> head;
  private Node<Cat> tail;
  private int modCount = 0;

  public CatLinkedList() {
    this.head = null;
    this.tail = null;
  }

  public Cat get(int index) {
    if (index < 0) throw new IndexOutOfBoundsException(
      "Index " + index + " is out of bounds"
    );
    Node<Cat> current = head;
    for (int i = 0; i < index; i++) {
      if (current == null) throw new IndexOutOfBoundsException(
        "Index " + index + " is out of bounds"
      );
      current = current.getNext();
    }
    if (current == null) throw new IndexOutOfBoundsException(
      "Index " + index + " is out of bounds"
    );
    return current.getValue();
  }

  public Node<Cat> getHead() {
    return head;
  }

  public Node<Cat> getTail() {
    return tail;
  }

  public void add(Cat value) {
    Node<Cat> newNode = new Node<>(value);
    if (head == null) {
      head = newNode;
      tail = newNode;
    } else {
      tail.setNext(newNode);
      tail = newNode;
    }
    modCount++;
  }

  public void insert(Cat value, int index) {
    if (index < 0) throw new IllegalArgumentException(
      "Index must be non-negative"
    );
    Node<Cat> newNode = new Node<>(value);
    if (index == 0) {
      newNode.setNext(head);
      head = newNode;
      if (tail == null) tail = newNode;
      modCount++;
      return;
    }
    Node<Cat> current = head;
    for (int i = 0; i < index - 1; i++) {
      if (current == null) throw new IndexOutOfBoundsException(
        "Index " + index + " is out of bounds"
      );
      current = current.getNext();
    }
    newNode.setNext(current.getNext());
    current.setNext(newNode);
    if (newNode.getNext() == null) tail = newNode;
    modCount++;
  }

  public boolean contains(Cat value) {
    for (Cat cat : this) {
      if (cat.equals(value)) return true;
    }
    return false;
  }

  public void remove(Cat value) {
    Node<Cat> current = head;
    Node<Cat> previous = null;
    while (current != null) {
      if (current.getValue().equals(value)) {
        if (previous == null) head = current.getNext();
        else previous.setNext(current.getNext());
        if (current == tail) tail = previous;
        modCount++;
        return;
      }
      previous = current;
      current = current.getNext();
    }
  }

  public void remove(int index) {
    if (index < 0) throw new IllegalArgumentException(
      "Index must be non-negative"
    );
    Node<Cat> current = head;
    Node<Cat> previous = null;
    for (int i = 0; i < index; i++) {
      if (current == null) throw new IndexOutOfBoundsException(
        "Index " + index + " is out of bounds"
      );
      previous = current;
      current = current.getNext();
    }
    if (previous == null) head = current.getNext();
    else previous.setNext(current.getNext());
    if (current == tail) tail = previous;
    modCount++;
  }

  @SuppressWarnings("unused")
  public int size() {
    int count = 0;
    for (Cat cat : this) count++;
    return count;
  }

  public void enter(Cat newCat) {
    if (!contains(newCat)) {
      add(newCat);
    }
  }

  public Cat delete(String name) {
    Node<Cat> current = head;
    Node<Cat> previous = null;
    while (current != null) {
      Cat c = current.getValue();
      if (c.name.equalsIgnoreCase(name)) {
        if (previous == null) head = current.getNext();
        else previous.setNext(current.getNext());
        if (current.getNext() == null) tail = previous;
        modCount++;
        return c;
      }
      previous = current;
      current = current.getNext();
    }
    return null;
  }

  public Cat delete(Cat cat) {
    Node<Cat> current = head;
    Node<Cat> previous = null;
    while (current != null) {
      Cat c = current.getValue();
      if (c.equals(cat)) {
        if (previous == null) head = current.getNext();
        else previous.setNext(current.getNext());
        if (current.getNext() == null) tail = previous;
        modCount++;
        return c;
      }
      previous = current;
      current = current.getNext();
    }
    return null;
  }

  public CatLinkedList search(
    String name,
    Cat.Pattern pattern,
    Double minW,
    Double maxW,
    String dateQuery
  ) {
    CatLinkedList results = new CatLinkedList();
    for (Cat c : this) {
      if (
        name != null && !c.name.toLowerCase().contains(name.toLowerCase())
      ) continue;
      if (pattern != null && c.pattern != pattern) continue;
      if (minW != null && c.weight < minW) continue;
      if (maxW != null && c.weight > maxW) continue;
      if (dateQuery != null) {
        String q = dateQuery.toLowerCase();
        boolean matchesBorn = c.born.toString().toLowerCase().contains(q);
        boolean matchesCame = c.came.toString().toLowerCase().contains(q);
        boolean matchesAdopted =
          c.adopted != null && c.adopted.toString().toLowerCase().contains(q);
        if (!matchesBorn && !matchesCame && !matchesAdopted) continue;
      }
      results.add(c);
    }
    return results;
  }

  public void display() {
    if (head == null) {
      System.out.println("(no cats in list)");
      return;
    }
    int i = 1;
    for (Cat cat : this) {
      System.out.println("--- Cat #" + i++ + " ---");
      System.out.println(cat);
      System.out.println();
    }
  }

  public void sort() {
    if (head == null || head.getNext() == null) return;
    CatLinkedList sorted = new CatLinkedList();
    for (Cat cat : this) {
      Node<Cat> cur = sorted.head;
      Node<Cat> prev = null;
      while (cur != null && cur.getValue().compareTo(cat) <= 0) {
        prev = cur;
        cur = cur.getNext();
      }
      Node<Cat> newNode = new Node<>(cat);
      newNode.setNext(cur);
      if (prev == null) sorted.head = newNode;
      else prev.setNext(newNode);
      if (cur == null) sorted.tail = newNode;
    }
    this.head = sorted.head;
    this.tail = sorted.tail;
    modCount++;
  }

  public int getModCount() {
    return modCount;
  }

  @Override
  public Iterator<Cat> iterator() {
    return new CatLinkedListIterator(this);
  }
}
