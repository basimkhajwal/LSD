package net.net63.codearcade.LSD.listeners;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.physics.box2d.*;
import net.net63.codearcade.LSD.components.*;
import net.net63.codearcade.LSD.events.GameEvent;

/**
 * Created by Basim on 04/01/16.
 */
public class WorldContactListener implements ContactListener {

    private Signal<GameEvent> gameEventSignal;
    private World world;

    private ComponentMapper<PlayerComponent> playerMapper;
    private ComponentMapper<WallComponent> wallMapper;
    private ComponentMapper<SensorComponent> sensorMapper;
    private ComponentMapper<LaserComponent> laserMapper;
    private ComponentMapper<StarComponent> starMapper;

    public WorldContactListener(World world, Signal<GameEvent> gameEventSignal) {
        this.world = world;
        this.gameEventSignal = gameEventSignal;

        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
        wallMapper = ComponentMapper.getFor(WallComponent.class);
        sensorMapper = ComponentMapper.getFor(SensorComponent.class);
        laserMapper = ComponentMapper.getFor(LaserComponent.class);
        starMapper = ComponentMapper.getFor(StarComponent.class);
    }

    private <T extends Component> Entity findEntity(ComponentMapper<T> componentMapper, Entity a, Entity b) {
        if (componentMapper.has(a)) return a;
        if (componentMapper.has(b)) return b;

        return null;
    }

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

        //Find entities
        Entity laserEntity = findEntity(laserMapper, entityA, entityB);
        Entity playerEntity = findEntity(playerMapper, entityA, entityB);

        //If a player collision occurred
        if (playerEntity != null) {
            //Get the player component
            PlayerComponent playerComponent = playerMapper.get(playerEntity);

            //Other entity (if player found)
            Entity other = (entityA == playerEntity) ? entityB : entityA;

            //Fire event that sensor was collided with and save the sensor
            if (sensorMapper.has(other)) {
                gameEventSignal.dispatch(GameEvent.PLATFORM_COLLISION);

                playerComponent.collisionPoint = contact.getWorldManifold().getPoints()[0];
                playerComponent.currentSensor = other;
            }

            //Fire event that a wall was collided with
            if (wallMapper.has(other)) {
                gameEventSignal.dispatch(GameEvent.WALL_COLLISION);
            }

            //Check if it collided with the laser
            if (laserMapper.has(other)) {
                gameEventSignal.dispatch(GameEvent.LASER_COLLISION);
            }

            //Check if the player collided with a star
            if (starMapper.has(other)) {
                gameEventSignal.dispatch(GameEvent.STAR_COLLISION);
                playerComponent.starCollected = other;
            }

        }

        //Check if the laser collided with an object other than the player
        if (laserEntity != null && playerEntity == null) {
            gameEventSignal.dispatch(GameEvent.LASER_OBJECT_COLLISION);
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
