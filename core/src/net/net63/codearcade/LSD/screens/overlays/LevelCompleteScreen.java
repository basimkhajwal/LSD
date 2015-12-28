package net.net63.codearcade.LSD.screens.overlays;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.managers.Assets;
import net.net63.codearcade.LSD.screens.GameScreen;
import net.net63.codearcade.LSD.screens.LevelSelectScreen;
import net.net63.codearcade.LSD.utils.Constants;
import net.net63.codearcade.LSD.utils.GUIBuilder;

/**
 * Created by Basim on 30/09/15.
 */
public class LevelCompleteScreen extends AbstractOverlay {

    private static final Color TITLE_COLOUR = new Color(71f / 255f, 104f / 255f, 33f / 255f, 1f);
    private static final Color TEXT_COLOUR = new Color(120f / 255f, 169f / 255f, 66f / 255f, 1f);

    private boolean replaying = false;
    private boolean nextLevel = false;
    private boolean backToMenu = false;

    private Array<ImageButton> buttons;
    private GameScreen previousGame;

    public LevelCompleteScreen(LSD game, GameScreen previousScreen) {
        super(game, previousScreen);
    }

    @Override
    public void setupUI(Stage stage) {

        //Cast to GameScreen since LevelComplete is only called after a GameScreen
        this.previousGame = (GameScreen) previousScreen;

        Image backingImage = new Image(Assets.getAsset(Assets.Images.TRANSITION_BACKGROUND, Texture.class));
        backingImage.setSize(400, 320);
        backingImage.setPosition(200, 200);

        Label gameOverLabel = GUIBuilder.createLabel("Level Complete", 50, TITLE_COLOUR);
        gameOverLabel.setPosition((800 - gameOverLabel.getWidth()) / 2, 440);

        Label levelLabel = GUIBuilder.createLabel("Level", Assets.FontSizes.FORTY, TEXT_COLOUR);
        levelLabel.setPosition((800 - levelLabel.getWidth()) / 2, 390);

        Label levelIDLabel = GUIBuilder.createLabel((previousGame.getLevelId() + 1) + "", Assets.FontSizes.TWO_HUNDRED, TEXT_COLOUR);
        levelIDLabel.setPosition((800 - levelIDLabel.getWidth()) / 2, 195);

        ImageButton nextLevelButton = GUIBuilder.createButton(Assets.Buttons.NEXT_LEVEL);
        nextLevelButton.setSize(120, 90);
        nextLevelButton.setPosition(480, 90);
        nextLevelButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                nextLevel = previousGame.getLevelId() < Constants.MAX_LEVEL;
            }

        });

        ImageButton replayLevelButton = GUIBuilder.createButton(Assets.Buttons.REPLAY_LEVEL);
        replayLevelButton.setSize(120, 90);
        replayLevelButton.setPosition(340, 90);
        replayLevelButton.addListener(new ClickListener() {

            @Override
            public void clicked (InputEvent event, float x, float y) {
                replaying = true;
            }

        });

        ImageButton backMenuButton = GUIBuilder.createButton(Assets.Buttons.BACK_MENU);
        backMenuButton.setSize(120, 90);
        backMenuButton.setPosition(200, 90);
        backMenuButton.addListener(new ClickListener() {

            @Override
            public void clicked (InputEvent event, float x, float y) {
                backToMenu = true;
            }

        });

        buttons = new Array<ImageButton>();
        buttons.add(nextLevelButton);
        buttons.add(backMenuButton);
        buttons.add(replayLevelButton);

        stage.addActor(backingImage);
        stage.addActor(gameOverLabel);
        stage.addActor(levelLabel);
        stage.addActor(levelIDLabel);
        stage.addActor(nextLevelButton);
        stage.addActor(replayLevelButton);
        stage.addActor(backMenuButton);
    }

    @Override
    public void checkChange() {
        for (ImageButton button: buttons) button.setChecked(button.isOver());

        if (replaying) game.setScreen(new GameScreen(game, previousGame.getLevelId()));
        if (nextLevel) game.setScreen(new GameScreen(game, previousGame.getLevelId() + 1));
        if (backToMenu) game.setScreen(new LevelSelectScreen(game, Constants.DEFAULT_PACK));
    }


}
