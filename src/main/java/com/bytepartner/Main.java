package com.bytepartner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private final Db db = new Db();

    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        List<Character> characters = new ArrayList<>();

        try {
            testConnection();

            try (Connection connection = db.get();
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(
                         "SELECT * FROM characters.character_classes")) {

                processResults(resultSet, characters);

            } // connection, statement og resultSet lukkes automatisk her

        } catch (SQLException e) {
            System.out.println("❌ Database fejl: " + e.getMessage());
            System.out.println("   SQLState: " + e.getSQLState());
        } catch (Exception e) {
            System.out.println("❌ Uventet fejl: " + e.getMessage());
        }

        loopThroughCharacterObjects(characters);
    }

    public void testConnection() {
        try (Connection c = db.get()) {
            DatabaseMetaData md = c.getMetaData();
            System.out.println("✅ Forbindelse OK: " + md.getURL());
            System.out.println("   Driver: " + md.getDriverName() + " - " + md.getDriverVersion());
        } catch (Exception e) {
            System.out.println("❌ Forbindelse FEJL: " + e.getMessage());
            System.out.println("Tip: Tjek URL/USER/PASS og at MySQL kører.");
        }
    }

    private static void loopThroughCharacterObjects(List<Character> characters) {
        for (Character character : characters) {
            character.showInfo();
            character.action();
        }
    }

    private static void processResults(ResultSet resultSet, List<Character> characters)
            throws SQLException {
        while (resultSet.next()) {
            String className     = resultSet.getString("class_name");
            String characterName = resultSet.getString("character_name");
            int characterAge     = resultSet.getInt("character_age");
            int characterLevel   = resultSet.getInt("character_level");

            System.out.println("Class: " + className
                    + " | Character: " + characterName
                    + " | Age: " + characterAge
                    + " | Level: " + characterLevel);

            addClassesToList(className, characterName, characterAge, characterLevel, characters);
        }
    }

    private static void addClassesToList(String className, String characterName,
                                         int characterAge, int characterLevel,
                                         List<Character> characters) {
        switch (className) {
            case "Jedi"         -> characters.add(new Jedi(characterName, characterAge, characterLevel));
            case "Sith"         -> characters.add(new Sith(characterName, characterAge, characterLevel));
            case "CloneTrooper" -> characters.add(new CloneTrooper(characterName, characterAge, characterLevel));
            case "Droid"        -> characters.add(new Droid(characterName, characterAge, characterLevel));
            default             -> System.out.println("Unknown class: " + className);
        }
    }
}