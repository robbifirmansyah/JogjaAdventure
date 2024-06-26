// Player.java
package entity;

import main.GamePanel;
import main.KeyHandler;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Set;

public class Player extends Entity {

    GamePanel gamePanel;
    KeyHandler keyHandler;

    private int spriteCounter = 0;
    private int spriteNum = 1;
    private boolean restrictMovement = true;

    private BufferedImage up1, up2, up3, down1, down2, down3;
    private BufferedImage left1, left2, left3, right1, right2, right3;
    private BufferedImage hpImage, speedImage; // Gambar untuk HP dan kecepatan
    public final int screenX;
    public final int screenY;
    private Set<Integer> visitableTiles;
    private int hp;
    private int speed;

    public Player(GamePanel gamePanel, KeyHandler keyHandler) {
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        screenX = gamePanel.screenWidth / 2 - (gamePanel.tileSize / 2);
        screenY = gamePanel.screenHeight / 2 - (gamePanel.tileSize / 2);

        hp = 5;    // Set default HP to 5
        speed = 5; // Set default speed to 5

        setDefaultValues();
        getPlayerImage();
        initializeVisitableTiles();
    }
    // Method to increase HP
    public void increaseHp(int amount) {
        hp += amount;
//        System.out.println("HP increased! Current HP: " + hp);
    }

    public void setDefaultValues() {
        worldX = gamePanel.tileSize * 23; // 23
        worldY = gamePanel.tileSize * 91; // 91
        speed = 5; // default speed
        direction = "down";
    }

    public void getPlayerImage() {
        try {
            up1 = loadImage("/res/player/tile004.png");
            up2 = loadImage("/res/player/tile005.png");
            up3 = loadImage("/res/player/tile006.png");
            down1 = loadImage("/res/player/tile000.png");
            down2 = loadImage("/res/player/tile001.png");
            down3 = loadImage("/res/player/tile002.png");
            left1 = loadImage("/res/player/tile012.png");
            left2 = loadImage("/res/player/tile013.png");
            left3 = loadImage("/res/player/tile014.png");
            right1 = loadImage("/res/player/tile008.png");
            right2 = loadImage("/res/player/tile009.png");
            right3 = loadImage("/res/player/tile010.png");

            hpImage = loadImage("/res/player/hp.png"); // Gambar untuk HP
            speedImage = loadImage("/res/player/speed.png"); // Gambar untuk kecepatan
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

    private void initializeVisitableTiles() {
        visitableTiles = Set.of(
                // Your existing visitable tiles
        );
    }

    public void update() {
        boolean moving = false;

        if (hp <= 0){
            gamePanel.checkGameOver();
            return;
        }

        if (keyHandler.upPressed) {
            if (canMove(worldX, worldY - speed)) {
                direction = "up";
                worldY -= speed;
                moving = true;
            }
        }
        if (keyHandler.downPressed) {
            if (canMove(worldX, worldY + speed)) {
                direction = "down";
                worldY += speed;
                moving = true;
            }
        }
        if (keyHandler.leftPressed) {
            if (canMove(worldX - speed, worldY)) {
                direction = "left";
                worldX -= speed;
                moving = true;
            }
        }
        if (keyHandler.rightPressed) {
            if (canMove(worldX + speed, worldY)) {
                direction = "right";
                worldX += speed;
                moving = true;
            }
        }

        if (moving) {
            // Check for HP increasing tile
//            int currentTileCode = gamePanel.getTileManager().getPlayerTileCode();
//            if (gamePanel.getTileManager().isHpIncreasingTile(currentTileCode)) {
//                increaseHp(10); // Increase HP by a specified amount
//            }
            spriteCounter++;
            if (spriteCounter > 10) {
                spriteNum++;
                if (spriteNum > 3) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        } else {
            spriteNum = 1; // Reset to default position when not moving
        }
    }

    private boolean canMove(int x, int y) {
        if (!restrictMovement) {
            return true;
        }
        int tileX = x / gamePanel.tileSize;
        int tileY = y / gamePanel.tileSize;
        int tileCode = gamePanel.getTileManager().mapTileNum[tileX][tileY];
        return gamePanel.getTileManager().isTileVisitable(tileCode);
    }

    // Method to decrease HP
    public void decreaseHp(int amount) {
        hp -= amount;
        if (hp < 0) hp = 0;
        // Add logic to handle player death if needed
    }

    // Method to decrease speed
    public void decreaseSpeed(int amount) {
        speed -= amount;
        if (speed < 1) speed = 1; // Minimum speed
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        switch (direction) {
            case "up":
                if (spriteNum == 1) {
                    image = up1;
                } else if (spriteNum == 2) {
                    image = up2;
                } else if (spriteNum == 3) {
                    image = up3;
                }
                break;
            case "down":
                if (spriteNum == 1) {
                    image = down1;
                } else if (spriteNum == 2) {
                    image = down2;
                } else if (spriteNum == 3) {
                    image = down3;
                }
                break;
            case "left":
                if (spriteNum == 1) {
                    image = left1;
                } else if (spriteNum == 2) {
                    image = left2;
                } else if (spriteNum == 3) {
                    image = left3;
                }
                break;
            case "right":
                if (spriteNum == 1) {
                    image = right1;
                } else if (spriteNum == 2) {
                    image = right2;
                } else if (spriteNum == 3) {
                    image = right3;
                }
                break;
        }

        int imgWidth = image.getWidth();
        int imgHeight = image.getHeight();
        float aspectRatio = (float) imgWidth / imgHeight;

        int newWidth = gamePanel.tileSize;
        int newHeight = (int) (gamePanel.tileSize / aspectRatio);

        g2.drawImage(image, screenX, screenY, newWidth, newHeight, null);

        // Draw HP and Speed representations
        drawStatus(g2);
    }

    private void drawStatus(Graphics2D g2) {
        int smallerTileSize = gamePanel.tileSize / 2; // Ukuran gambar yang lebih kecil dua kali lipat

        // Draw HP representation
        int hpX = 10;
        int hpY = 10;
        for (int i = 0; i < hp; i++) {
            g2.drawImage(hpImage, hpX + (i * smallerTileSize), hpY, smallerTileSize, smallerTileSize, null);
        }

        // Draw Speed representation
        int speedX = 10;
        int speedY = 10 + smallerTileSize + 10;
        for (int i = 0; i < speed; i++) {
            g2.drawImage(speedImage, speedX + (i * smallerTileSize), speedY, smallerTileSize, smallerTileSize, null);
        }
    }

    public void setRestrictMovement(boolean restrictMovement) {  // New method to enable/disable movement restriction
        this.restrictMovement = restrictMovement;
    }

    public boolean isRestrictMovement() {  // New method to check the current state of movement restriction
        return restrictMovement;
    }
}
