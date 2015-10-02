package net.net63.codearcade.LSD.utils;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Basim on 02/10/15.
 */
public class VectorImageFactory {


    public static Texture drawMenuPlayButton() {
        Pixmap pixmap = emptyPixmap(100, 100);

        

        return textureFromPixmap(pixmap);
    }

    private static Pixmap emptyPixmap(int width, int height) {
        return new Pixmap(width, height, Pixmap.Format.RGBA8888);
    }

    private static Texture textureFromPixmap(Pixmap pixmap) {
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

}
