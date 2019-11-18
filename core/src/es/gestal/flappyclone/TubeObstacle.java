package es.gestal.flappyclone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;


import java.util.Random;

public class TubeObstacle implements GameElement, Drawable {

    static final float gap = 400;
    static final float vx = 4;
    static final float distanceBetween = 800;
    static final int numberOfTubes = 4;
    static final Random randomGenerator = new Random();

    static int scoringTube = 0;

    Texture texture;

    Sprite topTube;
    Sprite bottomTube;

    static float maxOffset;
    float offset;

    float x;

    int index;

    TubeObstacleListener listener;

    Rectangle topTubeCollisionRectangle;
    Rectangle bottomTubeCollisionRectangle;

    public TubeObstacle(TubeObstacleListener listener, int index) {

        this.listener = listener;
        this.index = index;

        texture = new Texture("tube.png");

        topTube = new Sprite(texture);
        topTube.setRotation(180);

        bottomTube = new Sprite(texture);

        maxOffset = (texture.getHeight() * 2 + gap) - Gdx.graphics.getHeight();

        topTubeCollisionRectangle = new Rectangle();
        bottomTubeCollisionRectangle = new Rectangle();
    }

    @Override
    public void start() {
        randomizeGap();
        scoringTube = 0;
        setX(Gdx.graphics.getWidth() + index * distanceBetween);
    }

    public void recycle() {
        setX(x + numberOfTubes * distanceBetween);
    }

    @Override
    public void draw(SpriteBatch batch) {
        topTube.draw(batch);
        bottomTube.draw(batch);
    }

    @Override
    public void update() {

        if (x + texture.getWidth() / 2 < Gdx.graphics.getWidth() / 2 && scoringTube % numberOfTubes == index) {
            listener.incScore();
            scoringTube += 1;
        }

        if (x + texture.getWidth() < 0) {
            recycle();
        }

        setX(x - vx);
    }

    public void randomizeGap() {
        offset = randomGenerator.nextFloat() * maxOffset;
        topTube.setY(texture.getHeight() + gap - offset);
        bottomTube.setY(-offset);
    }



    public void setX(float x) {
        this.x = x;
        topTube.setX(x);
        bottomTube.setX(x);
    }

    public Rectangle[] collisionRectangles() {

        Rectangle[] rectangles = new Rectangle[2];

        rectangles[0] = topTubeCollisionRectangle.set(topTube.getX(), topTube.getY(), topTube.getWidth(), topTube.getHeight());
        rectangles[1] = bottomTubeCollisionRectangle.set(bottomTube.getX(), bottomTube.getY(), bottomTube.getWidth(), bottomTube.getHeight());

        return rectangles;
    }
}
