package net.net63.codearcade.LSD.screens;

import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.utils.BackgroundRenderer;
import net.net63.codearcade.LSD.utils.ShaderManager;

/**
 * Created by Basim on 13/11/15.
 */
public class LevelSelectScreen extends AbstractScreen {

    private LSD game;

    private BackgroundRenderer backgroundRenderer;

    public LevelSelectScreen(LSD game) {
        super(game);

        this.game = game;

        backgroundRenderer = new BackgroundRenderer(ShaderManager.Shaders.MENU, BackgroundRenderer.DEFAULT);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        backgroundRenderer.resize(width, height);
    }

    @Override
    public void render(float deltaTime) {
        super.render(deltaTime);

        backgroundRenderer.render(deltaTime);
    }

    @Override
    public void dispose() {
        super.dispose();

        backgroundRenderer.dispose();
    }


}
