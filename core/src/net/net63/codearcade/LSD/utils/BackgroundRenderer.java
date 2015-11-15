package net.net63.codearcade.LSD.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by Basim on 15/11/15.
 */
public class BackgroundRenderer implements Disposable {

    private ShaderProgram shaderProgram;

    private OrthographicCamera camera;
    private SpriteBatch batch;


    public BackgroundRenderer(ShaderProgram shaderProgram) {
        this.shaderProgram = shaderProgram;

        camera = new OrthographicCamera();
        batch = new SpriteBatch();
    }

    public void render(float deltaTime) {

        batch.setShader(shaderProgram);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();


        batch.end();
    }

    public void resize(int width, int height) {
        camera.setToOrtho(false);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }


}
