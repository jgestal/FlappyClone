package es.gestal.flappyclone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;

public class Bird implements GameElement, Drawable {

    static final float impulse = -30;
    static final float gravity = 2;

    float y;
    float x;

    float vy;

    BirdListener listener;

    Texture[] sprites;

    Circle collisionCircle;

    public Bird (BirdListener listener)  {

        this.listener = listener;

        sprites = new Texture[2];
        sprites[0] = new Texture("bird.png");
        sprites[1] = new Texture("bird2.png");

        collisionCircle = new Circle();
    }

    public void applyImpulse() {

        vy = Bird.impulse;
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

        vy = vy + gravity;
        y -= vy;

        float maxHeight = Gdx.graphics.getHeight() - sprites[0].getHeight();
        float minHeight = 0;

        if (y > maxHeight) {
            y = maxHeight;
        } else if (y <= minHeight) {
            listener.die();
            SoundManager.play(SFX.HIT);
        }

        collisionCircle.set(x + sprites[0].getWidth() / 2,y + sprites[0].getHeight() / 2,sprites[0].getHeight() / 2);
    }

    public Circle getCollisionCircle() {

        return collisionCircle;
    }
}
