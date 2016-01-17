package net.net63.codearcade.LSD.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
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
    private static final ArrayMap<String, Integer> levelsUnlocked = new ArrayMap<String, Integer>();

    private static float musicVolume;
    private static float soundVolume;
    private static boolean debugEnabled;

    /**
    * Load all the default values for the settings, this is automatically
    * called if there is an error reading the file
    */
    public static void loadDefaults() {

        //Set default single-valued variables
        setMusicVolume(1);
        setSoundVolume(1);
        setDebugEnabled(true);

        //Set default levels unlocked (all -1 but the first)
        levelsUnlocked.clear();
        setLevelsUnlocked(LevelManager.LevelPacks[0].name, 0);
        for (int i = 1; i < LevelManager.LevelPacks.length; i++) {
            setLevelsUnlocked(LevelManager.LevelPacks[i].name, -1);
        }
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

    private static final String ARRAY_DELIMITER = " xxx ";
    private static final String OBJECT_DELIMITER = "___";

    private static String levelsUnlockedToString() {

        String stringVal;
        Array<String> names = levelsUnlocked.keys().toArray(new Array<String>());

        //Iterate over each pair and add it to the string
        stringVal = names.first() + OBJECT_DELIMITER + levelsUnlocked.get(names.first());
        for (int i = 1; i < names.size; i++) {
            stringVal += ARRAY_DELIMITER + names.get(i) + OBJECT_DELIMITER + levelsUnlocked.get(names.get(i));
        }

        //Return this string
        return stringVal;
    }

    private static void loadLevelsUnlocked(String data) {

        //Split the string into all the pack-name to level count pairs
        for (String arrayVal: data.split(ARRAY_DELIMITER)) {

            //Split each pairing
            String[] split = arrayVal.split(OBJECT_DELIMITER);

            //Set the correct pair value
            setLevelsUnlocked(split[0], Integer.parseInt(split[1]));
        }
    }

    /* ---------------------- Getters & Setters -----------------------------*/

    public static int getLevelsUnlocked(String packName) { return levelsUnlocked.get(packName); }

    public static void setLevelsUnlocked(String packName, int numLevels) { levelsUnlocked.put(packName, numLevels); }

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
