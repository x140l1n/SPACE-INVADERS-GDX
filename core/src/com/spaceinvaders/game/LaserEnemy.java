package com.spaceinvaders.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.spaceinvaders.game.screens.GameScreen;

import org.jetbrains.annotations.NotNull;


public class LaserEnemy extends Rectangle implements Laser, Cloneable {
    private TextureRegion texture;
    private float movementSpeed;
    private int direction;
    private boolean shotTrackPlayer;
    private ShipPlayer player;

    public LaserEnemy(TextureRegion texture, float posX, float posY, float width, float height, float movementSpeed, int direction, boolean shotTrackPlayer, ShipPlayer player) {
        super(posX, posY, width, height);

        this.texture = texture;
        this.movementSpeed = movementSpeed;
        this.direction = direction;
        this.shotTrackPlayer = shotTrackPlayer;
        this.player = player;
    }

    @Override
    public void draw(@NotNull Batch batch) {
        batch.draw(this.texture, super.getX(), super.getY(), super.getWidth(), super.getHeight());
    }

    @Override
    public void move(float deltaTime) {
        if (this.shotTrackPlayer) {
            float xLaser = super.getX();
            float yLaser = super.getY();

            yLaser = GameScreen.SCREEN_HEIGHT - yLaser;

            Vector2 pointOrigin = new Vector2(xLaser, yLaser);
            Vector2 pointCenterShip = new Vector2(this.player.getX() + this.player.getWidth() / 2, (GameScreen.SCREEN_HEIGHT - this.player.getY()) + this.player.getHeight() / 2);

            float distance = pointOrigin.dst(pointCenterShip);

            if (distance >= 0) {
                float xDifference = pointCenterShip.x - pointOrigin.x;
                float yDifference = pointCenterShip.y - pointOrigin.y;

                float xMove = xDifference / distance * this.movementSpeed * deltaTime;
                float yMove = yDifference / distance * this.movementSpeed * deltaTime;

                if (yMove < (GameScreen.SCREEN_HEIGHT - this.player.getY()) + this.player.getHeight()) {
                    yMove = this.movementSpeed * deltaTime;
                }

                this.translateLaser(xMove, yMove * this.direction);
            }
        } else {
            translateLaser(0, this.movementSpeed * deltaTime * this.direction);
        }
    }

    private void translateLaser(float x, float y) {
        super.setPosition(super.getX() + x, super.getY() + y);
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

    public boolean isShotTrackPlayer() {
        return shotTrackPlayer;
    }

    public void setShotTrackPlayer(boolean shotTrackPlayer) {
        this.shotTrackPlayer = shotTrackPlayer;
    }

    public ShipPlayer getPlayer() {
        return player;
    }

    public void setPlayer(ShipPlayer player) {
        this.player = player;
    }
    //endregion
}