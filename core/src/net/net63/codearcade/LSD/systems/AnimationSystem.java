package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import net.net63.codearcade.LSD.components.AnimationComponent;
import net.net63.codearcade.LSD.components.RenderComponent;
import net.net63.codearcade.LSD.components.StateComponent;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * An animation manager that updates each animation
 * component as to which texture
 *
 * Created by Basim on 31/08/15.
 */
public class AnimationSystem extends IteratingSystem {

    //Mappers
    private ComponentMapper<AnimationComponent> animMapper;
    private ComponentMapper<RenderComponent> renderMapper;
    private ComponentMapper<StateComponent> stateMapper;

    public AnimationSystem() {
        super(Family.all(AnimationComponent.class, RenderComponent.class, StateComponent.class).get(), Constants.SYSTEM_PRIORITIES.ANIMATION);

        animMapper = ComponentMapper.getFor(AnimationComponent.class);
        renderMapper = ComponentMapper.getFor(RenderComponent.class);
        stateMapper = ComponentMapper.getFor(StateComponent.class);
    }


    @Override
    public void processEntity(Entity entity, float deltaTime) {
        StateComponent state = stateMapper.get(entity);

        //Update the state time
        state.time += deltaTime;

        //Set the texture at the current time
        Animation currentAnimation = animMapper.get(entity).animations.get(state.get());
        renderMapper.get(entity).texture = currentAnimation.getKeyFrame(state.time);
    }

}
