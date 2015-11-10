package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import net.net63.codearcade.LSD.utils.Assets;
import net.net63.codearcade.LSD.utils.Constants;
import net.net63.codearcade.LSD.utils.ShaderManager;

/**
 * Render the background for the game, using a separate
 * method since it requires a shader and custom scaling
 *
 * Created by Basim on 03/09/15.
 */
public class BackgroundRenderSystem extends EntitySystem implements Disposable {

    //Rendering caches
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Texture backgroundTexture;

    //Shaders and related material
    private ShaderProgram shaderProgram;
    private float time = 0;

    public BackgroundRenderSystem() {
        super(Constants.SYSTEM_PRIORITIES.BACKGROUND_RENDER);

        backgroundTexture = Assets.getAsset(Assets.Images.BACKGROUND, Texture.class);

        camera = new OrthographicCamera();
        camera.setToOrtho(false);

        batch = new SpriteBatch();
        shaderProgram = ShaderManager.getShader(ShaderManager.Shaders.MENU);
        shaderProgram.pedantic = false;

        //Send the texture size shader
        shaderProgram.begin();
        shaderProgram.setUniformf("invScreenSize", 1f/backgroundTexture.getWidth(), 1f/backgroundTexture.getHeight());
        shaderProgram.end();
    }

    @Override
    public void update(float deltaTime) {
        //Update the time parameter
        time += deltaTime;

        batch.setProjectionMatrix(camera.combined);
        batch.setShader(shaderProgram);
        batch.begin();
        shaderProgram.setUniformf("time", time);
        batch.draw(backgroundTexture, 0, 0, camera.viewportWidth, camera.viewportHeight);
        batch.end();
    }

    /**
     * Resize the shader and texture to match the screen size
     */
    public void resize(int width, int height) {
        //Update the shader camera
        camera.setToOrtho(false, width, height);

        //Resend the uniform for the screenSize
        shaderProgram.begin();
        shaderProgram.setUniformf("screenSize", width, height);
        shaderProgram.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        shaderProgram.dispose();
    }
}
