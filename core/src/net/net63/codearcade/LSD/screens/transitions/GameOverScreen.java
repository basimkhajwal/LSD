package net.net63.codearcade.LSD.screens.transitions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

/**
 * Created by Basim on 30/09/15.
 */
public class GameOverScreen extends TransitionScreen {

    private boolean replaying = false;
    private Array<ImageButton> buttons;

    public GameOverScreen(LSD game, GameScreen previousGame) {
        super(game, previousGame);

        buttons = new Array<ImageButton>();
    }

    @Override
    public void setupUI(Stage stage) {
        BitmapFont fontFifty = Assets.getFont(Assets.Fonts.DEFAULT, Assets.FontSizes.FIFTY);

        Image backingImage = new Image(Assets.getAsset(Assets.Images.TRANSITION_BACKGROUND, Texture.class));
        backingImage.setSize(400, 320);
        backingImage.setPosition(200, 200);

        Label gameOverLabel = new Label("Game Over", new Label.LabelStyle(fontFifty, Color.ORANGE));
        gameOverLabel.setPosition((800 - gameOverLabel.getWidth()) / 2, 440);

        ImageButton nextLevelButton = Assets.createButton(Assets.Buttons.NEXT_LEVEL);
        nextLevelButton.setSize(120, 90);
        nextLevelButton.setPosition(480, 90);

        ImageButton replayLevelButton = Assets.createButton(Assets.Buttons.REPLAY_LEVEL);
        replayLevelButton.setSize(120, 90);
        replayLevelButton.setPosition(340, 90);
        replayLevelButton.addListener(new ClickListener() {

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                replaying = true;
            }

        });

        ImageButton backMenuButton = Assets.createButton(Assets.Buttons.BACK_MENU);
        backMenuButton.setSize(120, 90);
        backMenuButton.setPosition(200, 90);

        buttons.add(nextLevelButton);
        buttons.add(backMenuButton);
        buttons.add(replayLevelButton);

        stage.addActor(backingImage);
        stage.addActor(gameOverLabel);
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
