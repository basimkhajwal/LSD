package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.physics.box2d.*;
import net.net63.codearcade.LSD.components.PlayerComponent;
import net.net63.codearcade.LSD.components.StateComponent;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 03/07/15.
 */
public class CollisionSystem extends EntitySystem implements ContactListener {

    private ComponentMapper<StateComponent> stateMapper;
    private ComponentMapper<PlayerComponent> playerMapper;

    public CollisionSystem () {
        super(Constants.SYSTEM_PRIORITIES.COLLISION);

        stateMapper = ComponentMapper.getFor(StateComponent.class);
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
    }

    @Override
    public void update(float deltaTime) { }

    @Override
    public void beginContact(Contact contact) {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        Body playerBody = null;

        if (playerMapper.has((Entity) a.getUserData())) playerBody = a;
        if (playerMapper.has((Entity) b.getUserData())) playerBody = b;

        if (playerBody != null) {
            StateComponent state = stateMapper.get((Entity) playerBody.getUserData());
            if (state.get() == PlayerComponent.STATE_JUMPING) state.set(PlayerComponent.STATE_HITTING);
        }
    }

    @Override
    public void endContact(Contact contact) { }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) { }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) { }
}
