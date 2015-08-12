package net.net63.codearcade.LSD.world;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
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
import net.net63.codearcade.LSD.components.BodyComponent;
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
    private Viewport viewport;

    private Vector2 aimPosition;

    private Entity player;

    private ComponentMapper<BodyComponent> bodyMapper;

    public GameWorld () {
        engine = new Engine();
        world = new World(Constants.WORLD_GRAVITY, true);

        gameCamera = new OrthographicCamera();
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, gameCamera);
        viewport.apply();

        bodyMapper = ComponentMapper.getFor(BodyComponent.class);

        setup();
    }

    public void setup() {
        addSystems();
        addListeners();

        aimPosition = new Vector2();

        WorldBuilder.setup(engine, world);
        WorldBuilder.createWorld();
        player = WorldBuilder.createPlayer();

        WorldBuilder.createSensor(6, 6, 0.5f, 2.5f);
        WorldBuilder.createSensor(7, 1, 2.5f, 0.5f);
        WorldBuilder.createSensor(5, 1, 2.5f, 0.5f);
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
        Vector3 worldPos = new Vector3(x, y, 0);
        gameCamera.unproject(worldPos);
        aimPosition.set(worldPos.x, worldPos.y);

        engine.getSystem(EffectRenderSystem.class).setDrawPlayer(true);
        engine.getSystem(EffectRenderSystem.class).updatePlayerProjection(bodyMapper.get(player).body.getPosition(), aimPosition);
    }

    public void launchPlayer() {
        engine.getSystem(EffectRenderSystem.class).setDrawPlayer(false);
        engine.getSystem(PlayerSystem.class).launchPlayer(aimPosition.x, aimPosition.y);
    }

    private void addSystems() {
        engine.addSystem(new WorldSystem());
        engine.addSystem(new CollisionSystem());
        engine.addSystem(new PlayerSystem());
        engine.addSystem(new RenderSystem(gameCamera));
        engine.addSystem(new DebugRenderSystem(gameCamera));
        engine.addSystem(new EffectRenderSystem(gameCamera));

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
