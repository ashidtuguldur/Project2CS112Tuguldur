package com.ashid;

import java.sql.*;

public class CatData {

  private static final String DB_URL = "jdbc:sqlite:src/main/resources/cats.db";

  public static List<Cat> load() {
    try (Connection conn = DriverManager.getConnection(DB_URL)) {
      initDB(conn);
      List<Cat> ls = new List<>();
      try (
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM cats")
      ) {
        while (rs.next()) {
          Date born = new Date(
            rs.getInt("born_day"),
            rs.getInt("born_year"),
            Month.valueOf(rs.getString("born_month"))
          );
          Date came = new Date(
            rs.getInt("came_day"),
            rs.getInt("came_year"),
            Month.valueOf(rs.getString("came_month"))
          );
          Cat cat = new Cat(
            born,
            came,
            Cat.Pattern.valueOf(rs.getString("pattern")),
            rs.getDouble("weight"),
            rs.getString("name")
          );
          String adoptedMonth = rs.getString("adopted_month");
          if (adoptedMonth != null) {
            cat.adopted = new Date(
              rs.getInt("adopted_day"),
              rs.getInt("adopted_year"),
              Month.valueOf(adoptedMonth)
            );
          }
          ls.add(cat);
        }
      }
      return ls;
    } catch (SQLException e) {
      throw new RuntimeException("Failed to load cats from DB", e);
    }
  }

  private static void initDB(Connection conn) throws SQLException {
    try (Statement stmt = conn.createStatement()) {
      stmt.execute(
        """
            CREATE TABLE IF NOT EXISTS cats (
                name TEXT PRIMARY KEY,
                born_day INTEGER NOT NULL,
                born_month TEXT NOT NULL,
                born_year INTEGER NOT NULL,
                came_day INTEGER NOT NULL,
                came_month TEXT NOT NULL,
                came_year INTEGER NOT NULL,
                pattern TEXT NOT NULL,
                weight REAL NOT NULL,
                adopted_day INTEGER,
                adopted_month TEXT,
                adopted_year INTEGER
            )
        """
      );
      try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM cats")) {
        if (rs.next() && rs.getInt(1) > 0) return;
      }
    }
    insertAll(conn);
  }

  private static void insertAll(Connection conn) throws SQLException {
    insert(
      conn,
      "Luna",
      3,
      Month.APRIL,
      2019,
      15,
      Month.SEPTEMBER,
      2022,
      Cat.Pattern.CALICO,
      92.0,
      null
    );
    insert(
      conn,
      "Mochi",
      11,
      Month.JULY,
      2020,
      2,
      Month.FEBRUARY,
      2023,
      Cat.Pattern.TABBY,
      108.5,
      new Date(20, 2023, Month.NOVEMBER)
    );
    insert(
      conn,
      "Shadow",
      22,
      Month.JANUARY,
      2018,
      7,
      Month.JUNE,
      2021,
      Cat.Pattern.SOLID,
      135.0,
      null
    );
    insert(
      conn,
      "Biscuit",
      5,
      Month.MARCH,
      2021,
      19,
      Month.OCTOBER,
      2022,
      Cat.Pattern.BICOLOR,
      97.3,
      new Date(14, 2024, Month.JANUARY)
    );
    insert(
      conn,
      "Pepper",
      17,
      Month.OCTOBER,
      2017,
      30,
      Month.AUGUST,
      2020,
      Cat.Pattern.TORTOISESHELL,
      88.0,
      null
    );
    insert(
      conn,
      "Nimbus",
      29,
      Month.AUGUST,
      2022,
      11,
      Month.MAY,
      2023,
      Cat.Pattern.COLORPOINT,
      76.5,
      null
    );
    insert(
      conn,
      "Cheddar",
      14,
      Month.DECEMBER,
      2016,
      3,
      Month.MARCH,
      2019,
      Cat.Pattern.TABBY,
      148.2,
      new Date(9, 2022, Month.JUNE)
    );
    insert(
      conn,
      "Maple",
      8,
      Month.FEBRUARY,
      2023,
      25,
      Month.JULY,
      2023,
      Cat.Pattern.TICKED,
      64.0,
      null
    );
    insert(
      conn,
      "Ghost",
      30,
      Month.JUNE,
      2015,
      18,
      Month.NOVEMBER,
      2018,
      Cat.Pattern.COLORPOINT,
      120.7,
      null
    );
    insert(
      conn,
      "Sable",
      1,
      Month.NOVEMBER,
      2021,
      14,
      Month.APRIL,
      2022,
      Cat.Pattern.SOLID,
      111.0,
      new Date(3, 2023, Month.AUGUST)
    );
    insert(
      conn,
      "Freckle",
      19,
      Month.SEPTEMBER,
      2020,
      7,
      Month.DECEMBER,
      2021,
      Cat.Pattern.SPOTTED,
      85.6,
      null
    );
    insert(
      conn,
      "Pebble",
      27,
      Month.MAY,
      2022,
      16,
      Month.JANUARY,
      2023,
      Cat.Pattern.BICOLOR,
      73.9,
      null
    );
    insert(
      conn,
      "Hazel",
      12,
      Month.AUGUST,
      2019,
      4,
      Month.FEBRUARY,
      2021,
      Cat.Pattern.CALICO,
      99.1,
      null
    );
    insert(
      conn,
      "Toast",
      6,
      Month.OCTOBER,
      2023,
      28,
      Month.MARCH,
      2024,
      Cat.Pattern.TABBY,
      58.4,
      null
    );
    insert(
      conn,
      "Cosmos",
      15,
      Month.APRIL,
      2018,
      22,
      Month.JULY,
      2020,
      Cat.Pattern.SPOTTED,
      103.3,
      new Date(11, 2021, Month.APRIL)
    );
  }

  public static void save(Cat cat) {
    try (Connection conn = DriverManager.getConnection(DB_URL)) {
      insert(
        conn,
        cat.name,
        cat.born.getDate(),
        cat.born.getMonth(),
        cat.born.getYear(),
        cat.came.getDate(),
        cat.came.getMonth(),
        cat.came.getYear(),
        cat.pattern,
        cat.weight,
        cat.adopted
      );
    } catch (SQLException e) {
      throw new RuntimeException("Failed to save cat", e);
    }
  }

  public static void updateAdopted(Cat cat) {
    try (Connection conn = DriverManager.getConnection(DB_URL)) {
      String sql = "UPDATE cats SET adopted_day=?, adopted_month=?, adopted_year=? WHERE name=?";
      try (PreparedStatement ps = conn.prepareStatement(sql)) {
        if (cat.adopted != null) {
          ps.setInt(1, cat.adopted.getDate());
          ps.setString(2, cat.adopted.getMonth().name());
          ps.setInt(3, cat.adopted.getYear());
        } else {
          ps.setNull(1, Types.INTEGER);
          ps.setNull(2, Types.VARCHAR);
          ps.setNull(3, Types.INTEGER);
        }
        ps.setString(4, cat.name);
        ps.executeUpdate();
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to update adopted date", e);
    }
  }

  private static void insert(
    Connection conn,
    String name,
    int bornDay,
    Month bornMonth,
    int bornYear,
    int cameDay,
    Month cameMonth,
    int cameYear,
    Cat.Pattern pattern,
    double weight,
    Date adopted
  ) throws SQLException {
    String sql = """
          INSERT OR IGNORE INTO cats
            (name, born_day, born_month, born_year, came_day, came_month, came_year, pattern, weight, adopted_day, adopted_month, adopted_year)
          VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
      """;
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, name);
      ps.setInt(2, bornDay);
      ps.setString(3, bornMonth.name());
      ps.setInt(4, bornYear);
      ps.setInt(5, cameDay);
      ps.setString(6, cameMonth.name());
      ps.setInt(7, cameYear);
      ps.setString(8, pattern.name());
      ps.setDouble(9, weight);
      if (adopted != null) {
        ps.setInt(10, adopted.getDate());
        ps.setString(11, adopted.getMonth().name());
        ps.setInt(12, adopted.getYear());
      } else {
        ps.setNull(10, Types.INTEGER);
        ps.setNull(11, Types.VARCHAR);
        ps.setNull(12, Types.INTEGER);
      }
      ps.executeUpdate();
    }
  }
}
