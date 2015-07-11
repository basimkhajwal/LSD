package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.physics.box2d.World;
import net.net63.codearcade.LSD.components.BodyComponent;
import net.net63.codearcade.LSD.components.WorldComponent;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 11/07/15.
 */
public class BodyRemovalSystem extends IteratingSystem {

    private ComponentMapper<BodyComponent> bodyMapper;
    private ComponentMapper<WorldComponent> worldMapper;

    private Engine engine;
    private World world;
    private ImmutableArray<Entity> worldEntities;

    public BodyRemovalSystem() {
        super(Family.all(BodyComponent.class).get(), Constants.SYSTEM_PRIORITIES.BODY_REMOVAL);

        worldMapper = ComponentMapper.getFor(WorldComponent.class);
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

        this.engine = engine;
        worldEntities = engine.getEntitiesFor(Family.all(WorldComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        world = worldMapper.get(worldEntities.first()).world;

        super.update(deltaTime);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        BodyComponent bodyComponent = bodyMapper.get(entity);

        if (bodyComponent.removeBody) {
            world.destroyBody(bodyComponent.body);
            engine.removeEntity(entity);
        }
    }

}
