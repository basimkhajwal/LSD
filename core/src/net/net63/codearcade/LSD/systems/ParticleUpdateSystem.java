package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import net.net63.codearcade.LSD.components.ParticleComponent;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 20/10/15.
 */
public class ParticleUpdateSystem extends IteratingSystem {

    private Engine engine;

    public ParticleUpdateSystem() {
        super(Family.all(ParticleComponent.class).get(), Constants.SYSTEM_PRIORITIES.PARTICLE_UPDATE);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

        this.engine = engine;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {

    }

}
