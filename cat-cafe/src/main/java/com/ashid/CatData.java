package com.ashid;

public class CatData {

  public static List<Cat> load() {
    List<Cat> ls = new List<>();

    Cat luna = new Cat(
      new Date(3, 2019, Month.APRIL),
      new Date(15, 2022, Month.SEPTEMBER),
      Cat.Pattern.CALICO, 92.0, "Luna"
    );

    Cat mochi = new Cat(
      new Date(11, 2020, Month.JULY),
      new Date(2, 2023, Month.FEBRUARY),
      Cat.Pattern.TABBY, 108.5, "Mochi"
    );
    mochi.adopted = new Date(20, 2023, Month.NOVEMBER);

    Cat shadow = new Cat(
      new Date(22, 2018, Month.JANUARY),
      new Date(7, 2021, Month.JUNE),
      Cat.Pattern.SOLID, 135.0, "Shadow"
    );

    Cat biscuit = new Cat(
      new Date(5, 2021, Month.MARCH),
      new Date(19, 2022, Month.OCTOBER),
      Cat.Pattern.BICOLOR, 97.3, "Biscuit"
    );
    biscuit.adopted = new Date(14, 2024, Month.JANUARY);

    Cat pepper = new Cat(
      new Date(17, 2017, Month.OCTOBER),
      new Date(30, 2020, Month.AUGUST),
      Cat.Pattern.TORTOISESHELL, 88.0, "Pepper"
    );

    Cat nimbus = new Cat(
      new Date(29, 2022, Month.AUGUST),
      new Date(11, 2023, Month.MAY),
      Cat.Pattern.COLORPOINT, 76.5, "Nimbus"
    );

    Cat cheddar = new Cat(
      new Date(14, 2016, Month.DECEMBER),
      new Date(3, 2019, Month.MARCH),
      Cat.Pattern.TABBY, 148.2, "Cheddar"
    );
    cheddar.adopted = new Date(9, 2022, Month.JUNE);

    Cat maple = new Cat(
      new Date(8, 2023, Month.FEBRUARY),
      new Date(25, 2023, Month.JULY),
      Cat.Pattern.TICKED, 64.0, "Maple"
    );

    Cat ghost = new Cat(
      new Date(30, 2015, Month.JUNE),
      new Date(18, 2018, Month.NOVEMBER),
      Cat.Pattern.COLORPOINT, 120.7, "Ghost"
    );

    Cat sable = new Cat(
      new Date(1, 2021, Month.NOVEMBER),
      new Date(14, 2022, Month.APRIL),
      Cat.Pattern.SOLID, 111.0, "Sable"
    );
    sable.adopted = new Date(3, 2023, Month.AUGUST);

    Cat freckle = new Cat(
      new Date(19, 2020, Month.SEPTEMBER),
      new Date(7, 2021, Month.DECEMBER),
      Cat.Pattern.SPOTTED, 85.6, "Freckle"
    );

    Cat pebble = new Cat(
      new Date(27, 2022, Month.MAY),
      new Date(16, 2023, Month.JANUARY),
      Cat.Pattern.BICOLOR, 73.9, "Pebble"
    );

    Cat hazel = new Cat(
      new Date(12, 2019, Month.AUGUST),
      new Date(4, 2021, Month.FEBRUARY),
      Cat.Pattern.CALICO, 99.1, "Hazel"
    );

    Cat toast = new Cat(
      new Date(6, 2023, Month.OCTOBER),
      new Date(28, 2024, Month.MARCH),
      Cat.Pattern.TABBY, 58.4, "Toast"
    );

    Cat cosmos = new Cat(
      new Date(15, 2018, Month.APRIL),
      new Date(22, 2020, Month.JULY),
      Cat.Pattern.SPOTTED, 103.3, "Cosmos"
    );
    cosmos.adopted = new Date(11, 2021, Month.APRIL);

    ls.add(luna);
    ls.add(mochi);
    ls.add(shadow);
    ls.add(biscuit);
    ls.add(pepper);
    ls.add(nimbus);
    ls.add(cheddar);
    ls.add(maple);
    ls.add(ghost);
    ls.add(sable);
    ls.add(freckle);
    ls.add(pebble);
    ls.add(hazel);
    ls.add(toast);
    ls.add(cosmos);

    return ls;
  }
}
