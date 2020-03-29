package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class Aid extends Sprite {

    private Rect worldBounds;
    private int health;

    private Vector2 speed;

    public Aid(){
        regions = new TextureRegion[1];
        speed = new Vector2();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(speed, delta);

    }

    public void set(
            TextureRegion region,
            int health,
            Rect worldBounds,
            float height,
            Vector2 speed
    ){
        this.regions[0] = region;
        this.health = health;
        this.worldBounds = worldBounds;
        setHeightProportion(height);
        this.speed = speed;
    }

    public int getHealth() {
        return health;
    }
}
