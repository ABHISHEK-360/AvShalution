package com.abhishek360.dev;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import static com.abhishek360.dev.FloorSimulation.*;

public class Shapes {
    public static int elevation = 350;
    String name, image;
    float height, width;
    Texture shapeTexture;
    Sprite shapeSprite;
    int arr[] = new int[ROWS * COLUMNS];

    public Shapes(String name, String image, int tilesY, int tilesX, float posX, float posY) {
        this.name = name;
        this.image = image;
        shapeTexture = new Texture(image);
        this.height = TILE_HEIGHT * tilesY;
        this.width = TILE_WIDTH * tilesX;
        shapeSprite = new Sprite(shapeTexture);
        shapeSprite.setSize(TILE_WIDTH * tilesX - 10, TILE_HEIGHT * tilesY - 10);
        shapeSprite.setPosition(posX, posY);
        shapeSprite.setAlpha(0.5f);

        int k = 0;
        for (int i = 0; i < tilesX; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (j < tilesY) {
                    arr[k] = elevation;
                }
                k++;
            }
            k--;
        }
    }

    public void setPosition(float posX, float posY) {
        shapeSprite.setPosition(posX, posY);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public int[] getArr() {
        return arr;
    }

    public void setArr(int[] arr) {
        this.arr = arr;
    }
}
