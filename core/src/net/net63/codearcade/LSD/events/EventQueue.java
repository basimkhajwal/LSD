package net.net63.codearcade.LSD.events;

import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;

import java.util.PriorityQueue;

/**
 * Created by Basim on 28/11/2015.
 */
public class EventQueue implements Listener<GameEvent> {

    private PriorityQueue<GameEvent> eventQueue;

    public EventQueue() {
        eventQueue = new PriorityQueue<GameEvent>();
    }

    public GameEvent poll() {
        return eventQueue.poll();
    }

    @Override
    public void receive(Signal<GameEvent> signal, GameEvent event) {
        eventQueue.add(event);
    }

}
