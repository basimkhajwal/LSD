package net.net63.codearcade.LSD.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.utils.*;

/**
 * Created by Basim on 13/11/15.
 */
public class LevelSelectScreen extends AbstractScreen {

    private static final int NUM_COLS = 5;
    private static final int MAX_ROWS = 4;

    private static final int PADDING_TOP = 20;
    private static final int PADDING_SIDE = 20;

    private static final int BUTTON_WIDTH = 50;
    private static final int BUTTON_HEIGHT = 30;

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

        Label title = GUIBuilder.createLabel("Level Select", Assets.FontSizes.FIFTY, Color.YELLOW);
        title.setX((800 -  title.getWidth()) / 2);
        title.setY(580 - title.getHeight());

        centreGUI.getStage().addActor(title);

        Table buttonTable = new Table();
        buttonTable.setPosition(PADDING_SIDE, PADDING_TOP);
        buttonTable.setSize(800 - 2*PADDING_SIDE, title.getY() - 2*PADDING_TOP);
        buttonTable.setDebug(true);

        for (int i = 0; i < levelPack.numLevels; i++) {
            
        }

        centreGUI.getStage().addActor(buttonTable);
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
