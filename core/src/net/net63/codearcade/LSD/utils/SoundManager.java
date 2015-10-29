package net.net63.codearcade.LSD.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 * Created by Basim on 29/10/15.
 */
public class SoundManager {

    private static final String BACKGROUND_MUSIC = "sounds/music.wav";
    private static Music backgroundMusic;

    public static class Sounds {

        public static final String PLAYER_DEATH = "sounds/death.wav";
        public static final String EXPLOSION = "sounds/explosion.wav";

    }

    public static void loadAll() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(BACKGROUND_MUSIC));

    }

    public static void playMusic() {
        backgroundMusic.play();
        backgroundMusic.setLooping(true);
    }

    public static void pauseMusic() {
        backgroundMusic.pause();
    }

    public static void playSound(String sound) {

    }

    public static void stopSounds() {

    }

    public static void dispose() {
        if (backgroundMusic != null) backgroundMusic.dispose();
    }

}
