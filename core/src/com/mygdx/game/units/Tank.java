package com.mygdx.game.units;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Map;
import com.mygdx.game.utiltts.Direction;
import com.mygdx.game.utiltts.TankOwner;
import com.mygdx.game.utiltts.Utils;
import com.mygdx.game.Weapon;

public abstract class Tank {
    GameScreen gameScreen;
    Weapon weapon;
    TankOwner tankOwner;
    TextureRegion texture;
    TextureRegion HpBarTexture;
    Vector2 position;
    Vector2 tmp;
    float speed;
    float angle;
    float GunAngle;
    float fireTimer;
    int HP;
    int HP_Max;
    Circle circle;

    public Vector2 getPosition() {
        return position;
    }

    public TankOwner getTankOwner(){
        return tankOwner;
    }

    public Tank(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.tmp = new Vector2(0, 0);
    }

    public Circle getCircle() {
        return circle;
    }

    public void move(Direction direction, float dt){
        tmp.set(position);
        tmp.add(speed * direction.getVx() * dt, speed * direction.getVy() * dt);
        if(gameScreen.getMap().isAreaClear(tmp.x, tmp.y, 20)){
            angle = direction.getAngle();
            position.set(tmp);
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 20, position.y - 20, 20, 20, 40, 40, 1, 1, angle);
        batch.draw(weapon.getTexture(), position.x - 20, position.y - 20, 20, 20, 40, 40, 1, 1, GunAngle);
        if(HP < HP_Max){
            batch.setColor(0, 0, 0, 1);
            batch.draw(HpBarTexture,position.x - 20, position.y + 20,  40, 8);
            batch.setColor(1, 0, 0,  1);
            batch.draw(HpBarTexture,position.x - 20, position.y + 20, ((float) HP  / HP_Max) * 40, 8);
            batch.setColor(1, 1, 1, 1);
        }

    }

    public void update(float dt){
        fireTimer += dt;
        if(position.x < 0)
            position.x = 0;
        if(position.x > Map.getSize_X())
            position.x = Map.getSize_X();
        if(position.y < 0)
            position.y = 0;
        if(position.y > Map.getSize_Y())
            position.y = Map.getSize_Y();
        circle.setPosition(position);
    }

    public void rotateGun(float x, float y, float dt){
        float angleTo = Utils.getAngle( position.x, position.y, x, y);
        GunAngle = Utils.makeRotation(GunAngle, angleTo, 180, dt);
        GunAngle = Utils.angleToFromNegPiToPosPi(GunAngle);
    }

    public void takeDamage(int damage){
        HP -= damage;
        if(HP <= 0)
            destroy();
    }

    public abstract void destroy();

    public void fire(){
        if(fireTimer >= weapon.getFirePeriod()){
            fireTimer = 0f;
            float angleRad = (float) Math.toRadians(GunAngle);
            gameScreen.getBulletEmitter().activate(this, position.x, position.y, weapon.getProjectileSpeed() * (float) Math.cos(angleRad), weapon.getProjectileSpeed() * (float) Math.sin(angleRad), weapon.getDamage(), weapon.getProjectileLifeTime());
        }
    }
}
