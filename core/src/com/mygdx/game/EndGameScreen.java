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
import com.badlogic.gdx.utils.StringBuilder;
import com.mygdx.game.units.EnemyBase;
import com.mygdx.game.units.PlayerTank;
import com.mygdx.game.utiltts.ScreenType;

public class EndGameScreen extends AbstractScreen {
    private final SpriteBatch batch;
    private TextureAtlas atlas;
    private BitmapFont font24;
    private Stage stage;
    private StringBuilder tmpString;
    private int Max_score;

    public EndGameScreen(SpriteBatch batch){
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
        TextButton exitButton = new TextButton("Exit", textButtonStyle);
        startButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                ScreenManager.getInstance().setScreen(ScreenType.GAME);
            }
        });
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                Gdx.app.exit();
            }
        });
        startButton.setPosition(0, 40);
        exitButton.setPosition(0, 0);
        group.addActor(startButton);
        group.addActor(exitButton);
        group.setPosition(580, 40);
        stage.addActor(group);
        Gdx.input.setInputProcessor(stage);
        int score = PlayerTank.getScore();
        PlayerTank.addScore(-score);
        if(score > Max_score)
            Max_score = score;

        this.tmpString = new StringBuilder();
        this.tmpString.setLength(0);
        if(PlayerTank.getKey() == 1 || EnemyBase.getKey() == 1)
            this.tmpString.append("YOU WIN");
        else if(PlayerTank.getKey() == 2)
            this.tmpString.append("You defeated:(");

        this.tmpString.append("\nRecord: ").append(Max_score);
        GameScreen.worldTimer = 0;
    }

    @Override
    public void render(float delta) {
        update(delta);
        PlayerTank.getMusic().stop();
        ScreenUtils.clear(0, 0, 0, 1);

        ScreenManager.getInstance().getCamera().position.set(0, 0, 1);
        ScreenManager.getInstance().getCamera().update();
        batch.setProjectionMatrix(ScreenManager.getInstance().getCamera().combined);

        ScreenUtils.clear(0.3f, 0.1f, 0.2f, 1);

        batch.begin();
        font24.draw(batch, tmpString, -60, 0);
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
