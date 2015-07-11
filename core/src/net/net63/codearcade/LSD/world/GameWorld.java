package net.net63.codearcade.LSD.world;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import net.net63.codearcade.LSD.listeners.BodyRemovalListener;
import net.net63.codearcade.LSD.systems.*;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 23/06/15.
 */
public class GameWorld implements Disposable{

    private Engine engine;
    private World world;

    private OrthographicCamera gameCamera;

    public GameWorld () {
        gameCamera = new OrthographicCamera();
        engine = new Engine();
        world = new World(Constants.WORLD_GRAVITY, true);

        setup();
    }

    public void setup() {
        addSystems();
        addListeners();

        WorldBuilder.setup(engine, world);
        WorldBuilder.createWorld();
        WorldBuilder.createPlayer();
        WorldBuilder.createSensor(1, 1, 2.5f, 0.5f);
    }

    public void resize() {
        setupCamera();
    }

    public void update(float delta) {
        engine.update(delta);
    }

    @Override
    public void dispose() {
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

        world.setContactListener(engine.getSystem(CollisionSystem.class));
    }

    private void addListeners() {
        engine.addEntityListener(new BodyRemovalListener(world));
    }

    private void setupCamera() {
        gameCamera.setToOrtho(false);
        gameCamera.combined.scl(Constants.METRE_TO_PIXEL, Constants.METRE_TO_PIXEL, 0);
    }
}
