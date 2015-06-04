package net.net63.codearcade.LSD;

import net.net63.codearcade.LSD.screens.MenuScreen;

import com.badlogic.gdx.Game;

/**
 * Top level game class and libGDX entry point for the game
 * 
 * @author Basim
 *
 */
public class LSD extends Game {
	
	public LSD() {
		super();
	}

	@Override
	public void create() {
		this.setScreen(new MenuScreen(this));
		
	}
	
	
	
}
