package net.net63.codearcade.LSD.screens.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.screens.AbstractScreen;
import net.net63.codearcade.LSD.screens.GameScreen;
import net.net63.codearcade.LSD.utils.Assets;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 30/09/15.
 */
public class GameOverScreen extends AbstractScreen {

    private LSD game;

    private Stage stage;

    private Texture overlayTexture;

    private Image overlay;

    private GameScreen previousGame;

    public GameOverScreen(LSD game, GameScreen previousGame) {
        super(game);

        this.game = game;
        this.previousGame = previousGame;

        stage = new Stage(new ExtendViewport(Constants.DEFAULT_SCREEN_WIDTH, Constants.DEFAULT_SCREEN_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        Pixmap overlay = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        overlay.setColor(0, 0, 0, 0.5f);
        overlay.fill();

        overlayTexture = new Texture(overlay);
        overlay.dispose();

        setupUI();
    }

    private void setupUI() {
        overlay = new Image(overlayTexture);
        overlay.setScaling(Scaling.stretch);

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

        ImageButton backMenuButton = Assets.createButton(Assets.Buttons.BACK_MENU);
        backMenuButton.setSize(120, 90);
        backMenuButton.setPosition(200, 90);

        stage.addActor(overlay);
        stage.addActor(backingImage);
        stage.addActor(gameOverLabel);
        stage.addActor(nextLevelButton);
        stage.addActor(replayLevelButton);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        previousGame.resize(width, height);

        Viewport viewport = stage.getViewport();
        viewport.update(width, height);

        Vector2 zero = new Vector2(0, height - 1);
        viewport.unproject(zero);

        overlay.setPosition(zero.x, zero.y);
        overlay.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        previousGame.render(delta);

        stage.getViewport().apply();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void hide() {
        super.hide();
        dispose();
    }

    @Override
    public void dispose() {
        super.dispose();

        overlayTexture.dispose();
        stage.dispose();
    }
}
