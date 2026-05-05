package com.ashid;

public class CatLinkedList {

  private Node<Cat> head;
  private Node<Cat> tail;

  public CatLinkedList() {
    this.head = null;
    this.tail = null;
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
  }

  public boolean contains(Cat value) {
    Node<Cat> current = head;
    while (current != null) {
      if (current.getValue().equals(value)) return true;
      current = current.getNext();
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
  }

  public int size() {
    int count = 0;
    Node<Cat> current = head;
    while (current != null) {
      count++;
      current = current.getNext();
    }
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
    Node<Cat> current = head;
    while (current != null) {
      Cat c = current.getValue();
      current = current.getNext();
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
    Node<Cat> current = head;
    while (current != null) {
      System.out.println("--- Cat #" + i++ + " ---");
      System.out.println(current.getValue());
      System.out.println();
      current = current.getNext();
    }
  }
}
