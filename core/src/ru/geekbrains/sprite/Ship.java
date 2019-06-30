package ru.geekbrains.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;

public abstract class Ship extends Sprite {

    protected BulletPool bulletPool;
    protected Rect worldBounds;

    protected int HP;

    protected TextureRegion bulletRegion;

    protected Vector2 speed0;
    protected Vector2 speed;
    protected Vector2 bulletSpeed;
    protected float bulletHeight;
    protected int damage;

    protected Sound shootSound;

    protected float reloadInterval;
    private  float reloadTimer;


    public Ship(TextureRegion region, int rows, int cols, int frames) {
        super(region, rows, cols, frames);
    }

    public Ship() {
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(speed, delta);
        reloadTimer += delta;
        if(reloadTimer >= reloadInterval){
            reloadTimer = 0f;
            shoot();
        }
    }

    protected void shoot(){
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, pos, bulletSpeed, bulletHeight, worldBounds, damage);
        shootSound.play();
    }
}
