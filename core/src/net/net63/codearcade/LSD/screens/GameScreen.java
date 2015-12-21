package net.net63.codearcade.LSD.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.managers.Assets;
import net.net63.codearcade.LSD.managers.LevelManager;
import net.net63.codearcade.LSD.screens.overlays.GameOverScreen;
import net.net63.codearcade.LSD.screens.overlays.LevelCompleteScreen;
import net.net63.codearcade.LSD.screens.overlays.PauseScreen;
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

    private static final Vector3 tmp = new Vector3();

    private static final float SLOW_MO_TIME_GAP = 1.5f;
    private static final float DEATH_TIME_GAP = 3f;

    private static final float SLOW_MO = 0.35f;

    private GameWorld gameWorld;

    private CentreGUI centreGUI;
    private Label scoreLabel;
    private ImageButton pauseButton;

    private int levelId;
    private boolean logicPaused = false;
    private boolean pauseClicked = false;

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

        Gdx.input.setInputProcessor(centreGUI.getStage());
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

        //Create the pause button in the top right corner
        pauseButton = GUIBuilder.createButton(Assets.Buttons.PAUSE);
        pauseButton.setSize(30, 45);
        pauseButton.setTouchable(Touchable.enabled);
        pauseButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                pauseClicked = true;
            }

        });

        //Create the GUI manager and add listeners
        centreGUI = new CentreGUI();
        Stage stage = centreGUI.getStage();
        stage.addActor(scoreLabel);
        stage.addActor(pauseButton);
        stage.addListener(new GameEventListener());
    }

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

        //Update the gui viewport and the game world
        gameWorld.resize(width, height);
		centreGUI.resize(width, height);

        //Set the pause button to be in the right corner
        Viewport viewport = centreGUI.getStage().getViewport();
        tmp.set(width, 0, 0);
        viewport.getCamera().unproject(tmp);
        pauseButton.setPosition(tmp.x - 20 - pauseButton.getWidth(), tmp.y - 15 - pauseButton.getHeight());
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
        scoreLabel.setText(gameWorld.getScore());
        pauseButton.setChecked(pauseButton.isOver());

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

        //Check if pausing is needed
        if (pauseClicked) {
            pauseLogic();
            pauseClicked = false;
            game.setScreen(new PauseScreen(game, this));
        }
	}

    @Override
    public void dispose() {
        super.dispose();

        //Dispose assets from members
        gameWorld.dispose();
        centreGUI.dispose();
    }


    private class GameEventListener extends ActorGestureListener {

        private boolean zooming = false;

        @Override
        public boolean handle(Event event) {
            tmp.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            centreGUI.getStage().getViewport().getCamera().unproject(tmp);

            if (logicPaused || centreGUI.getStage().hit(tmp.x, tmp.y, true) != null ) return false;
            return super.handle(event);
        }

        @Override
        public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
            if (!zooming) {
                gameWorld.aimPlayer(Gdx.input.getX(), Gdx.input.getY());
            }
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            gameWorld.launchPlayer();
            zooming = false;
        }

        @Override
        public void pan (InputEvent event, float x, float y, float deltaX, float deltaY) {
            if (!zooming) {
                gameWorld.aimPlayer(Gdx.input.getX(), Gdx.input.getY());
            }
        }

        @Override
        public void zoom(InputEvent event, float initialDistance, float distance) {
            gameWorld.applyZoom(initialDistance / distance);
            zooming = true;
        }
    }
}
