package com.ashid;

import java.util.Comparator;
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
    if (newNode.getNext() == null) {
      tail = newNode;
    }
    modCount++;
  }

  public boolean contains(Cat value) {
    for (Cat cat : this) {
      if (cat.equals(value)) {
        return true;
      }
    }
    return false;
  }

  @SuppressWarnings("unused")
  public int size() {
    int count = 0;
    for (Cat cat : this) {
      count++;
    }
    return count;
  }

  public void enter(Cat newCat) {
    if (!contains(newCat)) {
      add(newCat);
    }
  }

  public Cat delete(String name) {
    Iterator<Cat> it = iterator();
    while (it.hasNext()) {
      Cat c = it.next();
      if (c.name.equalsIgnoreCase(name)) {
        it.remove();
        return c;
      }
    }
    return null;
  }

  public Cat delete(Cat cat) {
    Iterator<Cat> it = iterator();
    while (it.hasNext()) {
      Cat c = it.next();
      if (c.equals(cat)) {
        it.remove();
        return c;
      }
    }
    return null;
  }

  void removeNode(Node<Cat> prev, Node<Cat> node) {
    if (prev == null) {
      head = node.getNext();
    } else {
      prev.setNext(node.getNext());
    }
    if (node == tail) {
      tail = prev;
    }
    modCount++;
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
      if (pattern != null && c.pattern != pattern) {
        continue;
      }
      if (minW != null && c.weight < minW) {
        continue;
      }
      if (maxW != null && c.weight > maxW) {
        continue;
      }
      if (dateQuery != null) {
        String q = dateQuery.toLowerCase();
        boolean matchesBorn = c.born.toString().toLowerCase().contains(q);
        boolean matchesCame = c.came.toString().toLowerCase().contains(q);
        boolean matchesAdopted =
          c.adopted != null && c.adopted.toString().toLowerCase().contains(q);
        if (!matchesBorn && !matchesCame && !matchesAdopted) {
          continue;
        }
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
      System.out.println("Cat #" + i++);
      System.out.println(cat);
      System.out.println();
    }
  }

  public void sort() {
    sort((a, b) -> a.compareTo(b));
  }

  public void sort(Comparator<Cat> comparator) {
    if (head == null || head.getNext() == null) {
      return;
    }
    head = mergeSort(head, comparator);
    Node<Cat> t = head;
    while (t.getNext() != null) {
      t = t.getNext();
    }
    tail = t;
    modCount++;
  }

  private Node<Cat> mergeSort(Node<Cat> node, Comparator<Cat> comparator) {
    if (node == null || node.getNext() == null) {
      return node;
    }
    Node<Cat> mid = getMid(node);
    Node<Cat> second = mid.getNext();
    mid.setNext(null);
    Node<Cat> left = mergeSort(node, comparator);
    Node<Cat> right = mergeSort(second, comparator);
    return merge(left, right, comparator);
  }

  private Node<Cat> getMid(Node<Cat> node) {
    Node<Cat> slow = node;
    Node<Cat> fast = node.getNext();
    while (fast != null && fast.getNext() != null) {
      slow = slow.getNext();
      fast = fast.getNext().getNext();
    }
    return slow;
  }

  private Node<Cat> merge(
    Node<Cat> a,
    Node<Cat> b,
    Comparator<Cat> comparator
  ) {
    Node<Cat> dummy = new Node<>(null);
    Node<Cat> cur = dummy;
    while (a != null && b != null) {
      if (comparator.compare(a.getValue(), b.getValue()) <= 0) {
        cur.setNext(a);
        a = a.getNext();
      } else {
        cur.setNext(b);
        b = b.getNext();
      }
      cur = cur.getNext();
    }
    cur.setNext(a != null ? a : b);
    return dummy.getNext();
  }

  public int getModCount() {
    return modCount;
  }

  @Override
  public Iterator<Cat> iterator() {
    return new CatLinkedListIterator(this);
  }
}
