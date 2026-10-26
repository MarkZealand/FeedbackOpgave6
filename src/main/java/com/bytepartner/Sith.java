package com.bytepartner;
public class Sith extends Character {
    public Sith(String name, int age, int level) {
        super(name, age, level);
    }

    @Override
    public void action() {
        System.out.println("Sith is using the Dark Side of the Force.");
    }
}
