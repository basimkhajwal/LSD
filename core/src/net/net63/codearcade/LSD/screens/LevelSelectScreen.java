package net.net63.codearcade.LSD.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.utils.*;

/**
 * A screen allowing the player to select which
 * level they want to play of a specific pack
 *
 * Created by Basim on 13/11/15.
 */
public class LevelSelectScreen extends AbstractScreen {

    // ------------------- Settings / Constants --------------------

    private static final int NUM_COLS = 4;
    private static final int MAX_ROWS = 4;

    private static final int PADDING_TOP = 40;
    private static final int PADDING_SIDE = 40;

    private static final int BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 90;

    private static final Color TEXT_COLOR = new Color(100/255f, 100/255f, 100/255f, 1f);

    // ----------------- Instance Variables ---------------------

    private LSD game;
    private LevelManager.LevelPack levelPack;

    private CentreGUI centreGUI;
    private BackgroundRenderer backgroundRenderer;

    private ImageButton[] buttons;

    private boolean changing = false;
    private int levelTo = 0;

    public LevelSelectScreen(LSD game, int mapPack) {
        super(game);

        this.game = game;

        //Get the level pack
        levelPack = LevelManager.getPack(mapPack);
        buttons = new ImageButton[levelPack.numLevels];

        //Create renderers
        centreGUI = new CentreGUI();
        backgroundRenderer = new BackgroundRenderer(ShaderManager.Shaders.MENU, BackgroundRenderer.DEFAULT);

        setupUI();
    }

    /**
     * Utility method to setup all the GUI
     */
    private void setupUI() {

        //Create and position the title at the top centre
        Label title = GUIBuilder.createLabel("Level Select", Assets.FontSizes.FIFTY, Color.YELLOW);
        title.setX((800 -  title.getWidth()) / 2);
        title.setY(580 - title.getHeight());

        //Add to the stage
        centreGUI.getStage().addActor(title);

        //The main table for storing the buttons
        Table buttonTable = new Table();
        buttonTable.setPosition(PADDING_SIDE, PADDING_TOP);
        buttonTable.setSize(800 - 2*PADDING_SIDE, title.getY() - 2*PADDING_TOP);
        //buttonTable.setDebug(true);

        int cols = 0;

        //Loop over every level and add a button for it to the table
        for (int i = 0; i < levelPack.numLevels; i++) {

            //Create the button
            ImageButton button = createButton(i);

            //Add it to the list and add a listener
            buttons[i] = button;
            button.addListener(new ButtonClickListener(i));

            //Add it to the table
            buttonTable.add(button)
                    .width(BUTTON_WIDTH)
                    .height(BUTTON_HEIGHT)
                    .uniform()
                    .fill().expand();

            //Add a new row at each appropriate position
            cols++;
            if (cols >= NUM_COLS) {
                buttonTable.row();
                cols = 0;
            }
        }

        //Add the table to the GUI stage
        centreGUI.getStage().addActor(buttonTable);
    }

    /**
     * Create a new button with the specified index
     *
     * @param num The number to show (zero based but the text will be 1 based)
     * @return The image button with the image and text
     */
    private ImageButton createButton(int num) {
        //Create the button
        ImageButton main = GUIBuilder.createButton(Assets.Buttons.LEVEL_SELECT);

        //Create the label
        Label text = GUIBuilder.createLabel(Integer.toString(num + 1), Assets.FontSizes.FORTY, TEXT_COLOR);
        text.setAlignment(Align.center);

        //Align the label on top on the centre
        main.clearChildren();
        main.stack(main.getImage(), text).expand().fill();

        return main;
    }

    /**
     * Listener class that modifies the state variables on a user click
     */
    private class ButtonClickListener extends ClickListener {

        private int level;

        public ButtonClickListener(int level) {
            this.level = level;
        }

        @Override
        public void clicked (InputEvent event, float x, float y) {
            changing = true;
            levelTo = level;
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        //Resize the background and the GUI
        backgroundRenderer.resize(width, height);
        centreGUI.resize(width, height);
    }

    @Override
    public void render(float deltaTime) {
        super.render(deltaTime);

        //Set the correct hover of each button
        for (ImageButton button : buttons) button.setChecked(button.isOver());

        //Render the background and the GUI
        backgroundRenderer.render(deltaTime);
        centreGUI.render(deltaTime);

        //Check if changing screen and change to the appropriate one
        if (changing) {
            dispose();
            game.setScreen(new GameScreen(game, levelTo));
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        backgroundRenderer.dispose();
        centreGUI.dispose();
    }


}
