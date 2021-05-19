package com.spaceinvaders.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.spaceinvaders.game.screens.GameScreen;

import java.util.LinkedList;

public class ThreadShield extends Thread {
    private LinkedList<Shield> shields;
    private ShipPlayer player;
    private float posY;
    private float width;
    private float height;
    private TextureRegion textureShield;
    private float movementSpeed;
    private float checkTimeFrequency;
    private float checkTimeLastTime;

    public ThreadShield(LinkedList<Shield> shields, ShipPlayer player, float posY, float width, float height, float movementSpeed, TextureRegion textureShield, float checkTimeFrequency) {
        this.shields = shields;
        this.player = player;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.textureShield = textureShield;
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
                if (this.player.getShields() == 0 && this.shields.size() == 0) {
                    synchronized (this.shields) {
                        this.shields.add(new Shield(this.posY - 5,
                                this.width,
                                this.height,
                                this.textureShield,
                                this.movementSpeed));
                    }
                }

                this.checkTimeLastTime = 0;
            }
        }
    }

    //region Getters & Setters
    public LinkedList<Shield> getShields() {
        return shields;
    }

    public void setShields(LinkedList<Shield> shields) {
        this.shields = shields;
    }

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

    public TextureRegion getTextureShield() {
        return textureShield;
    }

    public void setTextureShield(TextureRegion textureShield) {
        this.textureShield = textureShield;
    }

    public LinkedList<Shield> getHearts() {
        return shields;
    }

    public void setHearts(LinkedList<Shield> shields) {
        this.shields = shields;
    }

    public ShipPlayer getPlayer() {
        return player;
    }

    public void setPlayer(ShipPlayer player) {
        this.player = player;
    }
    //endregion
}
