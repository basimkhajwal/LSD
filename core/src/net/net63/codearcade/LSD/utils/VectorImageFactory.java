package net.net63.codearcade.LSD.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Basim on 02/10/15.
 */
public class VectorImageFactory {

    private static final boolean EXTEND_UP = true;

    private static float scale = 1f;

    public static void setup(int screenWidth, int screenHeight) {
        scale =  EXTEND_UP ? (screenHeight * 1.0f / Constants.DEFAULT_SCREEN_HEIGHT)
                          : (screenWidth * 1.0f / Constants.DEFAULT_SCREEN_WIDTH);
    }

    public static Texture drawMenuPlayButton() {
        final int WIDTH = (int) (150 * scale);
        final int HEIGHT = (int) (150 * scale);

        final int[] TR_POS = {
                //x                 y
                (3 * WIDTH) / 8,    HEIGHT / 4,
                (3 * WIDTH) / 8,    3 * (HEIGHT / 4),
                3 * (WIDTH / 4),    HEIGHT / 2
        };

        final Color CIRCLE_COLOUR = new Color(181 / 255f, 181 / 255f, 181 / 255f, 1f);
        final Color TRIANGLE_COLOUR = new Color(66 / 255f, 66 / 255f, 66 / 255f, 1f);

        Pixmap pixmap = emptyPixmap(WIDTH, HEIGHT);

        pixmap.setColor(CIRCLE_COLOUR);
        pixmap.fillCircle(WIDTH / 2, HEIGHT / 2, WIDTH / 2);

        pixmap.setColor(TRIANGLE_COLOUR);
        pixmap.fillTriangle(TR_POS[0], TR_POS[1], TR_POS[2], TR_POS[3], TR_POS[4], TR_POS[5]);

        pixmap.setColor(Color.RED);
        pixmap.drawLine(WIDTH/2, 0, WIDTH/2, HEIGHT);
        pixmap.drawLine(0, HEIGHT/2, WIDTH, HEIGHT/2);

        return textureFromPixmap(pixmap);
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
