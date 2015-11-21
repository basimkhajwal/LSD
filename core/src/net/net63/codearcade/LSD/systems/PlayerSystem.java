package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import net.net63.codearcade.LSD.components.*;
import net.net63.codearcade.LSD.utils.Constants;
import net.net63.codearcade.LSD.utils.SoundManager;
import net.net63.codearcade.LSD.world.LevelDescriptor;

/**
 * Created by Basim on 10/08/15.
 */
public class PlayerSystem extends IteratingSystem implements ContactListener {

    private ComponentMapper<BodyComponent> bodyMapper;
    private ComponentMapper<StateComponent> stateMapper;
    private ComponentMapper<SensorComponent> sensorMapper;
    private ComponentMapper<PlayerComponent> playerMapper;
    private ComponentMapper<WallComponent> wallMapper;

    private LevelDescriptor levelDescriptor;

    public PlayerSystem (LevelDescriptor levelDescriptor) {
        super(Family.all(PlayerComponent.class).get(), Constants.SYSTEM_PRIORITIES.PLAYER);

        this.levelDescriptor = levelDescriptor;

        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        stateMapper = ComponentMapper.getFor(StateComponent.class);
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
        sensorMapper = ComponentMapper.getFor(SensorComponent.class);
        wallMapper = ComponentMapper.getFor(WallComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        Body body = bodyMapper.get(entity).body;
        StateComponent state = stateMapper.get(entity);
        PlayerComponent playerComponent = playerMapper.get(entity);
        Vector2 position = body.getPosition();

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

        if (playerComponent.isDead) {
            playerComponent.deathTime += deltaTime;
        }

        if (playerComponent.isFlying) {
            Rectangle bounds = levelDescriptor.getWorldBounds();

            if ((!bounds.contains(position)) &&  (bounds.y + bounds.height) > position.y) {
                SoundManager.playSound(SoundManager.Sounds.PLAYER_DEATH);
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

        if (entityA == null || entityB == null) return;

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

            if (wallMapper.has(other)) {
                playerComponent.isDead = true;
                SoundManager.playSound(SoundManager.Sounds.PLAYER_DEATH);
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
