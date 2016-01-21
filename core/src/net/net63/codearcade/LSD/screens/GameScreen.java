package net.net63.codearcade.LSD.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.managers.Assets;
import net.net63.codearcade.LSD.managers.LevelManager;
import net.net63.codearcade.LSD.screens.overlays.GameOverScreen;
import net.net63.codearcade.LSD.screens.overlays.LevelCompleteScreen;
import net.net63.codearcade.LSD.screens.overlays.PauseScreen;
import net.net63.codearcade.LSD.utils.CentreGUI;
import net.net63.codearcade.LSD.utils.GUIBuilder;
import net.net63.codearcade.LSD.world.GameWorld;
import net.net63.codearcade.LSD.world.LevelDescriptor;

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

    private InputMultiplexer inputMultiplexer;
    private CentreGUI centreGUI;
    private Label scoreLabel;
    private ImageButton pauseButton;

    private TextureRegionDrawable starCollected, starEmpty;
    private Image[] starImages = new Image[3];
    private Table starTable;

    private int levelId;
    private int packId;
    private boolean logicPaused = false;
    private boolean pauseClicked = false;

    private float gameOverTime = 0f;

	public GameScreen(LSD game, int packId, int levelId) {
		super(game);

        this.levelId = levelId;
        this.packId = packId;

        //Create a new world with the map at the current level
        gameWorld = new GameWorld(LevelManager.getLevel(packId, levelId));
        setupUI();

        //Set the input multiplexer so the GUI and the game controls function
        inputMultiplexer = new InputMultiplexer(centreGUI.getStage(), new GestureDetector(5, 0.4f, 1.1f, 0.15f, new GameEventListener()));
        Gdx.input.setInputProcessor(inputMultiplexer);
	}

    @Override
    public void pauseLogic() {
        if (!logicPaused) gameWorld.pauseLogic();

        //Prevent input events
         if (Gdx.input.getInputProcessor() == inputMultiplexer) Gdx.input.setInputProcessor(null);

        logicPaused = true;
    }

    @Override
    public void resumeLogic() {
        if (logicPaused) gameWorld.resumeLogic();

        Gdx.input.setInputProcessor(inputMultiplexer);
        logicPaused = false;
    }

    /**
     * The level that is being played by this state
     *
      * @return The current level
     */
    public int getLevelId() { return levelId; }

    /**
     * The pack that holds the current level
     *
     * @return The id of the pack
     */
    public int getPackId() { return packId; }

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
                event.handle();
            }

        });

        starCollected = new TextureRegionDrawable(new TextureRegion(Assets.getAsset(Assets.Images.STAR, Texture.class)));
        starEmpty = new TextureRegionDrawable(new TextureRegion(Assets.getAsset(Assets.Images.EMPTY_STAR, Texture.class)));

        starTable = new Table();
        for (int i = 0; i < 3; i++) {
            starImages[i] = new Image(starEmpty);
            starTable.add(starImages[i]).size(25).space(10);
        }

        //Create the GUI manager and add listeners
        centreGUI = new CentreGUI();
        Stage stage = centreGUI.getStage();
        stage.addActor(scoreLabel);
        stage.addActor(pauseButton);
        stage.addActor(starTable);
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

        //Set the star group to be in the top left corner
        tmp.set(0, 0, 0);
        viewport.getCamera().unproject(tmp);
        starTable.setPosition(tmp.x + 65, tmp.y - 7 - starTable.getMinHeight());
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

        //Update the drawables for the stars accordingly
        LevelDescriptor levelDescriptor = gameWorld.getLevelDescriptor();
        for (int i = 0; i < 2; i++) starImages[i].setDrawable(levelDescriptor.isStarCollected(i) ? starCollected : starEmpty);

        //Handle user events and render the GUI
        centreGUI.render(delta);

        //Check if the game is finished
        if (!logicPaused && gameWorld.isGameOver()) {
            if (gameOverTime == 0) {
                Gdx.input.setInputProcessor(null);
                gameWorld.stopShake();
            }
            gameOverTime += delta;

            if (gameOverTime > DEATH_TIME_GAP) {

                //Either level over or level completed
                if (gameWorld.isGameWon()) game.setScreen(new LevelCompleteScreen(game, this));
                else game.setScreen(new GameOverScreen(game, this));
            }

        }

        //Check if pausing is needed
        if (pauseClicked) {
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

    private class GameEventListener extends GestureDetector.GestureAdapter {

        private float zoomRatio = 1f;
        private float currentZoom = 1f;

        @Override
        public boolean touchDown(float x, float y, int pointer, int button) {
            currentZoom *= zoomRatio;
            zoomRatio = 1f;

            gameWorld.aimPlayer((int) x, (int) y);
            return true;
        }

        @Override
        public boolean pan (float x, float y, float deltaX, float deltaY) {
            gameWorld.aimPlayer(Gdx.input.getX(), Gdx.input.getY());
            return true;
        }

        @Override
        public boolean tap(float x, float y, int count, int button) {
            gameWorld.launchPlayer();
            return true;
        }

        @Override
        public boolean panStop(float x, float y, int pointer, int button) {
            gameWorld.launchPlayer();
            return true;
        }

        @Override
        public boolean zoom(float initialDistance, float distance) {
            zoomRatio = initialDistance / distance;
            gameWorld.applyZoom(zoomRatio * currentZoom);
            return true;
        }
    }
}
