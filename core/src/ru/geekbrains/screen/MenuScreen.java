package ru.geekbrains.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.sprite.Background;
import ru.geekbrains.sprite.ButtonExit;
import ru.geekbrains.sprite.ButtonPlay;
import ru.geekbrains.sprite.Star;

public class MenuScreen extends BaseScreen {

    private Game game;

    public MenuScreen(Game game) {
        this.game = game;
    }

    private Texture bg;

    private int STAR_COUNT = 100;

    private Background background;

    private TextureAtlas atlas;

    private ButtonExit buttonExit;
    private ButtonPlay buttonPlay;

    private Star[] stars = new Star[STAR_COUNT];

    @Override
    public void show() {
        super.show();
        bg = new Texture("texture/space.jpg");
        background = new Background(new TextureRegion(bg));
        atlas = new TextureAtlas("texture/menuAtlas/menuAtlas.pack");
        buttonExit = new ButtonExit(atlas);
        buttonPlay = new ButtonPlay(atlas, game);
        for(int i = 0; i < STAR_COUNT; i++){
            stars[i] = new Star(atlas);
        }

    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for(int i = 0; i < STAR_COUNT; i++){
            stars[i].resize(worldBounds);
        }
        buttonExit.resize(worldBounds);
        buttonPlay.resize(worldBounds);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }

    public void update(float delta) {
        for(int i = 0; i < STAR_COUNT; i++){
            stars[i].update(delta);
        }
    }

    public void draw(){
        batch.begin();
        background.draw(batch);
        for(int i = 0; i < STAR_COUNT; i++){
            stars[i].draw(batch);
        }
        buttonExit.draw(batch);
        buttonPlay.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        bg.dispose();
        atlas.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        buttonExit.touchDown(touch, pointer);
        buttonPlay.touchDown(touch, pointer);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        buttonExit.touchUp(touch, pointer);
        buttonPlay.touchUp(touch, pointer);
        return false;
    }
}
