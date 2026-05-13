package com.ashid;

import java.util.Comparator;

public class CatComparator implements Comparator<Cat> {

  public enum By {
    NAME("Name"),
    DATE_CAME("Date Came"),
    DATE_BORN("Date Born"),
    WEIGHT("Weight");

    private final String label;

    By(String label) {
      this.label = label;
    }

    @Override
    public String toString() {
      return label;
    }
  }

  private final By by;
  private final boolean descending;

  public CatComparator() {
    this(By.DATE_CAME, false);
  }

  public CatComparator(By by, boolean descending) {
    this.by = by;
    this.descending = descending;
  }

  @Override
  public int compare(Cat c1, Cat c2) {
    int cmp;
    switch (by) {
      case NAME:
        cmp = c1.name.compareToIgnoreCase(c2.name);
        break;
      case DATE_CAME:
        cmp = c1.came.compareTo(c2.came);
        break;
      case DATE_BORN:
        cmp = c1.born.compareTo(c2.born);
        break;
      case WEIGHT:
        cmp = Double.compare(c1.weight, c2.weight);
        break;
      default:
        cmp = 0;
    }
    if (descending) cmp = -cmp;
    if (cmp != 0) return cmp;
    return c1.name.compareToIgnoreCase(c2.name);
  }
}
