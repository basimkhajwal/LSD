package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import net.net63.codearcade.LSD.events.EventQueue;
import net.net63.codearcade.LSD.events.GameEvent;
import net.net63.codearcade.LSD.utils.Constants;
import net.net63.codearcade.LSD.world.LevelDescriptor;

/**
 * Created by Basim on 25/11/15.
 */
public class TimerSystem extends EntitySystem implements Disposable {

    private static final float LONGEST_TIME = 10f;
    private static final float SHORTEST_TIME = 0.5f;

    private static final float PADDING_SIDE = 100;
    private static final float PADDING_BELOW = 30;
    private static final float HEIGHT = 20;
    private static final float WIDTH = Constants.DEFAULT_SCREEN_WIDTH - 2 * PADDING_SIDE;

    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;

    private float currentTime = 0;
    private float currentMaxTime;
    private float platformTimeStep;

    private EventQueue eventQueue;
    private Signal<GameEvent> gameEventSignal;

    private boolean firstPlatform = true;
    private boolean finished = false;
    private boolean timerOn = false;

    public TimerSystem(LevelDescriptor levelDescriptor, Signal<GameEvent> gameEventSignal) {
        super(Constants.SYSTEM_PRIORITIES.TIMER);

        this.gameEventSignal = gameEventSignal;

        currentMaxTime = LONGEST_TIME;
        platformTimeStep = (LONGEST_TIME - SHORTEST_TIME) / levelDescriptor.getSensorCount();

        eventQueue = new EventQueue();
        gameEventSignal.add(eventQueue);

        camera = new OrthographicCamera();
        camera.setToOrtho(false);

        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        for (GameEvent event: eventQueue.getEvents()) {

            if (event == GameEvent.LAUNCH_PLAYER) {
                endTimer();
            }

            else if (event == GameEvent.PLAYER_DEATH) {
                endTimer();
                finished = true;
            }

            else if (event == GameEvent.PLATFORM_COLLISION) {
                if (firstPlatform) {
                    firstPlatform = false;
                } else if (!finished) {
                    beginTimer();
                }
            }

            else if (event == GameEvent.RESIZE) {
                camera.setToOrtho(false);
            }
        }

        if (!timerOn) return;

        currentTime += deltaTime;
        float ratio = 1 - currentTime / currentMaxTime;

        float scaleX = ((float) Gdx.graphics.getWidth()) / Constants.DEFAULT_SCREEN_WIDTH;
        float scaleY = ((float) Gdx.graphics.getHeight()) / Constants.DEFAULT_SCREEN_HEIGHT;

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(PADDING_SIDE * scaleX, PADDING_BELOW * scaleY, WIDTH * ratio * scaleX, HEIGHT * scaleY);
        shapeRenderer.end();

        if (ratio <= 0) {
            gameEventSignal.dispatch(GameEvent.TIMER_OVER);
            endTimer();
        }
    }

    private void beginTimer() {
        timerOn = true;
        currentTime = 0;
        currentMaxTime -= platformTimeStep;
    }

    private void endTimer() {
        timerOn = false;
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
