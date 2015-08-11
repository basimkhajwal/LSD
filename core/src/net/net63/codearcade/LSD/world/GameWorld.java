package net.net63.codearcade.LSD.world;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.net63.codearcade.LSD.listeners.BodyRemovalListener;
import net.net63.codearcade.LSD.systems.*;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 23/06/15.
 */
public class GameWorld implements Disposable{

    private Engine engine;
    private World world;

    private Vector2 aimPosition;

    private OrthographicCamera gameCamera;
    private Viewport viewport;

    public GameWorld () {
        engine = new Engine();
        world = new World(Constants.WORLD_GRAVITY, true);
        gameCamera = new OrthographicCamera();
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, gameCamera);
        viewport.apply();

        setup();
    }

    public void setup() {
        addSystems();
        addListeners();

        aimPosition = new Vector2();

        WorldBuilder.setup(engine, world);
        WorldBuilder.createWorld();
        WorldBuilder.createPlayer();
        WorldBuilder.createSensor(1, 1, 2.5f, 0.5f);
    }

    public void resize(int w, int h) { viewport.update(w, h, true); }

    public void update(float delta) {

        // -- TEMP ---

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            gameCamera.translate(0, 1);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            gameCamera.translate(0, -1);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            gameCamera.translate(-1, 0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            gameCamera.translate(1, 0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            gameCamera.zoom += 0.02f;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            gameCamera.zoom -= 0.02f;
        }

        gameCamera.update();
        viewport.apply();

        engine.update(delta);
    }

    public void aimPlayer(int x, int y) {
        aimPosition.set(x, y);
    }

    public void launchPlayer() {
        Vector3 worldPos = new Vector3(aimPosition.x, aimPosition.y, 0);
        gameCamera.unproject(worldPos);

        //engine.getSystem(PlayerSystem.class).launchPlayer(worldPos.x, worldPos.y);
    }

    private void addSystems() {
        engine.addSystem(new WorldSystem());
        engine.addSystem(new CollisionSystem());
        engine.addSystem(new PlayerSystem());
        engine.addSystem(new RenderSystem(gameCamera));
        engine.addSystem(new DebugRenderSystem(gameCamera));

        world.setContactListener(engine.getSystem(CollisionSystem.class));
    }

    private void addListeners() {
        engine.addEntityListener(new BodyRemovalListener(world));
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
}
