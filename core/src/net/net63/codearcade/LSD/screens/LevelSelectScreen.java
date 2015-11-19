package net.net63.codearcade.LSD.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.utils.*;

/**
 * Created by Basim on 13/11/15.
 */
public class LevelSelectScreen extends AbstractScreen {

    private static final int NUM_COLS = 4;
    private static final int MAX_ROWS = 4;

    private static final int PADDING_TOP = 20;
    private static final int PADDING_SIDE = 20;

    private static final int BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 90;

    private static final Color TEXT_COLOR = new Color(100/255f, 100/255f, 100/255f, 1f);

    private LSD game;
    private LevelManager.LevelPack levelPack;

    private CentreGUI centreGUI;
    private BackgroundRenderer backgroundRenderer;

    private ImageButton[] buttons;

    public LevelSelectScreen(LSD game, int mapPack) {
        super(game);

        this.game = game;

        levelPack = LevelManager.getPack(mapPack);
        buttons = new ImageButton[levelPack.numLevels];

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
        //buttonTable.setDebug(true);

        int cols = 0;

        for (int i = 0; i < levelPack.numLevels; i++) {

            ImageButton button = createButton(i);

            buttons[i] = button;

            buttonTable.add(button)
                    .width(BUTTON_WIDTH)
                    .height(BUTTON_HEIGHT)
                    .uniform()
                    .fill().expand();

            cols++;
            if (cols >= NUM_COLS) {
                buttonTable.row();
                cols = 0;
            }
        }

        centreGUI.getStage().addActor(buttonTable);
    }

    private ImageButton createButton(int num) {
        ImageButton main = GUIBuilder.createButton(Assets.Buttons.LEVEL_SELECT);

        Label text = GUIBuilder.createLabel(Integer.toString(num + 1), Assets.FontSizes.FORTY, TEXT_COLOR);
        text.setAlignment(Align.center);

        main.clearChildren();
        main.stack(main.getImage(), text).expand().fill();

        return main;
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

        for (ImageButton button : buttons) button.setChecked(button.isOver());

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
