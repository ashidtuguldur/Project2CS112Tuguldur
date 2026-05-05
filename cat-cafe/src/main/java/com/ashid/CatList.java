package com.ashid;

import java.util.Iterator;

public class CatList {

  List<Cat> cats;

  public CatList(List<Cat> cats) {
    this.cats = cats;
  }

  public void enter(Cat newCat) {
    //check for duplicates there should be no duplicates.
    if (!cats.contains(newCat)) {
      cats.add(newCat);
    }
  }

  public Cat delete(String name) {
    //use the best practice
    //use iterator
    Iterator<Cat> it = cats.iterator();
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
    //use the best practice
    //use iterator
    Iterator<Cat> it = cats.iterator();
    while (it.hasNext()) {
      Cat c = it.next();
      if (c.equals(cat)) {
        it.remove();
        return c;
      }
    }
    return null;
  }

  public List<Cat> search(
    String name,
    Cat.Pattern pattern,
    Double minW,
    Double maxW,
    String dateQuery
  ) {
    List<Cat> results = new List<>();
    for (Cat c : cats) {
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
    if (cats.iterator().hasNext() == false) {
      System.out.println("(no cats in list)");
      return;
    }
    int i = 1;
    for (Cat c : cats) {
      System.out.println("--- Cat #" + i++ + " ---");
      System.out.println(c);
      System.out.println();
    }
  }
}
