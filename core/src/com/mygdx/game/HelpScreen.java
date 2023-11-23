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
import com.mygdx.game.utiltts.ScreenType;

public class HelpScreen extends AbstractScreen {
    private final SpriteBatch batch;
    private TextureAtlas atlas;
    private TextureRegion obstaclesTexture;
    private TextureRegion enemyTexture;
    private TextureRegion enemyBaseTexture;
    private TextureRegion buttonTexture;
    private BitmapFont font24;
    private Stage stage;
    private StringBuilder tmpString;

    public HelpScreen(SpriteBatch batch){
        this.batch = batch;
    }

    @Override
    public void show() {
        atlas = new TextureAtlas("game.pack");
        this.obstaclesTexture = atlas.findRegion("obstacles");
        this.enemyTexture = atlas.findRegion("botTankBase");
        this.enemyBaseTexture = atlas.findRegion("base");
        this.buttonTexture = atlas.findRegion("floorButton");

        font24 = new BitmapFont(Gdx.files.internal("font24.fnt"));
        Skin skin = new Skin();
        stage = new Stage();
        Group group = new Group();
        skin.add("simpleButton", new TextureRegion( atlas.findRegion("SimpleButton")));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font24;
        TextButton menuButton = new TextButton("Back", textButtonStyle);
        menuButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                ScreenManager.getInstance().setScreen(ScreenType.MENU);
            }
        });
        menuButton.setPosition(0, 40);
        group.addActor(menuButton);
        group.setPosition(580, 40);
        stage.addActor(group);
        Gdx.input.setInputProcessor(stage);

        this.tmpString = new StringBuilder();
        this.tmpString.setLength(0);
        this.tmpString.append("     W    " +
                             "\n A       D   You may ride by using this keys" +
                           "\n      S       and shoot by left mouse button.");
        this.tmpString.append("\n\n\n                - You can demolish some walls " +
                              "\n                and shoot through the water." +
                              "\n                - This is your enemy, " +
                              "\n               every 5 seconds a new one appears. " +
                              "\n\n\n                         - This is the enemy's base, destroy it to win." +
                              "\n\n\n             -try to get on this button, " +
                              "\n             maybe something will happen." +
                              "\n\nI wish you good luck, good game :)");

    }

    @Override
    public void render(float delta) {
        update(delta);

        ScreenManager.getInstance().getCamera().position.set(0, 0, 1);
        ScreenManager.getInstance().getCamera().update();
        batch.setProjectionMatrix(ScreenManager.getInstance().getCamera().combined);

        ScreenUtils.clear(0.3f, 0.1f, 0.5f, 1);

        batch.begin();
        batch.draw(obstaclesTexture, -200, 120);
        batch.draw(enemyTexture, -200, 80);
        batch.draw(enemyBaseTexture, -200, -80);
        batch.draw(buttonTexture, -200, -120);
        font24.draw(batch, tmpString, -200, 340);
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
