package net.net63.codearcade.LSD.events;

import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;

/**
 * Created by Basim on 28/11/2015.
 */
public class EventQueue implements Listener<GameEvent> {


    public EventQueue() {

    }

    @Override
    public void receive(Signal<GameEvent> signal, GameEvent event) {
        
    }

}
