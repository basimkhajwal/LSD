package net.net63.codearcade.LSD.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.net63.codearcade.LSD.LSD;

/**
 * Created by Basim on 13/11/15.
 */
public class LevelSelectScreen extends AbstractScreen {

    private LSD game;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    public LevelSelectScreen(LSD game) {
        super(game);

        this.game = game;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void render(float deltaTime) {
        super.render(deltaTime);
    }

    @Override
    public void dispose() {
        super.dispose();

    }


}
