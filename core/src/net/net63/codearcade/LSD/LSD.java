package net.net63.codearcade.LSD;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.FPSLogger;
import net.net63.codearcade.LSD.managers.Assets;
import net.net63.codearcade.LSD.managers.LevelManager;
import net.net63.codearcade.LSD.managers.ShaderManager;
import net.net63.codearcade.LSD.screens.LoadingScreen;
import net.net63.codearcade.LSD.utils.Settings;

/**
 * Top level game class and libGDX entry point for the game
 * 
 * @author Basim
 *
 */
public class LSD extends Game {

    private FPSLogger fpsLogger;
    public AssetManager assetManager;

	public LSD() {
        super();

        assetManager = new AssetManager();
        fpsLogger = new FPSLogger();
	}

	@Override
	public void create() {
        Settings.loadSettings();
        LevelManager.loadAll();
        ShaderManager.loadAll();
        Assets.loadFonts();

		this.setScreen(new LoadingScreen(this));
	}

    @Override
    public void pause() {
        super.pause();

        Settings.saveSettings();
    }

    @Override
    public void dispose() {
        super.dispose();

        Assets.dispose();
        LevelManager.dispose();
        Settings.saveSettings();
    }

    @Override
    public void render() {
        super.render();

        if (Settings.isDebugEnabled()) fpsLogger.log();
    }
	
	
}
