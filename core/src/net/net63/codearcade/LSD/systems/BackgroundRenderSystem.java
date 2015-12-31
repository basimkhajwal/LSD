package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import net.net63.codearcade.LSD.events.EventQueue;
import net.net63.codearcade.LSD.events.GameEvent;
import net.net63.codearcade.LSD.managers.ShaderManager;
import net.net63.codearcade.LSD.utils.BackgroundRenderer;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Render the background for the game, using a separate
 * method since it requires a shader and custom scaling
 *
 * Created by Basim on 03/09/15.
 */
public class BackgroundRenderSystem extends EntitySystem implements Disposable {

    private Engine engine;
    private TimerSystem timerSystem;

    private BackgroundRenderer backgroundRenderer;
    private EventQueue eventQueue;

    private float saturation = 0.1f;
    private boolean paused = false;
    private boolean timerOn = false;

    public BackgroundRenderSystem(Signal<GameEvent> signal) {
        super(Constants.SYSTEM_PRIORITIES.BACKGROUND_RENDER);

        eventQueue = new EventQueue();
        signal.add(eventQueue);

        backgroundRenderer = new BackgroundRenderer(ShaderManager.Shaders.GAME, BackgroundRenderer.DEFAULT);
        updateSaturation();
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void update(float deltaTime) {
        if (timerSystem == null) timerSystem = engine.getSystem(TimerSystem.class);

        for (GameEvent event : eventQueue.getEvents()) {
            switch (event) {

                case RESIZE:
                    backgroundRenderer.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                    break;

                case PAUSE_GAME:
                    paused = true;
                    resetSaturation();
                    break;

                case RESUME_GAME:
                    paused = false;
                    break;

                case TIMER_STARTED:
                    timerOn = true;
                    resetSaturation();
                    break;

                case TIMER_STOPPED:
                    timerOn = false;
                    resetSaturation();
                    break;

            }


        }

        if (!paused && timerOn) {
            saturation = timerSystem.getCurrentTime() / timerSystem.getCurrentMaxTime();
            saturation = Math.max(0.1f, saturation);

            updateSaturation();
        }

        backgroundRenderer.render(deltaTime);
    }

    private void resetSaturation() {
        saturation = 0.1f;
        updateSaturation();
    }

    private void updateSaturation() {
        ShaderProgram shader = backgroundRenderer.getShaderProgram();

        shader.begin();
        shader.setUniformf("saturation", saturation);
        shader.end();
    }

    @Override
    public void dispose() {
        backgroundRenderer.dispose();
    }
}
