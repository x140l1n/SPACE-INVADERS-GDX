package com.spaceinvaders.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.spaceinvaders.game.screens.GameScreen;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

public class ShipEnemy extends Rectangle implements Ship, Cloneable {
    private TextureRegion shipTexture;
    private Vector2 directionMovement;
    private float timeSinceLastDirectionChange;
    private float directionChangeFrequency;
    private TextureRegion laserTexture;
    private LinkedList<LaserEnemy> laserEnemies;
    private float movementSpeed;
    private boolean shotTrackPlayer;
    private float timeBetweenShot;
    private float timeSinceLastShot;
    private float widthLaser;
    private float heightLaser;
    private float movementSpeedLaser;
    private int lifes;
    private ShipPlayer player;
    private LinkedList<Explosion> explosions;

    public ShipEnemy(TextureRegion shipTexture,
                     TextureRegion laserTexture,
                     LinkedList<LaserEnemy> lasersEnemies,
                     float posY,
                     float width,
                     float height,
                     float movementSpeed,
                     boolean shotTrackPlayer,
                     float timeBetweenShot,
                     float widthLaser,
                     float heightLaser,
                     float movementSpeedLaser,
                     int lifes,
                     ShipPlayer player,
                     LinkedList<Explosion> explosions,
                     float directionChangeFrequency) {
        super(GameScreen.RANDOM.nextFloat() * (GameScreen.SCREEN_WIDTH - width) + width / 2, posY, width, height);

        this.shipTexture = shipTexture;
        this.laserTexture = laserTexture;
        this.laserEnemies = lasersEnemies;
        this.movementSpeed = movementSpeed;
        this.shotTrackPlayer = shotTrackPlayer;
        this.timeBetweenShot = timeBetweenShot;
        this.timeSinceLastShot = 0;
        this.widthLaser = widthLaser;
        this.heightLaser = heightLaser;
        this.movementSpeedLaser = movementSpeedLaser;
        this.lifes = lifes;
        this.player = player;
        this.explosions = explosions;
        this.directionChangeFrequency = directionChangeFrequency;
        this.timeSinceLastDirectionChange = 0;
        this.directionMovement = new Vector2(0, -1);
    }

    public void generateRandomVector() {
        double bearing = GameScreen.RANDOM.nextDouble() * 6.283185;
        this.directionMovement.x = (float) Math.sin(bearing);
        this.directionMovement.y = (float) Math.cos(bearing);
    }

    @Override
    public void draw(@NotNull Batch batch) {
        batch.draw(this.shipTexture, super.getX(), super.getY(), super.getWidth(), super.getHeight());
    }

    @Override
    public void move(float deltaTime) {
        float leftLimit = -super.getX();
        float bottomLimit = GameScreen.SCREEN_HEIGHT / 2 - super.getY();
        float rightLimit = GameScreen.SCREEN_WIDTH - super.getX() - super.getWidth();
        float topLimit = GameScreen.SCREEN_HEIGHT - super.getY() - super.getHeight();

        float xMove = this.directionMovement.x * this.movementSpeed * deltaTime;
        float yMove = this.directionMovement.y * this.movementSpeed * deltaTime;

        if (xMove > 0) xMove = Math.min(xMove, rightLimit);
        else xMove = Math.max(xMove, leftLimit);

        if (yMove > 0) yMove = Math.min(yMove, topLimit);
        else yMove = Math.max(yMove, bottomLimit);

        this.translateShip(xMove, yMove);
    }

    private void translateShip(float x, float y) {
        super.setPosition(super.getX() + x, super.getY() + y);
    }

    @Override
    public void shoot() {
        this.laserEnemies.add(new LaserEnemy(this.laserTexture, (super.getX() + super.getWidth() / 2) - 6 * Gdx.graphics.getDensity(), super.getY() + 15 * Gdx.graphics.getDensity(), this.widthLaser * Gdx.graphics.getDensity(), this.heightLaser * Gdx.graphics.getDensity(), this.movementSpeedLaser * Gdx.graphics.getDensity(), -1, this.shotTrackPlayer, this.player));
        this.timeSinceLastShot = 0;
    }

    @Override
    public void die() {
        this.lifes--;

        if (this.lifes == 0) {
            GameScreen.soundExplosion.play();
            this.explosions.add(new Explosion(this, 2F));
        }
    }

    @Override
    public ShipEnemy clone() {
        return new ShipEnemy(this.shipTexture,
                this.laserTexture,
                this.laserEnemies,
                super.getY(),
                super.getWidth(),
                super.getHeight(),
                this.movementSpeed,
                this.shotTrackPlayer,
                this.timeBetweenShot,
                this.widthLaser,
                this.heightLaser,
                this.movementSpeedLaser,
                this.lifes,
                this.player,
                this.explosions,
                this.directionChangeFrequency);
    }

    //region Getters & Setters
    public float getTimeSinceLastShot() {
        return timeSinceLastShot;
    }

    public void setTimeSinceLastShot(float timeSinceLastShot) {
        this.timeSinceLastShot = timeSinceLastShot;
    }

    public Vector2 getDirectionMovement() {
        return directionMovement;
    }

    public void setDirectionMovement(Vector2 directionMovement) {
        this.directionMovement = directionMovement;
    }

    public float getTimeSinceLastDirectionChange() {
        return timeSinceLastDirectionChange;
    }

    public void setTimeSinceLastDirectionChange(float timeSinceLastDirectionChange) {
        this.timeSinceLastDirectionChange = timeSinceLastDirectionChange;
    }

    public float getDirectionChangeFrequency() {
        return directionChangeFrequency;
    }

    public void setDirectionChangeFrequency(float directionChangeFrequency) {
        this.directionChangeFrequency = directionChangeFrequency;
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

    public int getLifes() {
        return lifes;
    }

    public void setLifes(int lifes) {
        this.lifes = lifes;
    }

    public boolean isShotTrackPlayer() {
        return shotTrackPlayer;
    }

    public void setShotTrackPlayer(boolean shotTrackPlayer) {
        this.shotTrackPlayer = shotTrackPlayer;
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

    public LinkedList<LaserEnemy> getLaserEnemies() {
        return laserEnemies;
    }

    public void setLaserEnemies(LinkedList<LaserEnemy> laserEnemies) {
        this.laserEnemies = laserEnemies;
    }

    public float getTimeBetweenShot() {
        return timeBetweenShot;
    }

    public void setTimeBetweenShot(float timeBetweenShot) {
        this.timeBetweenShot = timeBetweenShot;
    }

    public ShipPlayer getPlayer() {
        return player;
    }

    public void setPlayer(ShipPlayer player) {
        this.player = player;
    }
    //endregion
}
