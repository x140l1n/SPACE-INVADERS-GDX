package com.spaceinvaders.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Random;

public abstract class BaseScreen implements Screen {

    public static final float SCREEN_WIDTH = Gdx.graphics.getWidth();
    public static final float SCREEN_HEIGHT = Gdx.graphics.getHeight();
    public static final Random RANDOM = new Random();

    protected OrthographicCamera camera;
    protected Viewport viewport;
    protected GameMain game;

    protected TextureRegion backgroundTexture;

    public BaseScreen(GameMain game)
    {
        this.game = game;

        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(this.SCREEN_WIDTH, this.SCREEN_HEIGHT, this.camera);
    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height);
    }

    @Override
    public void dispose() {
        this.game.dispose();
        this.dispose();
    }
}