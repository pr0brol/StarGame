package ru.geekbrains.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.sprite.Gameground;
import ru.geekbrains.sprite.Ship;
import ru.geekbrains.sprite.Star;

public class GameScreen extends BaseScreen {

    private static final int STAR_COUNT = 64;

    private Texture gg;
    private TextureAtlas atlas;
    private Gameground gameground;

    private Star[] stars;

    private BulletPool bulletPool;

    private Ship ship;

    @Override
    public void show() {
        super.show();
        gg = new Texture("texture/game.jpg");
        gameground = new Gameground(new TextureRegion(gg));
        atlas = new TextureAtlas("texture/mainAtlas/mainAtlas.tpack");
        stars = new Star[STAR_COUNT];
        for(int i = 0; i < stars.length; i++){
            stars[i] = new Star(atlas);
        }
        bulletPool = new BulletPool();
        ship = new Ship(atlas, bulletPool);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        freeAllDestroyedSprites();
        draw();
    }

    public void update(float delta) {
        for(Star star: stars){
            star.update(delta);
        }
        ship.update(delta);
        bulletPool.updateActiveSprites(delta);
    }

    public void freeAllDestroyedSprites(){
        bulletPool.freeAllDestroyedActiveSprites();
    }

    public void draw(){
        batch.begin();
        gameground.draw(batch);
        for(Star star: stars){
            star.draw(batch);
        }
        ship.draw(batch);
        bulletPool.drawActiveSprites(batch);
        batch.end();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        gameground.resize(worldBounds);
        for(Star star: stars){
            star.resize(worldBounds);
        }
        ship.resize(worldBounds);
    }

    @Override
    public void dispose() {
        gg.dispose();
        atlas.dispose();
        bulletPool.dispose();
        super.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        ship.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        ship.keyUp(keycode);
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        ship.touchDown(touch, pointer);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        ship.touchUp(touch, pointer);
        return false;
    }
}
