package ru.geekbrains.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.EnemyPool;
import ru.geekbrains.pool.ExplosionPool;
import ru.geekbrains.sprite.Bullet;
import ru.geekbrains.sprite.ButtonNewGame;
import ru.geekbrains.sprite.Enemy;
import ru.geekbrains.sprite.GameOver;
import ru.geekbrains.sprite.Gameground;
import ru.geekbrains.sprite.MainShip;
import ru.geekbrains.sprite.Star;
import ru.geekbrains.utils.EnemyGenerator;

public class GameScreen extends BaseScreen {

    private static final int STAR_COUNT = 64;

    Music gameMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/GameSound.mp3"));

    private Texture gg;
    private TextureAtlas atlas;
    private Gameground gameground;

    private Star[] stars;

    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private ExplosionPool explosionPool;

    private EnemyGenerator enemyGenerator;

    private MainShip mainShip;
    private Sound laserSound;
    private Sound bulletSound;
    private Sound explosionSound;

    private ButtonNewGame buttonNewGame;
    private GameOver gameOver;

    private Game game;

    public GameScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        super.show();
        gg = new Texture("texture/game.jpg");
        gameground = new Gameground(new TextureRegion(gg));
        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        atlas = new TextureAtlas("texture/mainAtlas/mainAtlas.tpack");
        stars = new Star[STAR_COUNT];
        for(int i = 0; i < stars.length; i++){
            stars[i] = new Star(atlas);
        }
        bulletPool = new BulletPool();
        explosionPool = new ExplosionPool(atlas, explosionSound);
        enemyPool = new EnemyPool(bulletPool, explosionPool, bulletSound, worldBounds);
        enemyGenerator = new EnemyGenerator(atlas, enemyPool, worldBounds);
        mainShip = new MainShip(atlas, bulletPool, explosionPool, laserSound);
        buttonNewGame = new ButtonNewGame(atlas, game);
        gameOver = new GameOver(atlas);
        gameMusic.play();
        gameMusic.setLooping(true);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        checkCollisions();
        freeAllDestroyedSprites();
        draw();
    }

    public void update(float delta) {
        for(Star star: stars){
            star.update(delta);
        }
        explosionPool.updateActiveSprites(delta);
        if(!mainShip.isDestroyed()) {
            mainShip.update(delta);
            bulletPool.updateActiveSprites(delta);
            enemyPool.updateActiveSprites(delta);
            enemyGenerator.generate(delta);
        }else {
            gameOver.update(delta);
            buttonNewGame.update(delta);
        }
    }

    private void checkCollisions(){
        if(mainShip.isDestroyed()){
            return;
        }

        List<Enemy> enemyList = enemyPool.getActiveObjects();
        for(Enemy enemy : enemyList){
            if(enemy.isDestroyed()){
                continue;
            }
            float minDist = enemy.getHalfWidth() + mainShip.getHalfWidth();
            if(mainShip.pos.dst(enemy.pos) < minDist){
                enemy.destroy();
                mainShip.destroy();
            }
        }

        List<Bullet> bulletList = bulletPool.getActiveObjects();
        for(Bullet bullet : bulletList){
            if(bullet.isDestroyed()){
                continue;
            }
            if(bullet.getOwner() == mainShip){
                for(Enemy enemy : enemyList){
                    if(enemy.isDestroyed()){
                        continue;
                    }
                    if(enemy.isBulletCollision(bullet)){
                        enemy.damage(bullet.getDamage());
                        bullet.destroy();
                    }
                }
            }else {
                if(mainShip.isBulletCollision(bullet)){
                    mainShip.damage(bullet.getDamage());
                    bullet.destroy();
                }
            }
        }
    }

    public void freeAllDestroyedSprites(){
        bulletPool.freeAllDestroyedActiveSprites();
        enemyPool.freeAllDestroyedActiveSprites();
        explosionPool.freeAllDestroyedActiveSprites();
    }

    public void draw(){
        batch.begin();
        gameground.draw(batch);
        for(Star star: stars){
            star.draw(batch);
        }
        if(!mainShip.isDestroyed()) {
            mainShip.draw(batch);
            bulletPool.drawActiveSprites(batch);
            enemyPool.drawActiveSprites(batch);
        }else {
            gameOver.draw(batch);
            buttonNewGame.draw(batch);
        }
        explosionPool.drawActiveSprites(batch);
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
        gameOver.resize(worldBounds);
        buttonNewGame.resize(worldBounds);
    }

    @Override
    public void dispose() {
        gg.dispose();
        atlas.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        explosionPool.dispose();
        explosionSound.dispose();
        laserSound.dispose();
        bulletSound.dispose();
        super.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if(!mainShip.isDestroyed()){
            mainShip.keyDown(keycode);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(!mainShip.isDestroyed()) {
            mainShip.keyUp(keycode);
        }
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if(!mainShip.isDestroyed()){
            mainShip.touchDown(touch, pointer);
        }else {
            buttonNewGame.touchDown(touch, pointer);
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if(!mainShip.isDestroyed()) {
            mainShip.touchUp(touch, pointer);
        }else {
            buttonNewGame.touchUp(touch, pointer);
        }
        return false;
    }
}
