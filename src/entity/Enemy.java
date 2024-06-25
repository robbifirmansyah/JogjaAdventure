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
        worldX = (int)(Math.random() * gamePanel.screenWidth); // Random starting x position
        worldY = (int)(Math.random() * gamePanel.screenHeight); // Random starting y position
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
                    loadImage("/res/enemy/up1.png"),
                    loadImage("/res/enemy/up2.png"),
                    loadImage("/res/enemy/up3.png"),
                    loadImage("/res/enemy/up4.png"),
                    loadImage("/res/enemy/up5.png"),
                    loadImage("/res/enemy/up6.png"),
                    loadImage("/res/enemy/up7.png"),
                    loadImage("/res/enemy/up8.png")
            };
            downImages = new BufferedImage[]{
                    loadImage("/res/enemy/down1.png"),
                    loadImage("/res/enemy/down2.png"),
                    loadImage("/res/enemy/down3.png"),
                    loadImage("/res/enemy/down4.png"),
                    loadImage("/res/enemy/down5.png"),
                    loadImage("/res/enemy/down6.png"),
                    loadImage("/res/enemy/down7.png"),
                    loadImage("/res/enemy/down8.png")
            };
            leftImages = new BufferedImage[]{
                    loadImage("/res/enemy/left1.png"),
                    loadImage("/res/enemy/left2.png"),
                    loadImage("/res/enemy/left3.png"),
                    loadImage("/res/enemy/left4.png"),
                    loadImage("/res/enemy/left5.png"),
                    loadImage("/res/enemy/left6.png"),
                    loadImage("/res/enemy/left7.png"),
                    loadImage("/res/enemy/left8.png")
            };
            rightImages = new BufferedImage[]{
                    loadImage("/res/enemy/right1.png"),
                    loadImage("/res/enemy/right2.png"),
                    loadImage("/res/enemy/right3.png"),
                    loadImage("/res/enemy/right4.png"),
                    loadImage("/res/enemy/right5.png"),
                    loadImage("/res/enemy/right6.png"),
                    loadImage("/res/enemy/right7.png"),
                    loadImage("/res/enemy/right8.png")
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
                worldX -= speed;
                break;
            case "down":
                worldY += speed;
                break;
            case "left":
                worldX -= speed;
                break;
            case "right":
                worldY += speed;
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
        g2.drawImage(image, worldX, worldY, gamePanel.tileSize, gamePanel.tileSize, null);
    }
}
