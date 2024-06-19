package main;

import entity.Player;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

public class GamePanel extends JPanel implements Runnable, GameControl {
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;

    int FPS = 60;
    boolean paused = false;

    KeyHandler keyHandler = new KeyHandler(this);
    Thread gameThread;
    Player player = new Player(this, keyHandler);
    private AudioPlayer backgroundMusic;

    public AudioPlayer getBackgroundMusic() {
        return backgroundMusic;
    }

    private Timer gameTimer;
    private long startTime;
    private long elapsedTime;
    private JLabel timerLabel;

    public GamePanel(AudioPlayer backgroundMusic) {
        this.backgroundMusic = backgroundMusic;
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        // Initialize Timer
        gameTimer = new Timer();
        timerLabel = new JLabel("Time: 0");
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setFont(new Font("OneSize", Font.PLAIN, 20));
        this.add(timerLabel);
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    @Override
    public void startGame() {
        startTime = System.currentTimeMillis();
        startGameThread();
        startTimer();
    }

    private void startTimer() {
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                elapsedTime = System.currentTimeMillis() - startTime;
                timerLabel.setText("Time: " + (elapsedTime / 1000) + " seconds");
            }
        }, 0, 1000); // Update every second
    }

    private void stopTimer() {
        gameTimer.cancel();
    }

    // Call this method when the game is completed
    public void completeGame() {
        stopTimer();
        saveHighScore(elapsedTime / 1000); // Save elapsed time in seconds
    }

    private void saveHighScore(long timeInSeconds) {
        String playerName = JOptionPane.showInputDialog(this, "Congratulations! Enter your name:");
        if (playerName != null && !playerName.isEmpty()) {
            HighScoreManager highScoreManager = new HighScoreManager();
            highScoreManager.addHighScore(new HighScoreEntry(playerName, timeInSeconds));
            highScoreManager.saveHighScores();
        }
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double nextDrawTime = System.nanoTime();
        while (gameThread != null) {
            if (!paused) {
                update();
                repaint();
            }
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }
                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void update() {
        player.update();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        player.draw(g2);
        g2.dispose();
    }

    @Override
    public void togglePause() {
        paused = !paused;
        if (paused) {
            backgroundMusic.pause();
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (topFrame == null) {
                System.err.println("topFrame is null. Ensure GamePanel is added to a JFrame before calling togglePause.");
                return;
            }
            JLayeredPane layeredPane = new JLayeredPane();
            layeredPane.setPreferredSize(new Dimension(screenWidth, screenHeight));
            PausePanel pausePanel = new PausePanel(this);
            pausePanel.setBounds(0, 0, screenWidth, screenHeight);
            layeredPane.add(this, JLayeredPane.DEFAULT_LAYER);
            layeredPane.add(pausePanel, JLayeredPane.PALETTE_LAYER);
            topFrame.setContentPane(layeredPane);
            topFrame.validate();
        } else {
            backgroundMusic.resume();
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            topFrame.setContentPane(this);
            topFrame.validate();
            this.requestFocus();
        }
    }

    @Override
    public boolean isPaused() {
        return paused;
    }

    @Override
    public void resumeGame() {
        if (paused) {
            togglePause();
        }
    }

    @Override
    public void pauseGame() {
        if (!paused) {
            togglePause();
        }
    }

    @Override
    public void quitGame() {
        System.exit(0);
    }
}
