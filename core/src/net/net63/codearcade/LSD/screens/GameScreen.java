package net.net63.codearcade.LSD.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import net.net63.codearcade.LSD.LSD;
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
	
	public GameScreen(LSD game) {
		super(game);

		stage = new Stage();
        gameWorld = new GameWorld();

        stage.addListener(this);

        Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

        gameWorld.resize();
		stage.getViewport().update(width, height);
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);

		gameWorld.update(delta);
	}

    @Override
    public boolean handle(Event event) {
        if (event instanceof InputEvent) {
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
