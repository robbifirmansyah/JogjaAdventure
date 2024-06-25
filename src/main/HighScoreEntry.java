package main;

import java.io.Serializable;

public class HighScoreEntry implements Serializable {
    private String name;
    private long time; // Time in seconds

    public HighScoreEntry(String name, long time) {
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return name + ": " + time + " seconds";
    }
}

