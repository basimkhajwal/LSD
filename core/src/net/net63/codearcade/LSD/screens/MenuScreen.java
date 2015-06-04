package net.net63.codearcade.LSD.screens;

import net.net63.codearcade.LSD.LSD;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;


/**
 * The Menu Screen state where the user selects the options they want to
 * do
 * 
 * @author Basim
 *
 */
public class MenuScreen extends AbstractScreen{
	
	private Stage stage;
	
	public MenuScreen(LSD game) {
		super(game);
		
		this.clear = new Color(1, 0, 0, 1);
		
		stage = new Stage();
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		
		stage.getViewport().setScreenSize(width, height);
	}
	
}
