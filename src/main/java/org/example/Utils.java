package org.example;

public class Utils {
    enum Colors {
        BLACK("\u001B[30m"),
        RED("\u001B[31m"),
        GREEN("\u001B[32m"),
        YELLOW("\u001B[33m"),
        BLUE("\u001B[34m"),
        PURPLE("\u001B[35m"),
        CYAN("\u001B[36m"),
        WHITE("\u001B[37m");

        private final String code;

        Colors(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    private static final String ANSI_RESET = "\u001B[0m";

    public void displayColoredText(String text, String colorName) {
        try {
            Colors color = Colors.valueOf(colorName.toUpperCase());
            System.out.println(color.getCode() + text + ANSI_RESET);
        } catch (IllegalArgumentException e) {
            System.out.println(text);
        }
    }

    public String getHealthBars(int curHealth) {
        int filledLength = (int) Math.round((double) curHealth / 100 * 10);
        String filled = "█".repeat(filledLength);
        String empty = "░".repeat(10 - filledLength);
        return filled + empty;
    }

    public void clearScreen() {
        System.out.println("\u001B[2J");
    }
}
