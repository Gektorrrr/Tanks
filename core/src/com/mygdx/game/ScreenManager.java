package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.utiltts.ScreenType;

public class ScreenManager {



    private static final ScreenManager ourInstance = new ScreenManager();

    public static ScreenManager getInstance() {return ourInstance;}

    private ScreenManager() {
    }

    public static final int World_Width = 1280;
    public static final int World_Height = 720;
    private Game game;
    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    private EndGameScreen winScreen;
    private HelpScreen helpScreen;
    private Viewport viewport;
    private Camera camera;

    public void init(Game game, SpriteBatch batch){
        this.game = game;
        this.menuScreen = new MenuScreen(batch);
        this.winScreen = new EndGameScreen(batch);
        this.camera = new OrthographicCamera(World_Width, World_Height);
        this.camera.position.set((float) World_Width / 2, (float) World_Height / 2, 0);
        this.camera.update();
        this.viewport = new FitViewport(World_Width, World_Height, camera);
        this.gameScreen = new GameScreen(batch);
        this.helpScreen = new HelpScreen(batch);
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
        viewport.apply();
    }

    public Camera getCamera() {
        return camera;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void setScreen(ScreenType screenType){
        Gdx.input.setCursorCatched(false);

        Screen currentScreen = game.getScreen();
        switch (screenType){
            case MENU:
                game.setScreen(menuScreen);
                break;
            case GAME:
                game.setScreen(gameScreen);
                break;
            case WIN:
                game.setScreen(winScreen);
                break;
            case HELP:
                game.setScreen(helpScreen);
                break;
        }
        if(currentScreen != null)
            currentScreen.dispose();
    }
}
