package org.example;

import java.util.Scanner;

public class Menu {
    private final Scanner scanner = new Scanner(System.in);
    public int chooseOption() {
        System.out.print("""
                ===============================
                🔥 WIZARD DUEL: SHADOW REALMS 🔥
                ===============================
                1. Žaisti
                2. Nustatymai
                3. Kovų istorija
                4. Išeiti
                -------------------------------
                Pasirinkite veiksmą (1-4):\t""");

        int option = -1;
        while (option < 1 || option > 4) {
            try {
                option = Integer.parseInt(scanner.nextLine());
                if (option < 1 || option > 4) {
                    System.out.print("❌ Netinkamas pasirinkimas. Bandykite dar kartą (1-4): ");
                }
            } catch (NumberFormatException e) {
                System.out.print("❌ Įveskite skaičių (1-4): ");
            }
        }
        return option;
    }
}
