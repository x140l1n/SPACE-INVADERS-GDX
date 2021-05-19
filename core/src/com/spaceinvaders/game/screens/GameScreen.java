package com.spaceinvaders.game.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.spaceinvaders.game.Explosion;
import com.spaceinvaders.game.Heart;
import com.spaceinvaders.game.LaserEnemy;
import com.spaceinvaders.game.LaserPlayer;
import com.spaceinvaders.game.Shield;
import com.spaceinvaders.game.ShipEnemy;
import com.spaceinvaders.game.ShipPlayer;
import com.spaceinvaders.game.ThreadHeart;
import com.spaceinvaders.game.ThreadShield;

import java.util.LinkedList;
import java.util.ListIterator;

public class GameScreen extends BaseScreen implements Runnable {
    public static boolean gameOver = false;

    private boolean timeBoss = false;
    private boolean twoBosses = false;

    private final int MAX_ENEMIES_SCREEN = 7;

    private final String SCORE = "Score";
    private final String SHIELDS = "Shields";
    private final String LIFES = "Lifes";

    private TextureRegion playerTexturePropulsion;
    private TextureRegion laserPlayerTexture;
    private TextureRegion playersShieldTexture;
    private TextureRegion enemyTexture;
    private TextureRegion enemyTextureBoss;
    private TextureRegion laserEnemyTexture;
    private TextureRegion textureHeart;
    private TextureRegion textureShield;

    private SpriteBatch batch;

    private float backgroundOffsetY = 500;
    private final float BACKGROUND_SPEED = 100;

    private final float SHIP_WIDTH_PLAYER = 60;
    private final float SHIP_HEIGHT_PLAYER = 60;

    private final float HEALTH_WIDTH = 40;
    private final float HEALTH_HEIGHT = 40;

    private final float SHIP_WIDTH_ENEMY = 50;
    private final float SHIP_HEIGHT_ENEMY = 50;

    private final float SHIP_WIDTH_ENEMY_BOSS = 150;
    private final float SHIP_HEIGHT_ENEMY_BOSS = 150;

    private ShipEnemy[] typesEnemies;

    private ShipPlayer player;
    private LinkedList<ShipEnemy> enemies;
    private LinkedList<LaserEnemy> lasersEnemies;

    private Texture textureExplosion;
    private LinkedList<Explosion> explosions;
    private TextureRegion[][] textureRegions2Dexplosion;
    public static TextureRegion[] textureRegions1Dexplosion;

    private Thread threadCheckEnemies, threadHealth, threadShield;

    private float checkTimeEnemyFrequency = 1.2F;
    private float checkTimeEnemyLastTime;

    private LinkedList<Heart> hearts;
    private LinkedList<Shield> shields;

    private Music musicBackground;
    public static Sound soundExplosion;

    private float hudVerticalMargin, hudLeftX, hudRightX, hudCentreX, hudRow1Y, hudRow2Y, hudSectionWidth;

    private BitmapFont font;

    public GameScreen(GameMain game) {
        super(game);

        iniHUD();

        this.backgroundTexture = game.textures.findRegion("background");
        this.playerTexturePropulsion = game.textures.findRegion("ship_player_propulsion");
        TextureRegion shipPlayerTexture = game.textures.findRegion("ship_player");
        this.playersShieldTexture = game.textures.findRegion("shield");
        this.enemyTexture = game.textures.findRegion("ship_enemy");
        this.enemyTextureBoss = game.textures.findRegion("ship_enemy_boss");
        this.laserPlayerTexture = game.textures.findRegion("laser_player");
        this.laserEnemyTexture = game.textures.findRegion("laser_enemy");
        this.textureHeart = game.textures.findRegion("heart");
        this.textureShield = game.textures.findRegion("shield");
        this.enemyTexture.flip(true, true);
        this.enemyTextureBoss.flip(true, true);

        this.batch = new SpriteBatch();

        this.textureExplosion = new Texture("explosion.png");

        this.enemies = new LinkedList<>();
        this.lasersEnemies = new LinkedList<>();
        this.explosions = new LinkedList<>();
        this.hearts = new LinkedList<>();
        this.shields = new LinkedList<>();

        this.musicBackground = Gdx.audio.newMusic(Gdx.files.internal("background_music.ogg"));
        this.musicBackground.setLooping(true);
        this.musicBackground.setVolume(0.3F);

        this.soundExplosion = Gdx.audio.newSound(Gdx.files.internal("explosion.mp3"));

        iniExplosion();

        iniElements();
    }

    private void iniExplosion() {
        textureRegions2Dexplosion = TextureRegion.split(textureExplosion, textureExplosion.getWidth() / 4, textureExplosion.getHeight() / 4);

        textureRegions1Dexplosion = new TextureRegion[16];

        int index = 0;

        for (int i = 0; i < 16 / 4; i++) {
            for (int j = 0; j < 16 / 4; j++) {
                textureRegions1Dexplosion[index] = textureRegions2Dexplosion[i][j];
                index++;
            }
        }
    }

    private void iniHUD() {
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
        this.hudRow1Y = SCREEN_HEIGHT - hudVerticalMargin;
        this.hudRow2Y = hudRow1Y - hudVerticalMargin - font.getCapHeight();
        this.hudSectionWidth = SCREEN_WIDTH / 3;
    }

    public void iniElements() {
        GameScreen.gameOver = false;

        this.timeBoss = false;
        this.twoBosses = false;

        this.enemies.clear();
        this.lasersEnemies.clear();
        this.explosions.clear();
        this.hearts.clear();
        this.shields.clear();

        this.player = new ShipPlayer(this.playerTexturePropulsion,
                this.playersShieldTexture,
                this.laserPlayerTexture,
                (this.SCREEN_WIDTH / 2) - (this.SHIP_WIDTH_PLAYER * Gdx.graphics.getDensity()) / 2,
                (this.SCREEN_HEIGHT / 5) - (this.SHIP_HEIGHT_PLAYER * Gdx.graphics.getDensity()) / 2,
                this.SHIP_WIDTH_PLAYER * Gdx.graphics.getDensity(),
                this.SHIP_HEIGHT_PLAYER * Gdx.graphics.getDensity(),
                300 * Gdx.graphics.getDensity(),
                20,
                20,
                200,
                "",
                3,
                this.explosions,
                true);
        new Thread(this.player).start();

        this.typesEnemies = new ShipEnemy[2];

        //Types ships enemies.
        this.typesEnemies[0] = new ShipEnemy(this.enemyTexture,
                this.laserEnemyTexture,
                this.lasersEnemies,
                this.SCREEN_HEIGHT - 5,
                this.SHIP_WIDTH_ENEMY * Gdx.graphics.getDensity(),
                this.SHIP_HEIGHT_ENEMY * Gdx.graphics.getDensity(),
                300 * Gdx.graphics.getDensity(),
                false,
                1.5F,
                20,
                20,
                200,
                1,
                this.player,
                this.explosions,
                0.75F);

        this.typesEnemies[1] = new ShipEnemy(this.enemyTextureBoss,
                this.laserEnemyTexture,
                this.lasersEnemies,
                this.SCREEN_HEIGHT - 5,
                this.SHIP_WIDTH_ENEMY_BOSS * Gdx.graphics.getDensity(),
                this.SHIP_HEIGHT_ENEMY_BOSS * Gdx.graphics.getDensity(),
                150 * Gdx.graphics.getDensity(),
                true,
                2F,
                30,
                30,
                200,
                100,
                this.player,
                this.explosions,
                0.75F);

        this.threadHealth = new ThreadHeart(this.hearts,
                this.player,
                this.SCREEN_HEIGHT - 5,
                this.HEALTH_WIDTH * Gdx.graphics.getDensity(),
                this.HEALTH_HEIGHT * Gdx.graphics.getDensity(),
                120 * Gdx.graphics.getDensity(),
                this.textureHeart, 6F);
        this.threadHealth.start();

        this.threadShield = new ThreadShield(this.shields,
                this.player,
                this.SCREEN_HEIGHT - 5,
                this.HEALTH_WIDTH * Gdx.graphics.getDensity(),
                this.HEALTH_HEIGHT * Gdx.graphics.getDensity(),
                120 * Gdx.graphics.getDensity(),
                this.textureShield, 6F);
        this.threadShield.start();

        this.musicBackground.play();

        this.threadCheckEnemies = new Thread(this);
        this.threadCheckEnemies.start();

        this.enemies.add(this.typesEnemies[0].clone());
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.batch.begin();

        renderBackground(delta);

        renderPlayer(delta);
        renderEnemies(delta);
        renderExplosions(delta);

        renderLasersPlayer(delta);
        renderLasersEnemy(delta);

        if (this.threadHealth != null) ((ThreadHeart) this.threadHealth).update(delta);
        if (this.threadShield != null) ((ThreadShield) this.threadShield).update(delta);

        renderHealth(delta);
        renderShield(delta);

        renderHUD();

        this.checkTimeEnemyLastTime += delta;

        if (GameScreen.gameOver) {
            this.musicBackground.stop();

            try {
                this.threadShield.join();
                this.threadHealth.join();
                this.threadCheckEnemies.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            this.threadShield = null;
            this.threadHealth = null;
            this.threadCheckEnemies = null;

            ((GameOverScreen) this.game.gameOverScreen).setPlayer(this.player);
            this.game.setScreen(this.game.gameOverScreen);
        }

        this.batch.end();
    }

    private void renderBackground(float deltaTime) {
        this.backgroundOffsetY += deltaTime * this.BACKGROUND_SPEED;

        if (backgroundOffsetY > SCREEN_HEIGHT)
            backgroundOffsetY = 0;

        this.batch.draw(this.backgroundTexture, 0, -this.backgroundOffsetY, this.SCREEN_WIDTH, this.SCREEN_HEIGHT);
        this.batch.draw(this.backgroundTexture, 0, -this.backgroundOffsetY + this.SCREEN_HEIGHT, this.SCREEN_WIDTH, this.SCREEN_HEIGHT);
    }

    private void renderPlayer(float deltaTime) {
        this.player.draw(this.batch);
        this.player.move(deltaTime);

        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                this.player.shoot();
            }
        } else if (Gdx.app.getType() == Application.ApplicationType.Android) {
            if (Gdx.input.justTouched()) {
                float xTouched = Gdx.input.getX();
                float yTouched = Gdx.input.getY();

                yTouched = GameScreen.SCREEN_HEIGHT - yTouched;

                Vector2 pointTouch = new Vector2(xTouched, yTouched);
                Vector2 pointCenterShip = new Vector2(this.player.getX() + this.player.getWidth() / 2, this.player.getY() + this.player.getHeight() / 2);

                if (pointTouch.dst(pointCenterShip) < this.player.getWidth()) this.player.shoot();
            }
        }
    }

    private void renderLasersPlayer(float deltaTime) {
        ListIterator<LaserPlayer> lasers = this.player.getLasersPlayer().listIterator();

        while (lasers.hasNext()) {
            LaserPlayer laserPlayer = lasers.next();

            if (laserPlayer.getY() < this.SCREEN_HEIGHT) {

                synchronized (this.enemies) {
                    ListIterator<ShipEnemy> enemies = this.enemies.listIterator();

                    while (enemies.hasNext()) {
                        ShipEnemy enemy = enemies.next();

                        if (enemy.getY() < SCREEN_HEIGHT) {
                            if (laserPlayer.overlaps(enemy)) {
                                enemy.die();

                                if (enemy.getLifes() == 0) {
                                    this.player.setScore(this.player.getScore() + 100);

                                    if (!this.timeBoss)
                                        if (this.player.getScore() != 0 && (this.player.getScore() % 2000 == 0))
                                            this.timeBoss = true;

                                    if (enemy.isShotTrackPlayer()) {
                                        if (this.twoBosses) {
                                            if (this.enemies.size() == 0) {
                                                this.timeBoss = false;
                                            }

                                            this.twoBosses = false;
                                        } else {
                                            this.timeBoss = false;
                                            this.twoBosses = true;
                                        }
                                    }

                                    enemies.remove();
                                }

                                lasers.remove();

                                break;
                            }
                        }
                    }

                    laserPlayer.draw(this.batch);
                    laserPlayer.move(deltaTime);
                }
            } else {
                lasers.remove();
            }
        }
    }

    private void renderEnemies(float deltaTime) {
        synchronized (this.enemies) {
            ListIterator<ShipEnemy> enemies = this.enemies.listIterator();

            while (enemies.hasNext()) {
                ShipEnemy enemy = enemies.next();

                enemy.draw(this.batch);
                enemy.move(deltaTime);

                enemy.setTimeSinceLastDirectionChange(enemy.getTimeSinceLastDirectionChange() + deltaTime);

                if (enemy.getTimeSinceLastDirectionChange() > enemy.getDirectionChangeFrequency()) {
                    enemy.generateRandomVector();
                    enemy.setTimeSinceLastDirectionChange(enemy.getTimeSinceLastDirectionChange() - enemy.getDirectionChangeFrequency());
                }

                enemy.setTimeSinceLastShot(enemy.getTimeSinceLastShot() + deltaTime);

                if (enemy.getTimeSinceLastShot() - enemy.getTimeBetweenShot() >= 0) enemy.shoot();
            }
        }
    }

    private void renderLasersEnemy(float deltaTime) {
        ListIterator<LaserEnemy> lasers = this.lasersEnemies.listIterator();

        while (lasers.hasNext()) {
            LaserEnemy laserEnemy = lasers.next();

            if (laserEnemy.getY() > 0 && (laserEnemy.getX() > 0 && laserEnemy.getX() < this.SCREEN_WIDTH)) {
                if (laserEnemy.overlaps(this.player)) {
                    if (!this.player.isImmortal()) {
                        if (this.player.getShields() == 0) {
                            this.player.die();

                            if (this.player.getLifes() == 0) {
                                //Game over
                                GameScreen.gameOver = true;

                                break;
                            } else {
                                //Respawn
                                this.player.setX((this.SCREEN_WIDTH / 2) - (this.SHIP_WIDTH_PLAYER * Gdx.graphics.getDensity()) / 2);
                                this.player.setY((this.SCREEN_HEIGHT / 5) - (this.SHIP_HEIGHT_PLAYER * Gdx.graphics.getDensity()) / 2);
                                this.player.setY((this.SCREEN_HEIGHT / 5) - (this.SHIP_HEIGHT_PLAYER * Gdx.graphics.getDensity()) / 2);
                                this.player.setWidth(this.SHIP_WIDTH_PLAYER * Gdx.graphics.getDensity());
                                this.player.setHeight(this.SHIP_HEIGHT_PLAYER * Gdx.graphics.getDensity());
                                this.player.setImmortal(true);
                                this.player.setShields(0);
                                new Thread(this.player).start();
                            }
                        } else {
                            this.player.setShields(this.player.getShields() - 1);
                        }

                        lasers.remove();
                    }
                }

                laserEnemy.draw(this.batch);
                laserEnemy.move(deltaTime);
            } else {
                lasers.remove();
            }
        }
    }

    private void renderHealth(float deltaTime) {
        synchronized (this.hearts) {
            ListIterator<Heart> healths = this.hearts.listIterator();

            while (healths.hasNext()) {
                Heart heart = healths.next();
                if (heart.getY() >= 0) {
                    if (heart.overlaps(this.player)) {
                        if (this.player.getMaxLifes() > this.player.getLifes()) {
                            this.player.setLifes(this.player.getLifes() + 1);
                        }

                        healths.remove();
                    }

                    heart.draw(this.batch);
                    heart.move(deltaTime);
                } else {
                    healths.remove();
                }
            }
        }
    }

    private void renderShield(float deltaTime) {
        synchronized (this.shields) {
            ListIterator<Shield> shields = this.shields.listIterator();

            while (shields.hasNext()) {
                Shield shield = shields.next();

                if (shield.getY() >= 0) {
                    if (shield.overlaps(this.player)) {
                        if (this.player.getShields() != 3) {
                            this.player.setShields(3);
                        }

                        shields.remove();
                    }

                    shield.draw(this.batch);
                    shield.move(deltaTime);
                } else {
                    shields.remove();
                }
            }
        }
    }

    private void renderExplosions(float deltaTime) {
        ListIterator<Explosion> explosions = this.explosions.listIterator();

        while (explosions.hasNext()) {
            Explosion explosion = explosions.next();
            explosion.update(deltaTime);

            if (explosion.isFinished()) explosions.remove();
            else explosion.draw(batch);
        }
    }

    private void renderHUD() {
        this.font.draw(this.batch, this.SCORE, hudLeftX, hudRow1Y, hudSectionWidth, Align.left, false);
        this.font.draw(this.batch, this.SHIELDS, hudCentreX, hudRow1Y, hudSectionWidth, Align.center, false);
        this.font.draw(this.batch, this.LIFES, hudRightX, hudRow1Y, hudSectionWidth, Align.right, false);

        this.font.draw(this.batch, String.format("%06d", this.player.getScore()), hudLeftX, hudRow2Y, hudSectionWidth, Align.left, false);
        this.font.draw(this.batch, String.format("%01d", this.player.getShields()), hudCentreX, hudRow2Y, hudSectionWidth, Align.center, false);
        this.font.draw(this.batch, String.format("%01d", this.player.getLifes()), hudRightX, hudRow2Y, hudSectionWidth, Align.right, false);
    }

    @Override
    public void run() {
        while (!GameScreen.gameOver) {
            System.out.println(this.checkTimeEnemyLastTime);
            if (this.checkTimeEnemyLastTime > this.checkTimeEnemyFrequency) {
                if (this.enemies.size() < MAX_ENEMIES_SCREEN) {
                    if (!this.timeBoss) {
                        synchronized (this.enemies) {
                            if (!GameScreen.gameOver)
                                this.enemies.add(this.typesEnemies[0].clone()); //REGULAR ENEMY.
                        }
                    } else if (this.timeBoss && this.enemies.size() == 0) {
                        synchronized (this.enemies) {
                            if (!GameScreen.gameOver)
                                this.enemies.add(this.typesEnemies[1].clone()); //BOSS ENEMY.

                            if (this.twoBosses)
                                if (!GameScreen.gameOver)
                                    this.enemies.add(this.typesEnemies[1].clone()); //BOSS ENEMY.
                        }
                    }
                }

                this.checkTimeEnemyLastTime = 0;
            }
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
        this.musicBackground.dispose();
        this.soundExplosion.dispose();
        this.font.dispose();
        this.threadShield = null;
        this.threadHealth = null;
        this.threadCheckEnemies = null;
        this.enemies = null;
        this.lasersEnemies = null;
        this.explosions = null;
        this.hearts = null;
        this.shields = null;
    }
}
