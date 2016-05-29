package net.net63.codearcade.LSD.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.managers.Assets;
import net.net63.codearcade.LSD.managers.SoundManager;
import net.net63.codearcade.LSD.utils.CentreGUI;
import net.net63.codearcade.LSD.utils.GUIBuilder;

/**
 * Created by Basim on 29/05/16.
 */
public class LoadingScreen extends AbstractScreen {

    private static final float LOAD_DELAY = 0.5f;

    private CentreGUI centreGUI;
    private Image progressForeground;

    private boolean loaded = false;
    private float loadTime = 0f;

    public LoadingScreen(LSD game) {
        super(game);
        this.clear = Color.LIGHT_GRAY;

        centreGUI = new CentreGUI();
        setupUI(centreGUI.getStage());

        Assets.loadAll(game.assetManager);
        SoundManager.loadAll(game.assetManager);
    }

    private void setupUI(Stage stage) {

        Label title = GUIBuilder.createLabel("Loading", Assets.FontSizes.FIFTY, Color.DARK_GRAY);
        title.setPosition(400, 350, Align.center);

        Pixmap pix = new Pixmap(450, 20, Pixmap.Format.RGBA8888);
        pix.setColor(Color.GRAY);
        pix.fill();

        Image progressBackground = new Image(new Texture(pix));
        progressBackground.setPosition(400-pix.getWidth()/2, 250);

        pix.setColor(Color.DARK_GRAY);
        pix.fill();
        progressForeground = new Image(new Texture(pix));
        progressForeground.setPosition(400-pix.getWidth()/2, 250);

        pix.dispose();

        stage.addActor(title);
        stage.addActor(progressBackground);
        stage.addActor(progressForeground);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        centreGUI.resize(width, height);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        boolean switchScreen = false;

        if (loaded) {
            loadTime += delta;
            if (loadTime >= LOAD_DELAY) switchScreen = true;

        } else if (game.assetManager.update()) {
            loaded = true;
            loadTime = 0;
        }

        progressForeground.setWidth(450 * game.assetManager.getProgress());
        centreGUI.render(delta);

        if (switchScreen) {
            Assets.finishLoading();
            SoundManager.playMusic();
            game.setScreen(new MenuScreen(game));
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        centreGUI.dispose();
    }

}
