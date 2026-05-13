package com.ashid;

public enum Month {
  JANUARY(31),
  FEBRUARY(28),
  MARCH(31),
  APRIL(30),
  MAY(31),
  JUNE(30),
  JULY(31),
  AUGUST(31),
  SEPTEMBER(30),
  OCTOBER(31),
  NOVEMBER(30),
  DECEMBER(31);

  private final int maxDays;

  Month(int maxDays) {
    this.maxDays = maxDays;
  }

  public int getMaxDays(int year) {
    if (this == FEBRUARY && isLeapYear(year)) {
      return 29;
    }
    return maxDays;
  }

  private boolean isLeapYear(int year) {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
  }
}
