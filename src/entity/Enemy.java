package entity;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Enemy extends Entity {
    GamePanel gamePanel;
    private int startTileCode, endTileCode;
    private boolean movingToEnd = true;
    private int currentTileCode;
    private int spriteCounter = 0;
    private int spriteNum = 1;

    private BufferedImage[] upImages;
    private BufferedImage[] downImages;
    private BufferedImage[] leftImages;
    private BufferedImage[] rightImages;

    public Enemy(GamePanel gamePanel, int startTileCode, int endTileCode) {
        this.gamePanel = gamePanel;
        this.startTileCode = startTileCode;
        this.endTileCode = endTileCode;
        this.currentTileCode = startTileCode;
        setDefaultValues();
        getEnemyImage();
    }

    public void setDefaultValues() {
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
                    loadImage("/res/enemy/swordup6.png"),
                    loadImage("/res/enemy/up7.png"),
                    loadImage("/res/enemy/up8.png")
            };
            downImages = new BufferedImage[]{
                    loadImage("/res/enemy/sworddown1.png"),
                    loadImage("/res/enemy/sworddown2.png"),
                    loadImage("/res/enemy/sworddown3.png"),
                    loadImage("/res/enemy/sworddown4.png"),
                    loadImage("/res/enemy/sworddown5.png"),
                    loadImage("/res/enemy/sworddown6.png"),
                    loadImage("/res/enemy/down7.png"),
                    loadImage("/res/enemy/down8.png")
            };
            leftImages = new BufferedImage[]{
                    loadImage("/res/enemy/swordleft1.png"),
                    loadImage("/res/enemy/swordleft2.png"),
                    loadImage("/res/enemy/swordleft3.png"),
                    loadImage("/res/enemy/swordleft4.png"),
                    loadImage("/res/enemy/swordleft5.png"),
                    loadImage("/res/enemy/swordleft6.png"),
                    loadImage("/res/enemy/left7.png"),
                    loadImage("/res/enemy/left8.png")
            };
            rightImages = new BufferedImage[]{
                    loadImage("/res/enemy/swordright1.png"),
                    loadImage("/res/enemy/swordright2.png"),
                    loadImage("/res/enemy/swordright3.png"),
                    loadImage("/res/enemy/swordright4.png"),
                    loadImage("/res/enemy/swordright5.png"),
                    loadImage("/res/enemy/swordright6.png"),
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
        moveTo(currentTileCode);

        // Update sprite animation
        spriteCounter++;
        if (spriteCounter > 10) {
            spriteNum++;
            if (spriteNum > 3) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
        System.out.println("Enemy position: (" + worldX + ", " + worldY + ")"); // Log posisi musuh
    }

    private void moveTo(int targetTileCode) {
        // Directly move based on tile codes
        if (currentTileCode == targetTileCode) {
            movingToEnd = !movingToEnd;
            if (movingToEnd) {
                currentTileCode = endTileCode;
            } else {
                currentTileCode = startTileCode;
            }
        }
        switch (direction) {
            case "up":
                worldY -= speed; // Atur pergerakan ke atas
                break;
            case "down":
                worldY += speed; // Atur pergerakan ke bawah
                break;
            case "left":
                worldX -= speed; // Atur pergerakan ke kiri
                break;
            case "right":
                worldX += speed; // Atur pergerakan ke kanan
                break;
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
