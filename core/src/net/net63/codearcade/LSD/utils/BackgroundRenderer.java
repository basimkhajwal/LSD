package net.net63.codearcade.LSD.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by Basim on 15/11/15.
 */
public class BackgroundRenderer implements Disposable {

    private ShaderProgram shaderProgram;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Texture backgroundTexture;
    private BackgroundRenderable renderMethod;

    private Vector2 screenSize = new Vector2();
    private float time;

    public BackgroundRenderer(ShaderProgram shaderProgram, BackgroundRenderable renderMethod) {
        this.shaderProgram = shaderProgram;
        shaderProgram.pedantic = false;

        this.renderMethod = renderMethod;
        renderMethod.setup(this);
        backgroundTexture = Assets.getAsset(Assets.Images.BACKGROUND, Texture.class);

        camera = new OrthographicCamera();
        batch = new SpriteBatch();
    }

    public void render(float deltaTime) {

        time += deltaTime;

        batch.setShader(shaderProgram);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        shaderProgram.setUniformf("time", time);
        renderMethod.renderBackground(batch, backgroundTexture);

        batch.end();
    }

    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        screenSize.set(width, height);

        shaderProgram.begin();
        shaderProgram.setUniformf("screenSize", width, height);
        shaderProgram.end();
    }

    public interface BackgroundRenderable {
        void setup(BackgroundRenderer renderer);
        void renderBackground(SpriteBatch batch, Texture background);
    }

    public static final BackgroundRenderable DEFAULT = new BackgroundRenderable() {

        private BackgroundRenderer renderer;

        @Override
        public void setup(BackgroundRenderer renderer) {
            this.renderer = renderer;
        }

        @Override
        public void renderBackground(SpriteBatch batch, Texture background) {
            batch.draw
        }
    };

    public ShaderProgram getShaderProgram() {
        return shaderProgram;
    }

    public float getTime() {
        return time;
    }

    public Vector2 getScreenSize() {
        return screenSize;
    }

    @Override
    public void dispose() {
        batch.dispose();
    }


}
