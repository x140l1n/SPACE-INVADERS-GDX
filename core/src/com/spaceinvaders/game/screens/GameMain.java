package com.spaceinvaders.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.spaceinvaders.game.screens.BaseScreen;
import com.spaceinvaders.game.screens.GameOverScreen;
import com.spaceinvaders.game.screens.GameScreen;

public class GameMain extends Game {
    public BaseScreen gameScreen, gameOverScreen;
    public TextureAtlas textures;

    @Override
    public void create() {
        this.textures = new TextureAtlas(Gdx.files.internal("assets.pack"));
        this.gameScreen = new GameScreen(this);
        this.gameOverScreen = new GameOverScreen(this);
        setScreen(this.gameScreen);
    }

    @Override
    public void dispose() {
        this.gameScreen.dispose();
        this.textures.dispose();
        this.dispose();
    }
}
