package net.net63.codearcade.LSD.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import net.net63.codearcade.LSD.utils.Settings;

/**
 * SoundManager class to handle the loading,
 * caching and playing of sounds and music
 *
 * Created by Basim on 29/10/15.
 */
public class SoundManager {

    //Background music
    private static final String BACKGROUND_MUSIC = "sounds/music.mp3";

    //Sound effects
    public static class Sounds {
        public static final String PLAYER_DEATH = "sounds/death.wav";

        public static final String CLICK_1 = "sounds/click.wav";

        public static final String STAR_1 = "sounds/star.wav";

        public static final String EXPLOSION_1 = "sounds/explosion.wav";
        public static final String EXPLOSION_2 = "sounds/explosion2.wav";
        public static final String EXPLOSION_3 = "sounds/explosion3.wav";
        public static final String EXPLOSION_4 = "sounds/explosion4.wav";
    }
    private static final String[] _Sounds = {
            Sounds.PLAYER_DEATH, Sounds.EXPLOSION_1,
            Sounds.EXPLOSION_2, Sounds.EXPLOSION_3,
            Sounds.EXPLOSION_4, Sounds.CLICK_1,
            Sounds.STAR_1
        };

    //Arrays of grouped sounds
    private static final Array<String> explosions = new Array<String>(
            new String[]{Sounds.EXPLOSION_1, Sounds.EXPLOSION_2, Sounds.EXPLOSION_3, Sounds.EXPLOSION_4});
    private static final Array<String> clickSounds = new Array<String>(new String[] { Sounds.CLICK_1 });
    private static final Array<String> starSounds = new Array<String>(new String[] { Sounds.STAR_1 });

    private static AssetManager assetManager;

    private static boolean musicPlaying = false;

    /**
     * Load all the sound assets, must be called prior to being used
     */
    public static void loadAll(AssetManager assetManager) {

        SoundManager.assetManager = assetManager;

        //Create the background music from the file
        assetManager.load(BACKGROUND_MUSIC, Music.class);

        //Add each effect to the ArrayMap
        for (String soundFile: _Sounds) assetManager.load(soundFile, Sound.class);
    }

    private static Music getBackgroundMusic() {
        return assetManager.get(BACKGROUND_MUSIC, Music.class);
    }

    /**
     * Start playing the background music
     */
    public static void playMusic() {
        getBackgroundMusic().play();
        getBackgroundMusic().setLooping(true);
        getBackgroundMusic().setVolume(Settings.getMusicVolume());
        musicPlaying = true;
    }

    /**
     * Return the string code for a random explosion sound
     */
    public static String getExplosion() { return explosions.random(); }

    /**
     * Return the string code for a random click sound
     */
    public static String getClick() { return clickSounds.random(); }

    /**
     * Return the string code for a random star sound
     */
    public static String getStar() { return starSounds.random(); }

    /**
     * Stop playing the background music
     */
    public static void pauseMusic() {
        getBackgroundMusic().pause();
        musicPlaying = false;
    }

    /**
     * Play a specified sound effect
     *
     * @param sound The path of the sound to play
     */
    public static void playSound(String sound) {
        assetManager.get(sound, Sound.class).play(Settings.getSoundVolume());
    }

    /**
     * Sets the volume to play the background music
     *
     * @param volume The volume between 0 and 1
     */
    public static void setMusicVolume(float volume) { if (musicPlaying) getBackgroundMusic().setVolume(volume); }

}
