package es.gestal.flappyclone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;


import java.util.Random;

public class TubeObstacle implements GameElement, Drawable {

    static final float GAP = 400;
    static final float VX = 4;
    static final float DISTANCE_BETWEEN = 800;
    static final int NUMBER_OF_TUBES = 4;
    static final Random RANDOM_GENERATOR = new Random();

    static int scoringTube = 0;

    private static Texture texture = new Texture("tube.png");

    private static float MAX_OFFSET = (texture.getHeight() * 2 + GAP) - Gdx.graphics.getHeight();

    private Sprite topTube = new Sprite(texture);
    private Sprite bottomTube = new Sprite(texture);

    private int index;
    private float offset;

    private TubeObstacleListener listener;

    public TubeObstacle(TubeObstacleListener listener, int index) {

        this.listener = listener;
        this.index = index;

        topTube.setRotation(180);
    }

    @Override
    public void start() {
        randomizeGap();
        scoringTube = 0;
        setX(Gdx.graphics.getWidth() + index * DISTANCE_BETWEEN);
    }

    public void recycle() {
        setX(topTube.getX() + NUMBER_OF_TUBES * DISTANCE_BETWEEN);
    }

    @Override
    public void draw(SpriteBatch batch) {
        topTube.draw(batch);
        bottomTube.draw(batch);
    }

    @Override
    public void update() {

        if (topTube.getX() + texture.getWidth() / 2 < Gdx.graphics.getWidth() / 2 && scoringTube % NUMBER_OF_TUBES == index) {
            listener.tubePassesHalfScreen();
            scoringTube += 1;
        }

        if (topTube.getX() + texture.getWidth() < 0) {
            recycle();
        }

        setX(topTube.getX() - VX);
    }

    private void randomizeGap() {
        offset = RANDOM_GENERATOR.nextFloat() * MAX_OFFSET;
        topTube.setY(texture.getHeight() + GAP - offset);
        bottomTube.setY(-offset);
    }

    public void setX(float x) {
        topTube.setX(x);
        bottomTube.setX(x);
    }

    public Rectangle[] collisionRectangles() {

        return new Rectangle[] {
                new Rectangle(topTube.getX(), topTube.getY(), topTube.getWidth(), topTube.getHeight()),
                new Rectangle(bottomTube.getX(), bottomTube.getY(), bottomTube.getWidth(), bottomTube.getHeight())
        };
    }
}
