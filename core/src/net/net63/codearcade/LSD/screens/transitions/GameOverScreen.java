package net.net63.codearcade.LSD.screens.transitions;

import com.badlogic.gdx.scenes.scene2d.Stage;
import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.screens.AbstractScreen;

/**
 * Created by Basim on 30/09/15.
 */
public class GameOverScreen extends AbstractScreen {

    private LSD game;

    private Stage stage;

    public GameOverScreen(LSD game) {
        super(game);

        this.game = game;

        stage = new Stage();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void show() {
        super.show();


    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public void hide() {
        super.hide();
        dispose();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
