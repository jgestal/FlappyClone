/**
 * The Flappy Clone is just a clone of the Flappy Bird
 * game and was created with GDX Lib.
 *
 * @author  Juan Gestal
 * @version 1.0
 * @since   2019-11-16
 */

package es.gestal.flappyclone;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class Game extends ApplicationAdapter implements BirdListener, TubeObstacleListener {

	SpriteBatch batch;

	GameState gameState;

	int score;

	Bird bird;

	ParallaxBg[] backgrounds = new ParallaxBg[4];
	TubeObstacle[] tubes = new TubeObstacle[TubeObstacle.numberOfTubes];

	ScreenText scoreText;
	ScreenText screenText;

	@Override
	public void create () {

		batch = new SpriteBatch();

		SoundManager.loadSFX();

		bird = new Bird(this);

		for (int i=0; i<ParallaxBg.backgroundFiles.length; i++) {
			backgrounds[i] = new ParallaxBg(ParallaxBg.backgroundFiles[i],i*1.1f+.25f);
		}

		for (int i = 0; i<TubeObstacle.numberOfTubes; i++) {
			tubes[i] = new TubeObstacle(this,i);
		}

		scoreText = new ScreenText(Gdx.graphics.getWidth() / 2,Gdx.graphics.getHeight() - 100);
		screenText = new ScreenText(Gdx.graphics.getWidth() / 2, 300);

		start();

	}

	public void start() {

		gameState = GameState.TAP_TO_PLAY;

		bird.start();

		for (TubeObstacle tube: tubes) {
			tube.start();
		}

		updateScore(0);
	}

	private void updateScore(int newScore) {

		score = newScore;
		scoreText.setText(Integer.toString(score));
	}

	private void update() {

		switch(gameState) {

			case PLAY:

				updateGameElements();
				break;

			case TAP_TO_PLAY:

				if (Gdx.input.justTouched()) {

					gameState = GameState.PLAY;
					bird.applyImpulse();
					SoundManager.play(SFX.JUMP);
				}
				break;

			case GAME_OVER:

				if (Gdx.input.justTouched()) {
					start();
					gameState = GameState.TAP_TO_PLAY;
				}
				break;

		}
	}

	private void updateGameElements() {

		if (Gdx.input.justTouched()) {
			bird.applyImpulse();
			SoundManager.play(SFX.JUMP);
		}

		bird.update();

		for (TubeObstacle tube : tubes) {
			tube.update();
		}

		for (ParallaxBg background: backgrounds) {
			background.update();
		}
	}

	private void draw() {

		batch.begin();

		for (ParallaxBg background: backgrounds) {
			background.draw(batch);
		}

		bird.draw(batch);

		for (TubeObstacle tube : tubes) {
			tube.draw(batch);
		}

		drawScreenText();

		batch.end();

		//renderShapes();
	}

	private void drawScreenText() {

		switch (gameState) {
			case TAP_TO_PLAY:
				screenText.setText("TAP TO PLAY");
				screenText.draw(batch);
				break;


			case PLAY:
				scoreText.draw(batch);
				break;

			case GAME_OVER:
				screenText.setText("GAME OVER");
				screenText.draw(batch);

				scoreText.draw(batch);
				break;
		}
	}

	@Override
	public void render () {

		if (gameState == GameState.PLAY) {
			checkCollisions();
		}

		update();
		draw();
	}

	private void checkCollisions() {
		Circle birdCircle = bird.getCollisionCircle();
		for (TubeObstacle tube : tubes) {
			Rectangle[] tubeRectangles = tube.collisionRectangles();
			if (Intersector.overlaps(birdCircle, tubeRectangles[0]) || Intersector.overlaps(birdCircle, tubeRectangles[1])) {
				die();
				SoundManager.play(SFX.HIT);
				return;
			}
		}
	}



	@Override
	public void die() {

		gameState = GameState.GAME_OVER;
		SoundManager.play(SFX.HIT);
	}

	@Override
	public void incScore() {

		SoundManager.play(SFX.SCORE);
		updateScore(score+1);
	}

	@Override
	public void dispose () {

		batch.dispose();
	}

	/* Method to test collisions
	private void renderShapes() {

		ShapeRenderer shapeRenderer = new ShapeRenderer();

		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);

		Circle birdCircle = bird.getCollisionCircle();
		shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

		shapeRenderer.setColor(Color.BLUE);

		for (TubeObstacle tube: tubes) {

			Rectangle bottom = tube.bottomTubeCollisionRectangle;
			Rectangle top = tube.topTubeCollisionRectangle;

			shapeRenderer.rect(bottom.x, bottom.y, bottom.width, bottom.height);
			shapeRenderer.rect(top.x, top.y, top.width, top.height);
		}

		shapeRenderer.end();
	}
	 */
}
