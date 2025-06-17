package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.Inventory;
import main.KeyHandler;
import object.SuperObject;

public class Player extends Entity {

    private GamePanel gp;
    private KeyHandler keyH;
    private Inventory inventory;
    public final int screenX;
    public final int screenY;
    private int speed;
    private Rectangle fullArea;

    private BufferedImage up1, down1, left1, right1;

    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp, keyH);
        this.gp = gp;
        this.keyH = keyH;
        this.inventory = gp.getInventory();

        this.screenX = gp.screenWidth;
        this.screenY = gp.screenHeight;

        solidArea = new Rectangle(8, 16, 32, 32); // Adjust this if collision boundaries are wrong
        fullArea = new Rectangle(worldX, worldY, gp.tileSize, gp.tileSize);

        setDefaultValues();
        getPlayerImage();
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setDefaultValues() {
        worldX = 100;
        worldY = 200;
        direction = "down";
    }

    public void getPlayerImage() {
        try {
            up1 = ImageIO.read(new File("assets/entity/chefUp1.png"));
            right1 = ImageIO.read(new File("assets/entity/chefRight1.png"));
            down1 = ImageIO.read(new File("assets/entity/chefUp1.png"));
            left1 = ImageIO.read(new File("assets/entity/chefLeft1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        boolean moved = false;

        int originalX = worldX;
        int originalY = worldY;

        if (keyH.upPressed) {
            direction = "up";
            worldY = originalY - speed;
            if (!gp.cChecker.checkTileCollision(this, "up")) {
                worldY = originalY - speed;
                moved = true;
            } else {
                worldY = originalY; // reset
            }
        }

        if (keyH.downPressed) {
            direction = "down";
            worldY = originalY + speed;
            if (!gp.cChecker.checkTileCollision(this, "down")) {
                worldY = originalY + speed;
                moved = true;
            } else {
                worldY = originalY;
            }
        }

        if (keyH.leftPressed) {
            direction = "left";
            worldX = originalX - speed;
            if (!gp.cChecker.checkTileCollision(this, "left")) {
                worldX = originalX - speed;
                moved = true;
            } else {
                worldX = originalX;
            }
        }

        if (keyH.rightPressed) {
            direction = "right";
            worldX = originalX + speed;
            if (!gp.cChecker.checkTileCollision(this, "right")) {
                worldX = originalX + speed;
                moved = true;
            } else {
                worldX = originalX;
            }
        }

        fullArea.x = worldX;
        fullArea.y = worldY;

        if (keyH.ePressed) {
            if (inventory.hasItem()) {
                gp.getPickUp().placeItem(this);
            } else {
                gp.getPickUp().checkForPickUp(this);
            }
            keyH.ePressed = false;
        }

        // Clamp
        if (worldX < 0)
            worldX = 0;
        if (worldY < 0)
            worldY = 0;

        int maxWorldX = gp.tileSize * gp.maxWorldCol - gp.tileSize;
        int maxWorldY = gp.tileSize * gp.maxWorldRow - gp.tileSize;

        if (worldX > maxWorldX)
            worldX = maxWorldX;
        if (worldY > maxWorldY)
            worldY = maxWorldY;

        //System.out.println("Moved: " + moved + ", Position: " + worldX + ", " + worldY);
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch (direction) {
            case "up":
                image = up1;
                break;
            case "down":
                image = down1;
                break;
            case "left":
                image = left1;
                break;
            case "right":
                image = right1;
                break;
        }

        //System.out.println("Drawing player facing: " + direction);
        g2.drawImage(image, worldX, worldY, gp.tileSize, gp.tileSize, null);
    }

    public void addItemToInventory(SuperObject item) {
        inventory.addItem(item);
    }

    public boolean intersects(SuperObject obj) {
        return this.fullArea.intersects(obj.getBounds());
    }
}
