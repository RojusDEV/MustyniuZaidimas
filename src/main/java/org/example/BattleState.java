package org.example;

import java.util.ArrayDeque;
import java.util.Deque;

public class BattleState {
    public int round;
    public int playerHealth;
    public int enemyHealth;
    public Deque<String> playerAttacks;
    public Deque<String> enemyAttacks;
    public String playerAttack;
    public String enemyAttack;
    public String roundWinner;
    public boolean playerCombo;
    public boolean enemyCombo;

    public BattleState(int round, Player player, Enemy enemy,
                       String playerAttack, String enemyAttack,
                       String roundWinner, boolean playerCombo, boolean enemyCombo) {
        this.round = round;
        this.playerHealth = player.getHealth();
        this.enemyHealth = enemy.getHealth();
        this.playerAttacks = new ArrayDeque<>(player.getAttackDeque());
        this.enemyAttacks = new ArrayDeque<>(enemy.getAttackDeque());
        this.playerAttack = playerAttack;
        this.enemyAttack = enemyAttack;
        this.roundWinner = roundWinner;
        this.playerCombo = playerCombo;
        this.enemyCombo = enemyCombo;
    }
}
