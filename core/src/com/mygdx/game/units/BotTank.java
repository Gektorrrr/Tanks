package com.mygdx.game.units;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Weapon;
import com.mygdx.game.utiltts.Direction;
import com.mygdx.game.utiltts.TankOwner;

public class BotTank extends Tank{
    Direction preferredDirection;
    float aiTimer;
    float aiTimer2;
    float pursuitRadius;
    boolean active;
    Vector3 lastPosition;

    public boolean isActive() {
        return active;
    }



    public BotTank(GameScreen gameScreen, TextureAtlas atlas){
        super(gameScreen);
        this.tankOwner = TankOwner.AI;
        this.weapon = new Weapon(atlas, 0.4f);
        this.texture = atlas.findRegion("botTankBase");
        this.HpBarTexture = atlas.findRegion("bar");
        this.position = new Vector2(500, 500);
        this.lastPosition = new Vector3(0, 0, 0);
        this.speed = 100;
        this.HP = 2;
        this.HP_Max = this.HP;
        this.aiTimer2 = 3;
        this.preferredDirection = Direction.UP;
        this.pursuitRadius = 300;
        this.circle = new Circle(position.x, position.y, 40);
    }

    public void activate(float x, float y){
        active = true;
        HP_Max = 3;
        position.set(x, y);
        this.preferredDirection = Direction.values()[MathUtils.random(0, Direction.values().length - 1)];
        angle = preferredDirection.getAngle();
        aiTimer = 0;
        HP = HP_Max;

    }

    @Override
    public void destroy() {
        PlayerTank.addScore(10000);
        active = false;
    }

    public void update(float dt) {
        aiTimer += dt;
        if(aiTimer >= aiTimer2){
            aiTimer = 0;
            aiTimer2 = MathUtils.random(2.5f, 6f);
            preferredDirection = Direction.values()[MathUtils.random(0, Direction.values().length - 1)];
            angle = preferredDirection.getAngle();
        }
        move(preferredDirection, dt);
        float dst = this.position.dst(gameScreen.getPlayer().getPosition());
        if(dst < pursuitRadius){
            rotateGun(gameScreen.getPlayer().getPosition().x, gameScreen.getPlayer().getPosition().y, dt);
            fire();
        }
        if(Math.abs(position.x - lastPosition.x) < 0.5 && Math.abs(position.y - lastPosition.y) < 0.5){
            lastPosition.z += dt;
            if(lastPosition.z > 0.25){
                aiTimer += 10;
            }
        }
        else{
            lastPosition.x = position.x;
            lastPosition.y = position.y;
            lastPosition.z = 0;
        }
        super.update(dt);

    }

}
