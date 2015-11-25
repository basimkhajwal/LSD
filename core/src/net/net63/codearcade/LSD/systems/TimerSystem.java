package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import net.net63.codearcade.LSD.components.PlayerComponent;

/**
 * Created by Basim on 25/11/15.
 */
public class TimerSystem extends EntitySystem {

    private Entity player;

    private float currentTime = 0;

    public TimerSystem() {

    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

        player = engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
    }

    @Override
    public void update(float deltaTime) {

    }

}
