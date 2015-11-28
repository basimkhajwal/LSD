package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.utils.Disposable;
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

    public BackgroundRenderSystem() {
        super(Constants.SYSTEM_PRIORITIES.BACKGROUND_RENDER);

        backgroundRenderer = new BackgroundRenderer(ShaderManager.Shaders.MENU, BackgroundRenderer.DEFAULT);
    }

    @Override
    public void update(float deltaTime) {
        backgroundRenderer.render(deltaTime);
    }

    /**
     * Resize the shader and texture to match the screen size
     */
    public void resize(int width, int height) {
        backgroundRenderer.resize(width, height);
    }

    @Override
    public void dispose() {
        backgroundRenderer.dispose();
    }
}
