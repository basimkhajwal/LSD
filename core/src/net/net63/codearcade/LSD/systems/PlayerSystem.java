package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.*;
import net.net63.codearcade.LSD.components.BodyComponent;
import net.net63.codearcade.LSD.components.PlayerComponent;
import net.net63.codearcade.LSD.components.SensorComponent;
import net.net63.codearcade.LSD.components.StateComponent;
import net.net63.codearcade.LSD.utils.Constants;
import net.net63.codearcade.LSD.world.LevelDescriptor;

/**
 * Created by Basim on 10/08/15.
 */
public class PlayerSystem extends IteratingSystem implements ContactListener {

    private ComponentMapper<BodyComponent> bodyMapper;
    private ComponentMapper<StateComponent> stateMapper;
    private ComponentMapper<SensorComponent> sensorMapper;
    private ComponentMapper<PlayerComponent> playerMapper;

    private LevelDescriptor levelDescriptor;

    public PlayerSystem (LevelDescriptor levelDescriptor) {
        super(Family.all(PlayerComponent.class).get(), Constants.SYSTEM_PRIORITIES.PLAYER);

        this.levelDescriptor = levelDescriptor;

        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        stateMapper = ComponentMapper.getFor(StateComponent.class);
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
        sensorMapper = ComponentMapper.getFor(SensorComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        Body body = bodyMapper.get(entity).body;
        StateComponent state = stateMapper.get(entity);
        PlayerComponent playerComponent = playerMapper.get(entity);

        switch (state.get()) {

            case PlayerComponent.STATE_FIRING:

                body.setGravityScale(1.0f);
                body.applyLinearImpulse(playerComponent.launchImpulse, body.getWorldCenter(), true);

                if (playerComponent.sensorEntity != null) {
                    getEngine().removeEntity(playerComponent.sensorEntity);

                    playerComponent.isFlying = true;
                    playerComponent.sensorEntity = null;
                }

                state.set(PlayerComponent.STATE_JUMPING);
                break;

            case PlayerComponent.STATE_HITTING:

                body.setGravityScale(0f);
                body.setLinearVelocity(0, 0);

                state.set(PlayerComponent.STATE_STILL);
                break;

            case PlayerComponent.STATE_JUMPING:

                if (body.getLinearVelocity().y < 0) {
                    state.set(PlayerComponent.STATE_FALLING);
                }

                break;
        }

        if (playerComponent.isFlying) {
            if (! levelDescriptor.getWorldBounds().contains(body.getPosition()) ) {
                playerComponent.isDead = true;
            }
        }
    }

    /* ---------- BOX 2D Contact Stuff */

    @Override
    public void beginContact(Contact contact) {

        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        Entity entityA = (Entity) a.getUserData();
        Entity entityB = (Entity) b.getUserData();
        Entity playerEntity = null;

        if (playerMapper.has(entityA)) playerEntity = entityA;
        if (playerMapper.has(entityB)) playerEntity = entityB;

        Entity other = (entityA == playerEntity) ? entityB : entityA;

        if (playerEntity != null) {
            StateComponent state = stateMapper.get(playerEntity);
            PlayerComponent playerComponent = playerMapper.get(playerEntity);

            if (playerComponent.isFlying) {
                playerComponent.isFlying = false;
                state.set(PlayerComponent.STATE_HITTING);
            }

            if (sensorMapper.has(other)) {
                playerComponent.sensorEntity = other;
            }
        }
    }

    @Override
    public void endContact(Contact contact) { }
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) { }
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) { }

}
