package net.net63.codearcade.LSD.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ArrayMap;

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
        public static final String EXPLOSION = "sounds/explosion.wav";
    }
    private static final String[] _Sounds = {Sounds.PLAYER_DEATH, Sounds.EXPLOSION };

    //Cache for sound effects
    private static final ArrayMap<String, Sound> sounds = new ArrayMap<String, Sound>();

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
        sounds.get(sound).play(1.0f);
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
