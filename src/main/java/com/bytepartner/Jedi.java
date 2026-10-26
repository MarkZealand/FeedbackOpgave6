package com.bytepartner;
public class Jedi extends Character {

    public Jedi(String name, int age, int level) {
        super(name, age, level);
    }


    @Override
    public void action() {
        System.out.println("Jedi is using the Force.");
    }
}
