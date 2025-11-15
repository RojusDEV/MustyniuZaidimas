package org.example;

import java.util.Scanner;

public class Settings {
    private final Scanner scanner;
    private int totalRounds = 15;
    private double comboDamageMultiplier = 1.5;
    private int damagePerAttack = 10;
    private int comboBonusDamage = 20;
    private boolean autoSave = true;
    private Utils utils = new Utils();
    public int getDamagePerAttack() {
        return damagePerAttack;
    }

    public void setDamagePerAttack(int damage) {
        damagePerAttack = damage;
    }


    public int getComboBonusDamage() {
        return comboBonusDamage;
    }

    public boolean getAutoSave() {
        return autoSave;
    }

    public void setComboBonusDamage(int damage) {
        comboBonusDamage = (int)(damage * comboDamageMultiplier);
    }

    public Settings() {
        scanner = new Scanner(System.in);
    }

    public void toggleAutoSave() {
        autoSave=!autoSave;
    }

    public void updateRounds() {
        System.out.printf("""
                Kiek raundų norite nustatyti? (dabartinis: %d)
                Įveskite naują reikšmę: """, totalRounds);
        totalRounds = scanner.nextInt();
        if(totalRounds == 1) {
            System.out.printf("Kovos trukmė pakeista į %d raundą!", totalRounds);
        }
        else System.out.printf("Kovos trukmė pakeista į %d raundus!\n", totalRounds);
    }

    public void updateComboMultiplier() {
        System.out.printf("""
                Koki combo žalos daugiklis norite nustatyti? (dabartinis: %s)
                Įveskite naują reikšmę: """, comboDamageMultiplier);
        comboDamageMultiplier = scanner.nextDouble();
        System.out.printf("ombo žalos daugiklis pakeistas į [%fx!]\n", comboDamageMultiplier);
    }

    public int getTotalRounds() {
        return totalRounds;
    }

    public double getComboDamageMultiplier() {
        return comboDamageMultiplier;
    }

    public void showSettings() {
        boolean closeOptions = false;
        while(!closeOptions) {
            System.out.printf("""
                    \n \n \n \n \n \n
                    ======== NUSTATYMAI ========
                    1. Kovos trukmė (raundai): [%d]
                    2. Combo žalos daugiklis: [%.1fx]
                    3. Auto-save kovų istorija: [%b]
                    4. Grįžti į meniu
                    ----------------------------
                    Pasirinkite (1-4)\t""", totalRounds, comboDamageMultiplier, autoSave);
            short option = scanner.nextShort();
            utils.clearScreen();
            switch (option) {
                case 1:
                    updateRounds();
                    break;
                case 2:
                    updateComboMultiplier();
                    break;
                case 3:
                    toggleAutoSave();
                    break;
                case 4:
                    closeOptions = true;
                    utils.clearScreen();
                    break;
                default:
                    utils.displayColoredText("Neteisingas pasirinkimas, pasirinkite iš 1-4 pasirinkimų", "RED");
            }
        }
    }
}