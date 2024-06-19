package main;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HighScoreManager {
    private List<HighScoreEntry> highScores;
    private final String highScoreFile = "highscores.txt";

    public HighScoreManager() {
        highScores = new ArrayList<>();
        loadHighScores();
    }

    public void addHighScore(HighScoreEntry entry) {
        highScores.add(entry);
        Collections.sort(highScores, Comparator.comparingLong(HighScoreEntry::getTime));
    }

    public List<HighScoreEntry> getHighScores() {
        return highScores;
    }

    private void loadHighScores() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(highScoreFile))) {
            highScores = (List<HighScoreEntry>) ois.readObject();
        } catch (FileNotFoundException e) {
            // File not found, no action needed
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveHighScores() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(highScoreFile))) {
            oos.writeObject(highScores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

