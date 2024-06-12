package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {
    GamePanel gamePanel;
    KeyHandler keyHandler;

    private int spriteCounter = 0;
    private int spriteNum = 1;

    public Player(GamePanel gamePanel, KeyHandler keyHandler) {
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        x = 100;
        y = 100;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage() {
        try {
            up = loadImage("/res/player/tile004.png");
            up1 = loadImage("/res/player/tile005.png");
            up2 = loadImage("/res/player/tile006.png");
            up3 = loadImage("/res/player/tile007.png");
            left = loadImage("/res/player/tile012.png");
            left1 = loadImage("/res/player/tile013.png");
            left2 = loadImage("/res/player/tile014.png");
            left3 = loadImage("/res/player/tile015.png");
            right = loadImage("/res/player/tile008.png");
            right1 = loadImage("/res/player/tile009.png");
            right2 = loadImage("/res/player/tile010.png");
            right3 = loadImage("/res/player/tile011.png");
            down = loadImage("/res/player/tile000.png");
            down1 = loadImage("/res/player/tile001.png");
            down2 = loadImage("/res/player/tile002.png");
            down3 = loadImage("/res/player/tile003.png");
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
        boolean moving = false;

        if (keyHandler.upPressed) {
            direction = "up";
            y -= speed;
            moving = true;
        }
        if (keyHandler.downPressed) {
            direction = "down";
            y += speed;
            moving = true;
        }
        if (keyHandler.leftPressed) {
            direction = "left";
            x -= speed;
            moving = true;
        }
        if (keyHandler.rightPressed) {
            direction = "right";
            x += speed;
            moving = true;
        }

        if (moving) {
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

        g2.drawImage(image, x, y, newWidth, newHeight, null);
    }
}
