package net.net63.codearcade.LSD.listeners;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.physics.box2d.World;
import net.net63.codearcade.LSD.components.BodyComponent;
import net.net63.codearcade.LSD.components.SensorComponent;
import net.net63.codearcade.LSD.events.GameEvent;

/**
 * Created by Basim on 13/10/15.
 */
public class ParticleEffectListener implements Listener<GameEvent> {

    private ComponentMapper<SensorComponent> sensorMapper;
    private ComponentMapper<BodyComponent> bodyMapper;

    private World world;
    private Engine engine;

    public ParticleEffectListener(Engine engine, World world) {
        super();

        this.engine = engine;
        this.world = world;

        sensorMapper = ComponentMapper.getFor(SensorComponent.class);
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
    }

    @Override
    public void receive(Signal<GameEvent> signal, GameEvent event) {
        switch (event) {

            case LAUNCH_PLAYER:
                break;

            case PLAYER_DEATH:
                break;


            default:
                break;
        }


    }
}
