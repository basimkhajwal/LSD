package net.net63.codearcade.LSD.listeners;

import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import net.net63.codearcade.LSD.events.GameEvent;
import net.net63.codearcade.LSD.managers.SoundManager;

/**
 * Listener class that listens for game events and plays
 * the appropriate sound if an event occurs
 *
 * Created by Basim on 02/12/2015.
 */
public class SoundEventListener implements Listener<GameEvent> {

    @Override
    public void receive(Signal<GameEvent> signal, GameEvent event) {
        //By default no sound is played
        String sound = null;

        //Depending on the event play a specific sound
        switch (event) {

            case PLAYER_DEATH:
                sound = SoundManager.Sounds.PLAYER_DEATH;
                break;

            case LAUNCH_PLAYER:
                sound = SoundManager.getExplosion();
                break;

            case STAR_COLLISION:
                sound = SoundManager.getStar();
                break;

            default:
                break;
        }

        //Play the sound if one is being played
        if (sound != null) SoundManager.playSound(sound);
    }

}
