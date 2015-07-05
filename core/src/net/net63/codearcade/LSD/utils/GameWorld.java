package net.net63.codearcade.LSD.utils;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.sun.media.jfxmediaimpl.MediaDisposer;
import net.net63.codearcade.LSD.components.BodyComponent;
import net.net63.codearcade.LSD.components.PlayerComponent;
import net.net63.codearcade.LSD.components.SensorComponent;
import net.net63.codearcade.LSD.components.WorldComponent;
import net.net63.codearcade.LSD.systems.CollisionSystem;
import net.net63.codearcade.LSD.systems.DebugRenderSystem;
import net.net63.codearcade.LSD.systems.RenderSystem;
import net.net63.codearcade.LSD.systems.WorldSystem;

/**
 * Created by Basim on 23/06/15.
 */
public class GameWorld implements Disposable{

    private Engine engine;
    private World world;

    private OrthographicCamera gameCamera;

    public GameWorld () {
        engine = new Engine();
    }

    public void setup() {
        addSystems();

        createWorld();
        createPlayer();
        createSensor(1, 1, 2.5f, 0.5f);
    }

    public void resize() {
        setupCamera();
    }

    public void update(float delta) {
        engine.update(delta);
    }

    @Override
    public void dispose() {

        for (Entity box2dEntity: engine.getEntitiesFor(Family.all(BodyComponent.class).get())) {
            
        }

        world.dispose();

        for (EntitySystem entitySystem: engine.getSystems()) {
            engine.removeSystem(entitySystem);

            if (entitySystem instanceof Disposable) {
                ((Disposable) entitySystem).dispose();
            }
        }
    }

    private void addSystems() {
        engine.addSystem(new WorldSystem());
        engine.addSystem(new CollisionSystem());
        engine.addSystem(new RenderSystem(gameCamera));
        engine.addSystem(new DebugRenderSystem(gameCamera));
    }

    private void setupCamera() {
        gameCamera = new OrthographicCamera();
        gameCamera.setToOrtho(false);
        gameCamera.combined.scl(Constants.METRE_TO_PIXEL, Constants.METRE_TO_PIXEL, 0);
    }

    private Entity createWorld() {
        world = new World(Constants.WORLD_GRAVITY, true);
        world.setContactListener(engine.getSystem(CollisionSystem.class));

        WorldComponent worldComponent = new WorldComponent();
        worldComponent.world = world;

        return createEntityFrom(worldComponent);
    }

    private Entity createSensor(float x, float y, float width, float height) {

        SensorComponent sensorComponent = new SensorComponent();
        BodyComponent bodyComponent = new BodyComponent();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.fixedRotation = true;
        bodyDef.position.set((width / 2) + x, (height / 2) + y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = new PolygonShape();
        ((PolygonShape) fixtureDef.shape).setAsBox(width / 2, height / 2);

        bodyComponent.body = world.createBody(bodyDef);
        bodyComponent.body.createFixture(fixtureDef);

        return createEntityFrom(sensorComponent, bodyComponent);
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
