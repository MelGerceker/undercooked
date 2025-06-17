package main.tile;

import java.awt.image.BufferedImage;

/**
 * Represents a single tile in the game world, holding its image
 * and whether it blocks movement (collision).
 */
public class Tile {
    public BufferedImage image;
    public boolean collision = false;
}
