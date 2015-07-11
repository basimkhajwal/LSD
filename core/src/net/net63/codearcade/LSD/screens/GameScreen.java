package net.net63.codearcade.LSD.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.world.GameWorld;

/**
 * The main game screen class that holds the game logic
 * 
 * @author Basim
 *
 */
public class GameScreen extends AbstractScreen {
	
	private Stage stage;
    private GameWorld gameWorld;
	
	public GameScreen(LSD game) {
		super(game);

		stage = new Stage();
        gameWorld = new GameWorld();
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
}
