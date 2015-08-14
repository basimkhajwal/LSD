package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.physics.box2d.*;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 03/07/15.
 */
public class CollisionSystem extends EntitySystem implements ContactListener {


    public CollisionSystem () {
        super(Constants.SYSTEM_PRIORITIES.COLLISION);
    }

    @Override
    public void update(float deltaTime) { }

    @Override
    public void beginContact(Contact contact) {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();
        Body dynBody = null;

        if (a.getType() == BodyDef.BodyType.DynamicBody) dynBody = a;
        if (b.getType() == BodyDef.BodyType.DynamicBody) dynBody = b;

        if (dynBody != null) {
            dynBody.setLinearVelocity(0, 0);
        }
    }

    @Override
    public void endContact(Contact contact) { }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) { }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) { }
}
