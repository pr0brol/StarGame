package ru.geekbrains.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
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

    private int STAR_COUNT = 256;

    private Game game;

    Music fonMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));

    private Texture bg;
    private Background background;
    private TextureAtlas atlas;

    private ButtonExit buttonExit;
    private ButtonPlay buttonPlay;

    private Star[] stars;

    public MenuScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        super.show();
        bg = new Texture("texture/space.jpg");
        background = new Background(new TextureRegion(bg));
        atlas = new TextureAtlas("texture/menuAtlas/menuAtlas.pack");
        buttonExit = new ButtonExit(atlas);
        buttonPlay = new ButtonPlay(atlas, game);
        stars = new Star[STAR_COUNT];
        for(int i = 0; i < stars.length; i++){
            stars[i] = new Star(atlas);
        }
        fonMusic.play();
        fonMusic.setLooping(true);
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for(Star star: stars){
            star.resize(worldBounds);
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
        for(Star star: stars){
            star.update(delta);
        }
    }

    public void draw(){
        batch.begin();
        background.draw(batch);
        for(Star star: stars){
            star.draw(batch);
        }
        buttonExit.draw(batch);
        buttonPlay.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        bg.dispose();
        atlas.dispose();
        fonMusic.dispose();
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
