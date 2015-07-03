package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 03/07/15.
 */
public class RenderSystem extends IteratingSystem {

    public RenderSystem () {
        super(Family.all().get(), Constants.SYSTEM_PRIORITIES.RENDER);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {

    }
}
