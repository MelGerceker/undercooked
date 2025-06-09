package main;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {

        // Creates new object
        // gp.obj[0] = new OBJ_Key();

        // Sets the coordinates of the object created
        // gp.obj[0].worldX = 0;
        // gp.obj[0].worldY = 2*gp.tileSize;

        // This class would be used for displaying objects in the map but currently all
        // stations and parts of the kitchen map are tiles.

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

        //System.out.println("Delivery tile set at: " + gp.deliveryX + ", " + gp.deliveryY);

    }
}
