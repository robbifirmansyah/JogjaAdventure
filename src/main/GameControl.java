package main;

public interface GameControl {
    void startGame();
    void resumeGame();
    void pauseGame();
    void quitGame();

    void togglePause();

    boolean isPaused();
}
