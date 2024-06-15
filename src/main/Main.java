package main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Jogja Adventure");

        // Start screen panel with Start and Quit buttons
        JPanel startPanel = new JPanel();
        JButton startButton = new JButton("Start Game");
        JButton quitButton = new JButton("Quit Game");

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.getContentPane().removeAll();
                GamePanel gamePanel = new GamePanel();
                window.add(gamePanel);
                window.pack();
                window.setLocationRelativeTo(null);
                window.setVisible(true);
                gamePanel.startGameThread();
                gamePanel.requestFocusInWindow();  // Request focus for key events
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        startPanel.add(startButton);
        startPanel.add(quitButton);

        window.add(startPanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}
