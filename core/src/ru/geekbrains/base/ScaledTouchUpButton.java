package ru.geekbrains.base;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class ScaledTouchUpButton extends Sprite {

    private static final float SCALE = 0.9f;

    private int pointer;
    private boolean pressed;

    public ScaledTouchUpButton(TextureRegion region) {
        super(region);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if(pressed || !isMe(touch)){
            return false;
        }
        this.pointer = pointer;
        this.scale = SCALE;
        pressed = true;
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if(this.pointer != pointer || !pressed){
            return false;
        }
        if(isMe(touch)){
            action();
        }
        pressed = false;
        scale = 1f;
        return false;
    }

    public abstract void action();
}
