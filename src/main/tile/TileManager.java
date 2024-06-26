package main.tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import entity.Player;
import entity.Enemy;

public class TileManager {
    private final Set<Integer> hpIncreasingTiles = Set.of(
            2233, 2188, 2417, 2416, 2418, 2373
            // Add more tile codes as needed
    );

    // Check if a tile code increases HP
    public boolean isHpIncreasingTile(int tileCode) {
        return hpIncreasingTiles.contains(tileCode);
    }

    GamePanel gp;
    Tile[] tile;
    public int[][] mapTileNum;
    int tileSize = 16; // Size of each tile in pixels
    HashSet<Integer> tilesVisited; // Set to keep track of visited tile codes

    // Set of valid tile codes
    private final Set<Integer> validTileCodes = Set.of(
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

    public TileManager(GamePanel gp) {
        this.gp = gp;
        getTileImage();
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        loadMap("/res/maps/Maps siap input.csv");
        tilesVisited = new HashSet<>(); // Initialize the set
    }

    public void getTileImage() {
        try {
            BufferedImage tileSetImage = ImageIO.read(getClass().getResourceAsStream("/res/maps/asetbbaru.png"));

            int cols = tileSetImage.getWidth() / tileSize;
            int rows = tileSetImage.getHeight() / tileSize;
            int totalTiles = cols * rows;

            tile = new Tile[totalTiles]; // Dynamically adjust the size of the tile array

            int tileNum = 0;
            for (int y = 0; y < rows; y++) {
                for (int x = 0; x < cols; x++) {
                    tile[tileNum] = new Tile();
                    tile[tileNum].image = tileSetImage.getSubimage(x * tileSize, y * tileSize, tileSize, tileSize);
                    tileNum++;
                }
            }
            System.out.println("Total tiles loaded: " + totalTiles);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filePath)));
            int col = 0;
            int row = 0;

            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
                String line = br.readLine();
                if (line == null) break;
                String[] numbers = line.split(",");

                while (col < gp.maxWorldCol) {
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                    col++;
                }
                if (col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();

            // Debug: Print the mapTileNum array
            System.out.println("Map loaded:");
            for (int r = 0; r < gp.maxWorldRow; r++) {
                for (int c = 0; c < gp.maxWorldCol; c++) {
                    System.out.print(mapTileNum[c][r] + " ");
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to get the tile code from the CSV file based on the player's position
    public int getPlayerTileCode() {
        int playerTileX = gp.player.worldX / gp.tileSize;
        int playerTileY = gp.player.worldY / gp.tileSize;
        return mapTileNum[playerTileX][playerTileY];
    }

    // Method to update the set of visited tiles and print tile codes
    // Variable to store the previous tile code
    private int previousTileCode = -1;

    public void updateTilesVisited() {
        int playerTileX = gp.player.worldX / gp.tileSize;
        int playerTileY = gp.player.worldY / gp.tileSize;
        int tileCode = mapTileNum[playerTileX][playerTileY];

        // Print the tile code only if the player visits a different tile
        if (tileCode != previousTileCode) {
            System.out.println("Tile code visited: " + tileCode);
            previousTileCode = tileCode;
        }

        // Add the tile code to the set of visited tiles if not already present
        if (!tilesVisited.contains(tileCode)) {
            tilesVisited.add(tileCode);
        }
    }



    // Check if a tile code is valid
    public boolean isTileVisitable(int tileCode) {
        return validTileCodes.contains(tileCode);
    }

    public void draw(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;

        while (worldRow < gp.maxWorldRow) {
            int tileNum = mapTileNum[worldCol][worldRow];
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (tileNum >= 0 && tileNum < tile.length && tile[tileNum] != null) {
                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            } else {
                System.out.println("Invalid tile number: " + tileNum + " at (" + worldCol + "," + worldRow + ")");
            }

            worldCol++;
            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }

        // Update and print the tiles visited by the player
        updateTilesVisited();
    }
}