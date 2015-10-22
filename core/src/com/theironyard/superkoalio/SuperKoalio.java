package com.theironyard.superkoalio;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class SuperKoalio extends ApplicationAdapter {
    SpriteBatch batch;
    TextureRegion stand;
    TextureRegion jump;
    Animation walk; //makes the three last images make it look like koala is walking
    FitViewport viewport; //for screen adjustment

    float x = 0;
    float y = 0;
    float xVelocity = 0;
    float yVelocity = 0;
    float time = 0;
    boolean canJump = true;

    final float MAX_VELOCITY = 500;
    final float MAX_JUMP_VELOCITY = 1000;
    final int WIDTH = 18;
    final int HEIGHT = 26;
    final int DRAW_WIDTH = WIDTH*3; //makes koala bear bigger
    final int DRAW_HEIGHT = HEIGHT*3; //makes koala bear bigger
    final float GRAVITY = -50;

    @Override
    public void create () {
        batch = new SpriteBatch();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Texture sheet = new Texture("koalio.png");
        TextureRegion[][] tiles = TextureRegion.split(sheet, WIDTH, HEIGHT); //18 = width, 26 = height, from final above
        stand = tiles[0][0]; //stand image from koalio.png (1st image)
        jump = tiles[0][1]; //jump image from koalio.png (2nd image)
        walk = new Animation(0.1f, tiles[0][2], tiles[0][3], tiles[0][4]); //how long do you want each frame to appear, and what tiles to switch between
    }

    @Override
    public void render () {
        move();
        draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    void move() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && canJump) {
            yVelocity = MAX_JUMP_VELOCITY;
            canJump = false; //making it so koala can only jump once.
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) { //down action not really needed for a side scroll game.
            yVelocity = MAX_VELOCITY * -1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            xVelocity = MAX_VELOCITY;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            xVelocity = MAX_VELOCITY * -1;
        }

        yVelocity += GRAVITY;

        x += xVelocity * Gdx.graphics.getDeltaTime();
        y += yVelocity * Gdx.graphics.getDeltaTime();

        if (y < 0) {
            y = 0; //so koala bear doesn't fall off the screen from gravity.
            canJump = true; //when koala reaches the ground, he can jump again.
        }

        xVelocity *= 0.8;
        yVelocity *= 0.9;
    }

    void draw () {
        time += Gdx.graphics.getDeltaTime(); //lets us track time passed.

        TextureRegion img;
        if (y > 0) {
            img = jump;
        } else if (Math.round(xVelocity) != 0) {
            img = walk.getKeyFrame(time, true); //time
        } else {
            img = stand;
        }

        Gdx.gl.glClearColor(0.5f, (float)0.5, 1, 1); //blue, not transparent, two ways shown here to cast float when it expects a double.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        if (xVelocity >= 0) {
            batch.draw(img, x, y, DRAW_WIDTH, DRAW_HEIGHT);
        } else {
            batch.draw(img, x + DRAW_WIDTH, y, DRAW_WIDTH * -1, DRAW_HEIGHT);
        }
        batch.end();
    }
}
