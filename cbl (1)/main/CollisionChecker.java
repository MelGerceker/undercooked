package main;

import entity.Entity;

/**
 * Handles collision detection between game entities and map tiles.
 * 
 * Determines whether an entity collides with solid tiles based on its current
 * direction of movement.
 */
public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    /**
     * Checks whether the specified entity is about to collide with a solid tile in
     * the given direction.
     *
     * @param entity    the entity to check
     * @param direction the direction of movement ("up", "down", "left", "right")
     * @return true if a collision is detected; false otherwise
     */
    public boolean checkTileCollision(Entity entity, String direction) {
        // Calculate entity's world boundaries
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        // Calculate column and row positions in the tile map
        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        int tileNum1;
        int tileNum2;

        boolean collisionDetected = false;

        // Determine the direction of movement and adjust rows/columns accordingly
        switch (direction) {
            case "up":
                // When player wants to go up, two different cases are checked for collision
                int entityTopRowUp = (entityTopWorldY - entity.speed) / gp.tileSize;

                if (entityLeftCol >= 0 && entityRightCol >= 0 && entityTopRowUp >= 0 &&
                        entityLeftCol < gp.maxWorldCol && entityRightCol < gp.maxWorldCol &&
                        entityTopRowUp < gp.maxWorldRow) {

                    tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRowUp];
                    tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRowUp];

                    collisionDetected = isTileCollidable(tileNum1) || isTileCollidable(tileNum2);

                }
                break;

            case "down":
                int entityBottomRowDown = (entityBottomWorldY + entity.speed) / gp.tileSize;
                if (entityBottomRowDown < gp.tileM.maxWorldRow &&
                        entityLeftCol >= 0 && entityLeftCol < gp.tileM.maxWorldCol &&
                        entityRightCol >= 0 && entityRightCol < gp.tileM.maxWorldCol) {

                    tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRowDown];
                    tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRowDown];
                    collisionDetected = gp.tileM.tile[tileNum1].collision
                            || gp.tileM.tile[tileNum2].collision;
                }
                break;

            case "left":
                int entityLeftColLeft = (entityLeftWorldX - entity.speed) / gp.tileSize;
                if (entityLeftColLeft >= 0 &&
                        entityTopRow >= 0 && entityTopRow < gp.tileM.maxWorldRow &&
                        entityBottomRow >= 0 && entityBottomRow < gp.tileM.maxWorldRow) {

                    tileNum1 = gp.tileM.mapTileNum[entityLeftColLeft][entityTopRow];
                    tileNum2 = gp.tileM.mapTileNum[entityLeftColLeft][entityBottomRow];
                    collisionDetected = gp.tileM.tile[tileNum1].collision
                            || gp.tileM.tile[tileNum2].collision;
                }
                break;

            case "right":
                int entityRightColRight = (entityRightWorldX + entity.speed) / gp.tileSize;
                if (entityRightColRight < gp.tileM.maxWorldCol &&
                        entityTopRow >= 0 && entityTopRow < gp.tileM.maxWorldRow &&
                        entityBottomRow >= 0 && entityBottomRow < gp.tileM.maxWorldRow) {

                    tileNum1 = gp.tileM.mapTileNum[entityRightColRight][entityTopRow];
                    tileNum2 = gp.tileM.mapTileNum[entityRightColRight][entityBottomRow];
                    collisionDetected = gp.tileM.tile[tileNum1].collision
                            || gp.tileM.tile[tileNum2].collision;
                }
                break;

            default:
                // No direction matched so no action
                break;
        }

        // Update the entity's collision status and return the result
        entity.collisionOn = collisionDetected;
        return collisionDetected;
    }

    // Original checkTile method retained for additional usage if needed
    /**
     * Shortcut method that uses the entity's current direction for collision
     * detection.
     *
     * @param entity the entity to check
     */
    public void checkTile(Entity entity) {
        // Reuse checkTileCollision to simplify this method
        checkTileCollision(entity, entity.direction);
    }

    /**
     * Checks whether the given tile index is valid and collidable.
     *
     * @param tileNum the index of the tile to check
     * @return true if the tile is collidable; false otherwise
     */
    private boolean isTileCollidable(int tileNum) {
        return tileNum >= 0
                && tileNum < gp.tileM.tile.length
                && gp.tileM.tile[tileNum] != null
                && gp.tileM.tile[tileNum].collision;
    }

}
