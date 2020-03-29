package ru.geekbrains.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.ExplosionPool;

public class Enemy extends Ship {

    private enum State {DESCENT, FIGHT}

    private State state;

    private Vector2 startSpeed = new Vector2(0, -0.07f);

    public Enemy(BulletPool bulletPool, ExplosionPool explosionPool, Sound shootSound, Rect worldBounds) {
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.shootSound = shootSound;
        this.worldBounds = worldBounds;
        this.speed = new Vector2();
        this.speed0 = new Vector2();
        this.bulletSpeed = new Vector2();
        state = State.DESCENT;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        switch (state){
            case DESCENT:
                if(getTop() < worldBounds.getTop()){
                    speed.set(speed0);
                    state = State.FIGHT;
                }
                break;
            case FIGHT:
                if(getBottom() < worldBounds.getBottom()){
                    destroy();
                }
                reloadTimer += delta;
                if(reloadTimer >= reloadInterval){
                    reloadTimer = 0f;
                    shoot();
                }
                break;
        }
    }

    public void set(
            TextureRegion[] regions,
            Vector2 speed0,
            TextureRegion bulletRegion,
            float bulletHeight,
            float bulletVY,
            int damage,
            float reloadInterval,
            int HP,
            float height

    ){
        this.regions = regions;
        this.speed0.set(speed0);
        this.bulletRegion = bulletRegion;
        this.bulletHeight = bulletHeight;
        this.bulletSpeed.set(0, bulletVY);
        this.damage = damage;
        this.reloadInterval = reloadInterval;
        this.HP = HP;
        setHeightProportion(height);
        speed.set(startSpeed);
        reloadTimer = reloadInterval;
        state = State.DESCENT;
    }

    public boolean isBulletCollision(Rect bullet) {
        return !(bullet.getRight() < getLeft()
        || bullet.getLeft() > getRight()
        || bullet.getBottom() > getTop()
        || bullet.getTop() < pos.y);
    }
}
