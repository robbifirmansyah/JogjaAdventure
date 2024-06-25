package entity;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Enemy extends Entity {
    GamePanel gamePanel;
    private int spriteCounter = 0;
    private int spriteNum = 1;

    private BufferedImage[] upImages;
    private BufferedImage[] downImages;
    private BufferedImage[] leftImages;
    private BufferedImage[] rightImages;

    public Enemy(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        setDefaultValues();
        getEnemyImage();
    }

    public void setDefaultValues() {
        x = (int)(Math.random() * gamePanel.screenWidth); // Random starting x position
        y = (int)(Math.random() * gamePanel.screenHeight); // Random starting y position
        speed = 2;
        direction = getRandomDirection();
    }

    private String getRandomDirection() {
        int randomDirection = (int) (Math.random() * 4);
        switch (randomDirection) {
            case 0:
                return "up";
            case 1:
                return "down";
            case 2:
                return "left";
            case 3:
                return "right";
            default:
                return "down";
        }
    }

    public void getEnemyImage() {
        try {
            upImages = new BufferedImage[]{
                    loadImage("/res/enemy/swordup1.png"),
                    loadImage("/res/enemy/swordup2.png"),
                    loadImage("/res/enemy/swordup3.png"),
                    loadImage("/res/enemy/swordup4.png"),
                    loadImage("/res/enemy/swordup5.png"),
                    loadImage("/res/enemy/swordup6.png")
            };
            downImages = new BufferedImage[]{
                    loadImage("/res/enemy/sworddown1.png"),
                    loadImage("/res/enemy/sworddown2.png"),
                    loadImage("/res/enemy/sworddown3.png"),
                    loadImage("/res/enemy/sworddown4.png"),
                    loadImage("/res/enemy/sworddown5.png"),
                    loadImage("/res/enemy/sworddown6.png")
            };
            leftImages = new BufferedImage[]{
                    loadImage("/res/enemy/swordleft1.png"),
                    loadImage("/res/enemy/swordleft2.png"),
                    loadImage("/res/enemy/swordleft3.png"),
                    loadImage("/res/enemy/swordleft4.png"),
                    loadImage("/res/enemy/swordleft5.png"),
                    loadImage("/res/enemy/swordleft6.png")
            };
            rightImages = new BufferedImage[]{
                    loadImage("/res/enemy/swordright1.png"),
                    loadImage("/res/enemy/swordright2.png"),
                    loadImage("/res/enemy/swordright3.png"),
                    loadImage("/res/enemy/swordright4.png"),
                    loadImage("/res/enemy/swordright5.png"),
                    loadImage("/res/enemy/swordright6.png")
            };
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1); // Exit if images are not found
        }
    }

    private BufferedImage loadImage(String path) throws IOException {
        BufferedImage img = ImageIO.read(getClass().getResourceAsStream(path));
        if (img == null) {
            throw new IOException("Image not found: " + path);
        }
        return img;
    }

    public void update() {
        // Simple AI for enemy movement
        switch (direction) {
            case "up":
                y -= speed;
                break;
            case "down":
                y += speed;
                break;
            case "left":
                x -= speed;
                break;
            case "right":
                x += speed;
                break;
        }

        // Change direction at random intervals
        spriteCounter++;
        if (spriteCounter > 60) {
            int randomDirection = (int) (Math.random() * 4);
            switch (randomDirection) {
                case 0:
                    direction = "up";
                    break;
                case 1:
                    direction = "down";
                    break;
                case 2:
                    direction = "left";
                    break;
                case 3:
                    direction = "right";
                    break;
            }
            spriteCounter = 0;
        }

        // Update sprite animation
        spriteCounter++;
        if (spriteCounter > 10) {
            spriteNum++;
            if (spriteNum > 3) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage[] images = null;
        switch (direction) {
            case "up":
                images = upImages;
                break;
            case "down":
                images = downImages;
                break;
            case "left":
                images = leftImages;
                break;
            case "right":
                images = rightImages;
                break;
        }
        BufferedImage image = images[spriteNum - 1];
        g2.drawImage(image, x, y, 64, 64, null);
    }
}
