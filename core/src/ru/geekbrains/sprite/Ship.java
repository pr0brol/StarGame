package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class Ship extends Sprite {

    private static final float V_LEN = 0.01f;
    private Vector2 speed;
    private Vector2 buf;
    private Vector2 touch;

    private Rect worldBounds;

    public Ship(TextureAtlas atlas) {
        super(atlas.findRegion("main_ship"));
        setHeightProportion(0.15f);
        speed = new Vector2();
        buf = new Vector2();
        touch = new Vector2();
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        pos.set(0, 0);
    }

    @Override
    public void update(float delta) {
        buf.set(touch);
        if(buf.sub(pos).len() >= V_LEN){
            pos.add(speed);
        }else {
            pos.set(touch);
        }

    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        this.touch.set(touch);
        speed = touch.cpy().sub(pos);
        speed.setLength(V_LEN);
        return false;
    }


}
