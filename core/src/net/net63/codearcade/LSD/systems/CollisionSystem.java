package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
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
    public void update(float deltaTime) {

    }

    @Override
    public void beginContact(Contact contact) {
        Gdx.app.log("Collision", "Started");

        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        Body dynBody = null;

        if (a.getType() == BodyDef.BodyType.DynamicBody) {
            dynBody = a;
        }

        if (b.getType() == BodyDef.BodyType.DynamicBody) {
            dynBody = b;
        }

        if (dynBody != null) {
            Gdx.app.log("Collision", "Force applied");
            dynBody.applyLinearImpulse(new Vector2(0, 5), dynBody.getLocalCenter(), true);
            //dynBody.applyForceToCenter(0, 100, true);
        }
    }

    @Override
    public void endContact(Contact contact) {
        Gdx.app.log("Collision", "Ended");
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) { }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) { }
}
