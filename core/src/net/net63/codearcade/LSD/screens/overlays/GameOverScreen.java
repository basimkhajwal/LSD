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
import net.net63.codearcade.LSD.screens.GameScreen;
import net.net63.codearcade.LSD.utils.Assets;
import net.net63.codearcade.LSD.utils.GUIBuilder;

/**
 * Created by Basim on 30/09/15.
 */
public class GameOverScreen extends AbstractOverlay {

    private boolean replaying = false;

    private Array<ImageButton> buttons;

    public GameOverScreen(LSD game, GameScreen previousGame) {
        super(game, previousGame);
    }

    @Override
    public void setupUI(Stage stage) {
        Image backingImage = new Image(Assets.getAsset(Assets.Images.TRANSITION_BACKGROUND, Texture.class));
        backingImage.setSize(400, 320);
        backingImage.setPosition(200, 200);

        Label gameOverLabel = GUIBuilder.createLabel("Game Over", Assets.FontSizes.FIFTY, Color.ORANGE);
        gameOverLabel.setPosition((800 - gameOverLabel.getWidth()) / 2, 440);

        Label levelLabel = GUIBuilder.createLabel("Level", Assets.FontSizes.FOURTY, Color.MAROON);
        levelLabel.setPosition((800 - levelLabel.getWidth()) / 2, 390);

        Label levelIDLabel = GUIBuilder.createLabel((previousGame.getLevelId() + 1) + "", Assets.FontSizes.TWO_HUNDRED, Color.MAROON);
        levelIDLabel.setPosition((800 - levelIDLabel.getWidth()) / 2, 195);

        Image nextLevelButton = new Image(Assets.getAsset(Assets.Images.NEXT_LEVEL_DISABLED, Texture.class));
        nextLevelButton.setSize(120, 90);
        nextLevelButton.setPosition(480, 90);

        ImageButton replayLevelButton = GUIBuilder.createButton(Assets.Buttons.REPLAY_LEVEL);
        replayLevelButton.setSize(120, 90);
        replayLevelButton.setPosition(340, 90);
        replayLevelButton.addListener(new ClickListener() {

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                replaying = true;
            }

        });

        ImageButton backMenuButton = GUIBuilder.createButton(Assets.Buttons.BACK_MENU);
        backMenuButton.setSize(120, 90);
        backMenuButton.setPosition(200, 90);

        buttons = new Array<ImageButton>();
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
    }

}
