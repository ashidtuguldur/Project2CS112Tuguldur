package com.ashid;

public class App {

  public static void main(String[] args) {
    // Two identical cats — enter() should prevent the duplicate
    Cat a = new Cat(
      new Date(26, 2007, Month.JUNE),
      new Date(13, 2025, Month.AUGUST),
      Cat.Pattern.SOLID,
      130.0,
      "Ashid"
    );

    Cat b = new Cat(
      new Date(26, 2007, Month.JUNE),
      new Date(13, 2025, Month.AUGUST),
      Cat.Pattern.SOLID,
      130.0,
      "Ashid"
    );

    Cat c = new Cat(
      new Date(1, 2020, Month.MARCH),
      new Date(5, 2025, Month.JANUARY),
      Cat.Pattern.TABBY,
      95.5,
      "Mochi"
    );

    List<Cat> list = new List<>();
    list.add(a);

    CatList catList = new CatList(list);
    catList.enter(b); // duplicate — should be ignored
    catList.enter(c); // new cat — should be added

    System.out.println("=== All cats ===");
    catList.display();

    System.out.println("=== Search: TABBY pattern ===");
    List<Cat> found = catList.search(
      null,
      Cat.Pattern.TABBY,
      null,
      null,
      null,
      null
    );
    for (Cat cat : found) {
      System.out.println(cat);
      System.out.println();
    }

    System.out.println("=== Delete 'Mochi' ===");
    Cat removed = catList.delete("Mochi");
    System.out.println(
      "Removed: " + (removed != null ? removed.name : "not found")
    );
    System.out.println();

    System.out.println("=== After deletion ===");
    catList.display();

    System.out.println("=== Sort by arrival (CatComparator) ===");
    catList.cats.sort(new CatComparator());
    catList.display();
  }
}
