package com.ashid;

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

    public static final class Date {

        private int date;
        private int year;
        private Month month;

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

        @Override
        public String toString() {
            return month + " " + date + ", " + year;
        }

        private enum Month {
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
