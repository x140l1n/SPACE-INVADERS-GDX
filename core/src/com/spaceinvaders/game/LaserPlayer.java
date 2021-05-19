package com.spaceinvaders.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import org.jetbrains.annotations.NotNull;

public class LaserPlayer extends Rectangle implements Laser, Cloneable {
    private TextureRegion texture;
    private float movementSpeed;
    private int direction;

    public LaserPlayer(TextureRegion texture, float posX, float posY, float width, float height, float movementSpeed, int direction) {
        super(posX, posY, width, height);

        this.texture = texture;
        this.movementSpeed = movementSpeed;
        this.direction = direction;
    }

    @Override
    public void draw(@NotNull Batch batch) {
        batch.draw(this.texture, super.getX(), super.getY(), super.getWidth(), super.getHeight());
    }

    @Override
    public void move(float deltaTime) {
        super.setPosition(super.getX(), super.getY() + this.movementSpeed * deltaTime * this.direction);
    }

    //region Getters & Setters
    public TextureRegion getTexture() {
        return texture;
    }

    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }

    public float getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(float movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    //endregion
}
