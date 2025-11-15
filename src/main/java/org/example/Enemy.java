package org.example;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Enemy extends Player {

    public Enemy(Character character, boolean isAi) {
        super(character, isAi);
    }

    public void chooseAttackAdaptive(Player player, List<String> attacks) {
        if (Objects.equals(player.getPreviousAttack(), "Phoenix Flare") && attacks.contains("Arctic Blast")) {
            addAttack("Arctic Blast");
        } else if (Objects.equals(player.getPreviousAttack(), "Arctic Blast") && attacks.contains("Thorn Lash")) {
            addAttack("Thorn Lash");
        } else if (Objects.equals(player.getPreviousAttack(), "Thorn Lash") && attacks.contains("Phoenix Flare")) {
            addAttack("Phoenix Flare");
        } else {
            addAttack(attacks.get(new Random().nextInt(attacks.size())));
        }
    }
}
