package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import net.net63.codearcade.LSD.components.WorldComponent;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 25/06/15.
 */
public class WorldSystem extends IteratingSystem {

    private ComponentMapper<WorldComponent> wm;
    private float step;

    public WorldSystem() {
        super(Family.all(WorldComponent.class).get(), 1);

        wm = ComponentMapper.getFor(WorldComponent.class);
        step = 1.0f / Constants.BOX2D_FPS;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        WorldComponent worldComponent = wm.get(entity);

        worldComponent.accumulator += deltaTime;

        while (worldComponent.accumulator >= step) {
            worldComponent.world.step(step, Constants.BOX2D_VELOCITY_ITERATIONS, Constants.BOX2D_POSITION_ITERATIONS);
            worldComponent.accumulator -= step;
        }
    }
}
