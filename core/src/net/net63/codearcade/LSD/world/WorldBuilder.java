package net.net63.codearcade.LSD.world;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.*;
import net.net63.codearcade.LSD.components.*;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 10/07/15.
 */
public class WorldBuilder {

    private static Engine engine;
    private static World world;

    public static void setup(Engine engine, World world) {
        WorldBuilder.engine = engine;
        WorldBuilder.world = world;
    }

    public static Entity createWorld() {
        WorldComponent worldComponent = new WorldComponent();
        worldComponent.world = world;

        return createEntityFrom(worldComponent);
    }

    public static Entity createSensor(float x, float y, float width, float height) {

        SensorComponent sensorComponent = new SensorComponent();
        BodyComponent bodyComponent = new BodyComponent();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.fixedRotation = true;
        bodyDef.position.set((width / 2) + x, (height / 2) + y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.restitution = 0.0f;
        fixtureDef.shape = new PolygonShape();
        ((PolygonShape) fixtureDef.shape).setAsBox(width / 2, height / 2);

        bodyComponent.body = world.createBody(bodyDef);
        bodyComponent.body.createFixture(fixtureDef);

        return createEntityFrom(sensorComponent, bodyComponent);
    }

    public static Entity createPlayer() {

        PlayerComponent playerComponent = new PlayerComponent();
        StateComponent stateComponent = new StateComponent();
        AnimationComponent animationComponent = new AnimationComponent();
        BodyComponent bodyComponent = new BodyComponent();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        bodyDef.position.set(2.5f, 4);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.restitution = 0.0f;
        fixtureDef.shape = new CircleShape();
        fixtureDef.shape.setRadius(Constants.PLAYER_WIDTH / 2.0f);

        bodyComponent.body = world.createBody(bodyDef);
        bodyComponent.body.createFixture(fixtureDef);

        return createEntityFrom(playerComponent, bodyComponent, stateComponent, animationComponent);
    }

    private static Entity createEntityFrom(Component... components) {
        Entity entity = new Entity();

        for (Component component: components) {
            entity.add(component);

            if (component instanceof BodyComponent) {
                ((BodyComponent) component).body.setUserData(entity);
            }
        }

        engine.addEntity(entity);

        return entity;
    }
}
