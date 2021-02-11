package es.gestal.flappyclone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;

public class Bird implements GameElement, Drawable {

    private static final float IMPULSE = -30;
    private static final float GRAVITY = 2;

    private float y;
    private float x;
    private float vy;

    private Texture[] sprites;

    public Bird ()  {
        sprites = new Texture[2];
        sprites[0] = new Texture("bird.png");
        sprites[1] = new Texture("bird2.png");
    }

    public void applyImpulse() {

        vy = Bird.IMPULSE;
    }

    @Override
    public void start() {

        y = Gdx.graphics.getHeight() / 2 - sprites[0].getHeight() / 2;
        x = Gdx.graphics.getWidth() / 2 - sprites[0].getWidth() / 2;
    }

    @Override
    public void draw(SpriteBatch batch) {

        int flapState = vy > 0 ? 0 : 1;
        batch.draw(sprites[flapState], x, y);
    }

    @Override
    public void update() {
        vy = vy + GRAVITY;
        y -= vy;
    }

    public float getHeight() {
        return sprites[0].getHeight();
    }

    public float getY() {
        return y;
    }

    public Circle getCollisionCircle() {
        return new Circle(x + sprites[0].getWidth() / 2,y + sprites[0].getHeight() / 2,sprites[0].getHeight() / 2);
    }
}
