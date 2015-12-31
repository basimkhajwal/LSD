package net.net63.codearcade.LSD.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.managers.ShaderManager;
import net.net63.codearcade.LSD.utils.BackgroundRenderer;
import net.net63.codearcade.LSD.utils.CentreGUI;

/**
 * Created by Basim on 31/12/15.
 */
public class PackSelectScreen extends AbstractScreen {

    private BackgroundRenderer backgroundRenderer;
    private CentreGUI centreGUI;

    public PackSelectScreen(LSD game) {
        super(game);

        centreGUI = new CentreGUI();
        backgroundRenderer = new BackgroundRenderer(ShaderManager.Shaders.MENU, BackgroundRenderer.DEFAULT);

        setupUI(centreGUI.getStage());
    }

    private void setupUI(Stage stage) {

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        backgroundRenderer.resize(width, height);
        centreGUI.resize(width, height);
    }

    @Override
    public void render(float deltaTime) {
        super.render(deltaTime);

        backgroundRenderer.render(deltaTime);
        centreGUI.render(deltaTime);
    }

}
