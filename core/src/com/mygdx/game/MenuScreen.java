package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.units.PlayerTank;
import com.mygdx.game.utiltts.ScreenType;

public class MenuScreen extends AbstractScreen {
    private final SpriteBatch batch;
    private TextureAtlas atlas;
    private BitmapFont font24;
    private Stage stage;

    public MenuScreen(SpriteBatch batch){
        this.batch = batch;
    }

    @Override
    public void show() {
        atlas = new TextureAtlas("game.pack");
        font24 = new BitmapFont(Gdx.files.internal("font24.fnt"));
        Skin skin = new Skin();
        stage = new Stage();
        Group group = new Group();
        skin.add("simpleButton", new TextureRegion( atlas.findRegion("SimpleButton")));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font24;
        TextButton startButton = new TextButton("Start", textButtonStyle);
        TextButton helpButton = new TextButton("Help", textButtonStyle);
        TextButton exitButton = new TextButton("Exit", textButtonStyle);
        startButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                ScreenManager.getInstance().setScreen(ScreenType.GAME);
            }
        });
        helpButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                ScreenManager.getInstance().setScreen(ScreenType.HELP);
            }
        });
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                Gdx.app.exit();
            }
        });
        startButton.setPosition(0, 80);
        helpButton.setPosition(0, 40);
        exitButton.setPosition(0, 0);
        group.addActor(startButton);
        group.addActor(helpButton);
        group.addActor(exitButton);
        group.setPosition(580, 40);
        stage.addActor(group);
        Gdx.input.setInputProcessor(stage);
        int score = PlayerTank.getScore();
        PlayerTank.addScore(-score);
        GameScreen.worldTimer = 0;
    }

    @Override
    public void render(float delta) {
        update(delta);
        if (PlayerTank.getMusic() != null)
            PlayerTank.getMusic().stop();
        ScreenUtils.clear(0.5f, 0.1f, 0, 1);
        batch.setProjectionMatrix(ScreenManager.getInstance().getCamera().combined);

        batch.begin();

        batch.end();
        stage.draw();
    }

    public void update(float dt) {
            stage.act(dt);
    }



    @Override
    public void dispose() {
        font24.dispose();
        atlas.dispose();
        stage.draw();
    }
}
