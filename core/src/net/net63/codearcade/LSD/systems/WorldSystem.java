package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalIteratingSystem;
import com.badlogic.gdx.utils.Disposable;
import net.net63.codearcade.LSD.components.WorldComponent;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 25/06/15.
 */
public class WorldSystem extends IntervalIteratingSystem implements Disposable{

    private ComponentMapper<WorldComponent> worldMapper;
    private float step;

    @SuppressWarnings("unchecked")
    public WorldSystem() {
        super(Family.all(WorldComponent.class).get(), 1.0f / Constants.BOX2D_FPS, Constants.SYSTEM_PRIORITIES.WORLD);

        worldMapper = ComponentMapper.getFor(WorldComponent.class);
        step = 1.0f / Constants.BOX2D_FPS;
    }

    @Override
    protected void processEntity(Entity entity) {
        worldMapper.get(entity).world.step(step, Constants.BOX2D_VELOCITY_ITERATIONS, Constants.BOX2D_POSITION_ITERATIONS);
    }

    @Override
    public void dispose() {
        for (Entity entity: this.getEntities()) {
            worldMapper.get(entity).world.dispose();
        }
    }
}
