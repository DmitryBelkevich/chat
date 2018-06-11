package com.hard.models;

public class Entity {
    private String str;

    public Entity(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "str='" + str + '\'' +
                '}';
    }
}
