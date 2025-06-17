package main.tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import main.GamePanel;
import object.SuperObject;

/**
 * Manages the game's tile map by loading tile images and reading the map layout
 * from map.txt
 * Also handles drawing tiles and checking item placement within a defined
 * target area.
 */
public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];

    public int maxWorldCol;
    public int maxWorldRow;

    // Coordinates for checking if the item is placed correctly
    private final int TARGET_X = 144;
    private final int TARGET_Y = 40;
    private final int DEFLECTION = 20;

    /**
     * Constructs a TileManager and initializes tile data and map layout.
     * 
     * @param gp the GamePanel that provides configuration values
     */
    public TileManager(GamePanel gp) {
        this.gp = gp;

        this.maxWorldCol = gp.maxWorldCol;
        this.maxWorldRow = gp.maxWorldRow;

        tile = new Tile[20];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap();
    }

    /**
     * Loads tile images and configures collision settings for each tile type.
     * Images are loaded from the assets folder and assigned to their respective
     * indices.
     */
    public void getTileImage() {
        try {
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(new File("assets/tiles/floor1.png"));

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(new File("assets/tiles/table.png"));
            tile[1].collision = true;

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(new File("assets/tiles/tomato.png"));
            tile[2].collision = true;

            tile[3] = new Tile();
            tile[3].image = ImageIO.read(new File("assets/tiles/lettuce.png"));
            tile[3].collision = true;

            tile[4] = new Tile();
            tile[4].image = ImageIO.read(new File("assets/tiles/doner.png"));
            tile[4].collision = true;

            tile[5] = new Tile();
            tile[5].image = ImageIO.read(new File("assets/tiles/wrap.png"));
            tile[5].collision = true;

            tile[7] = new Tile();
            tile[7].image = ImageIO.read(new File("assets/tiles/cola.png"));
            tile[7].collision = true;

            tile[6] = new Tile();
            tile[6].image = ImageIO.read(new File("assets/tiles/delivery.png"));
            tile[6].collision = true;

            tile[8] = new Tile();
            tile[8].image = ImageIO.read(new File("assets/tiles/station.png"));
            tile[8].collision = true;

            tile[9] = new Tile();
            tile[9].image = ImageIO.read(new File("assets/tiles/cutting.png"));
            tile[9].collision = true;

            tile[10] = new Tile();
            tile[10].image = ImageIO.read(new File("assets/tiles/tile02.png"));
            tile[10].collision = true;

            tile[11] = new Tile();
            tile[11].image = ImageIO.read(new File("assets/tiles/table000.png"));
            tile[11].collision = true;

            tile[12] = new Tile();
            tile[12].image = ImageIO.read(new File("assets/tiles/table001.png"));
            tile[12].collision = true;

            tile[13] = new Tile();
            tile[13].image = ImageIO.read(new File("assets/tiles/table002.png"));
            tile[13].collision = true;

            tile[14] = new Tile();
            tile[14].image = ImageIO.read(new File("assets/tiles/table003.png"));
            tile[14].collision = true;

            tile[15] = new Tile();
            tile[15].image = ImageIO.read(new File("assets/tiles/table004.png"));
            tile[15].collision = true;

            tile[16] = new Tile();
            tile[16].image = ImageIO.read(new File("assets/tiles/table005.png"));
            tile[16].collision = true;

            tile[17] = new Tile();
            tile[17].image = ImageIO.read(new File("assets/tiles/table006.png"));
            tile[17].collision = true;

            tile[18] = new Tile();
            tile[18].image = ImageIO.read(new File("assets/tiles/table007.png"));
            tile[18].collision = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the map layout from a text file, populating the `mapTileNum` array.
     * Each line in the file represents a row of tile indices, separated by spaces.
     */
    public void loadMap() {
        try {
            InputStream is = new FileInputStream(new File("assets/map.txt"));
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            int col = 0;
            int row = 0;

            while ((col < gp.maxWorldCol) && (row < gp.maxWorldRow)) {
                String line = br.readLine();

                while (col < gp.maxWorldCol) {
                    String[] numbers = line.split(" ");
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Draws all the tiles on the screen based on the current map configuration.
     *
     * @param g2 the Graphics2D object used for drawing
     */
    public void draw(Graphics2D g2) {
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        while ((col < gp.maxScreenCol) && (row < gp.maxScreenRow)) {
            int tileNum = mapTileNum[col][row];

            if (tileNum >= 0 && tileNum < tile.length && tile[tileNum] != null
                    && tile[tileNum].image != null) {
                g2.drawImage(tile[tileNum].image, x, y, gp.tileSize, gp.tileSize, null);
            }
            // g2.drawImage(tile[tileNum].image, x, y, gp.tileSize, gp.tileSize, null);
            col++;
            x += gp.tileSize;

            if (col == gp.maxScreenCol) {
                col = 0;
                x = 0;
                row++;
                y += gp.tileSize;
            }
        }
    }

    /**
     * Checks whether a given item is within the defined target area,
     * accounting for a small deflection range.
     *
     * @param item the object to check
     * @return true if the item is within the target area; false otherwise
     */
    public boolean isWithinTargetArea(SuperObject item) {
        int itemX = item.getX(); // Assuming the item has getX() for x-coordinate
        int itemY = item.getY(); // Assuming the item has getY() for y-coordinate

        // Check if the item is within the target area with a deflection of 20
        return (itemX >= TARGET_X - DEFLECTION && itemX <= TARGET_X + DEFLECTION) &&
                (itemY >= TARGET_Y - DEFLECTION && itemY <= TARGET_Y + DEFLECTION);
    }
}
