package es.gestal.flappyclone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {

    static Sound scoreSFX;
    static Sound jumpSFX;
    static Sound hitSFX;

    public static void loadSFX() {
        scoreSFX = Gdx.audio.newSound(Gdx.files.internal("score.mp3"));
        jumpSFX = Gdx.audio.newSound(Gdx.files.internal("jump.mp3"));
        hitSFX = Gdx.audio.newSound(Gdx.files.internal("hit.mp3"));
    }

    public static void play(SFX sfx) {
        switch (sfx) {
            case SCORE:
                scoreSFX.play();
                break;
            case HIT:
                hitSFX.play();
                break;
            case JUMP:
                jumpSFX.play();
                break;
        }
    }
}
