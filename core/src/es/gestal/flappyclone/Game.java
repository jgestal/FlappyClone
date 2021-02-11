package es.gestal.flappyclone;

/**
 * The Flappy Clone is just a clone of the Flappy Bird
 * game and was created with GDX Lib.
 *
 * @author  Juan Gestalt
 * @version 1.0
 * @since   2019-11-16
 */

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class Game extends ApplicationAdapter implements TubeObstacleListener {

	SpriteBatch batch;
	GameState gameState;

	int score;

	Bird bird;
	ParallaxBg[] backgrounds = new ParallaxBg[4];
	TubeObstacle[] tubes = new TubeObstacle[TubeObstacle.NUMBER_OF_TUBES];

	ScreenText scoreText;
	ScreenText screenText;

	@Override
	public void create () {

		batch = new SpriteBatch();

		SoundManager.loadSFX();

		bird = new Bird();
		createBackgrounds();
		createTubeObstacles();

		scoreText = new ScreenText(Gdx.graphics.getWidth() / 2,Gdx.graphics.getHeight() - 100);
		screenText = new ScreenText(Gdx.graphics.getWidth() / 2, 300);

		start();
	}

	private void createBackgrounds() {
		for (int i=0; i<ParallaxBg.backgroundFiles.length; i++) {
			backgrounds[i] = new ParallaxBg(ParallaxBg.backgroundFiles[i],i*1.1f+.25f);
		}
	}

	private void createTubeObstacles() {
		for (int i = 0; i<TubeObstacle.NUMBER_OF_TUBES; i++) {
			tubes[i] = new TubeObstacle(this,i);
		}
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

	@Override
	public void render () {
		update();
		draw();
	}

	private void update() {

		switch(gameState) {

			case PLAY:
				playUpdate();
				break;

			case TAP_TO_PLAY:
				tapToPlayUpdate();
				break;

			case GAME_OVER:
				gameOverUpdate();
				break;
		}
	}

	private void playUpdate() {
		if (!isBirdCollidingWithTube() && !isBirdOutOfBounds()) {
			readPlayerInput();
			updateGameElements();
		}
		else {
			gameOver();
		}
	}

	private void readPlayerInput() {
		if (Gdx.input.justTouched()) {
			bird.applyImpulse();
			SoundManager.play(SFX.JUMP);
		}
	}

	private boolean isBirdCollidingWithTube() {
		Circle birdCircle = bird.getCollisionCircle();
		for (TubeObstacle tube : tubes) {
			Rectangle[] tubeRectangles = tube.collisionRectangles();
			if (Intersector.overlaps(birdCircle, tubeRectangles[0]) || Intersector.overlaps(birdCircle, tubeRectangles[1])) {
				return true;
			}
		}
		return false;
	}

	private boolean isBirdOutOfBounds() {

		float birdHeight = bird.getHeight();
		float birdY = bird.getY();

		float maxHeight = Gdx.graphics.getHeight() - birdHeight;
		float minHeight = 0;

		return birdY > maxHeight || birdY <= minHeight;
	}

	private void updateGameElements() {

		bird.update();

		for (TubeObstacle tube : tubes) {
			tube.update();
		}

		for (ParallaxBg background: backgrounds) {
			background.update();
		}
	}

	private void tapToPlayUpdate() {

		if (Gdx.input.justTouched()) {
			gameState = GameState.PLAY;
			bird.applyImpulse();
			SoundManager.play(SFX.JUMP);
		}
	}

	private void gameOverUpdate() {

		if (Gdx.input.justTouched()) {
			start();
			gameState = GameState.TAP_TO_PLAY;
		}
	}

	private void draw() {

		batch.begin();

		drawBackgrounds(batch);
		bird.draw(batch);
		drawTubes(batch);
		drawScreenText(batch);

		//renderShapes();

		batch.end();
	}

	private void drawBackgrounds(SpriteBatch batch) {
		for (ParallaxBg background: backgrounds) {
			background.draw(batch);
		}
	}

	private void drawTubes(SpriteBatch batch) {
		for (TubeObstacle tube : tubes) {
			tube.draw(batch);
		}
	}

	private void drawScreenText(SpriteBatch batch) {

		if (gameState == GameState.PLAY) {
			scoreText.draw(batch);
		}
		else {
			screenText.setText(gameState == GameState.TAP_TO_PLAY ? "TAP TO PLAY" : "GAME OVER");
			screenText.draw(batch);
		}
	}

	public void gameOver() {
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
