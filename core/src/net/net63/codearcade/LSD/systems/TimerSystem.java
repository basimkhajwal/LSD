package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import net.net63.codearcade.LSD.components.PlayerComponent;
import net.net63.codearcade.LSD.events.EventQueue;
import net.net63.codearcade.LSD.events.GameEvent;
import net.net63.codearcade.LSD.utils.Constants;
import net.net63.codearcade.LSD.world.LevelDescriptor;

/**
 * Created by Basim on 25/11/15.
 */
public class TimerSystem extends EntitySystem implements Disposable {

    private static final float PADDING_SIDE = 100;
    private static final float PADDING_BELOW = 30;
    private static final float HEIGHT = 20;
    private static final float WIDTH = Constants.DEFAULT_SCREEN_WIDTH - 2 * PADDING_SIDE;

    private Entity player;
    private LevelDescriptor levelDescriptor;

    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;

    private float currentTime = 0;
    private float currentMaxTime = Float.POSITIVE_INFINITY;

    private EventQueue eventQueue;
    private Signal<GameEvent> gameEventSignal;

    private boolean timerOn = false;

    public TimerSystem(LevelDescriptor levelDescriptor, Signal<GameEvent> gameEventSignal) {
        super(Constants.SYSTEM_PRIORITIES.TIMER);

        this.levelDescriptor = levelDescriptor;
        this.gameEventSignal = gameEventSignal;

        eventQueue = new EventQueue();
        gameEventSignal.add(eventQueue);

        camera = new OrthographicCamera();
        camera.setToOrtho(false);

        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

        player = engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        for (GameEvent event: eventQueue.getEvents()) {

            if (event == GameEvent.LAUNCH_PLAYER) {
                endTimer();
            }

            else if (event == GameEvent.PLATFORM_COLLISION) {
                beginTimer();
            }

        }

        if (!timerOn) return;

        currentTime += deltaTime;
        float ratio = 1 - currentTime / currentMaxTime;

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(PADDING_SIDE, PADDING_BELOW, WIDTH * ratio, HEIGHT);
        shapeRenderer.end();
        
        if (ratio <= 0) {
            gameEventSignal.dispatch(GameEvent.TIMER_OVER);
            endTimer();
        }
    }

    private void beginTimer() {
        timerOn = true;
        currentTime = 0;
        currentMaxTime = 5f; //TEMP
    }

    private void endTimer() {
        timerOn = false;
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
