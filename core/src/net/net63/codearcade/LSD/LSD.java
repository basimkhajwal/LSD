package net.net63.codearcade.LSD;

import com.badlogic.gdx.graphics.FPSLogger;
import net.net63.codearcade.LSD.screens.MenuScreen;
import net.net63.codearcade.LSD.utils.Assets;

import com.badlogic.gdx.Game;
import net.net63.codearcade.LSD.utils.Constants;
import net.net63.codearcade.LSD.utils.SoundManager;

/**
 * Top level game class and libGDX entry point for the game
 * 
 * @author Basim
 *
 */
public class LSD extends Game {

    private FPSLogger fpsLogger;

	public LSD() {
        super();

        fpsLogger = new FPSLogger();
	}

	@Override
	public void create() {
		Assets.loadAll();
        SoundManager.loadAll();
        SoundManager.playMusic();

		this.setScreen(new MenuScreen(this));
	}

    @Override
    public void render() {
        super.render();

        if (Constants.DEBUG) {
            fpsLogger.log();
        }
    }
	
	
}
