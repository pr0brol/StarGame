package ru.geekbrains.pool;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.SpritePool;
import ru.geekbrains.sprite.Aid;

public class AidPool extends SpritePool<Aid> {

    private TextureAtlas atlas;

    public AidPool(TextureAtlas atlas){
        this.atlas = atlas;
    }

    @Override
    protected Aid newObject() {
        return new Aid();
    }
}
