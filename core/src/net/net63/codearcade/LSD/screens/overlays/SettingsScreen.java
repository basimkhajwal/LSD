package net.net63.codearcade.LSD.screens.overlays;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.managers.Assets;
import net.net63.codearcade.LSD.screens.AbstractScreen;

/**
 * Created by Basim on 21/12/15.
 */
public class SettingsScreen extends AbstractOverlay {



    public SettingsScreen(LSD game, AbstractScreen previousScreen) {
        super(game, previousScreen);

        disposeScreen = false;



    }

    @Override
    public void setupUI(Stage stage) {

        NinePatch bg = new NinePatch(Assets.getAsset(Assets.Images.SETTINGS_BACKGROUND, Texture.class), 9, 9, 9, 9);
        Image background = new Image(bg);
        background.setSize(700, 500);
        background.setPosition(50, 50);
        stage.addActor(background);
        
    }

    @Override
    public void checkChange() {

    }

}
