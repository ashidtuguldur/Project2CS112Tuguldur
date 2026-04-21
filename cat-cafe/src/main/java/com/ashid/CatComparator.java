package com.ashid;

import java.util.Comparator;

public class CatComparator implements Comparator<Cat> {

  @Override
  public int compare(Cat c1, Cat c2) {
    int cmp = c1.came.compareTo(c2.came);
    if (cmp != 0) return cmp;
    cmp = c1.born.compareTo(c2.born);
    if (cmp != 0) return cmp;
    return c1.name.compareToIgnoreCase(c2.name);
  }
}
