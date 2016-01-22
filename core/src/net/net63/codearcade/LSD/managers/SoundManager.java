package net.net63.codearcade.LSD.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
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
    private static Music backgroundMusic;

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

    //Cache for sound effects
    private static final ArrayMap<String, Sound> sounds = new ArrayMap<String, Sound>();

    //Arrays of grouped sounds
    private static final Array<String> explosions = new Array<String>(
            new String[]{Sounds.EXPLOSION_1, Sounds.EXPLOSION_2, Sounds.EXPLOSION_3, Sounds.EXPLOSION_4});
    private static final Array<String> clickSounds = new Array<String>(new String[] { Sounds.CLICK_1 });
    private static final Array<String> starSounds = new Array<String>(new String[] { Sounds.STAR_1 });


    /**
     * Load all the sound assets, must be called prior to being used
     */
    public static void loadAll() {
        //Create the background music from the file
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(BACKGROUND_MUSIC));

        //Add each effect to the ArrayMap
        for (String soundFile: _Sounds) {
            sounds.put(soundFile, Gdx.audio.newSound(Gdx.files.internal(soundFile)));
        }
    }

    /**
     * Start playing the background music
     */
    public static void playMusic() {
        backgroundMusic.play();
        backgroundMusic.setLooping(true);
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
        backgroundMusic.pause();
    }

    /**
     * Play a specified sound effect
     *
     * @param sound The path of the sound to play
     */
    public static void playSound(String sound) {
        sounds.get(sound).play(Settings.getSoundVolume());
    }

    /**
     * Sets the volume to play the background music
     *
     * @param volume The volume between 0 and 1
     */
    public static void setMusicVolume(float volume) {
        backgroundMusic.setVolume(volume);
    }

    /**
     * Dispose all the assets, sounds cannot be played after this is called
     */
    public static void dispose() {
        //Dispose the background music
        if (backgroundMusic != null) backgroundMusic.dispose();

        //Dispose all the other sound effects
        for (Sound sound: sounds.values()) {
            sound.dispose();
        }
        sounds.clear();
    }

}
