package main;

import object.OBJ_Doner;
import object.OBJ_Lettuce;
import object.OBJ_Tomato;
import object.OBJ_Wrap;
import object.OBJ_cutLettuce;
import object.OBJ_cutTomato;
import object.OBJ_donerl;
import object.OBJ_donerlt;
import object.OBJ_donert;
import object.OBJ_donerw;
import object.SuperObject;

/**
 * Handles logic for cutting items and creating dishes with multiple items.
 */
public class Recipe {

    private GamePanel gp;

    public Recipe(GamePanel gp) {
        this.gp = gp;
    }

    public void cuttingTomato() {
        int tileSize = gp.tileSize;

        // Loop through all objects in the game world
        for (int i = 0; i < gp.obj.length; i++) {
            SuperObject obj = gp.obj[i];

            if (obj != null) {
                // Calculate object's tile position
                int objTileX = obj.worldX / tileSize;
                int objTileY = obj.worldY / tileSize;

                // Check if the object is on tile[8] and is specifically an OBJ_Tomato
                if (gp.tileM.mapTileNum[objTileX][objTileY] == 9 && obj instanceof OBJ_Tomato) {
                    // Remove the OBJ_Tomato from the game
                    gp.obj[i] = null;

                    // Create a new OBJ_cutTomato and place it at the same position
                    OBJ_cutTomato cutTomato = new OBJ_cutTomato();
                    cutTomato.setPosition(obj.worldX, obj.worldY);
                    gp.obj[i] = cutTomato;
                    return; // Exit after replacement
                } else {
                    // This else statement is left here for possible debugging.
                }
            }
        }
    }

    public void cuttinglettuce() { // same as tomato
        int tileSize = gp.tileSize;
        for (int i = 0; i < gp.obj.length; i++) {
            SuperObject obj = gp.obj[i];
            if (obj != null) {
                int objTileX = obj.worldX / tileSize;
                int objTileY = obj.worldY / tileSize;
                if (gp.tileM.mapTileNum[objTileX][objTileY] == 9 && obj instanceof OBJ_Lettuce) {
                    gp.obj[i] = null;
                    OBJ_cutLettuce cutLettuce = new OBJ_cutLettuce();
                    cutLettuce.setPosition(obj.worldX, obj.worldY);
                    gp.obj[i] = cutLettuce;
                    return;
                } else {
                }
            }
        }
    }

    public void replaceItemsWithDonerWOnTile8() {
        // adding wrap and doner to make a doner-wrap item
        int tileSize = gp.tileSize;
        boolean foundDoner = false;
        boolean foundWrap = false;
        int donerIndex = -1;
        int wrapIndex = -1;
        int targetX = -1;
        int targetY = -1;

        // Loop through all objects in the game world to find OBJ_Doner and OBJ_Wrap on
        // tile[8]
        for (int i = 0; i < gp.obj.length; i++) {
            SuperObject obj = gp.obj[i];

            if (obj != null) {
                // Calculate object's tile position
                int objTileX = obj.worldX / tileSize;
                int objTileY = obj.worldY / tileSize;

                // Check if the object is on tile[8]
                if (gp.tileM.mapTileNum[objTileX][objTileY] == 8) {
                    if (obj instanceof OBJ_Doner) {
                        foundDoner = true;
                        donerIndex = i;
                        targetX = obj.worldX;
                        targetY = obj.worldY;
                    } else if (obj instanceof OBJ_Wrap) {
                        foundWrap = true;
                        wrapIndex = i;
                        targetX = obj.worldX;
                        targetY = obj.worldY;
                    }
                }
            }
        }

        // If both OBJ_Doner and OBJ_Wrap are found on tile[8], replace them with
        // OBJ_donerw
        if (foundDoner && foundWrap) {
            // Remove OBJ_Doner and OBJ_Wrap from the game world
            gp.obj[donerIndex] = null;
            gp.obj[wrapIndex] = null;

            // Create a new OBJ_donerw and place it at the target location
            OBJ_donerw donerw = new OBJ_donerw();
            donerw.setPosition(targetX, targetY);

            // Add the new OBJ_donerw to the game world in an available slot
            for (int i = 0; i < gp.obj.length; i++) {
                if (gp.obj[i] == null) {
                    gp.obj[i] = donerw;
                    break;
                }
            }

        } else {
            // This else statement is left here for possible debugging.
        }
    }

    // Same logic as replaceItemsWithDonerWOnTile8()
    public void replaceItemsWithDonerLT() {
        // Creates wraps with doner, tomato and/or lettuce
        int tileSize = gp.tileSize;
        boolean foundDoner = false;
        boolean foundWrap = false;
        boolean foundCutTomato = false;
        boolean foundCutLettuce = false;
        boolean foundDonerw = false;
        boolean foundDonerl = false;
        boolean foundDonert = false;

        int donerIndex = -1;
        int wrapIndex = -1;
        int CutTomatoIndex = -1;
        int CutLettuceIndex = -1;
        int donerwIndex = -1;
        int donerlIndex = -1;
        int donertIndex = -1;

        int targetX = -1;
        int targetY = -1;

        // Loop through all objects in the game world to find OBJ_Doner and OBJ_Wrap on
        for (int i = 0; i < gp.obj.length; i++) {
            SuperObject obj = gp.obj[i];
            if (obj != null) {
                // Calculate object's tile position
                int objTileX = obj.worldX / tileSize;
                int objTileY = obj.worldY / tileSize;
                // Check if the object is on tile[8]
                if (gp.tileM.mapTileNum[objTileX][objTileY] == 8) {
                    if (obj instanceof OBJ_Doner) {
                        foundDoner = true;
                        donerIndex = i;
                        targetX = obj.worldX;
                        targetY = obj.worldY;
                    } else if (obj instanceof OBJ_Wrap) {
                        foundWrap = true;
                        wrapIndex = i;
                        targetX = obj.worldX;
                        targetY = obj.worldY;
                    } else if (obj instanceof OBJ_cutTomato) {
                        foundCutTomato = true;
                        CutTomatoIndex = i;
                        targetX = obj.worldX;
                        targetY = obj.worldY;
                    } else if (obj instanceof OBJ_cutLettuce) {
                        foundCutLettuce = true;
                        CutLettuceIndex = i;
                        targetX = obj.worldX;
                        targetY = obj.worldY;
                    } else if (obj instanceof OBJ_donerw) {
                        foundDonerw = true;
                        donerwIndex = i;
                        targetX = obj.worldX;
                        targetY = obj.worldY;
                    } else if (obj instanceof OBJ_donerl) {
                        foundDonerl = true;
                        donerlIndex = i;
                        targetX = obj.worldX;
                        targetY = obj.worldY;
                    } else if (obj instanceof OBJ_donert) {
                        foundDonert = true;
                        donertIndex = i;
                        targetX = obj.worldX;
                        targetY = obj.worldY;
                    }
                }
            }
        }

        // Makes doner with tomatoes
        if (foundDonerw && foundCutTomato) {
            // Check if indices are valid before attempting to access gp.obj
            if (donerwIndex != -1 && CutTomatoIndex != -1) {

                // Remove OBJ_Doner and OBJ_Wrap from the game world
                gp.obj[donerwIndex] = null;
                gp.obj[CutTomatoIndex] = null;

                // Create a new OBJ_donert and place it at the target location
                OBJ_donert donert = new OBJ_donert();
                donert.setPosition(targetX, targetY);

                // Add the new OBJ_donert to the game world in an available slot
                for (int i = 0; i < gp.obj.length; i++) {
                    if (gp.obj[i] == null) {
                        gp.obj[i] = donert;
                        break;
                    }
                }
            }

        } else if (foundDonerw && foundCutLettuce) { // creates doner with lettuce
            if (donerwIndex != -1 && CutLettuceIndex != -1) {
                gp.obj[donerwIndex] = null;
                gp.obj[CutLettuceIndex] = null;
                OBJ_donerl donerl = new OBJ_donerl();
                donerl.setPosition(targetX, targetY);
                for (int i = 0; i < gp.obj.length; i++) {
                    if (gp.obj[i] == null) {
                        gp.obj[i] = donerl;
                        break;
                    }
                }

            }
        } else if (foundDonerl && foundCutTomato && donerlIndex != -1 && CutTomatoIndex != -1) {
            // creates doner with both tomatoes and lettuce
            gp.obj[donerlIndex] = null;
            gp.obj[CutTomatoIndex] = null;

            OBJ_donerlt donerlt = new OBJ_donerlt();
            donerlt.setPosition(targetX, targetY);
            for (int i = 0; i < gp.obj.length; i++) {
                if (gp.obj[i] == null) {
                    gp.obj[i] = donerlt;
                    break;
                }
            }

        } else if (foundDonert && foundCutLettuce && donertIndex != -1 && CutLettuceIndex != -1) {
            // creates doner with both tomatoes and lettuce
            gp.obj[donertIndex] = null;
            gp.obj[CutLettuceIndex] = null;

            OBJ_donerlt donerlt = new OBJ_donerlt();
            donerlt.setPosition(targetX, targetY);
            for (int i = 0; i < gp.obj.length; i++) {
                if (gp.obj[i] == null) {
                    gp.obj[i] = donerlt;
                    break;
                }
            }
        }
    }

}
