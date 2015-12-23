package net.net63.codearcade.LSD.screens.overlays;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.managers.Assets;
import net.net63.codearcade.LSD.screens.AbstractScreen;
import net.net63.codearcade.LSD.utils.GUIBuilder;

/**
 * Created by Basim on 21/12/15.
 */
public class SettingsScreen extends AbstractOverlay {

    private ImageButton crossButton;
    private boolean crossPressed = false;

    public SettingsScreen(LSD game, AbstractScreen previousScreen) {
        super(game, previousScreen);

        disposeScreen = false;
    }

    @Override
    public void setupUI(Stage stage) {

        NinePatch bg = new NinePatch(Assets.getAsset(Assets.Images.SETTINGS_BACKGROUND, Texture.class), 9, 9, 9, 9);
        Image background = new Image(bg);
        background.setSize(400, 500);
        background.setPosition(200, 50);

        Label title = GUIBuilder.createLabel("Settings", Assets.FontSizes.FORTY, Color.MAROON);
        title.setPosition((800 - title.getWidth()) / 2, 550 - title.getHeight() - 20);

        crossButton = GUIBuilder.createButton(Assets.Buttons.CROSS);
        crossButton.setSize(25, 25);
        crossButton.setPosition(background.getX() + background.getWidth() - 20,
                background.getY() + background.getHeight() - 20);
        crossButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                crossPressed = true;
            }

        });

        stage.addActor(background);
        stage.addActor(title);
        stage.addActor(crossButton);

    }

    @Override
    public void checkChange() {

        crossButton.setChecked(crossButton.isOver());

        if (crossPressed) {
            previousScreen.resumeLogic();
            game.setScreen(previousScreen);
        }
    }

}
