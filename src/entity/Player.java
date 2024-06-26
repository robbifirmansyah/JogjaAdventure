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
    private boolean restrictMovement = true;  // New field to control movement restriction

    private BufferedImage up1, up2, up3, down1, down2, down3;
    private BufferedImage left1, left2, left3, right1, right2, right3;
    public final int screenX;
    public final int screenY;
    private Set<Integer> visitableTiles;
    private int hp;

    public Player(GamePanel gamePanel, KeyHandler keyHandler) {
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        screenX = gamePanel.screenWidth / 2 - (gamePanel.tileSize / 2);
        screenY = gamePanel.screenHeight / 2 - (gamePanel.tileSize / 2);
        hp = 100;

        setDefaultValues();
        getPlayerImage();
        initializeVisitableTiles();
        setRestrictMovement(true);
    }
    // Method to increase HP
    public void increaseHp(int amount) {
        hp += amount;
//        System.out.println("HP increased! Current HP: " + hp);
    }

    public void setDefaultValues() {
        worldX = gamePanel.tileSize * 23; // 23
        worldY = gamePanel.tileSize * 91; // 91
        speed = 4;
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
                2051, 2135, 2136, 2137, 2139, 2141, 2143, 2144,
                2147, 2148, 2150, 2151, 2184, 2188, 2189, 2192,
                2193, 2194, 2229, 2233, 2234, 2236, 2237, 2238,
                2274, 2275, 2276, 2277, 2280, 2284, 2311, 2307, 2308, 2312, 2314,
                2315, 2316, 2319, 2320, 2321, 2322, 2323, 2324, 2325, 2326,
                2327, 2328, 2329, 2330, 2331, 2332, 2335, 2336, 2337, 2352, 2356,
                2372, 2376, 2380, 2381, 2397,
                2401, 2402, 2403,
                2417, 2421, 2422, 2425, 2426, 2427, 2442,
                2444, 2445, 2460, 2486, 2487, 2488, 2489, 2494, 2495, 2532,
                2542, 2577, 2587, 2900, 2901, 2902, 2903, 2904, 2905, 2906,
                2907, 2908, 2909, 2910, 2911, 2946, 2948, 2949, 3206,
                3209, 3251, 3252, 3253, 3297, 3298, 3311, 3312, 3342, 3343,
                3398, 3443, 3482, 3483, 3484, 3485, 3486, 3487, 3488, 3716,
                3749, 3750, 3751, 3752, 3753, 3754, 3761, 3798, 3799, 3800,
                3801, 3802, 3803, 3804, 3805, 3806, 3851, 3852, 4022, 4023,
                4024, 4025, 4026, 4027, 4067, 4068, 4069, 4070, 4071, 4072,
                4073, 4112, 4113, 4114, 4115, 4116, 4117, 4118, 4202, 4203,
                4204, 4205, 4206, 4207, 4208, 4338, 4339, 4340, 4341, 4342,
                4382, 4383, 4384, 4385, 4386, 4387, 4427, 4428, 4429, 4430,
                4431, 4432, 4433, 4660, 4661, 4694, 4695, 4696, 4697, 4698,
                4699, 4700, 4701, 4702, 4703, 4706, 4740, 4741, 4742, 4743,
                4744, 4745, 4746, 4747, 4748, 4749, 4750, 4751, 4792, 4834,
                4879
        );
    }

    public void update() {

        boolean moving = false;

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
            int currentTileCode = gamePanel.getTileManager().getPlayerTileCode();
            if (gamePanel.getTileManager().isHpIncreasingTile(currentTileCode)) {
                increaseHp(10); // Increase HP by a specified amount
            }
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
    }

    public void setRestrictMovement(boolean restrictMovement) {  // New method to enable/disable movement restriction
        this.restrictMovement = restrictMovement;
    }

    public boolean isRestrictMovement() {  // New method to check the current state of movement restriction
        return restrictMovement;
    }
}
