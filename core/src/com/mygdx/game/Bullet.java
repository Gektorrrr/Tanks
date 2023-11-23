package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.units.Tank;

public class Bullet {
    private Tank owner;
    private final Vector2 position;
    private final Vector2 velocity;
    private int damage;
    private boolean active;
    private float lifeTime;
    private float maxLifeTime;

    public Bullet() {
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.damage = 0;
        this.active = false;
    }

    public Tank getOwner(){
        return owner;
    }

    public int getDamage(){
        return damage;
    }

    public Vector2 getPosition(){
        return position;
    }

    public boolean isActive(){
        return active;
    }

    public void deactivate(){
        active = false;
    }

    public void activate(Tank owner, float x, float y, float vx, float vy, int damage, float maxLifeTime){
        this.owner = owner;
        this.position.set(x, y);
        this.velocity.set(vx, vy);
        this.damage = damage;
        this.maxLifeTime = maxLifeTime;
        this.active = true;
    }

    public void update(float dt){
        position.mulAdd(velocity , dt);
        lifeTime += dt;
        if (lifeTime >= maxLifeTime){
            lifeTime = 0;
            deactivate();
        }
        if(position.x < 0 || position.x > Map.getSize_X() || position.y < 0 || position.y > Map.getSize_Y())
            deactivate();
    }
}
