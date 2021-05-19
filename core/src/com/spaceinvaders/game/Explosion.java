package com.spaceinvaders.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.spaceinvaders.game.screens.GameScreen;

import org.jetbrains.annotations.NotNull;

public class Explosion {
    private Animation<TextureRegion> explosionAnimation;
    private float explosionTimer;
    private float posX;
    private float posY;
    private float width;
    private float height;

    public Explosion(@NotNull Rectangle rectangle, float totalAnimationTime) {
        this.posX = rectangle.x;
        this.posY = rectangle.y;
        this.width = rectangle.width;
        this.height = rectangle.height;

        this.explosionAnimation = new Animation<>(totalAnimationTime / 16, GameScreen.textureRegions1Dexplosion);
        this.explosionTimer = 0;
    }

    public void update(float deltaTime) {
        this.explosionTimer += deltaTime;
    }

    public void draw(@NotNull SpriteBatch batch) {
        batch.draw(explosionAnimation.getKeyFrame(this.explosionTimer),
                this.posX,
                this.posY,
                this.width,
                this.height);
    }

    public boolean isFinished() {
        return this.explosionAnimation.isAnimationFinished(explosionTimer);
    }
}
