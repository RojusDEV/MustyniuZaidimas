package org.example;
import java.time.LocalDateTime;
import java.util.*;

public class Game {
    private JsonManager jsonManager;
    private BattleSystem battleSystem;
    private Settings settings;

    private Player player;
    private Enemy enemy;

    private Menu menu;
    private LinkedList<Character> charactersList;
    private final Scanner scanner = new Scanner(System.in);
    private final Utils utils = new Utils();

    public void init() {
        loadData();
        displayIntro();
        showMenu();
    }

    private void showMenu() {
        boolean inMenu = true;
        while (inMenu) {
            int option = menu.chooseOption();

            switch (option) {
                case 1 -> startGame();
                case 2 -> settings.showSettings();
                case 3 -> showHistory();
                case 4 -> {
                    System.out.println("👋 Išeinama iš žaidimo...");
                    inMenu = false;
                    System.exit(0);
                }
                default -> utils.displayColoredText("Neteisingas pasirinkimas", "RED");
            }
        }
    }

    public void loadData() {
        jsonManager = new JsonManager();
        settings = new Settings();
        battleSystem = new BattleSystem(settings);
        menu = new Menu();

        charactersList = jsonManager.retrieve_characters();
    }

    public void displayIntro() {
        System.out.print("""
            ===========================================
            🔥 WIZARD DUEL: SHADOW REALMS 🔥
            ===========================================
            Senovėje, kai šviesa ir tamsa kovojo dėl valdžios,
            šeši galingi burtininkai stojo į dvikovą dėl magijos sostų.
            Kas įvaldys liepsną, ledą ir gamtos jėgą?
            Kas taps tikruoju Magijos Valdovu?

            Pasiruošk savo lazdelę...
            Ir tegul prasideda dvikova!
            ===========================================
            [ENTER] Tęsti""");
        waitForUserResponse();
    }

    public void startGame() {
        boolean playing = true;

        while (playing) {
            showTournamentIntro();
            chooseCharacter();
            String result = battleSystem.startBattle(player, enemy);

            if(settings.getAutoSave()) {
                addBattleToHistory(player, enemy, battleSystem.getWinner());
            }

            switch (result) {
                case "restart" -> {}
                case "menu" -> {
                    playing = false;
                }
                case "history" -> {
                    showHistory();
                    playing = false;
                }
                default -> playing = false;
            }
        }
    }


    private void showTournamentIntro() {
        System.out.print("""
            🪄 Sveikas atvykęs į WIZARD DUEL 🪄
            Magijos turnyras prasideda čia.
            Kiekvienas burtininkas valdo tris jėgas:
            🔥 Phoenix Flare  ❄️ Arctic Blast  🌿 Thorn Lash
            Išsirink savo čempioną ir įrodyk, kas stipresnis!
            
            [ENTER] Pradėti kovą""");
        waitForUserResponse();
    }

    public void chooseCharacter() {
        int charactersListLength = charactersList.size();
        System.out.println("========= PASIRINKITE PERSONAŽĄ =========");
        int i = 1;
        for(Character character : charactersList) {
            String name = character.getName();
            String description = character.getDescription();
            System.out.printf("%d) %s\t- %s\n", i, name, description);
            i++;
        }
        System.out.println("-----------------------------------------");
        System.out.printf("[1-%d] pasirinkti: ", charactersListLength);

        short option = scanner.nextShort();
        scanner.nextLine();


        Character character = charactersList.get(option - 1);

        player = new Player(character, false);

        //Picks random character except currently selected character.
        Random rng = new Random();

        int randomNum;
        do {
            randomNum = rng.nextInt(charactersListLength);
        } while (randomNum == (option - 1));

        enemy = new Enemy(charactersList.get(randomNum), true);

        System.out.printf("[%s]\n", player.character.getName());
        System.out.printf("Aprašymas: %s\n", player.character.getDescription());

        List<String> attacks = jsonManager.retrieve_attacks();
        //Character possible attacks
        System.out.print("Galimos atakos: ");

        for(String attack : attacks) {
            System.out.printf("%s | ", attack);
        }

        utils.clearScreen();
    }

    //========= Settings Management =========

    public void showHistory() {
        HistoryList historyList = jsonManager.retrieve_history();
        LinkedList<HistoryList.History> list = historyList.getHistoryList();

        if (list.isEmpty()) {
            System.out.println("Istorija tuščia.");
            return;
        }

        int i = 1;
        for (HistoryList.History h : list) {
            System.out.printf("""
                    =====================================
                    %d)
                    ID: %d
                    Characters: %s vs %s
                    Winner: %s
                    Total Rounds: %d
                    TIMESTAMP: %s
                    =====================================
                    
                    """,
                    i,
                    h.getMatch_id(),
                    h.getPlayers().getHuman().getCharacterName(),
                    h.getPlayers().getAI().getCharacterName(),
                    h.getResult().getWinner(),
                    h.getResult().getRoundsPlayer(),
                    h.getTimestamp());
            i++;
        }
        System.out.printf("""
        (Viso kovų: %d)
        [ENTER] Grįžti į pagrindinį meniu """, list.size());
        waitForUserResponse();
    }


    public void addBattleToHistory(Player player, Player enemy, String winner) {
        HistoryList.History history = new HistoryList.History();

        // Players
        HistoryList.CharacterData humanCharacterData = new HistoryList.CharacterData();
        humanCharacterData.setCharacterName(player.getName());
        HistoryList.CharacterData aiCharacterData = new HistoryList.CharacterData();
        aiCharacterData.setCharacterName(enemy.getName());

        HistoryList.Players players = new HistoryList.Players();
        players.setHuman(humanCharacterData);
        players.setAI(aiCharacterData);

        // Result
        HistoryList.Result results = new HistoryList.Result();
        results.setWinner(winner);
        results.setRoundsPlayer(battleSystem.getRound());

        // Stats
        HistoryList.CharacterStats humanStats = new HistoryList.CharacterStats();
        humanStats.setHp_start((short) 100);
        humanStats.setHp_end((short) player.getHealth());
        humanStats.setTotal_damage_dealt(player.getTotalDamageDealt());

        HistoryList.CharacterStats aiStats = new HistoryList.CharacterStats();
        aiStats.setHp_start((short) 100);
        aiStats.setHp_end((short) enemy.getHealth());
        aiStats.setTotal_damage_dealt(enemy.getTotalDamageDealt());

        HistoryList.Stats stats = new HistoryList.Stats();
        stats.setHuman(humanStats);
        stats.setAI(aiStats);

        history.setPlayers(players);
        history.setResult(results);
        history.setStats(stats);
        history.setTimestamp(LocalDateTime.now().toString());
        history.setMatch_id((int)(Math.random() * 999));

        jsonManager.save_history(history);
    }


    //========= Helper Methods =========
    public void waitForUserResponse() {
        boolean next = false;
        while(!next) {
            String input;
            input = scanner.nextLine();
            if(input.isEmpty()) {
                next = true;
            }
        }
        utils.clearScreen();
    }

}
