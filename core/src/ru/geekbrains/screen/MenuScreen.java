package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.sprite.Background;
import ru.geekbrains.sprite.Logo;

public class MenuScreen extends BaseScreen {

    private static final float V_LEN = 0.01f;
    private static final float LOGO_SIZE = 0.5f;

    private Texture img;
    private Texture bg;

    private Vector2 position;
    private Vector2 speed;
    private Vector2 touch;

    private Background background;
    private Logo logo;

    @Override
    public void show() {
        super.show();
        img = new Texture("badlogic.jpg");
        bg = new Texture("space.jpg");
        background = new Background(new TextureRegion(bg));
        logo = new Logo(new TextureRegion(img));
        speed = new Vector2();
        touch = super.getTouch();
        position = new Vector2();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        logo.resize(worldBounds);
        logo.setSize(LOGO_SIZE, LOGO_SIZE);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0.2f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        logo.draw(batch);
//        speed = touch.sub(position);
//        speed.setLength(V_LEN);
//        position.add(speed);
        logo.pos.set(touch);
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        img.dispose();
        bg.dispose();
    }
}
