package net.net63.codearcade.LSD.utils;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import net.net63.codearcade.LSD.components.BodyComponent;
import net.net63.codearcade.LSD.components.PlayerComponent;
import net.net63.codearcade.LSD.components.WorldComponent;
import net.net63.codearcade.LSD.systems.DebugRenderSystem;
import net.net63.codearcade.LSD.systems.WorldSystem;

/**
 * Created by Basim on 23/06/15.
 */
public class GameWorld {

    private Engine engine;
    private World world;

    public GameWorld (Engine engine) {
        this.engine = engine;
    }

    public void setup() {

        world = new World(Constants.WORLD_GRAVITY, true);

        createWorld();
        createPlayer();

        engine.addSystem(new WorldSystem());
        engine.addSystem(new DebugRenderSystem());
    }

    private Entity createWorld() {
        WorldComponent worldComponent = new WorldComponent();
        worldComponent.world = world;

        return createEntityFrom(worldComponent);
    }

    private Entity createPlayer() {

        PlayerComponent playerComponent = new PlayerComponent();
        BodyComponent bodyComponent = new BodyComponent();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        bodyDef.position.set(4, 4);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = new PolygonShape();
        ((PolygonShape) fixtureDef.shape).setAsBox(1f, 1.5f);

        bodyComponent.body = world.createBody(bodyDef);
        bodyComponent.body.createFixture(fixtureDef);

        return createEntityFrom(playerComponent, bodyComponent);
    }

    private Entity createEntityFrom(Component... components) {
        Entity entity = new Entity();

        for (Component component: components) {
            entity.add(component);
        }

        engine.addEntity(entity);

        return entity;
    }
}
