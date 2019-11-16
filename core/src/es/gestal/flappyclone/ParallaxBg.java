package es.gestal.flappyclone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ParallaxBg implements GameElement, Drawable {

    static public final String[] backgroundFiles = {"clouds_bg.png", "graybuildings_bg.png", "city_bg.png", "grass_bg.png"};

    Texture texture;

    float x;
    float vx;

    public ParallaxBg(String bgName, float vx) {

        texture = new Texture(bgName);
        this.vx = vx;
    }

    @Override
    public void start() {

        x = 0;
    }

    @Override
    public void draw(SpriteBatch batch) {

        batch.draw(texture,x,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(texture,x+Gdx.graphics.getWidth(), 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void update() {

        x -= vx;
        if (x + Gdx.graphics.getWidth() < 0) {
            x += Gdx.graphics.getWidth();
        }
    }
}
