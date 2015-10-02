package net.net63.codearcade.LSD.screens.transitions;

import com.badlogic.gdx.scenes.scene2d.Stage;
import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.screens.AbstractScreen;
import net.net63.codearcade.LSD.screens.GameScreen;

/**
 * Created by Basim on 30/09/15.
 */
public class LevelCompleteScreen extends AbstractScreen {

    private LSD game;
    private GameScreen previousGame;

    private Stage stage;

    public LevelCompleteScreen(LSD game, GameScreen previousGame) {
        super(game);

        this.previousGame = previousGame;
        this.game = game;

        stage = new Stage();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        stage.getViewport().update(width, height);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        previousGame.render(delta);
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
