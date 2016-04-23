package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
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
public class PlayerSystem extends IteratingSystem {

    //Various component mappers
    private ComponentMapper<BodyComponent> bodyMapper;
    private ComponentMapper<StateComponent> stateMapper;
    private ComponentMapper<PlayerComponent> playerMapper;
    private ComponentMapper<RenderComponent> renderMapper;
    private ComponentMapper<StarComponent> starMapper;

    //Game state
    private LevelDescriptor levelDescriptor;
    private Signal<GameEvent> gameEventSignal;
    private EventQueue eventQueue;
    private World world;

    /**
     * Create a new player system
     *
     * @param levelDescriptor The level descriptor of the world
     * @param gameEventSignal The event generator of the world
     */
    public PlayerSystem (LevelDescriptor levelDescriptor, World world, Signal<GameEvent> gameEventSignal) {
        super(Family.all(PlayerComponent.class).get(), Constants.SYSTEM_PRIORITIES.PLAYER);

        //Save instances
        this.levelDescriptor = levelDescriptor;
        this.gameEventSignal = gameEventSignal;
        this.world = world;

        //Register a new event queue
        eventQueue = new EventQueue();
        gameEventSignal.add(eventQueue);

        //Instantiate mappers
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        stateMapper = ComponentMapper.getFor(StateComponent.class);
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
        starMapper = ComponentMapper.getFor(StarComponent.class);
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
                    platformCollision(entity);
                    break;

                //Various events that signal player death
                case WALL_COLLISION:
                case LASER_COLLISION:
                case TIMER_OVER:
                    applyDeath = true;
                    break;

                //Explicit death signal (fired by this system)
                case PLAYER_DEATH:
                    killPlayer(entity);
                    break;

                //If it collided with a star then update the level descriptor and removed that star
                case STAR_COLLISION:
                    //Update the level descriptor
                    levelDescriptor.setStarCollected(starMapper.get(playerComponent.starCollected).index, true);

                    //Remove that star
                    getEngine().removeEntity(playerComponent.starCollected);

                    //Remove pointer
                    playerComponent.starCollected = null;

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

        //Destroy the weld attached between player and sensor
        if (playerComponent.sensorJoint != null) world.destroyJoint(playerComponent.sensorJoint);

        //Apply appropriate launch impulse, set the velocity to zero
        body.setLinearVelocity(0, 0);
        body.setGravityScale(1);
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
     * Apply a platform collision
     *
     * @param player The entity
     */
    private void platformCollision(Entity player) {
        Body body = bodyMapper.get(player).body;
        PlayerComponent playerComponent = playerMapper.get(player);
        StateComponent state = stateMapper.get(player);

        //Set the player state and set the velocity to 0
        body.setLinearVelocity(0, 0);
        body.setGravityScale(0);
        playerComponent.isFlying = false;
        state.set(PlayerComponent.STATE_STILL);

        //Create a weld to join the player to the platform
        WeldJointDef jointDef = new WeldJointDef();
        Body sensorBody = bodyMapper.get(playerComponent.currentSensor).body;

        jointDef.bodyA = body;
        jointDef.bodyB = sensorBody;
        jointDef.localAnchorA.set(body.getLocalPoint(playerComponent.collisionPoint));
        jointDef.localAnchorB.set(sensorBody.getLocalPoint(playerComponent.collisionPoint));
        jointDef.referenceAngle = sensorBody.getAngle() - body.getAngle();
        playerComponent.sensorJoint = world.createJoint(jointDef);
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

}
