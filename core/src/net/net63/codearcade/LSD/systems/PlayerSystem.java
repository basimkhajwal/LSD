package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import net.net63.codearcade.LSD.components.*;
import net.net63.codearcade.LSD.events.EventQueue;
import net.net63.codearcade.LSD.events.GameEvent;
import net.net63.codearcade.LSD.utils.Constants;
import net.net63.codearcade.LSD.world.LevelDescriptor;

/**
 * Player system which handles the abstract and specific
 * details with controlling the player
 *
 * Created by Basim on 10/08/15.
 */
public class PlayerSystem extends IteratingSystem implements ContactListener {

    //Various component mappers
    private ComponentMapper<BodyComponent> bodyMapper;
    private ComponentMapper<StateComponent> stateMapper;
    private ComponentMapper<SensorComponent> sensorMapper;
    private ComponentMapper<PlayerComponent> playerMapper;
    private ComponentMapper<WallComponent> wallMapper;
    private ComponentMapper<RenderComponent> renderMapper;

    //Game state
    private LevelDescriptor levelDescriptor;
    private Signal<GameEvent> gameEventSignal;
    private EventQueue eventQueue;

    /**
     * Create a new player system
     *
     * @param levelDescriptor The level descriptor of the world
     * @param gameEventSignal The event generator of the world
     */
    public PlayerSystem (LevelDescriptor levelDescriptor, Signal<GameEvent> gameEventSignal) {
        super(Family.all(PlayerComponent.class).get(), Constants.SYSTEM_PRIORITIES.PLAYER);

        //Save instances
        this.levelDescriptor = levelDescriptor;
        this.gameEventSignal = gameEventSignal;

        //Register a new event queue
        eventQueue = new EventQueue();
        gameEventSignal.add(eventQueue);

        //Instantiate mappers
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        stateMapper = ComponentMapper.getFor(StateComponent.class);
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
        sensorMapper = ComponentMapper.getFor(SensorComponent.class);
        wallMapper = ComponentMapper.getFor(WallComponent.class);
        renderMapper = ComponentMapper.getFor(RenderComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        //Get all needed components
        Body body = bodyMapper.get(entity).body;
        StateComponent state = stateMapper.get(entity);
        PlayerComponent playerComponent = playerMapper.get(entity);
        Vector2 position = body.getPosition();

        //Whether or not to kill the player in this tick
        boolean applyDeath = false;

        //Handle incoming events
        for (GameEvent event : eventQueue.getEvents()) {
            switch(event) {

                //Fire the player
                case LAUNCH_PLAYER:
                    if (state.get() == PlayerComponent.STATE_AIMING) launchPlayer(entity);
                    break;

                //Colliding with the next platform
                case PLATFORM_COLLISION:
                    body.setGravityScale(0f);
                    body.setLinearVelocity(0, 0);

                    playerComponent.isFlying = false;

                    state.set(PlayerComponent.STATE_STILL);
                    break;

                //Various events that signal player death
                case WALL_COLLISION:
                case TIMER_OVER:
                    applyDeath = true;
                    break;

                //Explicit death signal (fired by this system)
                case PLAYER_DEATH:
                    killPlayer(entity);
                    break;

                //Don't handle other events
                default:
                    break;
            }
        }

        //Update the state to falling (for a different animation) as the player starts moving down
        if (state.get() == PlayerComponent.STATE_JUMPING && body.getLinearVelocity().y < 0) {
            state.set(PlayerComponent.STATE_FALLING);
        }

        //Check if player remains within bounds, appy death otherwise
        if (playerComponent.isFlying && !playerComponent.isDead) {
            Rectangle bounds = levelDescriptor.getWorldBounds();

            if ((!bounds.contains(position)) && (bounds.y + bounds.height) > position.y) {
                applyDeath = true;
            }
        }

        //Apply death (if the player isn't already dead)
        if (applyDeath && !playerComponent.isDead) gameEventSignal.dispatch(GameEvent.PLAYER_DEATH);
    }

    /**
     * Launch the player to the touch coordinates
     */
    private void launchPlayer(Entity player) {
        //Get components
        PlayerComponent playerComponent = playerMapper.get(player);
        Body body = bodyMapper.get(player).body;

        //Re-apply gravity apply appropriate launch impulse
        body.setGravityScale(1.0f);
        body.applyLinearImpulse(playerComponent.launchImpulse, body.getWorldCenter(), true);

        //Destroy the sensor on which the player is on
        if (playerComponent.currentSensor != null) {
            getEngine().removeEntity(playerComponent.currentSensor);
        }

        //Apply the new state
        playerComponent.isFlying = true;
        playerComponent.currentSensor = null;
        stateMapper.get(player).set(PlayerComponent.STATE_JUMPING);
    }

    /**
     * Kill the player
     *
     * @param player The entity
     */
    private void killPlayer(Entity player) {
        //Set the state variables
        playerMapper.get(player).isDead = true;
        renderMapper.get(player).render = false;
        stateMapper.get(player).set(PlayerComponent.STATE_DEAD);

        //Stop the players motion and gravity
        bodyMapper.get(player).body.setGravityScale(0);
        bodyMapper.get(player).body.setLinearVelocity(Vector2.Zero);
    }

    /* ---------- BOX 2D Contact Stuff */

    @Override
    public void beginContact(Contact contact) {

        //Get bodies
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        //End if no entity-entity collision occurred
        if (! (a.getUserData() instanceof Entity) || ! (b.getUserData() instanceof Entity)) return;

        //Check if entities have collided
        Entity entityA = (Entity) a.getUserData();
        Entity entityB = (Entity) b.getUserData();
        Entity playerEntity = null;

        //Find player entity
        if (playerMapper.has(entityA)) playerEntity = entityA;
        if (playerMapper.has(entityB)) playerEntity = entityB;

        //If a player collision occurred
        if (playerEntity != null) {
            //Get the player component
            PlayerComponent playerComponent = playerMapper.get(playerEntity);

            //Other entity (if player found)
            Entity other = (entityA == playerEntity) ? entityB : entityA;

            //Fire event that sensor was collided with and save the sensor
            if (sensorMapper.has(other)) {
                gameEventSignal.dispatch(GameEvent.PLATFORM_COLLISION);
                playerComponent.currentSensor = other;
            }

            //Fire event that a wall was collided with
            if (wallMapper.has(other)) {
                gameEventSignal.dispatch(GameEvent.WALL_COLLISION);
            }

        }
    }

    // ------------------ Un-used contact functions ------------------
    @Override
    public void endContact(Contact contact) { }
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) { }
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) { }

}
