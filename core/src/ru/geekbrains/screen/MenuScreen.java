package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;

public class MenuScreen extends BaseScreen {

    private Texture img;
    private Texture background;
    private Vector2 touch;
    private Vector2 position;
    private Vector2 speed;
    private int keycode;


    @Override
    public void show() {
        super.show();
        img = new Texture("badlogic.jpg");
        background = new Texture("space.jpg");
        touch = new Vector2();
        position = new Vector2();
        speed = new Vector2();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(background, 0, 0, 640, 480);
        batch.draw(img, position.x, position.y);
        batch.end();
        if(position.x != touch.x & position.y != touch.y){
            float x = touch.x - position.x;
            float y = touch.y - position.y;
            speed.set(x, y);
            speed.nor();
        }
        if(keycode == 19){
            speed.set(0f, 0.2f);
        }else if(keycode == 20){
            speed.set(0f, -0.2f);
        }else if(keycode == 21){
            speed.set(-0.2f, 0);
        }else if(keycode == 22){
            speed.set(0.2f, 0);
        }else if(keycode == 66){
            speed.set(0, 0);
        }

        if(Gdx.graphics.getHeight() > (position.y + img.getHeight()) & (Gdx.graphics.getWidth() > (position.x + img.getWidth()))){
            position.add(speed);
        }else {
            position.set(((Gdx.graphics.getWidth() / 2) - img.getWidth() / 2), ((Gdx.graphics.getWidth() / 2) - img.getWidth() / 2));
        }

    }

    @Override
    public void dispose() {
        super.dispose();
        img.dispose();
        background.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touch.set(screenX, Gdx.graphics.getHeight() - screenY);
        System.out.println("touchDown screenX = " + touch.x + " screenY = " + touch.y);
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        this.keycode = keycode;
        return false;
    }
}
