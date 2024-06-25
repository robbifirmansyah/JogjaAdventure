package entity;

import java.awt.image.BufferedImage;

abstract class Entity {
    public int worldX, worldY;
    public int speed;

    public BufferedImage up, up1, up2, up3, down, down1, down2, down3, left, left1, left2, left3, right, right1, right2, right3;
    public String direction;

}