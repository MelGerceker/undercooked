package main;

/**
 * Responsible for placing objects into the game world, such as setting delivery
 * tile coordinates.
 * 
 * Although initially designed for placing interactive objects, this class
 * currently
 * focuses on identifying special tile types (like delivery zones) and updating
 * relevant coordinates.
 */
public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    /**
     * Identifies and sets the world coordinates of special objects in the game map.
     * 
     * Currently, this method searches the map for the delivery tile (tile ID 6) and
     * updates {@code gp.deliveryX} and {@code gp.deliveryY} accordingly.
     * 
     */
    public void setObject() {

        // Find delivery tile and set coords
        for (int row = 0; row < gp.maxWorldRow; row++) {
            for (int col = 0; col < gp.maxWorldCol; col++) {
                int tileNum = gp.tileM.mapTileNum[col][row];
                if (tileNum == 6) { // 6 represents the delivery tile
                    gp.deliveryX = col * gp.tileSize;
                    gp.deliveryY = row * gp.tileSize;
                }
            }
        }

        // System.out.println("Delivery tile set at: " + gp.deliveryX + ", " +
        // gp.deliveryY);

    }
}
