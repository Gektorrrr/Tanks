package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.units.BotTank;

public class BotEmitter {
    private final BotTank[] bots;
    private static final int MAX_BOTS = 50;

    public BotTank[] getBots() {
        return bots;
    }

    public BotEmitter(GameScreen gameScreen, TextureAtlas atlas){
        this.bots = new BotTank[MAX_BOTS];
        for (int i = 0; i < bots.length; i++)
            this.bots[i] = new BotTank(gameScreen, atlas);
    }

    public void activate(float x, float y){
        for (BotTank bot : bots) {
            if (!bot.isActive()) {
                bot.activate(x, y);
                break;
            }
        }
    }

    public void render(SpriteBatch batch){
        for (BotTank bot : bots) {
            if (bot.isActive())
                bot.render(batch);
        }
    }

    public void update(float dt){
        for (BotTank bot : bots)
            if (bot.isActive())
                bot.update(dt);
    }
}
