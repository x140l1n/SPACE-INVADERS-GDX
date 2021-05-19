package com.spaceinvaders.game;

import com.badlogic.gdx.graphics.g2d.Batch;

public interface Ship {
    void draw(Batch batch);

    void move(float deltaTime);

    void shoot();

    void die();
}
