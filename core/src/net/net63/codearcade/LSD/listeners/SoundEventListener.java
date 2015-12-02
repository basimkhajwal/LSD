package net.net63.codearcade.LSD.listeners;

import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import net.net63.codearcade.LSD.events.GameEvent;

/**
 * Created by Basim on 02/12/2015.
 */
public class SoundEventListener implements Listener<GameEvent> {

    @Override
    public void receive(Signal<GameEvent> signal, GameEvent event) {
        switch (event) {
            
        }
    }

}
