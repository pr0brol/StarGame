package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.util.List;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.AidPool;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.EnemyPool;
import ru.geekbrains.pool.ExplosionPool;
import ru.geekbrains.sprite.Aid;
import ru.geekbrains.sprite.Bullet;
import ru.geekbrains.sprite.ButtonNewGame;
import ru.geekbrains.sprite.Enemy;
import ru.geekbrains.sprite.GameOver;
import ru.geekbrains.sprite.GameGround;
import ru.geekbrains.sprite.MainShip;
import ru.geekbrains.sprite.Star;
import ru.geekbrains.sprite.TrackingStar;
import ru.geekbrains.utils.AidGenerator;
import ru.geekbrains.utils.EnemyGenerator;
import ru.geekbrains.utils.Font;

public class GameScreen extends BaseScreen {

    private static final String FRAGS = "Frags: ";
    private static final String HP = "HP: ";
    private static final String LEVEL = "Level: ";
    private static final String SCORE = "Score: ";
    private enum State {PLAYING, PAUSE, GAME_OVER}
    private static final int STAR_COUNT = 64;

    Music gameMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/GameSound.mp3"));

    private Font font;
    private Texture gg;
    private TextureAtlas aidAtlas;
    private TextureAtlas atlas;
    private GameGround gameground;

    private TrackingStar[] stars;

    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private ExplosionPool explosionPool;
    private AidPool aidPool;

    private EnemyGenerator enemyGenerator;
    private AidGenerator aidGenerator;

    private MainShip mainShip;
    private Sound laserSound;
    private Sound bulletSound;
    private Sound explosionSound;
    private Sound aidSound;

    private ButtonNewGame buttonNewGame;
    private GameOver gameOver;

    private State state;
    private State oldState;

    private int frags = 0;
    private StringBuilder sbFrags;
    private StringBuilder sbHP;
    private StringBuilder sbLevel;
    private StringBuilder sbScore;

    @Override
    public void show() {
        super.show();
        font = new Font("font/font.fnt", "font/font.png");
        font.setSize(0.03f);
        gg = new Texture("texture/game.jpg");
        aidAtlas  = new TextureAtlas("texture/aidAtlas/health.pack");
        gameground = new GameGround(new TextureRegion(gg));
        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        aidSound = Gdx.audio.newSound(Gdx.files.internal("sounds/aidsound.mp3"));
        atlas = new TextureAtlas("texture/mainAtlas/mainAtlas.tpack");
        stars = new TrackingStar[STAR_COUNT];
        bulletPool = new BulletPool();
        explosionPool = new ExplosionPool(atlas, explosionSound);
        enemyPool = new EnemyPool(bulletPool, explosionPool, bulletSound, worldBounds);
        aidPool = new AidPool(aidAtlas);
        enemyGenerator = new EnemyGenerator(atlas, enemyPool, worldBounds);
        aidGenerator = new AidGenerator(aidAtlas, aidPool, worldBounds);
        mainShip = new MainShip(atlas, bulletPool, explosionPool, laserSound);
        gameOver = new GameOver(atlas);
        buttonNewGame = new ButtonNewGame(atlas, this);
        sbFrags = new StringBuilder();
        sbHP = new StringBuilder();
        sbLevel = new StringBuilder();
        sbScore = new StringBuilder();
        for(int i = 0; i < stars.length; i++){
            stars[i] = new TrackingStar(atlas, mainShip.getSpeed());
        }
        gameMusic.play();
        gameMusic.setLooping(true);
        state = State.PLAYING;
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
        if(state == State.PLAYING) {
            mainShip.update(delta);
            bulletPool.updateActiveSprites(delta);
            enemyPool.updateActiveSprites(delta);
            aidPool.updateActiveSprites(delta);
            enemyGenerator.generate(delta, enemyGenerator.getScore());
            aidGenerator.generate(delta);
        }
    }

    private void checkCollisions(){
        if(state != State.PLAYING){
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
                mainShip.damage(mainShip.getHP());
                state = State.GAME_OVER;
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

                        if(enemy.isDestroyed()){
                            frags++;
                            enemyGenerator.setScore(enemyGenerator.getScore() + enemy.getDamage());
                        }
                        bullet.destroy();
                    }
                }
            }else {
                if(mainShip.isBulletCollision(bullet)){
                    mainShip.damage(bullet.getDamage());
                    if(mainShip.isDestroyed()){
                        state = State.GAME_OVER;
                    }
                    bullet.destroy();
                }
            }
        }

        List<Aid> aidList = aidPool.getActiveObjects();
        for(Aid aid: aidList){
            float aidDist = aid.getHalfWidth() + mainShip.getHalfWidth();
            if(mainShip.pos.dst(aid.pos) < aidDist){
                aid.destroy();
                aidSound.play();
                mainShip.setHP(aid.getHealth());
                enemyGenerator.setScore(enemyGenerator.getScore() + (aid.getHealth() / 10));
            }
        }
    }

    public void freeAllDestroyedSprites(){
        bulletPool.freeAllDestroyedActiveSprites();
        enemyPool.freeAllDestroyedActiveSprites();
        explosionPool.freeAllDestroyedActiveSprites();
        aidPool.freeAllDestroyedActiveSprites();
    }

    public void draw(){
        batch.begin();
        gameground.draw(batch);
        for(Star star: stars){
            star.draw(batch);
        }
        if(state == State.PLAYING) {
            mainShip.draw(batch);
            bulletPool.drawActiveSprites(batch);
            enemyPool.drawActiveSprites(batch);
            aidPool.drawActiveSprites(batch);
        }else if(state == State.GAME_OVER){
            gameOver.draw(batch);
            buttonNewGame.draw(batch);
        }
        explosionPool.drawActiveSprites(batch);
        printInfo();
        batch.end();
    }

    private void printInfo(){
        sbFrags.setLength(0);
        sbHP.setLength(0);
        sbLevel.setLength(0);
        sbScore.setLength(0);
        font.draw(batch, sbFrags.append(FRAGS).append(frags), worldBounds.getLeft(), worldBounds.getTop());
        font.draw(batch, sbHP.append(HP).append(mainShip.getHP()), worldBounds.pos.x, worldBounds.getTop(), Align.center);
        font.draw(batch, sbLevel.append(LEVEL).append(enemyGenerator.getLevel()), worldBounds.getRight(), worldBounds.getTop(), Align.right);
        font.draw(batch, sbScore.append(SCORE).append(enemyGenerator.getScore()), worldBounds.getLeft(), worldBounds.getTop() - 0.05f);
    }

    @Override
    public void pause() {
        super.pause();
        oldState = state;
        state = State.PAUSE;
        gameMusic.pause();
    }

    @Override
    public void resume() {
        super.resume();
        state = oldState;
        gameMusic.play();
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
        font.dispose();
        gg.dispose();
        atlas.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        explosionPool.dispose();
        aidPool.dispose();
        explosionSound.dispose();
        laserSound.dispose();
        bulletSound.dispose();
        aidSound.dispose();
        gameMusic.dispose();
        super.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if(state == State.PLAYING){
            mainShip.keyDown(keycode);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(state == State.PLAYING) {
            mainShip.keyUp(keycode);
        }
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if(state == State.PLAYING){
            mainShip.touchDown(touch, pointer);
        }else if(state == State.GAME_OVER){
            buttonNewGame.touchDown(touch, pointer);
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if(state == State.PLAYING) {
            mainShip.touchUp(touch, pointer);
        }else if(state == State.GAME_OVER){
            buttonNewGame.touchUp(touch, pointer);
        }
        return false;
    }

    public void startNewGame(){
        mainShip.setToNewGame(worldBounds);

        state = State.PLAYING;
        frags = 0;
        enemyGenerator.setScore(0);
        enemyGenerator.setLevel(1);
        aidPool.freeAllActiveSprites();
        bulletPool.freeAllActiveSprites();
        enemyPool.freeAllActiveSprites();
        explosionPool.freeAllActiveSprites();
    }
}
