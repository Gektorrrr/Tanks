package com.mygdx.game.units;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Map;
import com.mygdx.game.ScreenManager;
import com.mygdx.game.utiltts.ScreenType;

public class EnemyBase{

    private int HP;
    private final int HP_Max;
    private static int key;
    private final Vector2 position;
    private final TextureRegion texture;
    private final TextureRegion HpBarTexture;
    private final Circle circle;

    public Circle getCircle() {
        return circle;
    }

    public EnemyBase(TextureAtlas atlas){
        this.texture = atlas.findRegion("base");
        this.HpBarTexture = atlas.findRegion("bar");
        this.position = new Vector2(Map.getSize_X() - 200, Map.getSize_Y() - 440);
        this.circle = new Circle(position.x + 80, position.y + 80, 80);
        this.HP = 25;
        this.HP_Max = this.HP;
        key = 0;
    }

    public void destroy() {
        if(HP <= 0){
            key = 1;
            ScreenManager.getInstance().setScreen(ScreenType.WIN);
        }
    }

    public void render(SpriteBatch batch){
        batch.draw(texture, position.x, position.y);
        if(HP <= HP_Max){
            batch.setColor(0, 0, 0, 1);
            batch.draw(HpBarTexture, position.x + 40, position.y + 170,  80, 8);
            batch.setColor(1, 0, 0,  1);
            batch.draw(HpBarTexture, position.x + 40, position.y + 170, ((float) HP  / HP_Max) * 80, 8);
            batch.setColor(1, 1, 1, 1);
        }
    }

    public static int getKey(){
        return key;
    }

    public void takeDamage(int damage){
        HP -= damage;
        if(HP <= 0)
            destroy();
    }
}

