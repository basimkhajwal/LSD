package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.*;
import net.net63.codearcade.LSD.components.PlayerComponent;
import net.net63.codearcade.LSD.utils.Constants;
import net.net63.codearcade.LSD.world.LevelDescriptor;

/**
 * Created by Basim on 25/11/15.
 */
public class TimerSystem extends EntitySystem {

    private Entity player;
    private LevelDescriptor levelDescriptor;

    private float currentTime = 0;

    public TimerSystem(LevelDescriptor levelDescriptor) {
        super(Constants.SYSTEM_PRIORITIES.TIMER);

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
