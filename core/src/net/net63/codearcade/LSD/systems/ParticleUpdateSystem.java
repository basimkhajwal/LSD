package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import net.net63.codearcade.LSD.components.ParticleComponent;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 20/10/15.
 */
public class ParticleUpdateSystem extends IteratingSystem {

    private Engine engine;
    private World world;

    private ComponentMapper<ParticleComponent> particleMapper;


    public ParticleUpdateSystem(World world) {
        super(Family.all(ParticleComponent.class).get(), Constants.SYSTEM_PRIORITIES.PARTICLE_UPDATE);

        this.world = world;
        particleMapper = ComponentMapper.getFor(ParticleComponent.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

        this.engine = engine;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        ParticleComponent component = particleMapper.get(entity);

        component.currentTime += deltaTime;
        
        if (component.currentTime >= component.finalTime) {
            for (Body body : component.particles) world.destroyBody(body);
            engine.removeEntity(entity);
        }
    }

}
