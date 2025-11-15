package org.example;

import java.util.LinkedList;

public class Character {
    private String name;
    private String description;
    private LinkedList<Combo> combos = new LinkedList<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public LinkedList<Combo> getCombos() {
        return combos;
    }

    public void setCombos(LinkedList<Combo> combos) {
        this.combos = combos;
    }
}
