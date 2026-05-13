package com.ashid;

public final class Date implements Comparable<Date> {

  private final int date;
  private final int year;
  private final Month month;

  public Date(int date, int year, Month month) {
    int maxDays = month.getMaxDays(year);
    if (date < 1 || date > maxDays) {
      throw new IllegalArgumentException(
        "Invalid day " +
          date +
          " for " +
          month +
          " " +
          year +
          ". Must be between 1 and " +
          maxDays +
          "."
      );
    }
    this.date = date;
    this.month = month;
    this.year = year;
  }

  public int getDate() {
    return date;
  }

  public int getYear() {
    return year;
  }

  public Month getMonth() {
    return month;
  }

  @Override
  public String toString() {
    return month + " " + date + ", " + year;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Date)) {
      return false;
    }
    Date other = (Date) obj;
    return (
      this.year == other.year &&
      this.month == other.month &&
      this.date == other.date
    );
  }

  @Override
  public int compareTo(Date other) {
    if (this.year != other.year) {
      return Integer.compare(this.year, other.year);
    }
    if (this.month != other.month) {
      return Integer.compare(this.month.ordinal(), other.month.ordinal());
    }
    return Integer.compare(this.date, other.date);
  }

  @Override
  public int hashCode() {
    int result = year;
    result = 31 * result + month.ordinal();
    result = 31 * result + date;
    return result;
  }
}
