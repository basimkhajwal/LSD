package net.net63.codearcade.LSD.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ArrayMap;

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
    private static final String[] _Sounds = {Sounds.PLAYER_DEATH, Sounds.EXPLOSION };
    private static final ArrayMap<String, Sound> sounds = new ArrayMap<String, Sound>();

    public static void loadAll() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(BACKGROUND_MUSIC));

        for (String soundFile: _Sounds) {
            sounds.put(soundFile, Gdx.audio.newSound(Gdx.files.internal(soundFile)));
        }
    }

    public static void playMusic() {
        backgroundMusic.play();
        backgroundMusic.setLooping(true);
    }

    public static void pauseMusic() {
        backgroundMusic.pause();
    }

    public static void playSound(String sound) {
        sounds.get(sound).play(1.0f);
    }

    public static void dispose() {
        if (backgroundMusic != null) backgroundMusic.dispose();
    }

}
