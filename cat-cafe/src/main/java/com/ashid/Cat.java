package com.ashid;

import java.util.Objects;

public final class Cat implements Comparable<Cat> {

  public final Date born;
  public final Date came;
  public Date adopted;
  public final Pattern pattern;
  public Double weight;
  public final String name;

  public Cat(
    Date born,
    Date came,
    Pattern pattern,
    Double weight,
    String name
  ) {
    this.born = born;
    this.came = came;
    this.adopted = null;
    this.pattern = pattern;
    this.weight = weight;
    this.name = name;
  }

  @Override
  public int compareTo(Cat other) {
    int cmp = this.name.compareToIgnoreCase(other.name);
    if (cmp != 0) return cmp;
    cmp = this.born.compareTo(other.born);
    if (cmp != 0) return cmp;
    cmp = this.came.compareTo(other.came);
    if (cmp != 0) return cmp;
    cmp = Integer.compare(this.pattern.ordinal(), other.pattern.ordinal());
    if (cmp != 0) return cmp;
    return this.weight.compareTo(other.weight);
  }

  @Override
  public String toString() {
    return (
      "Name:    " +
      name +
      "\n" +
      "Born:    " +
      born +
      "\n" +
      "Came:    " +
      came +
      "\n" +
      "Pattern: " +
      pattern +
      "\n" +
      "Weight:  " +
      weight +
      " oz\n" +
      "Adopted: " +
      (adopted != null ? adopted : "Not yet adopted")
    );
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Cat)) return false;
    Cat other = (Cat) obj;
    return (
      this.name.equals(other.name) &&
      this.born.equals(other.born) &&
      this.came.equals(other.came) &&
      Objects.equals(this.weight, other.weight) &&
      this.pattern == other.pattern
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, born, came, weight, pattern);
  }

  public enum Pattern {
    SOLID,
    TABBY,
    TORTOISESHELL,
    CALICO,
    BICOLOR,
    COLORPOINT,
    TICKED,
    SPOTTED,
  }
}
