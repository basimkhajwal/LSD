package net.net63.codearcade.LSD.utils;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import net.net63.codearcade.LSD.components.BodyComponent;
import net.net63.codearcade.LSD.components.PlayerComponent;
import net.net63.codearcade.LSD.components.WorldComponent;

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
    }

    public Entity createWorld() {
        WorldComponent worldComponent = new WorldComponent();
        worldComponent.world = world;

        return createEntityFrom(worldComponent);
    }

    public Entity createPlayer() {

        PlayerComponent playerComponent = new PlayerComponent();
        BodyComponent bodyComponent = new BodyComponent();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        bodyComponent.body = world.createBody(bodyDef);

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
