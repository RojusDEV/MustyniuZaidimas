package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class JsonManager {
    private static final GsonBuilder builder = new GsonBuilder();
    private static final Gson gson = builder.setPrettyPrinting().create();

    private static final String HISTORY_DIR = "history";
    private static final String HISTORY_FILE = HISTORY_DIR + "/history.json";
    private final Path historyPath = Paths.get(HISTORY_FILE);

    public JsonManager() {
        File dir = new File(HISTORY_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public List<String> retrieve_attacks() {
        try (InputStream input = getClass().getResourceAsStream("/data/characters.json")) {
            if (input == null) {
                System.out.println("Json failas nerastas");
                throw new RuntimeException("Failas nerastas: /data/characters.json");
            }

            JsonReader reader = new JsonReader(new InputStreamReader(input, StandardCharsets.UTF_8));
            CharactersData data = gson.fromJson(reader, CharactersData.class);
            return data.getAttacks();

        } catch (Exception e) {
            System.out.println("Klaida nuskaitant JSON failą");
            throw new RuntimeException(e);
        }
    }

    public LinkedList<Character> retrieve_characters() {
        try (InputStream input = getClass().getResourceAsStream("/data/characters.json")) {
            if (input == null) {
                System.out.println("Json failas nerastas");
                throw new RuntimeException("Resource not found: /data/characters.json");
            }

            JsonReader reader = new JsonReader(new InputStreamReader(input, StandardCharsets.UTF_8));
            CharactersData data = gson.fromJson(reader, CharactersData.class);
            return data.getCharacters();

        } catch (Exception e) {
            System.out.println("Klaida nuskaitant JSON formato failą");
            throw new RuntimeException(e);
        }
    }

    private HistoryList loadHistoryListSafe() {
        if (!Files.exists(historyPath)) return new HistoryList();
        try (Reader reader = Files.newBufferedReader(historyPath)) {
            HistoryList list = gson.fromJson(reader, HistoryList.class);
            return (list != null) ? list : new HistoryList();
        } catch (IOException e) {
            return new HistoryList();
        }
    }

    public void save_history(HistoryList.History newHistory) {
        HistoryList historyList = loadHistoryListSafe();
        historyList.addHistory(newHistory);
        try (Writer writer = Files.newBufferedWriter(historyPath)) {
            gson.toJson(historyList, writer);
        } catch (IOException e) {
            throw new RuntimeException("Klaida įrašant istoriją", e);
        }
    }

    public HistoryList retrieve_history() {
        return loadHistoryListSafe();
    }
}
