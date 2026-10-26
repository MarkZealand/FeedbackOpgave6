package com.bytepartner;
public abstract class Character {
    String name;
    int age;
    int level;

    public Character(String name, int age, int level) {
        this.name = name;
        this.age = age;
        this.level = level;
    }

    public void action(){
        System.out.println("Character is performing an action.");
    }
    public void showInfo(){
        System.out.println("Name: " + name + " Age: " + age + " Level: " + level);
    }
}
