package net.net63.codearcade.LSD.screens;

import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.managers.ShaderManager;
import net.net63.codearcade.LSD.utils.BackgroundRenderer;
import net.net63.codearcade.LSD.utils.CentreGUI;

/**
 * Created by Basim on 29/05/16.
 */
public class LoadingScreen extends AbstractScreen {

    private BackgroundRenderer backgroundRenderer;
    private CentreGUI centreGUI;

    public LoadingScreen(LSD game) {
        super(game);

        backgroundRenderer = new BackgroundRenderer(ShaderManager.Shaders.MENU, BackgroundRenderer.DEFAULT);
        centreGUI = new CentreGUI();
    }

    @Override
    public void render(float delta) {
        super.render(delta);


    }

    @Override
    public void dispose() {
        super.dispose();

        centreGUI.dispose();
        backgroundRenderer.dispose();
    }

}
