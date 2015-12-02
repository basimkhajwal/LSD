package net.net63.codearcade.LSD.listeners;

import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import net.net63.codearcade.LSD.events.GameEvent;
import net.net63.codearcade.LSD.managers.SoundManager;

/**
 * Created by Basim on 02/12/2015.
 */
public class SoundEventListener implements Listener<GameEvent> {

    @Override
    public void receive(Signal<GameEvent> signal, GameEvent event) {
        String sound = null;

        switch (event) {

            case PLAYER_DEATH:
                sound = SoundManager.Sounds.PLAYER_DEATH;
                break;

            case LAUNCH_PLAYER:
                sound = SoundManager.Sounds.EXPLOSION;
                break;

            default:
                break;
        }

        if (sound != null) SoundManager.playSound(sound);
    }

}
