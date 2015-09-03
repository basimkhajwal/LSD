package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import net.net63.codearcade.LSD.utils.Assets;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 03/09/15.
 */
public class BackgroundRenderSystem extends EntitySystem implements Disposable {

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Texture backgroundTexture;

    public BackgroundRenderSystem() {
        super(Constants.SYSTEM_PRIORITIES.BACKGROUND_RENDER);

        backgroundTexture = Assets.getAsset(Assets.Images.BACKGROUND, Texture.class);

        camera = new OrthographicCamera();
        camera.setToOrtho(false);

        batch = new SpriteBatch();
    }

    @Override
    public void update(float deltaTime) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, camera.viewportWidth, camera.viewportHeight);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
