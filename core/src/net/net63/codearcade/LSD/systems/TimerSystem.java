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
    private float currentMaxTime = Float.POSITIVE_INFINITY;

    private boolean timerOn = false;

    public TimerSystem(LevelDescriptor levelDescriptor) {
        super(Constants.SYSTEM_PRIORITIES.TIMER);

        this.levelDescriptor = levelDescriptor;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

        player = engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (timerOn) currentTime += deltaTime;
    }

    public void beginTimer() {
        timerOn = true;
        currentMaxTime = 5f; //TEMP
    }

    public void endTimer() {
        timerOn = false;
    }
}
