package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import net.net63.codearcade.LSD.components.*;
import net.net63.codearcade.LSD.events.EventQueue;
import net.net63.codearcade.LSD.events.GameEvent;
import net.net63.codearcade.LSD.utils.Constants;
import net.net63.codearcade.LSD.world.LevelDescriptor;
import net.net63.codearcade.LSD.world.WorldBuilder;

/**
 * Created by Basim on 10/08/15.
 */
public class PlayerSystem extends IteratingSystem implements ContactListener {

    private ComponentMapper<BodyComponent> bodyMapper;
    private ComponentMapper<StateComponent> stateMapper;
    private ComponentMapper<SensorComponent> sensorMapper;
    private ComponentMapper<PlayerComponent> playerMapper;
    private ComponentMapper<WallComponent> wallMapper;
    private ComponentMapper<RenderComponent> renderMapper;

    private LevelDescriptor levelDescriptor;
    private Signal<GameEvent> gameEventSignal;
    private EventQueue eventQueue;

    public PlayerSystem (LevelDescriptor levelDescriptor, Signal<GameEvent> gameEventSignal) {
        super(Family.all(PlayerComponent.class).get(), Constants.SYSTEM_PRIORITIES.PLAYER);

        this.levelDescriptor = levelDescriptor;
        this.gameEventSignal = gameEventSignal;

        eventQueue = new EventQueue();
        gameEventSignal.add(eventQueue);

        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        stateMapper = ComponentMapper.getFor(StateComponent.class);
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
        sensorMapper = ComponentMapper.getFor(SensorComponent.class);
        wallMapper = ComponentMapper.getFor(WallComponent.class);
        renderMapper = ComponentMapper.getFor(RenderComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        Body body = bodyMapper.get(entity).body;
        StateComponent state = stateMapper.get(entity);
        PlayerComponent playerComponent = playerMapper.get(entity);
        Vector2 position = body.getPosition();

        boolean applyDeath = false;

        for (GameEvent event : eventQueue.getEvents()) {
            switch(event) {
                case LAUNCH_PLAYER:
                    if (state.get() == PlayerComponent.STATE_AIMING) {
                        launchPlayer(entity);
                    }
                    break;

                case PLATFORM_COLLISION:
                    body.setGravityScale(0f);
                    body.setLinearVelocity(0, 0);

                    playerComponent.isFlying = false;

                    state.set(PlayerComponent.STATE_STILL);
                    break;

                case WALL_COLLISION:
                case TIMER_OVER:
                    if (!playerComponent.isDead) applyDeath = true;
                    break;

                case PLAYER_DEATH:
                    killPlayer(entity);
                    break;

                default:
                    break;
            }
        }

        if (state.get() == PlayerComponent.STATE_JUMPING && body.getLinearVelocity().y < 0) {
            state.set(PlayerComponent.STATE_FALLING);
        }

        if (playerComponent.isFlying && !playerComponent.isDead) {
            Rectangle bounds = levelDescriptor.getWorldBounds();

            if ((!bounds.contains(position)) && (bounds.y + bounds.height) > position.y) {
                applyDeath = true;
            }
        }

        if (applyDeath) gameEventSignal.dispatch(GameEvent.PLAYER_DEATH);
    }

    private void launchPlayer(Entity player) {
        PlayerComponent playerComponent = playerMapper.get(player);
        Body body = bodyMapper.get(player).body;

        body.setGravityScale(1.0f);
        body.applyLinearImpulse(playerComponent.launchImpulse, body.getWorldCenter(), true);

        if (playerComponent.currentSensor != null) {
            getEngine().removeEntity(playerComponent.currentSensor);

            playerComponent.isFlying = true;
            playerComponent.currentSensor = null;
        }

        stateMapper.get(player).set(PlayerComponent.STATE_JUMPING);
    }

    private void killPlayer(Entity player) {
        Body body = bodyMapper.get(player).body;

        playerMapper.get(player).isDead = true;
        renderMapper.get(player).render = false;
        stateMapper.get(player).set(PlayerComponent.STATE_DEAD);

        // --- Particle Effect ----
        int num = Constants.PLAYER_DEATH_PARTICLES;

        Vector2 pos = body.getPosition();
        float radius = body.getFixtureList().first().getShape().getRadius();

        Vector2[] positions = new Vector2[num];
        Color[] colors = new Color[num];

        for (int i = 0; i < num; i++) {
            positions[i] = new Vector2(pos);
            positions[i].add(MathUtils.random(-radius, radius), MathUtils.random(-radius, radius));

            colors[i] = Color.BROWN;
        }

        WorldBuilder.createParticleEffect(positions, colors, 2, 5, 5);
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
            PlayerComponent playerComponent = playerMapper.get(playerEntity);

            if (sensorMapper.has(other)) {
                gameEventSignal.dispatch(GameEvent.PLATFORM_COLLISION);
                playerComponent.currentSensor = other;
            }

            if (wallMapper.has(other)) {
                gameEventSignal.dispatch(GameEvent.WALL_COLLISION);
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
