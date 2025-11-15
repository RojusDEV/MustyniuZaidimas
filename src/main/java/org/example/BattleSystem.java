package org.example;

import java.util.List;
import java.util.Scanner;

public class BattleSystem {
    //Variables
    private int round = 1;
    private int maxRoundCount;
    private double comboDamageMultiplier;

    private String roundWinner;
    private String gameWinner;

    private Boolean stopGame = false;

    private Boolean enemyCombo = false;
    private Boolean playerCombo = false;
    // Players object
    private Player player;
    private Enemy enemy;

    //Classes
    private final Settings settings;
    private JsonManager jsonManager = new JsonManager();
    private final Scanner scanner = new Scanner(System.in);
    private final UndoSystem undoSystem = new UndoSystem();
    //attacks
    private final List<String> attacks;

    private String prevEnemyAttack;
    private String prevPlayerAttack;
    private int curDamageDealt;

    //Colors
    private final Utils utils;

    public BattleSystem(Settings settings) {
        this.settings = settings;
        maxRoundCount = settings.getTotalRounds();
        comboDamageMultiplier = settings.getComboDamageMultiplier();
        attacks = jsonManager.retrieve_attacks();
        utils = new Utils();
    }

    public String startBattle(Player player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;
        round = 1;

        gameWinner = null;
        roundWinner = null;

        stopGame = false;

        maxRoundCount = settings.getTotalRounds();
        comboDamageMultiplier = settings.getComboDamageMultiplier();

        player.setUndoUsed(false);

        while(!isBattleOver() && !stopGame) {
            playRound();
        }
        if (stopGame) {
            return "menu";
        }
        return chooseOption();
    }


    public void playRound() {
        utils.clearScreen();
        displayMenu();

        if (stopGame) {
            return;
        }

        determineRoundWinner();

        undoSystem.saveState(new BattleState(round, player, enemy,
                prevPlayerAttack, prevEnemyAttack, roundWinner, playerCombo, enemyCombo));
        round++;

        if (isBattleOver()) {
            determineGameWinner();
            displayGameWinner();
        }
    }


    public String getWinner() {
        return gameWinner;
    }

    public int getRound() {
        return round;
    }

    public void displayHealth() {
        System.out.printf("""
                🧙 %s                         vs                         🧙 %s
                HP: [%s] %d/100                                           HP: [%s] %d/100
                -----------------------------------------------------------------------------
                """, player.getName(), enemy.getName(), utils.getHealthBars(player.getHealth()), player.getHealth(), utils.getHealthBars(enemy.getHealth()), enemy.getHealth());
    }

    public void displayMenu() {
        String undoStatus = player.hasUsedUndo() ? "IŠNAUDOTA" : "NEIŠNAUDOTA";
        System.out.printf("""
                ================================= WIZARD DUEL =================================
                Raundas: %d/%d                    UNDO: [%s]            Combo x%.1f
                -------------------------------------------------------------------------------
                """, round, maxRoundCount, undoStatus, comboDamageMultiplier);

        displayHealth();
        //========== Kovos logas ==========

        String enemyPreviousAttack = prevEnemyAttack;
        String playerPreviousAttack = prevPlayerAttack;
        if(roundWinner != null) {
            if(roundWinner.equals("player")) {
                System.out.printf("Raundas %d: %s laimėjo – „%s“ > „%s“\n", round, roundWinner, playerPreviousAttack, enemyPreviousAttack);
                System.out.printf("➡ Ataka „%s“ nuėmė %d gyvybių iš %s\n", playerPreviousAttack, curDamageDealt, enemy.getName());
            } else if(roundWinner.equals("enemy")) {
                System.out.printf("Raundas %d: %s laimėjo – „%s“ > „%s“\n", round, roundWinner, playerPreviousAttack, enemyPreviousAttack);
                System.out.printf("➡ Ataka „%s“ nuėmė %d gyvybių iš %s", enemyPreviousAttack, curDamageDealt, player.getName());
            } else {
                System.out.printf("Raundas %d: abu laimėjo – „%s“ = „%s", round, playerPreviousAttack, enemyPreviousAttack);
            }
        }

        int i = 1;
        System.out.printf("\n");
        System.out.println("JŪSŲ PASIRINKIMAS:");
        for(String attack : attacks) {
            System.out.printf("%d) %s\n", i, attack);
            i++;
        }
        System.out.print("""
                4) UNDO (grįžti 1–3 žingsniais)
                5) Baigti kovą (grįžti į meniu)
                > Įveskite pasirinkimą (1–5):""" + " ");

        short option = scanner.nextShort();

        switch (option) {
            case 1, 2, 3:
                enemy.chooseAttackAdaptive(player, attacks);
                player.addAttack(attacks.get(option - 1));
                break;
            case 4:
                if (!player.hasUsedUndo()) {
                    undoMove();
                    player.setUndoUsed(true);
                } else {
                    utils.displayColoredText("❌ UNDO jau panaudotas, daugiau nebegalima!", "RED");
                }
                break;
            case 5:
                stopGame = true;
                break;
            default:
                utils.displayColoredText("Blogas įvedimas, iveskite skaičiu nuo [1-5]", "RED");
                displayMenu();
                break;
        }

    }

    public void undoMove() {
        utils.displayColoredText("Kiek žingsnių atsukti (1–3)? ", "BLUE");
        int steps = scanner.nextInt();
        BattleState state = undoSystem.undo(steps);
        if (state != null) {
            this.round = state.round;
            player.setHealth(state.playerHealth);
            enemy.setHealth(state.enemyHealth);
            player.setAttackDeque(state.playerAttacks);
            enemy.setAttackDeque(state.enemyAttacks);
            this.prevPlayerAttack = player.getPreviousAttack();
            this.prevEnemyAttack = enemy.getPreviousAttack();
            this.roundWinner = state.roundWinner;
            this.playerCombo = state.playerCombo;
            this.enemyCombo = state.enemyCombo;
            System.out.println("🔁 Atsukta " + steps + " žingsniais atgal!");
        }
    }

    public void displayGameWinner() {
        System.out.println("""
                ================================= WIZARD DUEL =================================
                                               💀 KOVA BAIGTA 💀
                -------------------------------------------------------------------------------""");
        displayHealth();

        switch (gameWinner) {
            case "enemy" -> utils.displayColoredText("☠ REZULTATAS: PRALAIMĖJIMAS", "RED");
            case "player" -> utils.displayColoredText("❤ REZULTATAS: LAIMĖJIMAS", "GREEN");
            case "draw" ->  utils.displayColoredText("🟰 REZULTATAS: LYGIOSIOS", "BLUE");
            default -> utils.displayColoredText("Klaida.", "RED");
        };

    }

    public String chooseOption() {
        System.out.print("""
                -------------------------------------------------------------------------------
                JŪSŲ PASIRINKIMAS:
                1) 🔁 Bandyk dar kartą
                2) 📜 Peržiūrėti kovų istoriją
                3) 🏠 Grįžti į meniu
                > Įveskite pasirinkimą (1–3):\s""");

        short opt = scanner.nextShort();

        switch (opt) {
            case 1 -> { return "restart"; }
            case 2 -> { return "history"; }
            case 3 -> { return "menu"; }
            default -> {
                utils.displayColoredText("Blogas įvedimas, iveskite skaičių nuo [1-3]", "RED");
                return "menu";
            }
        }
    }

    public boolean isBattleOver() {
        if(player.gameLost() || enemy.gameLost()) {
            return true;
        } else return round > maxRoundCount;
    }

    public void determineGameWinner() {
        if(player.getHealth() == enemy.getHealth()) {
            gameWinner = "draw";
        } else if(player.getHealth() > enemy.getHealth()) {
            gameWinner = "player";
        } else {
            gameWinner = "enemy";
        }
    }

    public void displayComboMessage(Boolean playerDidCombo, Boolean enemyDidCombo) {
        if (playerDidCombo) {
            System.out.println();
            utils.displayColoredText("Player: 🔥 COMBO HIT!", "PURPLE");
        }

        if (enemyDidCombo) {
            System.out.println();
            utils.displayColoredText("Enemy: 💥 COMBO HIT!", "RED");
        }
    }

    public void determineRoundWinner() {
        prevPlayerAttack = player.getPreviousAttack();
        prevEnemyAttack = enemy.getPreviousAttack();

        if (prevPlayerAttack == null || prevEnemyAttack == null) {
            roundWinner = "draw";
            return;
        }


        // --- DRAW ---
        if (prevPlayerAttack.equals(prevEnemyAttack)) {
            roundWinner = "draw";

            boolean playerComboHit = player.containsCombo();
            boolean enemyComboHit = enemy.containsCombo();

            displayComboMessage(playerComboHit, enemyComboHit);

            int playerDamage = enemyComboHit
                    ? (int)(settings.getDamagePerAttack() * comboDamageMultiplier)
                    : settings.getDamagePerAttack();
            player.decreaseHealth(playerDamage);
            enemy.addTotalDamageDealt(playerDamage);


            int enemyDamage = playerComboHit
                    ? (int)(settings.getDamagePerAttack() * comboDamageMultiplier)
                    : settings.getDamagePerAttack();
            enemy.decreaseHealth(enemyDamage);
            player.addTotalDamageDealt(enemyDamage);

            System.out.printf("⚔️ DRAW! %s gavo %d žalos, %s gavo %d žalos%n",
                    player.getName(), playerDamage, enemy.getName(), enemyDamage);

            return;
        }

        // --- PLAYER WINS ---
        if (
                (prevPlayerAttack.equals("Phoenix Flare") && prevEnemyAttack.equals("Thorn Lash")) ||
                        (prevPlayerAttack.equals("Arctic Blast") && prevEnemyAttack.equals("Phoenix Flare")) ||
                        (prevPlayerAttack.equals("Thorn Lash") && prevEnemyAttack.equals("Arctic Blast"))
        ) {
            roundWinner = "player";

            boolean comboHit = player.containsCombo();
            applyDamage(player, enemy, comboHit);
            if(comboHit) {
                displayComboMessage(true, false);
                player.resetComboProgress();
            }
            enemyCombo = false;
            playerCombo = comboHit;
        }
        // --- ENEMY WINS ---
        else {
            roundWinner = "enemy";

            boolean comboHit = enemy.containsCombo();
            applyDamage(enemy, player, comboHit);
            if (comboHit) {
                displayComboMessage(false, true);
                enemy.resetComboProgress();
            }
            playerCombo = false;
            enemyCombo = comboHit;
        }
    }



    public void applyDamage(Player attacker, Player target, boolean isCombo) {
        int damage;
        if (isCombo) {
            damage = (int)(settings.getDamagePerAttack() * comboDamageMultiplier);
            System.out.printf("🔥 COMBO HIT! %s padarė %d žalos %s!\n",
                    attacker.getName(), damage, target.getName());
        } else {
            damage = settings.getDamagePerAttack();
        }

        target.decreaseHealth(damage);
        attacker.addTotalDamageDealt(damage);
        curDamageDealt = damage;
    }

}
