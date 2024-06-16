package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StartPanel extends JPanel {
    private JLabel startButton;
    private JLabel quitButton;
    private JLabel volumeButton;
    private JLabel titleLabel;
    private int volumeLevel = 1;
    private JFrame window;
    private AudioPlayer backgroundMusic;

    private Image startImage;
    private Image quitImage;
    private Image volumeMute;
    private Image volume1;
    private Image volume2;
    private Image volume3;
    private Image backgroundImage;
    private Image titleImage;
    private List<Image> clouds;
    private List<Point> cloudPositions;

    private static final int CLOUD_SPEED = 1;

    public StartPanel(JFrame window, AudioPlayer backgroundMusic) {
        this.window = window;
        this.backgroundMusic = backgroundMusic;
        this.setPreferredSize(new Dimension(768, 576));
        this.setLayout(null);

        loadImages();

        titleLabel = createLabel(titleImage, 150, 100);
        startButton = createButton(startImage, 175, 350);
        quitButton = createButton(quitImage, 410, 350);
        volumeButton = createButton(volume1, 675, 490);

        add(titleLabel);
        add(startButton);
        add(quitButton);
        add(volumeButton);

        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                window.getContentPane().removeAll();
                GamePanel gamePanel = new GamePanel(backgroundMusic);
                window.add(gamePanel);
                window.pack();
                window.setLocationRelativeTo(null);
                window.setVisible(true);
                gamePanel.startGameThread();
                gamePanel.requestFocusInWindow();
            }
        });

        quitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        volumeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                volumeLevel = (volumeLevel + 1) % 4;
                switch (volumeLevel) {
                    case 0:
                        volumeButton.setIcon(new ImageIcon(volumeMute));
                        backgroundMusic.setVolume(-80.0f); // Mute
                        break;
                    case 1:
                        volumeButton.setIcon(new ImageIcon(volume1));
                        backgroundMusic.setVolume(-20.0f); // Low volume
                        break;
                    case 2:
                        volumeButton.setIcon(new ImageIcon(volume2));
                        backgroundMusic.setVolume(-10.0f); // Medium volume
                        break;
                    case 3:
                        volumeButton.setIcon(new ImageIcon(volume3));
                        backgroundMusic.setVolume(0.0f); // Full volume
                        break;
                }
            }
        });

        startCloudAnimation();
    }

    private void loadImages() {
        titleImage = loadImage("/res/start/title.png");
        startImage = loadImage("/res/start/start_button.png");
        quitImage = loadImage("/res/start/exit_button.png");
        volumeMute = loadImage("/res/start/volume_mute.png");
        volume1 = loadImage("/res/start/volume_1.png");
        volume2 = loadImage("/res/start/volume_2.png");
        volume3 = loadImage("/res/start/volume_3.png");
        backgroundImage = loadImage("/res/start/background.png");

        clouds = new ArrayList<>();
        for (int i = 0; i <= 5; i++) {
            clouds.add(loadImage("/res/start/cloud_" + i + ".png"));
        }
        cloudPositions = new ArrayList<>();
        for (int i = 0; i < clouds.size(); i++) {
            cloudPositions.add(new Point((i * 350), (int) (Math.random() * 500 + 50)));
        }
    }

    private Image loadImage(String path) {
        URL url = getClass().getResource(path);
        if (url != null) {
            return new ImageIcon(url).getImage();
        } else {
            System.err.println("Could not load image: " + path);
            return null;
        }
    }

    private JLabel createButton(Image img, int x, int y) {
        if (img == null) {
            return new JLabel("Image not found");
        }
        JLabel button = new JLabel(new ImageIcon(img));
        button.setBounds(x, y, img.getWidth(null), img.getHeight(null));
        return button;
    }

    private JLabel createLabel(Image img, int x, int y) {
        if (img == null) {
            return new JLabel("Image not found");
        }
        JLabel label = new JLabel(new ImageIcon(img));
        label.setBounds(x, y, img.getWidth(null), img.getHeight(null));
        return label;
    }

    private void startCloudAnimation() {
        Thread cloudThread = new Thread(() -> {
            while (true) {
                for (int i = 0; i < cloudPositions.size(); i++) {
                    Point pos = cloudPositions.get(i);
                    pos.x -= CLOUD_SPEED;
                    if (pos.x + clouds.get(i).getWidth(null) < 0) {
                        pos.x = getWidth();
                        pos.y = (int) (Math.random() * 200 + 50);
                    }
                }
                repaint();
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        cloudThread.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        for (int i = 0; i < clouds.size(); i++) {
            g.drawImage(clouds.get(i), cloudPositions.get(i).x, cloudPositions.get(i).y, this);
        }
    }
}
