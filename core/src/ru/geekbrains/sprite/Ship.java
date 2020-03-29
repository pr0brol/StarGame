package ru.geekbrains.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.ExplosionPool;

public abstract class Ship extends Sprite {

    protected BulletPool bulletPool;
    protected ExplosionPool explosionPool;
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
    protected float reloadTimer;

    private float damageAnimateInterval = 0.1f;
    private float damageAnimateTimer = damageAnimateInterval;


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
        damageAnimateTimer += delta;
        if(damageAnimateTimer >= damageAnimateInterval){
            frame = 0;
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        boom();
    }

    public void damage(int damage){
        damageAnimateTimer = 0f;
        frame = 1;
        HP -= damage;
        if(HP <= 0){
            HP = 0;
            destroy();
        }
    }

    public int getDamage() {
        return damage;
    }

    public int getHP(){
        return HP;
    }

    public void setHP(int health){
        HP += health;
        if(HP > 100){
            HP = 100;
        }
    }

    public Vector2 getSpeed(){
        return speed;
    }

    protected void shoot(){
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, pos, bulletSpeed, bulletHeight, worldBounds, damage);
        shootSound.play();
    }

    protected void boom(){
        Explosion explosion = explosionPool.obtain();
        explosion.set(getHeight(), pos);
    }
}
