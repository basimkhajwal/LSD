package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import net.net63.codearcade.LSD.components.AnimationComponent;
import net.net63.codearcade.LSD.components.RenderComponent;
import net.net63.codearcade.LSD.components.StateComponent;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 31/08/15.
 */
public class AnimationSystem extends IteratingSystem {

    public AnimationSystem() {
        super(Family.all(AnimationComponent.class, RenderComponent.class, StateComponent.class).get(), Constants.SYSTEM_PRIORITIES.ANIMATION);
    }


    @Override
    public void processEntity(Entity entity, float deltaTime) {

    }

}
