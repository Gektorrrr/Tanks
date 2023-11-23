package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.units.Tank;

public class BulletEmitter {
    private final TextureRegion bulletTexture;
    private final Bullet[] bullets;
    private static final int MAX_BULLETS = 500;

    public BulletEmitter(TextureAtlas atlas){
        this.bulletTexture = atlas.findRegion("projectile");
        this.bullets = new Bullet[MAX_BULLETS];
        for (int i = 0; i < MAX_BULLETS; i++)
            this.bullets[i] = new Bullet();
    }

    public void activate(Tank owner, float x, float y, float vx, float vy, int damage, float maxLifeTime){
        for (Bullet bullet : bullets) {
            if (!bullet.isActive()) {
                bullet.activate(owner, x, y, vx, vy, damage, maxLifeTime);
                break;
            }


        }
    }

    public Bullet[] getBullets() {
        return bullets;
    }

    public void render(SpriteBatch batch){
        for (Bullet bullet : bullets) {
            if (bullet.isActive())
                batch.draw(bulletTexture, bullet.getPosition().x - 8, bullet.getPosition().y - 8);
        }
    }

    public void update(float dt){
        for (Bullet bullet : bullets)
            if (bullet.isActive())
                bullet.update(dt);
    }


}
