package com.spaceinvaders.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.spaceinvaders.game.screens.GameScreen;

import java.util.LinkedList;

public class ThreadHeart extends Thread {
    private LinkedList<Heart> hearts;
    private ShipPlayer player;
    private float posY;
    private float width;
    private float height;
    private TextureRegion textureHeart;
    private float movementSpeed;
    private float checkTimeFrequency;
    private float checkTimeLastTime;

    public ThreadHeart(LinkedList<Heart> hearts, ShipPlayer player, float posY, float width, float height, float movementSpeed, TextureRegion textureHeart, float checkTimeFrequency) {
        this.hearts = hearts;
        this.player = player;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.textureHeart = textureHeart;
        this.movementSpeed = movementSpeed;
        this.checkTimeFrequency = checkTimeFrequency;
        this.checkTimeLastTime = 0;
    }

    public void update(float deltaTime) {
        this.checkTimeLastTime += deltaTime;
    }

    @Override
    public void run() {
        while (!GameScreen.gameOver) {
            System.out.println(this.checkTimeLastTime);
            if (this.checkTimeLastTime > this.checkTimeFrequency) {
                if (this.player.getMaxLifes() > this.player.getLifes() && this.hearts.size() == 0) {
                    synchronized (this.hearts) {
                        this.hearts.add(new Heart(this.posY - 5,
                                this.width,
                                this.height,
                                this.textureHeart,
                                this.movementSpeed));
                    }
                }

                this.checkTimeLastTime = 0;
            }
        }
    }

    //region Getters & Setters
    public float getCheckTimeFrequency() {
        return checkTimeFrequency;
    }

    public void setCheckTimeFrequency(float checkTimeFrequency) {
        this.checkTimeFrequency = checkTimeFrequency;
    }

    public float getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(float movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public TextureRegion getTextureHeart() {
        return textureHeart;
    }

    public void setTextureHeart(TextureRegion textureHeart) {
        this.textureHeart = textureHeart;
    }

    public LinkedList<Heart> getHearts() {
        return hearts;
    }

    public void setHearts(LinkedList<Heart> hearts) {
        this.hearts = hearts;
    }

    public ShipPlayer getPlayer() {
        return player;
    }

    public void setPlayer(ShipPlayer player) {
        this.player = player;
    }
    //endregion
}
