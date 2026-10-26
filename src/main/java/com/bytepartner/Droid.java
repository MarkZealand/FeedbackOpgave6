package com.bytepartner;
public class Droid extends Character {
    public Droid(String name, int age, int level) {
        super(name, age, level);
    }

    @Override
    public void action() {
        System.out.println("Droid is performing a task.");
    }

    public void superTest(){
        super.showInfo();
    }
}
