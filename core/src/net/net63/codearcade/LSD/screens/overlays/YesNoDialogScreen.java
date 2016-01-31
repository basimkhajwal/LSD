package net.net63.codearcade.LSD.screens.overlays;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.managers.Assets;
import net.net63.codearcade.LSD.screens.AbstractScreen;

/**
 * Created by Basim on 31/01/16.
 */
public class YesNoDialogScreen extends AbstractOverlay {

    private String title;
    private String message;

    public YesNoDialogScreen(LSD game, AbstractScreen previousScreen, String title, String message) {
        super(game, previousScreen);

        this.title = title;
        this.message = message;
    }

    @Override
    public void setupUI(Stage stage) {

        NinePatch bg = new NinePatch(Assets.getAsset(Assets.Images.SETTINGS_BACKGROUND, Texture.class), 9, 9, 9, 9);
        final Image background = new Image(bg);
        background.setSize(400, 500);
        background.setPosition(200, 50);



        stage.addActor(background);

    }

    @Override
    public void update() {

    }
}
