package com.bytepartner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        List<Character> characters = new ArrayList<>();

        dbQuery result = getResult();

        processResults(result, characters);

        loopThroughCharacterObjects(characters);

        closeConnections(result);
    }

    private static void loopThroughCharacterObjects(List<Character> characters) {
        for (Character character : characters) {
            character.showInfo();
            character.action();
        }
    }

    private static void closeConnections(dbQuery result) throws SQLException {
        result.resultSet().close();
        result.statement().close();
        result.connection().close();
    }

    private static void processResults(dbQuery result, List<Character> characters) throws SQLException {
        while (result.resultSet().next()) {
            String className = result.resultSet().getString("class_name");
            String characterName = result.resultSet().getString("character_name");
            int characterAge = result.resultSet().getInt("character_age");
            int characterLevel = result.resultSet().getInt("character_level");
            System.out.println("Class: " + className + " | Character: " + characterName + " | Age: " + characterAge + " | Level: " + characterLevel);

            addClassesToList(className, characterName, characterAge, characterLevel, characters);
        }
    }

    private static void addClassesToList(String className, String characterName, int characterAge, int characterLevel, List<Character> characters) {
        switch(className) {
            case "Jedi":
                Jedi jedi = new Jedi(characterName, characterAge, characterLevel);
                characters.add(jedi);
                break;
            case "Sith":
                Sith sith = new Sith(characterName, characterAge, characterLevel);
                characters.add(sith);
                break;
            case "CloneTrooper":
                CloneTrooper cloneTrooper = new CloneTrooper(characterName, characterAge, characterLevel);
                characters.add(cloneTrooper);
                break;
            case "Droid":
                Droid droid = new Droid(characterName, characterAge, characterLevel);
                characters.add(droid);
                break;
            default:
                System.out.println("Unknown class: " + className);
        }
    }

    private static dbQuery getResult() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/", "root", "*******"
        );

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM characters.character_classes");
        dbQuery result = new dbQuery(connection, statement, resultSet);
        return result;
    }

    private record dbQuery(Connection connection, Statement statement, ResultSet resultSet) {
    }
}
