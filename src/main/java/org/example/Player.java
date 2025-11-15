package org.example;

import java.util.*;

class PlayerState {
    int health;

    ArrayDeque<String> recentAttacks;

    public PlayerState(int health, Queue<String> recentAttacks) {
        this.health = health;
        this.recentAttacks = new ArrayDeque<>(recentAttacks);
    }
}

public class Player {
    private String name;
    private int health;
    private boolean undoUsed;
    private int totalDamageDealt = 0;

    private Deque<String> attackDeque = new ArrayDeque<>();
    private Stack<PlayerState> undoStack = new Stack<>();
    private boolean gameLost = false;
    Character character;

    public Player(Character character, boolean isAi) {
        this.character = character;
        setName(character.getName());
        health = 100;
        undoUsed = false;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {return name;}

    public Deque<String> getAttackDeque() {
        return new ArrayDeque<>(attackDeque);
    }

    public void setAttackDeque(Deque<String> attacks) {
        this.attackDeque = new ArrayDeque<>(attacks);
    }

    public boolean hasUsedUndo() {
        return undoUsed;
    }

    public void setUndoUsed(boolean used) {
        this.undoUsed = used;
    }

    public boolean gameLost() {
        return gameLost;
    }
    public int getTotalDamageDealt() {
        return totalDamageDealt;
    }

    public void addTotalDamageDealt(int damage) {
        totalDamageDealt += damage;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
    public void resetComboProgress() {
        attackDeque.clear();
    }

    public void addAttack(String attack) {
        undoStack.push(new PlayerState(health, attackDeque));

        if (undoStack.size() > 3) {
            undoStack.removeElementAt(0);
        }

        attackDeque.add(attack);

        if (attackDeque.size() > 3) {
            attackDeque.removeFirst();
        }
    }

    public void increaseHealth(int delta) {
        if(!(health + delta > 100)) {
            health += delta;
        }
    }

    public String getPreviousAttack() {
        return attackDeque.peekLast();
    }

    public void decreaseHealth(int delta) {
        if(health - delta <= 0) {
            gameLost = true;
            health = 0;
        } else {
            health -= delta;
        }
    }

    public boolean containsCombo() {
        if (attackDeque.size() < 3) return false;

        List<String> lastThree = new ArrayList<>(attackDeque);
        lastThree = lastThree.subList(lastThree.size() - 3, lastThree.size());

        for (Combo combo : character.getCombos()) {
            List<String> comboAttacks = new ArrayList<>(combo.attacks);

            if (comboAttacks.equals(lastThree)) {
                return true;
            }
        }
        return false;
    }
}
