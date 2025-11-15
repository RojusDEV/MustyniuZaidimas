package org.example;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;

public class HistoryList {
    @SerializedName("matches")
    private final LinkedList<History> historyList = new LinkedList<>();

    public void addHistory(History history) {
        historyList.add(history);
    }

    public LinkedList<History> getHistoryList() {
        return historyList;
    }

    public static class History {
        private int match_id;
        private String  timestamp;
        private Players players;
        private Result result;
        private Stats stats;

        public int getMatch_id() { return match_id; }
        public void setMatch_id(int match_id) { this.match_id = match_id; }

        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

        public Players getPlayers() { return players; }
        public void setPlayers(Players players) { this.players = players; }

        public Result getResult() { return result; }
        public void setResult(Result result) { this.result = result; }

        public Stats getStats() { return stats; }
        public void setStats(Stats stats) { this.stats = stats; }
    }

    public static class Players {
        private CharacterData human;
        private CharacterData AI;

        public CharacterData getHuman() { return human; }
        public void setHuman(CharacterData human) { this.human = human; }

        public CharacterData getAI() { return AI; }
        public void setAI(CharacterData AI) { this.AI = AI; }
    }

    public static class CharacterData {
        @SerializedName("character_name")
        private String characterName;

        public String getCharacterName() { return characterName; }
        public void setCharacterName(String characterName) { this.characterName = characterName; }
    }

    public static class Result {
        private String winner;
        @SerializedName("rounds_player")
        private int roundsPlayer;

        public String getWinner() { return winner; }
        public void setWinner(String winner) { this.winner = winner; }

        public int getRoundsPlayer() { return roundsPlayer; }
        public void setRoundsPlayer(int roundsPlayer) { this.roundsPlayer = roundsPlayer; }
    }

    public static class Stats {
        private CharacterStats human;
        private CharacterStats AI;

        public CharacterStats getHuman() { return human; }
        public void setHuman(CharacterStats human) { this.human = human; }

        public CharacterStats getAI() { return AI; }
        public void setAI(CharacterStats AI) { this.AI = AI; }
    }

    public static class CharacterStats {
        private short hp_start;
        private short hp_end;
        private int total_damage_dealt;
        private int combos_landed;

        public short getHp_start() { return hp_start; }
        public void setHp_start(short hp_start) { this.hp_start = hp_start; }

        public short getHp_end() { return hp_end; }
        public void setHp_end(short hp_end) { this.hp_end = hp_end; }

        public int getTotal_damage_dealt() { return total_damage_dealt; }
        public void setTotal_damage_dealt(int total_damage_dealt) { this.total_damage_dealt = total_damage_dealt; }

        public int getCombos_landed() { return combos_landed; }
        public void setCombos_landed(int combos_landed) { this.combos_landed = combos_landed; }
    }
}
