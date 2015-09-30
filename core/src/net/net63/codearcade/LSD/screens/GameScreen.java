package net.net63.codearcade.LSD.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.utils.Assets;
import net.net63.codearcade.LSD.utils.Constants;
import net.net63.codearcade.LSD.world.GameWorld;

/**
 * The main game screen class that holds the game logic
 * 
 * @author Basim
 *
 */
public class GameScreen extends AbstractScreen implements EventListener {

    private Stage stage;
    private GameWorld gameWorld;

    private TiledMap gameMap;
    private Label scoreLabel;

    private boolean logicPaused = false;

	public GameScreen(LSD game, int levelId) {
		super(game);

        gameMap = Assets.getTiledMap(Constants.LEVELS[levelId]);

        gameWorld = new GameWorld(gameMap);
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

    private void setupUI() {
        scoreLabel = new Label("", new Label.LabelStyle(Assets.getFont(Assets.Fonts.DEFAULT, Assets.FontSizes.FIFTY), Color.YELLOW));
        scoreLabel.setPosition((800 - scoreLabel.getWidth()) / 2.0f , 550 - scoreLabel.getHeight());

        stage = new Stage(new ExtendViewport(800, 600));
        stage.addActor(scoreLabel);

        stage.addListener(this);
        Gdx.input.setInputProcessor(stage);
    }

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

        gameWorld.resize(width, height);
		stage.getViewport().update(width, height);
	}

    @Override
    public void show() { if (logicPaused) resumeLogic(); }

    @Override
    public void hide() { pauseLogic(); }

	@Override
	public void render(float delta) {
		super.render(delta);

        gameWorld.update(delta);
        updateScore();

        stage.getViewport().apply();
        stage.act(delta);
        stage.draw();

        if (gameWorld.isGameOver()) {
            gameWorld.dispose();
            gameWorld = new GameWorld(gameMap);
        }
	}

    private void updateScore() {
        scoreLabel.setText(gameWorld.getScore());
    }

    @Override
    public boolean handle(Event event) {

        if (!logicPaused && event instanceof InputEvent) {
            InputEvent inputEvent = (InputEvent) event;

            switch (inputEvent.getType()) {

                case touchDown: case touchDragged:
                    gameWorld.aimPlayer(Gdx.input.getX(), Gdx.input.getY());

                    break;

                case touchUp:
                    gameWorld.launchPlayer();
                    break;

                default:
                    return false;
            }

            return true;
        }

        return false;
    }
}
