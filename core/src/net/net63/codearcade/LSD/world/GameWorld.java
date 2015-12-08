package net.net63.codearcade.LSD.world;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.net63.codearcade.LSD.components.PlayerComponent;
import net.net63.codearcade.LSD.components.SensorComponent;
import net.net63.codearcade.LSD.components.StateComponent;
import net.net63.codearcade.LSD.events.GameEvent;
import net.net63.codearcade.LSD.listeners.BodyRemovalListener;
import net.net63.codearcade.LSD.listeners.ParticleEffectListener;
import net.net63.codearcade.LSD.listeners.SoundEventListener;
import net.net63.codearcade.LSD.systems.*;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * The main game world handles and controls the game events
 * and is responsible for rendering and updating the logic
 * of the game but maintains no GUI of its own
 *
 * Created by Basim on 23/06/15.
 */
public class GameWorld implements Disposable, EntityListener {

    private Engine engine;
    private World world;

    private OrthographicCamera gameCamera;
    private Viewport viewport;

    private LevelDescriptor levelDescriptor;
    private Entity player;

    private Signal<GameEvent> gameEventSignal;

    private ComponentMapper<PlayerComponent> playerMapper;
    private ComponentMapper<StateComponent> stateMapper;
    private ComponentMapper<SensorComponent> sensorMapper;

    private boolean logicPaused = false;

    /**
     * Create a new game with the given map
     *
     * @param map The level map
     */
    public GameWorld (TiledMap map) {

        //Create the event signal
        gameEventSignal = new Signal<GameEvent>();

        //Setup the camera and viewport
        gameCamera = new OrthographicCamera();
        viewport = new ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, gameCamera);

        //Component mappers for fast component retrieval
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
        stateMapper = ComponentMapper.getFor(StateComponent.class);
        sensorMapper = ComponentMapper.getFor(SensorComponent.class);

        //Setup the world to the given map
        setup(map);
    }

    private void setup(TiledMap gameMap) {
        //Initialise the entity engine and Box2d world
        engine = new Engine();
        world = new World(Constants.WORLD_GRAVITY, true);

        //Level descriptor handles score, achievements etc.
        levelDescriptor = new LevelDescriptor();

        //Add all the elements from the tiled map
        WorldBuilder.setup(engine, world, levelDescriptor);
        WorldBuilder.loadFromMap(gameMap);

        //Maintain a pointer to the player entity since it is commonly used
        player = engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first();

        //Add entity systems for logic/rendering and listeners
        addSystems();
        addListeners();
    }

    /**
     * Resize this world to the new screen dimensions
     *
     * @param w Screen width
     * @param h Screen height
     */
    public void resize(int w, int h) {
        //Update the viewport
        viewport.update(w, h, true);

        //Dispatch a resize event
        gameEventSignal.dispatch(GameEvent.RESIZE);
    }

    //Temporary debug to easily move around the world
    private void debugInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) gameCamera.translate(0, 1);
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) gameCamera.translate(0, -1);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) gameCamera.translate(-1, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) gameCamera.translate(1, 0);

        if (Gdx.input.isKeyPressed(Input.Keys.Q)) gameCamera.zoom += 0.02f;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) gameCamera.zoom -= 0.02f;
    }

    /**
     * Change the zoom setting of the game camera
     *
     * @param amount The zoom to set the camera to
     */
    public void applyZoom(float amount) {
        gameCamera.zoom = amount;
        gameCamera.update();
    }

    public void update(float delta) {
        if (!logicPaused) debugInput();

        //Set the viewport and appropriate camera
        viewport.apply();
        gameCamera.update();

        //Call all the logic
        engine.update(delta);

        //Reset the camera if it was moved when shaking
        engine.getSystem(CameraShakeSystem.class).restoreCamera();
    }

    /**
     * Aim the player to a certain point as
     * the highest point on it's trajectory
     *
     * @param x The x-position in screen space
     * @param y The y-position in screen space
     */
    public void aimPlayer(int x, int y) {
        StateComponent state = stateMapper.get(player);
        PlayerComponent playerComponent = playerMapper.get(player);

        //Check if the player is still or already aiming, otherwise don't aim
        if (state.get() == PlayerComponent.STATE_STILL || state.get() == PlayerComponent.STATE_AIMING) {

            //Convert to world space
            Vector2 worldPos = new Vector2(x, y);
            viewport.unproject(worldPos);

            //Set the aim position and invalidate (force re-compute)
            state.set(PlayerComponent.STATE_AIMING);
            playerComponent.aimPosition = worldPos;
            playerComponent.invalidateAim = true;
        }
    }

    /**
     *  Fire the player if it is already aiming
     */
    public void launchPlayer() {
        //Fire the player-fire event
        gameEventSignal.dispatch(GameEvent.LAUNCH_PLAYER);
    }

    /**
     * Stop the shake effect if it is currently on
     */
    public void stopShake() {
        engine.getSystem(CameraShakeSystem.class).stopShake();
    }

    /**
     * Pause logic so when update is called
     * the game won't continue but will still
     * be rendered
     */
    public void pauseLogic() {
        if (logicPaused) return;

        engine.getSystem(CameraShakeSystem.class).stopShake();

        engine.getSystem(WorldSystem.class).setProcessing(false);
        engine.getSystem(PlayerAimSystem.class).setProcessing(false);
        engine.getSystem(PlayerSystem.class).setProcessing(false);
        engine.getSystem(AnimationSystem.class).setProcessing(false);

        logicPaused = true;
    }

    /**
     * Resume logic if the game was previously paused
     */
    public void resumeLogic() {
        if (!logicPaused) return;

        engine.getSystem(WorldSystem.class).setProcessing(true);
        engine.getSystem(PlayerAimSystem.class).setProcessing(true);
        engine.getSystem(PlayerSystem.class).setProcessing(true);
        engine.getSystem(AnimationSystem.class).setProcessing(true);

        logicPaused = false;
    }

    //Add the entity systems that contain the bulk of the logic
    private void addSystems() {
        engine.addSystem(new WorldSystem());
        engine.addSystem(new PlayerAimSystem(world));
        engine.addSystem(new PlayerSystem(levelDescriptor, gameEventSignal));

        engine.addSystem(new AnimationSystem());
        engine.addSystem(new CameraMovementSystem(gameCamera, gameEventSignal));
        engine.addSystem(new CameraShakeSystem(gameCamera));
        engine.addSystem(new ParticleUpdateSystem(world));

        engine.addSystem(new BackgroundRenderSystem(gameEventSignal));
        engine.addSystem(new ParallaxEffectSystem(gameCamera, levelDescriptor));
        engine.addSystem(new RenderSystem(gameCamera));
        //engine.addSystem(new DebugRenderSystem(gameCamera));
        engine.addSystem(new ParticleRenderSystem(gameCamera));
        engine.addSystem(new AimRenderSystem(gameCamera));
        engine.addSystem(new TimerSystem(levelDescriptor, gameEventSignal));
    }

    //Add listeners for physics and entity events
    private void addListeners() {
        engine.addEntityListener(0, this);
        engine.addEntityListener(1, new BodyRemovalListener(world));

        world.setContactListener(engine.getSystem(PlayerSystem.class));

        gameEventSignal.add(new SoundEventListener());
        gameEventSignal.add(new ParticleEffectListener(player));
    }

    @Override
    public void entityAdded(Entity entity) { }

    @Override
    public void entityRemoved(Entity entity) {
        if (sensorMapper.has(entity)) {
            levelDescriptor.setSensorsDestroyed(levelDescriptor.getSensorsDestroyed() + 1);
            engine.getSystem(CameraShakeSystem.class).applyShake(0.5f);
        }
    }

    public String getScore() {
        return levelDescriptor.getSensorsDestroyed() + "/" + levelDescriptor.getSensorCount();
    }

    public boolean isPlayerDead() { return playerMapper.get(player).isDead; }

    public boolean isGameWon() { return levelDescriptor.getSensorsDestroyed() == levelDescriptor.getSensorCount(); }
    public boolean isGameOver() { return isPlayerDead() || isGameWon(); }

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
