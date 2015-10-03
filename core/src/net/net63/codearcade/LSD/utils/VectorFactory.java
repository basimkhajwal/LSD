package net.net63.codearcade.LSD.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;

/**
 * Created by Basim on 02/10/15.
 */
public class VectorFactory {

    private static final boolean EXTEND_UP = true;

    private static VectorCache cache;
    private static float scale = 1f;

    public static void setup(int screenWidth, int screenHeight) {
        scale =  EXTEND_UP ? (screenHeight * 1.0f / Constants.DEFAULT_SCREEN_HEIGHT)
                          : (screenWidth * 1.0f / Constants.DEFAULT_SCREEN_WIDTH);

        if (cache != null) cache.dispose();
        cache = new VectorCache();
    }

    public static Texture drawMenuPlayButton(ButtonType type) {
        int circle, triangle;

        if (type == ButtonType.HOVER) circle = 255;
        else if (type == ButtonType.DOWN) circle = 181;
        else circle = 235;

        triangle =  66;

        return drawMenuPlayButton(grayColor(circle), grayColor(triangle));
    }

    private static Texture drawMenuPlayButton(Color circleColour, Color triangleColour) {
        final int WIDTH = (int) (156 * scale);
        final int HEIGHT = (int) (156 * scale);

        final int[] TR_POS = {
                //x                 y
                (3 * WIDTH) / 10,   HEIGHT / 4,
                (3 * WIDTH) / 10,   (4 * HEIGHT) / 5,
                (4 * WIDTH) /  5,   HEIGHT / 2
        };

        Pixmap pixmap = emptyPixmap(WIDTH, HEIGHT);

        pixmap.setColor(circleColour);
        pixmap.fillCircle(WIDTH / 2, HEIGHT / 2, WIDTH / 2);

        pixmap.setColor(triangleColour);
        pixmap.fillTriangle(TR_POS[0], TR_POS[1], TR_POS[2], TR_POS[3], TR_POS[4], TR_POS[5]);

        return textureFromPixmap(pixmap);
    }

    // ----------------- Vector Type Enums --------------------

    public static enum ButtonType {
        NORMAL, HOVER, DOWN
    }

    public static enum Buttons {
        MENU_PLAY
    }


    // ----------------- Efficient Cache Storage ---------------

    public static class VectorCache implements Disposable {

        private HashMap<String, Texture> textures;

        public VectorCache() {
            textures = new HashMap<String, Texture>();
        }

        @Override
        public void dispose() {
            for (Texture texture: textures.values()) texture.dispose();
        }
    }

    // ------------------ Helper Methods -----------------------

    private static Color grayColor(int grayAmount) {
        return new Color(grayAmount / 255.0f, grayAmount / 255.0f, grayAmount / 255.0f, 1.0f);
    }

    private static Pixmap emptyPixmap(int width, int height) {
        return new Pixmap(width, height, Pixmap.Format.RGBA8888);
    }

    private static Texture textureFromPixmap(Pixmap pixmap) {
        Texture texture = new Texture(pixmap);
        //pixmap.dispose();
        return texture;
    }

}
