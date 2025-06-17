package main;

import entity.Player;
import object.OBJ_Cola;
import object.OBJ_Doner;
import object.OBJ_Lettuce;
import object.OBJ_Tomato;
import object.OBJ_Wrap;
import object.SuperObject;

public class PickUp {

    private GamePanel gp;

    public PickUp(GamePanel gp) {
        this.gp = gp;
    }

    // Method to check if player can pick up any item
    public void checkForPickUp(Player player) {

        // Check if the player is within range.
        int range = 1;
        int playerTileX = player.worldX / gp.tileSize;
        int playerTileY = player.worldY / gp.tileSize;

        // System.out.print("playerx:" + playerTileX + "playery:" + playerTileY);

        // Loop over nearby tiles within the defined range
        for (int x = playerTileX - range; x <= playerTileX + range; x++) {
            for (int y = playerTileY - range; y <= playerTileY + 2 * range; y++) {
                // in the second for loop, the y range is increased to allow pickup from the
                // bottom row tiles
                if (x >= 0 && y >= 0 && x < gp.maxWorldCol && y < gp.maxWorldRow) {
                    if (gp.tileM.mapTileNum[x][y] == 2) {
                        gp.getInventory().addItem(new OBJ_Tomato());
                        return;
                    }
                    if (gp.tileM.mapTileNum[x][y] == 7) {
                        gp.getInventory().addItem(new OBJ_Cola());
                        return;
                    }
                    if (gp.tileM.mapTileNum[x][y] == 5) {
                        gp.getInventory().addItem(new OBJ_Wrap());
                        return;
                    }
                    if (gp.tileM.mapTileNum[x][y] == 3) {
                        gp.getInventory().addItem(new OBJ_Lettuce());
                        return;
                    }
                }
            }
        }

        for (int x = playerTileX - range; x <= playerTileX + range; x++) {
            for (int y = playerTileY - range; y <= playerTileY + range; y++) {
                // Ensure we’re within map bounds
                if (x >= 0 && y >= 0 && x < gp.maxWorldCol && y < gp.maxWorldRow) {
                    if (gp.tileM.mapTileNum[x][y] == 4) {
                        gp.getInventory().addItem(new OBJ_Doner());
                        return;
                    }
                }
            }
        }

        if (gp.getInventory().hasItem()) { // The inventory already has an item
            // so e is pressed to place item from inventory into the game world
            SuperObject item = gp.getInventory().removeItem();
            item.setPosition(player.worldX, player.worldY);
            // Place item at players location

            // add item to game world
            for (int i = 0; i < gp.obj.length; i++) {
                if (gp.obj[i] == null) {
                    gp.obj[i] = item;
                    break;
                }
            }
        } else { // inventory is empty
            // Original logic to pick up an item
            boolean foundItem = false;
            for (int i = 0; i < gp.obj.length; i++) {
                SuperObject obj = gp.obj[i];
                if (obj != null) {
                    if (Math.abs(player.worldX - obj.worldX) < gp.tileSize
                            && Math.abs(player.worldY - obj.worldY) < gp.tileSize) {
                        gp.getInventory().addItem(obj);
                        gp.obj[i] = null;
                        foundItem = true;
                        break;
                    } else {
                        // else statement for debugging
                    }
                }
            }
            if (!foundItem) {
                // This if statement is left here for possible debugging.
            }
        }
    }

    public void placeItem(Player player) {
        if (!gp.getInventory().hasItem()) {
            return; // exit if inventory is empty
        }

        SuperObject item = gp.getInventory().getItem();

        // Calculate position two tiles below the player
        int dropX = player.worldX;
        int dropY = player.worldY;

        // Place the item two tiles below the player's position
        item.setPosition(dropX, dropY);
        gp.getInventory().clearItem(); // Clear the item from inventory

        // Add item back into game world
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] == null) {
                gp.obj[i] = item;
                break;
            }
        }

        // Check if tile[8] is within range of the drop location
        int range = 1; // Define range as one tile around the dropped item
        int dropTileX = dropX / gp.tileSize;
        int dropTileY = dropY / gp.tileSize;

        for (int x = dropTileX - range; x <= dropTileX + range; x++) {
            for (int y = dropTileY - range; y <= dropTileY + range; y++) {
                // Ensure we’re within map bounds
                if (x >= 0 && y >= 0 && x < gp.maxWorldCol && y < gp.maxWorldRow) {
                    if (gp.tileM.mapTileNum[x][y] == 8) { // Check for tile[8]
                        // Change the item's position to the location of tile[8]
                        int targetX = x * gp.tileSize;
                        int targetY = y * gp.tileSize;
                        item.setPosition(targetX, targetY);
                        // Exit loop early after snapping the item to tile[8]
                        return;
                    } else if (gp.tileM.mapTileNum[x][y] == 9) { // New check for tile[9]
                        // Auto-place the item on top of tile[9]
                        int targetX = x * gp.tileSize;
                        int targetY = y * gp.tileSize;
                        item.setPosition(targetX, targetY);
                        return;
                    }
                }
            }
        }
    }

    public boolean isObjectOnTile8() {
        int tileSize = gp.tileSize;

        // Loop through all objects in the game world
        for (SuperObject obj : gp.obj) {
            if (obj != null) {
                // Calculate object's tile position
                int objTileX = obj.worldX / gp.tileSize;
                int objTileY = obj.worldY / gp.tileSize;

                // Check if the object's tile position corresponds to a tile[8]
                if (gp.tileM.mapTileNum[objTileX][objTileY] == 8) {
                    return true;
                } else {
                    // else statement for debugging
                }
            }
        }
        // If no objects are on tile[8], return false
        return false;
    }

}
