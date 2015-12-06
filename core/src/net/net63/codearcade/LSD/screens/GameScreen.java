package net.net63.codearcade.LSD.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.managers.LevelManager;
import net.net63.codearcade.LSD.screens.overlays.GameOverScreen;
import net.net63.codearcade.LSD.screens.overlays.LevelCompleteScreen;
import net.net63.codearcade.LSD.utils.CentreGUI;
import net.net63.codearcade.LSD.utils.Constants;
import net.net63.codearcade.LSD.utils.GUIBuilder;
import net.net63.codearcade.LSD.world.GameWorld;

/**
 * The main game screen class that represents the state when the
 * game is being played
 * 
 * @author Basim
 *
 */
public class GameScreen extends AbstractScreen {

    private static final float SLOW_MO_TIME_GAP = 1.5f;
    private static final float DEATH_TIME_GAP = 3f;

    private static final float SLOW_MO = 0.35f;

    private CentreGUI centreGUI;
    private GameWorld gameWorld;
    private Label scoreLabel;


    private int levelId;
    private boolean logicPaused = false;

    private float gameOverTime = 0f;

	public GameScreen(LSD game, int levelId) {
		super(game);

        this.levelId = levelId;

        //Create a new world with the map at the current level
        gameWorld = new GameWorld(LevelManager.getLevel(Constants.DEFAULT_PACK, levelId));
        setupUI();
	}

    @Override
    public void pauseLogic() {
        if (!logicPaused) gameWorld.pauseLogic();
        logicPaused = true;
    }

    @Override
    public void resumeLogic() {
        if (logicPaused) gameWorld.resumeLogic();
        logicPaused = false;
    }

    /**
     * The level that is being played by this state
     *
      * @return The current level
     */
    public int getLevelId() { return levelId; }

    private void setupUI() {
        //Create and position the score the label at the top centre
        scoreLabel = GUIBuilder.createLabel("", 50, Color.YELLOW);
        scoreLabel.setPosition((800 - scoreLabel.getWidth()) / 2.0f , 550 - scoreLabel.getHeight());

        //Create the GUI manager and add listeners
        centreGUI = new CentreGUI();
        centreGUI.getStage().addActor(scoreLabel);
        centreGUI.getStage().addListener(new GameEventListener());
    }

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

        //Update the gui viewport and the game world
        gameWorld.resize(width, height);
		centreGUI.resize(width, height);
	}

    @Override
    public void show() { if (logicPaused) resumeLogic(); }

    @Override
    public void hide() { pauseLogic(); }

	@Override
	public void render(float delta) {
		super.render(delta);

        //Render / complete logic (in slow motion if game is finished)
        if (gameWorld.isGameOver() && gameOverTime < SLOW_MO_TIME_GAP) {
            gameWorld.update(delta * SLOW_MO);
        } else {
            gameWorld.update(delta);
        }

        //Update UI elements
        updateScore();

        //Handle user events and render the GUI
        centreGUI.render(delta);

        //Check if the game is finished
        if (!logicPaused && gameWorld.isGameOver()) {
            if (gameOverTime == 0) gameWorld.stopShake();
            gameOverTime += delta;

            if (gameOverTime > DEATH_TIME_GAP) {
                //Stop game logic
                pauseLogic();

                //Either level over or level completed
                if (gameWorld.isGameWon()) game.setScreen(new LevelCompleteScreen(game, this));
                else game.setScreen(new GameOverScreen(game, this));
            }

        }
	}

    @Override
    public void dispose() {
        super.dispose();

        //Dispose assets from members
        gameWorld.dispose();
        centreGUI.dispose();
    }

    private void updateScore() {
        scoreLabel.setText(gameWorld.getScore());
    }


    private class GameEventListener extends ActorGestureListener {

        @Override
        public boolean handle(Event event) {
            if (logicPaused) return false;
            return super.handle(event);
        }

        @Override
        public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
            gameWorld.aimPlayer(Gdx.input.getX(), Gdx.input.getY());
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            gameWorld.launchPlayer();
        }

        @Override
        public void pan (InputEvent event, float x, float y, float deltaX, float deltaY) {
            gameWorld.aimPlayer(Gdx.input.getX(), Gdx.input.getY());
        }

        @Override
        public void zoom(InputEvent event, float initialDistance, float distance) {
            gameWorld.applyZoom(initialDistance / distance);
        }
    }
}
