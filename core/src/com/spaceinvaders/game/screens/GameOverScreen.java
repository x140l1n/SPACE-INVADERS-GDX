package com.spaceinvaders.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.spaceinvaders.game.ShipPlayer;

public class GameOverScreen extends BaseScreen {

    private Stage stage;
    private Skin skin;
    private TextButton buttonPlayAgain;
    private int HEIGHT_BUTTON = 50;
    private float FONT_SCALE_BUTTON = 0.3F;

    private SpriteBatch batch;

    private float backgroundOffsetY = 500;
    private float backgroundSpeed = 100;

    private ShipPlayer player;

    private BitmapFont font;

    private float hudVerticalMargin, hudLeftX, hudRightX, hudCentreX, hudRowCenter1Y, hudRowCenter2Y, hudRowCenter3Y, hudSectionWidth;

    private final String GAME_OVER = "GAME OVER";
    private final String PLAY_AGAIN = "PLAY AGAIN";

    private String scoreText;

    private float timerText = 0;
    private final float letterSpawnTime = .1F;
    private String drawText = "";
    private int stringIndex = 0;

    private boolean row1Complete = false;
    private boolean row2Complete = false;

    private Music typeSound;

    public GameOverScreen(final GameMain game) {
        super(game);

        this.batch = new SpriteBatch();
        this.font = new BitmapFont();

        this.backgroundTexture = game.textures.findRegion("background");

        this.typeSound = Gdx.audio.newMusic(Gdx.files.internal("type.mp3"));

        iniLayout();

        //Add button play again.
        this.stage = new Stage(this.viewport);
        this.skin = new Skin();
        this.skin.addRegions(game.textures);
        this.skin.load(Gdx.files.internal("uiskin.json"));

        this.buttonPlayAgain = new TextButton(this.PLAY_AGAIN, this.skin);
        this.buttonPlayAgain.setWidth(hudSectionWidth);
        this.buttonPlayAgain.setHeight(this.HEIGHT_BUTTON * Gdx.graphics.getDensity());
        this.buttonPlayAgain.getLabel().setFontScale(this.FONT_SCALE_BUTTON * Gdx.graphics.getDensity());
        this.buttonPlayAgain.setPosition(this.hudCentreX, this.hudRowCenter3Y);
        this.buttonPlayAgain.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                row1Complete = false;
                row2Complete = false;
                stringIndex = 0;
                timerText = 0;
                drawText = "";

                buttonPlayAgain.setTouchable(Touchable.disabled);

                ((GameScreen) game.gameScreen).iniElements();
                game.setScreen(game.gameScreen);

                return false;
            }
        });

        this.stage.addActor(this.buttonPlayAgain);
        this.buttonPlayAgain.setTouchable(Touchable.disabled);

        Gdx.input.setInputProcessor(this.stage);
    }

    @Override
    public void show() {
        this.scoreText = "YOUR SCORE: \n" + this.player.getScore();
    }

    private void iniLayout() {
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("space_invaders.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        //params.size = ((Gdx.app.getType() == Application.ApplicationType.Android  ? 75 : 35));
        params.size = (int) (20 * (SCREEN_HEIGHT / SCREEN_WIDTH) * Gdx.graphics.getDensity());
        params.borderWidth = 3.6F;
        params.color = Color.WHITE;

        this.font = fontGenerator.generateFont(params);

        this.font.getData().setScale(0.52F);

        this.hudVerticalMargin = font.getCapHeight() / 1.5F;
        this.hudLeftX = hudVerticalMargin;
        this.hudRightX = SCREEN_WIDTH * 2 / 3 - hudLeftX;
        this.hudCentreX = SCREEN_WIDTH / 3;
        this.hudRowCenter1Y = SCREEN_HEIGHT / 1.5F - hudVerticalMargin;
        this.hudRowCenter2Y = SCREEN_HEIGHT / 1.8F - hudVerticalMargin;
        this.hudRowCenter3Y = SCREEN_HEIGHT / 3F - hudVerticalMargin;
        this.hudSectionWidth = SCREEN_WIDTH / 3;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.batch.begin();

        renderBackground(delta);

        renderText(delta);

        this.batch.end();
    }

    private void renderBackground(float deltaTime) {
        this.backgroundOffsetY += deltaTime * this.backgroundSpeed;

        if (backgroundOffsetY > GameScreen.SCREEN_HEIGHT)
            backgroundOffsetY = 0;

        this.batch.draw(this.backgroundTexture, 0, -this.backgroundOffsetY, GameScreen.SCREEN_WIDTH, GameScreen.SCREEN_HEIGHT);
        this.batch.draw(this.backgroundTexture, 0, -this.backgroundOffsetY + GameScreen.SCREEN_HEIGHT, GameScreen.SCREEN_WIDTH, GameScreen.SCREEN_HEIGHT);
    }

    private void renderText(float delta) {
        if (!this.drawText.equals(this.GAME_OVER) && !this.row1Complete) {
            this.timerText += delta;

            if (this.timerText >= this.letterSpawnTime && this.stringIndex < this.GAME_OVER.length()) {
                this.drawText += this.GAME_OVER.charAt(this.stringIndex);
                this.stringIndex++;
                this.timerText -= letterSpawnTime;
            }
        } else if (!this.row1Complete) {
            this.row1Complete = true;
            this.stringIndex = 0;
            this.timerText = 0;
            this.drawText = "";
        }

        if (!this.drawText.equals(this.scoreText) && this.row1Complete) {
            this.timerText += delta;

            if (this.timerText >= this.letterSpawnTime && this.stringIndex < this.scoreText.length()) {
                this.drawText += this.scoreText.charAt(this.stringIndex);
                this.stringIndex++;
                this.timerText -= this.letterSpawnTime;
            }
        } else if (this.row1Complete) {
            this.row2Complete = true;
            this.stringIndex = 0;
            this.timerText = 0;
            this.drawText = "";
        }

        if (row1Complete) {
            this.font.draw(this.batch, this.GAME_OVER, hudCentreX, hudRowCenter1Y, hudSectionWidth, Align.center, false);

            if (row2Complete)
                this.font.draw(this.batch, this.scoreText, hudCentreX, hudRowCenter2Y, hudSectionWidth, Align.center, false);
            else {
                if (!this.typeSound.isPlaying()) this.typeSound.play();
                this.font.draw(this.batch, this.drawText, hudCentreX, hudRowCenter2Y, hudSectionWidth, Align.center, false);
            }
        } else {
            if (!this.typeSound.isPlaying()) this.typeSound.play();
            this.font.draw(this.batch, this.drawText, hudCentreX, hudRowCenter1Y, hudSectionWidth, Align.center, false);
        }

        if (row1Complete && row2Complete) {
            this.stage.act(delta);
            this.stage.draw();

            if (!this.buttonPlayAgain.isTouchable()) this.buttonPlayAgain.setTouchable(Touchable.enabled);
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        this.batch.dispose();
        this.font.dispose();
        this.stage.dispose();
        this.skin.dispose();
    }

    //region Getters & Setters
    public ShipPlayer getPlayer() {
        return player;
    }

    public void setPlayer(ShipPlayer player) {
        this.player = player;
    }
    //endregion
}
