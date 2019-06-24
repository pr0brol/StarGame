package ru.geekbrains.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.sprite.Gameground;
import ru.geekbrains.sprite.Ship;

public class GameScreen extends BaseScreen {

    private Texture gg;

    private TextureAtlas atlas;

    private Gameground gameground;
    private Ship ship;

    @Override
    public void show() {
        super.show();
        gg = new Texture("texture/game.jpg");
        gameground = new Gameground(new TextureRegion(gg));
        atlas = new TextureAtlas("texture/mainAtlas/mainAtlas.tpack");
        ship = new Ship(atlas);
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        gameground.resize(worldBounds);
        ship.resize(worldBounds);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }

    public void update(float delta) {
        ship.update(delta);
    }

    public void draw(){
        batch.begin();
        gameground.draw(batch);
        ship.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        gg.dispose();
        super.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return super.keyUp(keycode);
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
