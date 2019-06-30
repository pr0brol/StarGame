package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.EnemyPool;
import ru.geekbrains.sprite.Enemy;
import ru.geekbrains.sprite.Gameground;
import ru.geekbrains.sprite.MainShip;
import ru.geekbrains.sprite.Star;
import ru.geekbrains.utils.EnemyGenerator;

public class GameScreen extends BaseScreen {

    private static final int STAR_COUNT = 64;

    private Texture gg;
    private TextureAtlas atlas;
    private Gameground gameground;

    private Star[] stars;

    private BulletPool bulletPool;
    private EnemyPool enemyPool;

    private EnemyGenerator enemyGenerator;

    private MainShip mainShip;
    private Sound laserSound;
    private Sound bulletSound;

    @Override
    public void show() {
        super.show();
        gg = new Texture("texture/game.jpg");
        gameground = new Gameground(new TextureRegion(gg));
        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        atlas = new TextureAtlas("texture/mainAtlas/mainAtlas.tpack");
        stars = new Star[STAR_COUNT];
        for(int i = 0; i < stars.length; i++){
            stars[i] = new Star(atlas);
        }
        bulletPool = new BulletPool();
        enemyPool = new EnemyPool(bulletPool, bulletSound, worldBounds);
        enemyGenerator = new EnemyGenerator(atlas, enemyPool, worldBounds);
        mainShip = new MainShip(atlas, bulletPool, laserSound);
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
        mainShip.update(delta);
        bulletPool.updateActiveSprites(delta);
        enemyPool.updateActiveSprites(delta);
        enemyGenerator.generate(delta);
        for(Enemy enemy: enemyPool.getActiveObjects()){
            if(!mainShip.isOutside(enemy)){
                enemy.destroy();
            }
        }
    }

    public void freeAllDestroyedSprites(){
        bulletPool.freeAllDestroyedActiveSprites();
        enemyPool.freeAllDestroyedActiveSprites();
    }

    public void draw(){
        batch.begin();
        gameground.draw(batch);
        for(Star star: stars){
            star.draw(batch);
        }
        mainShip.draw(batch);
        bulletPool.drawActiveSprites(batch);
        enemyPool.drawActiveSprites(batch);
        batch.end();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        gameground.resize(worldBounds);
        for(Star star: stars){
            star.resize(worldBounds);
        }
        mainShip.resize(worldBounds);
    }

    @Override
    public void dispose() {
        gg.dispose();
        atlas.dispose();
        bulletPool.dispose();
        bulletSound.dispose();
        enemyPool.dispose();
        super.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        mainShip.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        mainShip.keyUp(keycode);
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        mainShip.touchDown(touch, pointer);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        mainShip.touchUp(touch, pointer);
        return false;
    }
}
