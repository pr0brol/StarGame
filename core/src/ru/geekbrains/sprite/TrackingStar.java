package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

public class TrackingStar extends Star{

    private Vector2 trackingSpeed;
    private Vector2 sumSpeed = new Vector2();

    public TrackingStar(TextureAtlas atlas, Vector2 trackingSpeed) {
        super(atlas);
        this.trackingSpeed = trackingSpeed;
    }

    @Override
    public void update(float delta) {
        sumSpeed.setZero().mulAdd(trackingSpeed, 0.2f).rotate(180).add(speed);
        pos.mulAdd(sumSpeed, delta);
        checkAndHandleBounds();
    }
}
