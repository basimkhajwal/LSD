package net.net63.codearcade.LSD.world;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.net63.codearcade.LSD.components.PlayerComponent;
import net.net63.codearcade.LSD.components.SensorComponent;
import net.net63.codearcade.LSD.components.StateComponent;
import net.net63.codearcade.LSD.listeners.BodyRemovalListener;
import net.net63.codearcade.LSD.systems.*;
import net.net63.codearcade.LSD.utils.Assets;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 23/06/15.
 */
public class GameWorld implements Disposable, EntityListener {

    private Engine engine;
    private World world;

    private OrthographicCamera gameCamera;
    private Viewport viewport;

    private LevelDescriptor levelDescriptor;
    private Entity player;

    private ComponentMapper<PlayerComponent> playerMapper;
    private ComponentMapper<StateComponent> stateMapper;
    private ComponentMapper<SensorComponent> sensorMapper;

    public GameWorld () {
        engine = new Engine();
        world = new World(Constants.WORLD_GRAVITY, true);

        gameCamera = new OrthographicCamera();
        viewport = new ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, gameCamera);
        viewport.apply();

        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
        stateMapper = ComponentMapper.getFor(StateComponent.class);
        sensorMapper = ComponentMapper.getFor(SensorComponent.class);

        setup();
    }

    public void setup() {
        addSystems();
        addListeners();

        levelDescriptor = new LevelDescriptor();

        WorldBuilder.setup(engine, world, levelDescriptor);
        WorldBuilder.loadFromMap(Assets.getTiledMap(Assets.LevelMaps.TEST));
        player = WorldBuilder.createPlayer();
    }

    public void resize(int w, int h) {
        viewport.update(w, h, true);
    }

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
        StateComponent state = stateMapper.get(player);

        if (state.get() == PlayerComponent.STATE_STILL || state.get() == PlayerComponent.STATE_AIMING) {
            Vector2 worldPos = new Vector2(x, y);
            viewport.unproject(worldPos);

            state.set(PlayerComponent.STATE_AIMING);
            playerMapper.get(player).aimPosition = worldPos;
        }
    }

    public void launchPlayer() {
        if (stateMapper.get(player).get() == PlayerComponent.STATE_AIMING) {
            stateMapper.get(player).set(PlayerComponent.STATE_FIRING);
        }
    }

    private void addSystems() {
        engine.addSystem(new WorldSystem());
        engine.addSystem(new PlayerSystem(levelDescriptor));

        engine.addSystem(new AnimationSystem());

        engine.addSystem(new BackgroundRenderSystem());
        engine.addSystem(new RenderSystem(gameCamera));
        engine.addSystem(new DebugRenderSystem(gameCamera));
        engine.addSystem(new EffectRenderSystem(gameCamera));

        world.setContactListener(engine.getSystem(PlayerSystem.class));
    }

    private void addListeners() {
        engine.addEntityListener(new BodyRemovalListener(world));
        engine.addEntityListener(this);
    }

    @Override
    public void entityAdded(Entity entity) { }

    @Override
    public void entityRemoved(Entity entity) {
        if (sensorMapper.has(entity)) {
            levelDescriptor.setSensorsDestroyed(levelDescriptor.getSensorsDestroyed() + 1);
        }
    }

    public String getScore() {
        return levelDescriptor.getSensorsDestroyed() + "/" + levelDescriptor.getSensorCount();
    }

    @Override
    public void dispose() {
        for (EntitySystem entitySystem : engine.getSystems()) {
            engine.removeSystem(entitySystem);

            if (entitySystem instanceof Disposable) {
                ((Disposable) entitySystem).dispose();
            }
        }
    }
}
