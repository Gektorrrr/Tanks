package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Weapon {
    private final TextureRegion texture;
    private float firePeriod;
    private final int damage;
    private final float projectileSpeed;
    private final float projectileLifeTime;

    public Weapon(TextureAtlas atlas, float firePeriod){
        this.texture = atlas.findRegion("simpleWeapon");
        this.firePeriod = firePeriod;
        this.damage = 1;
        float radius = 500;
        this.projectileSpeed = 320;
        this.projectileLifeTime = radius / this.projectileSpeed;
    }

    public void setFirePeriod(float firePeriod) {
        this.firePeriod = firePeriod;
    }

    public float getProjectileSpeed() {
        return projectileSpeed;
    }

    public float getProjectileLifeTime() {
        return projectileLifeTime;
    }

    public TextureRegion getTexture(){
        return texture;
    }

    public float getFirePeriod(){
        return firePeriod;
    }

    public int getDamage(){
        return damage;
    }

}
