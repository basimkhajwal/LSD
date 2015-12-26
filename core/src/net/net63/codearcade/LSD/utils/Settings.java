package net.net63.codearcade.LSD.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Class to hold the settings (mutable) for the game for
 * the curren user
 * 
 * @author Basim
 *
 */
public class Settings {

    //Constant not to be changed preferences handle, the name must never change
    private static final Preferences preferences = Gdx.app.getPreferences("lsd-settings");

    private static int currentLevel;
    private static float musicVolume;
    private static float soundVolume;

    /**
    * Load all the default values for the settings, this is automatically
    * called if there is an error reading the file
    */
    public static void loadDefaults() {
        setCurrentLevel(0);
        setMusicVolume(1);
        setSoundVolume(1);
    }

	/**
	 * Load all the parameters in the settings from the preferences file
	 */
	public static void loadSettings() {

        //Initially load defaults so any missing values are accounted for
        loadDefaults();

        //Iterate over each value in the key set and set the corresponding value
        for (String name: preferences.get().keySet()) {
            if (name == "currentLevel") currentLevel = preferences.getInteger(name);
            if (name == "musicVolume")  musicVolume = preferences.getFloat(name);
            if (name == "soundVolume")  soundVolume = preferences.getFloat(name);
        }

	}
	
	/**
	 * Stores all the parameters of this class in the global preferences
	 */
	public static void saveSettings() {

        //Reset the preferences file
        preferences.clear();

        //Set all the key-value pairs
        preferences.putInteger("currentLevel", currentLevel);
        preferences.putFloat("musicVolume", musicVolume);
        preferences.putFloat("soundVolume", soundVolume);

        //Save the preferences
        preferences.flush();
	}

    /* ---------------------- Getters & Setters -----------------------------*/

    public static int getCurrentLevel() {
        return currentLevel;
    }

    public static void setCurrentLevel(int newLevel) {
        currentLevel = newLevel;
    }

    public static float getMusicVolume() {
        return musicVolume;
    }

    public static void setMusicVolume(float musicVolume) {
        Settings.musicVolume = musicVolume;
    }

    public static float getSoundVolume() {
        return soundVolume;
    }

    public static void setSoundVolume(float soundVolume) {
        Settings.soundVolume = soundVolume;
    }


}
