package ru.geekbrains.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;

public class Enemy extends Ship {

    private Vector2 startSpeed = new Vector2(0, -0.07f);

    public Enemy(BulletPool bulletPool, Sound shootSound, Rect worldBounds) {
        this.bulletPool = bulletPool;
        this.shootSound = shootSound;
        this.worldBounds = worldBounds;
        this.speed = new Vector2();
        this.speed0 = new Vector2();
        this.bulletSpeed = new Vector2();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if(getBottom() < worldBounds.getBottom()){
            destroy();
        }
        if(getTop() > worldBounds.getTop()){
            speed.set(startSpeed);
        }else {
            speed.set(speed0);
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
        speed.set(speed0);
    }
}
