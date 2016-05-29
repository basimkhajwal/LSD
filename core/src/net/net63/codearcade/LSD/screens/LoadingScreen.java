package net.net63.codearcade.LSD.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.managers.Assets;
import net.net63.codearcade.LSD.utils.CentreGUI;
import net.net63.codearcade.LSD.utils.GUIBuilder;

/**
 * Created by Basim on 29/05/16.
 */
public class LoadingScreen extends AbstractScreen {

    private CentreGUI centreGUI;

    public LoadingScreen(LSD game) {
        super(game);
        this.clear = Color.LIGHT_GRAY;

        centreGUI = new CentreGUI();
        setupUI(centreGUI.getStage());
    }

    private void setupUI(Stage stage) {

        Label title = GUIBuilder.createLabel("Loading", Assets.FontSizes.HUNDRED, Color.DARK_GRAY);
        title.setPosition(400, 400, Align.center);

        stage.addActor(title);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        centreGUI.render(delta);
    }

    @Override
    public void dispose() {
        super.dispose();

        centreGUI.dispose();
    }

}
