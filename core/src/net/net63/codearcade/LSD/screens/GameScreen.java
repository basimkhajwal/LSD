package net.net63.codearcade.LSD.screens;

import net.net63.codearcade.LSD.LSD;

import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * The main game screen class that holds the game logic
 * 
 * @author Basim
 *
 */
public class GameScreen extends AbstractScreen {
	
	private Stage stage;
	
	public GameScreen(LSD game) {
		super(game);
		
		stage = new Stage();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		
		stage.getViewport().update(width, height);
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
		
		
	}
}
