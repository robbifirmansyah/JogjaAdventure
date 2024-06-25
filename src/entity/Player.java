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
        visitableTiles = Set.of(4703, 4702, 2233, 2188, 2417, 2416, 2418, 2373, 2372, 2371, 2321, 2276, 2240, 3088, 2141,
                2325, 2322, 2277, 2096, 2143, 2323, 2144, 2324, 2280, 2236, 2326, 2328, 2234, 2189, 4747, 4792,
                4834, 4879, 4701, 4700, 4699, 4698, 4697, 4696, 4695, 4694, 4745, 4744, 4743, 4742, 4741, 4740,
                4746, 4748, 4749, 4750, 4751, 4706, 4661, 4660, 4036, 2442, 2401, 2356, 2311, 2307, 2308, 2309,
                2310, 2402, 2403, 2358, 2357, 2443, 2352, 2397, 2315, 2314, 2329, 2269, 2150, 2445, 2444, 2319,
                2320, 2398, 2353, 2051, 2494, 2495, 2184, 2139, 2237, 2327, 4431, 4432, 2316, 2185, 2140, 2238,
                4433, 2369, 2230, 2229, 2274, 2275, 4427, 4428, 4429, 4430, 2331, 2376, 2421, 3311, 3312, 2183,
                2228, 4382, 4338, 4339, 4340, 4341, 4342, 4387, 4386, 4383, 4384, 4385, 2399, 2354, 2577, 2532,
                2487, 2425, 2380, 2335, 2147, 2948, 2330, 2151, 2488, 3251, 3252, 3253, 3209, 3206, 2337, 2336,
                2486, 2273, 2489, 2135, 2136, 2137, 2332, 2405, 2406, 2460, 4208, 4207, 4206, 4205, 4204, 4203,
                4202, 2426, 2381, 4073, 4072, 4071, 4070, 4069, 4068, 4067, 2377, 4023, 2415, 4022, 2370, 4024,
                4114, 4113, 4112, 2422, 4115, 4116, 4117, 4027, 4025, 4026, 4118, 2427, 2400, 2411, 2312, 2404,
                2412, 3749, 3750, 3751, 3752, 3753, 3754, 3799, 3798, 3800, 3801, 3802, 3803, 3804, 3805, 3806,
                3761, 3716, 3488, 3487, 3486, 3485, 3484, 3483, 3482, 2587, 2542, 3443, 3398, 3851, 3852, 2284,
                2193, 2192, 2194, 3343, 3342, 3297, 2148, 2949, 2903, 2902, 2901, 2900, 2899, 2898, 2897, 2896,
                2895, 2904, 2905, 2906, 2907, 2908, 2909, 2910, 2911, 2946, 3298 );
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