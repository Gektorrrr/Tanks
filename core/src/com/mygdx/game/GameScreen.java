package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.units.BotTank;
import com.mygdx.game.units.EnemyBase;
import com.mygdx.game.units.PlayerTank;
import com.mygdx.game.units.Tank;
import com.mygdx.game.utiltts.ScreenType;

public class GameScreen extends AbstractScreen {
    private final SpriteBatch batch;
    private TextureAtlas atlas;
    private BitmapFont font24;
    private Map map;
    private PlayerTank player;
    private BulletEmitter bulletEmitter;
    private BotEmitter botEmitter;
    private EnemyBase enemyBase;
    private float gameTimer;
    static float worldTimer;
    private Stage stage;
    private  boolean paused;
    private Vector2 mousePosition;
    private TextureRegion cursor;
    private Sound sound;
    private static final boolean FRIENDLY_FIRE = false;

    public PlayerTank getPlayer() {
        return player;
    }

    public Map getMap() {
        return map;
    }

    public BulletEmitter getBulletEmitter() {
        return bulletEmitter;
    }

    public Vector2 getMousePosition() {
        return mousePosition;
    }

    public GameScreen(SpriteBatch batch){
        this.batch = batch;
    }

    public float getWorldTimer() {
        return worldTimer;
    }

    @Override
    public void show() {
        atlas = new TextureAtlas("game.pack");
        map = new Map(atlas);
        player = new PlayerTank(this, atlas);
        bulletEmitter = new BulletEmitter(atlas);
        botEmitter = new BotEmitter(this, atlas);
        enemyBase = new EnemyBase(atlas);
        font24 = new BitmapFont(Gdx.files.internal("font24.fnt"));
        mousePosition = new Vector2();
        cursor = new TextureRegion(atlas.findRegion("cursor"));


        sound = Gdx.audio.newSound(Gdx.files.internal("1.mp3"));
        Group group = new Group();
        stage = new Stage();
        Skin skin = new Skin();
        skin.add("simpleButton", new TextureRegion( atlas.findRegion("SimpleButton")));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font24;
        TextButton pauseButton = new TextButton("Pause", textButtonStyle);
        TextButton exitButton = new TextButton("Exit", textButtonStyle);
        pauseButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                paused = !paused;
            }
        });
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                ScreenManager.getInstance().setScreen(ScreenType.MENU);
            }
        });
        pauseButton.setPosition(0, 40);
        exitButton.setPosition(0, 0);
        group.addActor(pauseButton);
        group.addActor(exitButton);
        group.setPosition(1120, 630);
        stage.addActor(group);
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCursorCatched(true);
        gameTimer = 0;

    }

    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(0.3f, 0.1f, 0.1f, 1);

        ScreenManager.getInstance().getCamera().position.set(player.getPosition().x, player.getPosition().y, 0);
        ScreenManager.getInstance().getCamera().update();

        batch.setProjectionMatrix(ScreenManager.getInstance().getCamera().combined);
        batch.begin();
        map.render(batch);
        botEmitter.render(batch);
        bulletEmitter.render(batch);
        player.render(batch);
        player.renderHUD(batch, font24);
        enemyBase.render(batch);
        batch.end();

        stage.draw();

        batch.begin();
        batch.draw(cursor, mousePosition.x - 24, mousePosition.y - 24, 24, 24, 48, 48, 1, 1, worldTimer * 30);
        batch.end();
    }

    public void update(float dt) {
        mousePosition.set(Gdx.input.getX(), Gdx.input.getY());
        ScreenManager.getInstance().getViewport().unproject(mousePosition);

        worldTimer += dt;


        if(!paused) {
            gameTimer += dt;
            if (gameTimer >= 5) {
                gameTimer = 0;
                float xSpawn;
                float ySpawn;
                do {
                    xSpawn = MathUtils.random(0,  map.getSize_X());
                    ySpawn = MathUtils.random(0, map.getSize_Y());
                } while (!map.isAreaClear(xSpawn, ySpawn, 20));
                botEmitter.activate(xSpawn, ySpawn);
            }
            player.update(dt);
            map.update(player.getPosition());
            botEmitter.update(dt);
            bulletEmitter.update(dt);
            checkCollisions();
            stage.act(dt);
        }
    }

    public void checkCollisions() {
        for (int i = 0; i < bulletEmitter.getBullets().length; i++) {
            Bullet bullet = bulletEmitter.getBullets()[i];
            if (bullet.isActive()) {
                for (int j = 0; j < botEmitter.getBots().length; j++) {
                    BotTank bot = botEmitter.getBots()[j];
                    if (bot.isActive()) {
                        if (checkBulletOwner(bot, bullet) && bot.getCircle().contains(bullet.getPosition())) {
                            bullet.deactivate();
                            bot.takeDamage(bullet.getDamage());
                            if(MathUtils.random() < 0.3)
                                sound.play(0.2f);
                            break;
                        }
                    }
                }
                if (checkBulletOwner(player, bullet) && player.getCircle().contains(bullet.getPosition())) {
                    bullet.deactivate();
                    player.takeDamage(bullet.getDamage());
                }
                if (enemyBase.getCircle().contains(bullet.getPosition())) {
                    bullet.deactivate();
                    enemyBase.takeDamage(bullet.getDamage());
                }
                map.checkWallsBulletsCollision(bullet);
            }
        }
    }

    public boolean checkBulletOwner(Tank tank, Bullet bullet) {
        if (!FRIENDLY_FIRE)
            return tank.getTankOwner() != bullet.getOwner().getTankOwner();

        else
            return tank != bullet.getOwner();
    }

    @Override
    public void dispose() {

        font24.dispose();
        atlas.dispose();
        stage.dispose();
    }
}
