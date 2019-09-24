package ru.geekbrains.utils;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;
import ru.geekbrains.pool.AidPool;
import ru.geekbrains.sprite.Aid;

public class AidGenerator {

    private static final int AID_SMALL_HEALTH = 10;
    private static final float AID_SMALL_HEIGHT = 0.02f;

    private static final int AID_MIDDLE_HEALTH = 25;
    private static final float AID_MIDDLE_HEIGHT = 0.03f;

    private static final int AID_BIG_HEALTH = 50;
    private static final float AID_BIG_HEIGHT = 0.04f;

    private TextureRegion aidRegion;

    private AidPool aidPool;

    private float generateInterval = 50f;
    private float generateTimer;

    private Rect worldBounds;

    private Vector2 speed = new Vector2(0f, -0.05f);

    public AidGenerator(TextureAtlas atlas, AidPool aidPool, Rect worldBounds) {
        aidRegion = atlas.findRegion("aid");
        this.aidPool = aidPool;
        this.worldBounds = worldBounds;
    }

    public void generate(float delta){

        generateTimer += delta;
        if(generateTimer >= generateInterval) {
            generateTimer = 0;
            Aid aid = aidPool.obtain();
            float typeAid = (float) Math.random();
            if (typeAid < 0.5f) {
                aid.set(
                        aidRegion,
                        AID_SMALL_HEALTH,
                        worldBounds,
                        AID_SMALL_HEIGHT,
                        speed
                );
            }else if(typeAid < 0.8f){
                aid.set(
                        aidRegion,
                        AID_MIDDLE_HEALTH,
                        worldBounds,
                        AID_MIDDLE_HEIGHT,
                        speed
                );
            }else {
                aid.set(
                        aidRegion,
                        AID_BIG_HEALTH,
                        worldBounds,
                        AID_BIG_HEIGHT,
                        speed
                );
            }

            aid.pos.x = Rnd.nextFloat(worldBounds.getLeft() + aid.getHalfWidth(), worldBounds.getRight() - aid.getHalfWidth());
            aid.pos.y = worldBounds.getTop();
        }
    }
}
