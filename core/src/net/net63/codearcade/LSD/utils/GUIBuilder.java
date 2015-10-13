package net.net63.codearcade.LSD.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by Basim on 12/10/15.
 */
public class GUIBuilder {

    private GUIBuilder() { }

    public static ImageButton createButton(String button) {
        Texture txt = Assets.getAsset(button + ".png", Texture.class);
        Texture txtDwn = Assets.getAsset(button + "_down.png", Texture.class);
        Texture txtHover = Assets.getAsset(button + "_hover.png", Texture.class);

        TextureRegionDrawable btn = new TextureRegionDrawable(new TextureRegion(txt));
        TextureRegionDrawable btnDown = new TextureRegionDrawable(new TextureRegion(txtDwn));
        TextureRegionDrawable btnChecked = new TextureRegionDrawable(new TextureRegion(txtHover));

        return new ImageButton(btn, btnDown, btnChecked);
    }

    public static Label createLabel(String text, int size, Color color) {
        return new Label(text, new Label.LabelStyle(Assets.getFont(Assets.Fonts.DEFAULT, size), color));
    }
}
