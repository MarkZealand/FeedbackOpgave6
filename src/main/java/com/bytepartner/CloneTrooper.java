package com.bytepartner;

public class CloneTrooper extends Character {
    public CloneTrooper(String name, int age, int level) {
        super(name, age, level);
    }

    @Override
    public void action() {
        System.out.println("Clone Trooper is fighting for the Republic.");
    }
}
