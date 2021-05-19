package com.spaceinvaders.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.spaceinvaders.game.screens.GameScreen;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

public class ShipPlayer extends Rectangle implements Ship, Runnable {
    private TextureRegion shipTexture;
    private TextureRegion shipShieldTexture;
    private TextureRegion laserTexture;
    private LinkedList<LaserPlayer> lasersPlayer;
    private float movementSpeed;
    private float widthLaser;
    private float heightLaser;
    private float movementSpeedLaser;
    private String namePlayer;
    private int score;
    private int shields;
    private int lifes;
    private int maxLifes;
    private LinkedList<Explosion> explosions;
    private boolean immortal;

    public ShipPlayer(TextureRegion shipTexture,
                      TextureRegion shipShieldTexture,
                      TextureRegion laserTexture,
                      float posX,
                      float posY,
                      float width,
                      float height,
                      float movementSpeed,
                      float widthLaser,
                      float heightLaser,
                      float movementSpeedLaser,
                      String namePlayer,
                      int lifes,
                      LinkedList<Explosion> explosions,
                      boolean immortal) {
        super(posX, posY, width, height);

        this.shipTexture = shipTexture;
        this.shipShieldTexture = shipShieldTexture;
        this.laserTexture = laserTexture;
        this.lasersPlayer = new LinkedList<>();
        this.movementSpeed = movementSpeed;
        this.widthLaser = widthLaser;
        this.heightLaser = heightLaser;
        this.movementSpeedLaser = movementSpeedLaser;
        this.namePlayer = namePlayer;
        this.score = 0;
        this.shields = 0;
        this.lifes = lifes;
        this.maxLifes = lifes;
        this.explosions = explosions;
        this.immortal = immortal;
    }

    @Override
    public void draw(@NotNull Batch batch) {
        if (this.shields == 0)
            batch.draw(this.shipTexture, super.getX(), super.getY(), super.getWidth(), super.getHeight());
        else
            batch.draw(this.shipShieldTexture, super.getX(), super.getY(), super.getWidth(), super.getHeight());
    }

    @Override
    public void move(float deltaTime) {
        float leftLimit = -super.getX();
        float bottomLimit = -super.getY();
        float rightLimit = GameScreen.SCREEN_WIDTH - super.getX() - super.getWidth();
        float topLimit = GameScreen.SCREEN_HEIGHT / 2 - super.getY() - super.getHeight();

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && rightLimit > 0) {
            this.translateShip(Math.min(this.movementSpeed * deltaTime, rightLimit), 0f);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) && topLimit > 0) {
            this.translateShip(0f, Math.min(this.movementSpeed * deltaTime, topLimit));
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && leftLimit < 0) {
            this.translateShip(Math.max(-this.movementSpeed * deltaTime, leftLimit), 0f);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && bottomLimit < 0) {
            this.translateShip(0f, Math.max(-this.movementSpeed * deltaTime, bottomLimit));
        }

        if (Gdx.input.isTouched() && Gdx.app.getType() == Application.ApplicationType.Android) {
            float xTouched = Gdx.input.getX();
            float yTouched = Gdx.input.getY();

            yTouched = GameScreen.SCREEN_HEIGHT - yTouched;

            Vector2 pointDest = new Vector2(xTouched, yTouched);
            Vector2 pointCenterShip = new Vector2(super.getX() + super.getWidth() / 2, super.getY() + super.getHeight() / 2);

            float distance = pointDest.dst(pointCenterShip);

            if (distance > 10) {
                float xDifference = pointDest.x - pointCenterShip.x;
                float yDifference = pointDest.y - pointCenterShip.y;

                float xMove = xDifference / distance * this.movementSpeed * deltaTime;
                float yMove = yDifference / distance * this.movementSpeed * deltaTime;

                if (xMove > 0) xMove = Math.min(xMove, rightLimit);
                else xMove = Math.max(xMove, leftLimit);

                if (yMove > 0) yMove = Math.min(yMove, topLimit);
                else yMove = Math.max(yMove, bottomLimit);

                this.translateShip(xMove, yMove);
            }
        }
    }

    private void translateShip(float x, float y) {
        super.setPosition(super.getX() + x, super.getY() + y);
    }

    @Override
    public void shoot() {
        lasersPlayer.add(new LaserPlayer(this.laserTexture, (super.getX() + super.getWidth() / 2) - 16 * Gdx.graphics.getDensity(), super.getY() + 15 * Gdx.graphics.getDensity(), this.widthLaser * Gdx.graphics.getDensity(), this.heightLaser * Gdx.graphics.getDensity(), this.movementSpeedLaser * Gdx.graphics.getDensity(), 1));
        lasersPlayer.add(new LaserPlayer(this.laserTexture, (super.getX() + super.getWidth() / 2) + 10 * Gdx.graphics.getDensity(), super.getY() + 15 * Gdx.graphics.getDensity(), this.widthLaser * Gdx.graphics.getDensity(), this.heightLaser * Gdx.graphics.getDensity(), this.movementSpeedLaser * Gdx.graphics.getDensity(), 1));
    }

    @Override
    public void die() {
        this.lifes--;
        GameScreen.soundExplosion.play();
        this.explosions.add(new Explosion(this, 2F));
        Gdx.input.vibrate(200);
    }

    @Override
    public void run() {
        try {
            Thread.sleep(3000);
            this.setImmortal(false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //region Getters & Setters
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public TextureRegion getShipShieldTexture() {
        return shipShieldTexture;
    }

    public void setShipShieldTexture(TextureRegion shipShieldTexture) {
        this.shipShieldTexture = shipShieldTexture;
    }

    public int getShields() {
        return shields;
    }

    public void setShields(int shields) {
        this.shields = shields;
    }

    public int getMaxLifes() {
        return maxLifes;
    }

    public void setMaxLifes(int maxLifes) {
        this.maxLifes = maxLifes;
    }

    public LinkedList<Explosion> getExplosions() {
        return explosions;
    }

    public void setExplosions(LinkedList<Explosion> explosions) {
        this.explosions = explosions;
    }

    public float getWidthLaser() {
        return widthLaser;
    }

    public void setWidthLaser(float widthLaser) {
        this.widthLaser = widthLaser;
    }

    public float getHeightLaser() {
        return heightLaser;
    }

    public void setHeightLaser(float heightLaser) {
        this.heightLaser = heightLaser;
    }

    public float getMovementSpeedLaser() {
        return movementSpeedLaser;
    }

    public void setMovementSpeedLaser(float movementSpeedLaser) {
        this.movementSpeedLaser = movementSpeedLaser;
    }

    public float getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(float movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public TextureRegion getShipTexture() {
        return shipTexture;
    }

    public void setShipTexture(TextureRegion shipTexture) {
        this.shipTexture = shipTexture;
    }

    public TextureRegion getLaserTexture() {
        return laserTexture;
    }

    public void setLaserTexture(TextureRegion laserTexture) {
        this.laserTexture = laserTexture;
    }

    public LinkedList<LaserPlayer> getLasersPlayer() {
        return lasersPlayer;
    }

    public void setLasersPlayer(LinkedList<LaserPlayer> lasersPlayer) {
        this.lasersPlayer = lasersPlayer;
    }

    public String getNamePlayer() {
        return namePlayer;
    }

    public void setNamePlayer(String namePlayer) {
        this.namePlayer = namePlayer;
    }

    public int getLifes() {
        return lifes;
    }

    public void setLifes(int lifes) {
        this.lifes = lifes;
    }

    public boolean isImmortal() {
        return immortal;
    }

    public void setImmortal(boolean immortal) {
        this.immortal = immortal;
    }
    //endregion
}
