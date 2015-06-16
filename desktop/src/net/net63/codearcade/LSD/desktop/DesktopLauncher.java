package net.net63.codearcade.LSD.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Desktop launcher for LSD
 * 
 * @author Basim
 *
 */
public class DesktopLauncher {
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.title = Constants.TITLE;
		config.width = 800;
		config.height = 480;
		
		new LwjglApplication(new LSD(), config);
	}
}