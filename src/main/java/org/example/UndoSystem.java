package org.example;

import java.util.Stack;

public class UndoSystem {
    private final Stack<BattleState> history = new Stack<>();

    public void saveState(BattleState state) {
        history.push(state);
    }

    public BattleState undo(int steps) {
        if (steps < 1 || steps > 3) {
            System.out.println("Galima atsukti tik 1–3 žingsnius!");
            return null;
        }
        if (history.size() < steps) {
            System.out.println("Nepakanka istorijos atsukti " + steps + " žingsnių.");
            return null;
        }

        for (int i = 0; i < steps; i++) {
            history.pop();
        }

        return history.isEmpty() ? null : history.peek();
    }
}
