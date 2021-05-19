package com.spaceinvaders.game;

import com.badlogic.gdx.graphics.g2d.Batch;

public interface Laser {
    void draw(Batch batch);

    void move(float deltaTime);
}
