package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import net.net63.codearcade.LSD.events.EventQueue;
import net.net63.codearcade.LSD.events.GameEvent;
import net.net63.codearcade.LSD.utils.BackgroundRenderer;
import net.net63.codearcade.LSD.utils.Constants;
import net.net63.codearcade.LSD.managers.ShaderManager;

/**
 * Render the background for the game, using a separate
 * method since it requires a shader and custom scaling
 *
 * Created by Basim on 03/09/15.
 */
public class BackgroundRenderSystem extends EntitySystem implements Disposable {

    private BackgroundRenderer backgroundRenderer;

    private EventQueue eventQueue;

    public BackgroundRenderSystem(Signal<GameEvent> signal) {
        super(Constants.SYSTEM_PRIORITIES.BACKGROUND_RENDER);

        eventQueue = new EventQueue();
        signal.add(eventQueue);

        backgroundRenderer = new BackgroundRenderer(ShaderManager.Shaders.MENU, BackgroundRenderer.DEFAULT);
    }

    @Override
    public void update(float deltaTime) {
        for (GameEvent event : eventQueue.getEvents()) {
            if (event == GameEvent.RESIZE) {
                backgroundRenderer.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            }
        }

        backgroundRenderer.render(deltaTime);
    }

    @Override
    public void dispose() {
        backgroundRenderer.dispose();
    }
}
