package net.net63.codearcade.LSD.screens.transitions;

import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.screens.AbstractScreen;

/**
 * Created by Basim on 30/09/15.
 */
public abstract class TransitionScreen extends AbstractScreen {

    private LSD game;

    private AbstractScreen previousScreen;

    private boolean drawOverlay = false;

    public TransitionScreen(LSD game, AbstractScreen previousScreen) {
        super(game);

        this.previousScreen = previousScreen;
        this.game = game;
    }

    public void switchOver(AbstractScreen newScreen) {
        previousScreen.dispose();
        this.dispose();
        game.setScreen(newScreen);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        previousScreen.resize(width, height);
    }

    @Override
    public void render(float deltaTime) {
        super.render(deltaTime);

        previousScreen.render(deltaTime);
    }
}
