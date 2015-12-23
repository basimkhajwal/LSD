package net.net63.codearcade.LSD.screens.overlays;

import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.screens.AbstractScreen;

/**
 * Created by Basim on 21/12/15.
 */
public class SettingsScreen extends AbstractScreen {

    private AbstractScreen previousScreen;

    public SettingsScreen(LSD game, AbstractScreen previousScreen) {
        super(game);

        this.previousScreen = previousScreen;
        previousScreen.pauseLogic();
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
