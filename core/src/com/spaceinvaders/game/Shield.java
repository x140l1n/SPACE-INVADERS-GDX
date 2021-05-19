package com.spaceinvaders.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.spaceinvaders.game.screens.GameScreen;

import org.jetbrains.annotations.NotNull;

public class Shield extends Rectangle
{
    private TextureRegion texture;
    private float movementSpeed;

    public Shield(float y, float width, float height, TextureRegion texture, float movementSpeed) {
        super(GameScreen.RANDOM.nextFloat() * (GameScreen.SCREEN_WIDTH - width) + width / 2, y, width, height);
        this.texture = texture;
        this.movementSpeed = movementSpeed;
    }

    public void draw(@NotNull Batch batch) {
        batch.draw(this.texture, super.getX(), super.getY(), super.getWidth(), super.getHeight());
    }

    public void move(float deltaTime) {
        super.setPosition(super.getX(), super.getY() + this.movementSpeed * deltaTime * -1);
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
    //endregion
}
