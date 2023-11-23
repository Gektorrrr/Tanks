package com.mygdx.game.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.StringBuilder;
import com.mygdx.game.*;
import com.mygdx.game.utiltts.Direction;
import com.mygdx.game.utiltts.ScreenType;
import com.mygdx.game.utiltts.TankOwner;

public class PlayerTank extends Tank{

    static int key;
    static int score;
    static float firePeriod;
    StringBuilder tmpString;
    private final Sound sound;
    static Music music;

    public static Music getMusic() {
        return music;
    }

    public PlayerTank(GameScreen gameScreen, TextureAtlas atlas){
        super(gameScreen);
        this.tankOwner = TankOwner.PLAYER;
        firePeriod = 0.4f;
        this.weapon = new Weapon(atlas, firePeriod);
        this.texture = atlas.findRegion("playerTankBase");
        this.HpBarTexture = atlas.findRegion("bar");
        this.position = new Vector2(100, 100);
        this.speed = 100;
        this.HP_Max = 10;
        this.HP = this.HP_Max;
        this.circle = new Circle(position.x + 20, position.y + 20, 20);
        sound = Gdx.audio.newSound(Gdx.files.internal("2.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("3.mp3"));
        key = 0;
        this.tmpString = new StringBuilder();
    }

    public void update(float dt) {
        checkMoment(dt);
        rotateGun(gameScreen.getMousePosition().x, gameScreen.getMousePosition().y, dt);
        if(Gdx.input.isTouched())
            fire();

        if(position.x >= Map.getSize_X() - 100 && gameScreen.getWorldTimer() <= 90) {
            key = 1;
            ScreenManager.getInstance().setScreen(ScreenType.WIN);
        }
        super.update(dt);
    }

    @Override
    public void destroy() {
        key = 2;
        sound.play(0.2f);
        ScreenManager.getInstance().setScreen(ScreenType.WIN);
    }

    public static int getScore() {
        return score;
    }

    public static int getKey() {
        return key;
    }

    public static void addScore(int amount){
        firePeriod -= 0.05f;
        score += amount;
    }

    public void renderHUD(SpriteBatch batch, BitmapFont font24){
        weapon.setFirePeriod(firePeriod);
        tmpString.setLength(0);
        tmpString.append("Score: ").append(score);
        tmpString.append("\nLive time: ").append((int) gameScreen.getWorldTimer());
        font24.draw(batch, tmpString, getPosition().x - 630, getPosition().y + 360);
    }

    public void checkMoment(float dt){
        music.play();
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            move(Direction.UP, dt);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.A)){
            move(Direction.LEFT, dt);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.S)){
            move(Direction.DOWN, dt);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.D)){
            move(Direction.RIGHT, dt);
        }

    }
}
