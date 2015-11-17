package net.net63.codearcade.LSD.screens;

import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.utils.BackgroundRenderer;
import net.net63.codearcade.LSD.utils.CentreGUI;
import net.net63.codearcade.LSD.utils.LevelManager;
import net.net63.codearcade.LSD.utils.ShaderManager;

/**
 * Created by Basim on 13/11/15.
 */
public class LevelSelectScreen extends AbstractScreen {

    private static final int NUM_COLS = 5;

    private LSD game;
    private LevelManager.LevelPack levelPack;

    private CentreGUI centreGUI;
    private BackgroundRenderer backgroundRenderer;

    public LevelSelectScreen(LSD game, int mapPack) {
        super(game);

        this.game = game;

        levelPack = LevelManager.getPack(mapPack);

        centreGUI = new CentreGUI();
        backgroundRenderer = new BackgroundRenderer(ShaderManager.Shaders.MENU, BackgroundRenderer.DEFAULT);

        setupUI();
    }

    private void setupUI() {

        int rowNum = (int) Math.ceil(levelPack.numLevels / ((double) NUM_COLS));
        int colNum = NUM_COLS;

        
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

    @Override
    public void dispose() {
        super.dispose();

        backgroundRenderer.dispose();
        centreGUI.dispose();
    }


}
