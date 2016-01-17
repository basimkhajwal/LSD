package net.net63.codearcade.LSD.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import net.net63.codearcade.LSD.managers.LevelManager;
import net.net63.codearcade.LSD.managers.SoundManager;

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

    //Array of integers which specify how many levels are unlocked in each pack
    // - A value of -1 means that this particular pack is locked
    // - Otherwise it specifies the maximum index level that is unlocked (levels are unlocked in order)
    private static final int[] levelsUnlocked = new int[LevelManager.LevelPacks.length];

    private static float musicVolume;
    private static float soundVolume;
    private static boolean debugEnabled;

    /**
    * Load all the default values for the settings, this is automatically
    * called if there is an error reading the file
    */
    public static void loadDefaults() {
        setMusicVolume(1);
        setSoundVolume(1);
        setDebugEnabled(true);
    }

	/**
	 * Load all the parameters in the settings from the preferences file
	 */
	public static void loadSettings() {

        //Initially load defaults so any missing values are accounted for
        loadDefaults();

        //Iterate over each value in the key set and set the corresponding value
        for (String name: preferences.get().keySet()) {
            if (name.equals("musicVolume"))  setMusicVolume(preferences.getFloat(name));
            if (name.equals("soundVolume"))  setSoundVolume(preferences.getFloat(name));
            if (name.equals("debugEnabled")) setDebugEnabled(preferences.getBoolean(name));
            if (name.equals("levelsUnlocked")) loadLevelsUnlocked(preferences.getString(name));
        }

	}
	
	/**
	 * Stores all the parameters of this class in the global preferences
	 */
	public static void saveSettings() {

        //Reset the preferences file
        preferences.clear();

        //Set all the key-value pairs
        preferences.putFloat("musicVolume", musicVolume);
        preferences.putFloat("soundVolume", soundVolume);
        preferences.putBoolean("debugEnabled", debugEnabled);

        //Set all the serialized values
        preferences.putString("levelsUnlocked", levelsUnlockedToString());

        //Save the preferences
        preferences.flush();
	}

    /* ------------------------ Serialization -------------------------------*/

    private static String levelsUnlockedToString() {

        //Create a string of all the numbers separated by spaces
        String stringVal = "" + levelsUnlocked[0];
        for (int i = 1; i < levelsUnlocked.length; i++) {
            stringVal += " " + levelsUnlocked[i];
        }

        //Return this string
        return stringVal;
    }

    private static void loadLevelsUnlocked(String data) {

        //Split the string into all the values
        String[] split = data.split(" ");

        //Load all the separate integers into the position on the map
        for (int i = 0; i < Math.min(split.length, levelsUnlocked.length); i++) {
            levelsUnlocked[i] = Integer.parseInt(split[i]);
        }

    }

    /* ---------------------- Getters & Setters -----------------------------*/

    public static int[] getLevelsUnlocked() { return levelsUnlocked; }

    public static void setLevelsUnlocked(int[] levelsUnlocked) {
        for (int i = 0; i < Settings.levelsUnlocked.length; i++) Settings.levelsUnlocked[i] = levelsUnlocked[i];
    }

    public static float getMusicVolume() {
        return musicVolume;
    }

    public static void setMusicVolume(float musicVolume) {
        Settings.musicVolume = musicVolume;
        SoundManager.setMusicVolume(musicVolume);
    }

    public static float getSoundVolume() {
        return soundVolume;
    }

    public static void setSoundVolume(float soundVolume) {
        Settings.soundVolume = soundVolume;
    }

    public static boolean isDebugEnabled() {
        return debugEnabled;
    }

    public static void setDebugEnabled(boolean debugEnabled) {
        Settings.debugEnabled = debugEnabled;
    }

}
