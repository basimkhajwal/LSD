package net.net63.codearcade.LSD.screens.transitions;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.screens.AbstractScreen;
import net.net63.codearcade.LSD.screens.GameScreen;

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

        stage = new Stage();

        Pixmap overlay = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        overlay.setColor(0, 0, 0, 0.5f);
        overlay.fill();

        overlayTexture = new Texture(overlay);
        overlay.dispose();

        setupUI();
    }

    private void setupUI() {
        overlay = new Image(overlayTexture);


        stage.addActor(overlay);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        stage.getViewport().update(width, height);

        overlay.setPosition(0, 0);
        overlay.setSize(width, height);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        previousGame.render(delta);

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

        stage.dispose();
    }
}
