package com.ashid;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.sql.*;

public class CatData {

  private static final String DB_DIR = findProjectDir();
  private static final String PHOTOS_DIR =
    DB_DIR + "/src/main/resources/photos";
  private static final String DB_URL =
    "jdbc:sqlite:" + DB_DIR + "/src/main/resources/cats.db";

  private static String findProjectDir() {
    try {
      java.net.URL loc = CatData.class.getProtectionDomain()
        .getCodeSource()
        .getLocation();
      File src = new File(loc.toURI());
      File root = src.getParentFile().getParentFile();
      if (root != null && root.exists()) return root.getAbsolutePath();
    } catch (Exception ignored) {}
    return System.getProperty("user.dir");
  }

  public static List<Cat> load() {
    new File(DB_DIR).mkdirs();
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
          cat.photoPath = rs.getString("photo_path");
          cat.notes = rs.getString("notes");
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
                adopted_year INTEGER,
                photo_path TEXT,
                notes TEXT
            )
        """
      );
      try {
        stmt.execute("ALTER TABLE cats ADD COLUMN photo_path TEXT");
      } catch (SQLException ignored) {}
      try {
        stmt.execute("ALTER TABLE cats ADD COLUMN notes TEXT");
      } catch (SQLException ignored) {}
      try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM cats")) {
        if (rs.next() && rs.getInt(1) > 0) return;
      }
    }
    insertAll(conn);
  }

  private static void insertAll(Connection conn) throws SQLException {
    insertSeed(
      conn, "Ashid",
      26, Month.JUNE, 2007,
      11, Month.AUGUST, 2025,
      Cat.Pattern.SPOTTED, 127.0, null,
      "src/main/resources/photos/Ashid.JPG",
      "Self portrait May 8th, 2026.\nDouble exposure, Light.\nFromm Piano Room."
    );
    insertSeed(
      conn, "Aleena",
      22, Month.DECEMBER, 2006,
      13, Month.AUGUST, 2025,
      Cat.Pattern.TABBY, 100.0, null,
      "src/main/resources/photos/Aleena.JPG",
      "#1 Dave's Hot Chicken Consumer."
    );
    insertSeed(
      conn, "Kristin",
      20, Month.FEBRUARY, 2007,
      13, Month.AUGUST, 2025,
      Cat.Pattern.CALICO, 125.0, null,
      "src/main/resources/photos/Kristin.JPG",
      "Wrap it Up!!!"
    );
    insertSeed(
      conn, "Thunderpaws",
      14, Month.MARCH, 2019,
      4, Month.JULY, 2021,
      Cat.Pattern.BICOLOR, 132.0, null,
      null, null
    );
    insertSeed(
      conn, "Mittens",
      2, Month.OCTOBER, 2020,
      15, Month.JANUARY, 2022,
      Cat.Pattern.CALICO, 89.5, null,
      null, null
    );
    insertSeed(
      conn, "Noodle",
      30, Month.MAY, 2021,
      8, Month.NOVEMBER, 2022,
      Cat.Pattern.TABBY, 78.3, null,
      null, null
    );
    insertSeed(
      conn, "Waffles",
      7, Month.AUGUST, 2018,
      20, Month.MARCH, 2020,
      Cat.Pattern.TICKED, 115.0, null,
      null, null
    );
    insertSeed(
      conn, "Cobweb",
      31, Month.DECEMBER, 2016,
      2, Month.FEBRUARY, 2019,
      Cat.Pattern.SOLID, 141.7, null,
      null, null
    );
    insertSeed(
      conn, "Pickles",
      19, Month.JULY, 2022,
      5, Month.OCTOBER, 2023,
      Cat.Pattern.TORTOISESHELL, 67.4, null,
      null, null
    );
  }

  private static void insertSeed(
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
    Date adopted,
    String photoPath,
    String notes
  ) throws SQLException {
    String sql = """
          INSERT OR IGNORE INTO cats
            (name, born_day, born_month, born_year, came_day, came_month, came_year,
             pattern, weight, adopted_day, adopted_month, adopted_year, photo_path, notes)
          VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
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
      if (photoPath != null) {
        ps.setString(13, photoPath);
      } else {
        ps.setNull(13, Types.VARCHAR);
      }
      if (notes != null) {
        ps.setString(14, notes);
      } else {
        ps.setNull(14, Types.VARCHAR);
      }
      ps.executeUpdate();
    }
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

  public static void delete(Cat cat) {
    try (Connection conn = DriverManager.getConnection(DB_URL)) {
      try (
        PreparedStatement ps = conn.prepareStatement(
          "DELETE FROM cats WHERE name=?"
        )
      ) {
        ps.setString(1, cat.name);
        ps.executeUpdate();
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to delete cat", e);
    }
  }

  public static void updateAdopted(Cat cat) {
    try (Connection conn = DriverManager.getConnection(DB_URL)) {
      String sql =
        "UPDATE cats SET adopted_day=?, adopted_month=?, adopted_year=? WHERE name=?";
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

  public static void updatePhoto(Cat cat) {
    try (Connection conn = DriverManager.getConnection(DB_URL)) {
      try (
        PreparedStatement ps = conn.prepareStatement(
          "UPDATE cats SET photo_path=? WHERE name=?"
        )
      ) {
        if (cat.photoPath != null) {
          ps.setString(1, cat.photoPath);
        } else {
          ps.setNull(1, Types.VARCHAR);
        }
        ps.setString(2, cat.name);
        ps.executeUpdate();
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to update photo", e);
    }
  }

  public static void updateNotes(Cat cat) {
    try (Connection conn = DriverManager.getConnection(DB_URL)) {
      try (
        PreparedStatement ps = conn.prepareStatement(
          "UPDATE cats SET notes=? WHERE name=?"
        )
      ) {
        if (cat.notes != null) {
          ps.setString(1, cat.notes);
        } else {
          ps.setNull(1, Types.VARCHAR);
        }
        ps.setString(2, cat.name);
        ps.executeUpdate();
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to update notes", e);
    }
  }

  public static String copyPhoto(String sourcePath, String catName)
    throws IOException {
    new File(PHOTOS_DIR).mkdirs();
    String ext = sourcePath.substring(sourcePath.lastIndexOf('.'));
    String fileName = catName.replaceAll("[^a-zA-Z0-9_-]", "_") + ext;
    Path dest = Paths.get(PHOTOS_DIR, fileName);
    Files.copy(
      Paths.get(sourcePath),
      dest,
      StandardCopyOption.REPLACE_EXISTING
    );
    return "src/main/resources/photos/" + fileName;
  }

  public static String resolvePhotoPath(String relativePath) {
    if (relativePath == null) {
      return null;
    }
    if (Paths.get(relativePath).isAbsolute()) {
      return relativePath;
    }
    return DB_DIR + "/" + relativePath;
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
