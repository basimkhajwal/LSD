package net.net63.codearcade.LSD.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

/**
 * A utility renderer that renders the background given
 * certain variables with a shader
 *
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

    /**
     * Create a new background shader
     *
     * @param shaderName The string file stub of the shader
     * @param renderMethod The method to render the texture by
     */
    public BackgroundRenderer(String shaderName, BackgroundRenderable renderMethod) {
        this(ShaderManager.getShader(shaderName), renderMethod);
    }

    /**
     * Create a new background renderer
     *
     * @param shaderProgram The shader by which to render with
     * @param renderMethod The method to render the texture by
     */
    public BackgroundRenderer(ShaderProgram shaderProgram, BackgroundRenderable renderMethod) {
        this.shaderProgram = shaderProgram;
        shaderProgram.pedantic = false;

        this.renderMethod = renderMethod;

        camera = new OrthographicCamera();
        batch = new SpriteBatch();

        //Get the background and set the shader uniform
        backgroundTexture = Assets.getAsset(Assets.Images.BACKGROUND, Texture.class);
        backgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        shaderProgram.begin();
        shaderProgram.setUniformf("invScreenSize", 1.0f/backgroundTexture.getWidth(), 1.0f/backgroundTexture.getHeight());
        shaderProgram.end();

        renderMethod.setup(this);
    }

    /**
     * Render the background
     *
     * @param deltaTime The time to update by in seconds
     */
    public void render(float deltaTime) {

        //Increment time
        time += deltaTime;

        //Set the batch variables
        batch.setShader(shaderProgram);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        //Send the time
        shaderProgram.setUniformf("time", time);

        //Render according to the method
        renderMethod.renderBackground(batch, deltaTime);

        batch.end();
    }

    /**
     * Resize the background
     *
     * @param width The width of the screen
     * @param height The height of the screen
     */
    public void resize(int width, int height) {
        //Set the sizes
        camera.setToOrtho(false, width, height);
        screenSize.set(width, height);

        //Send the screenSize uniform
        shaderProgram.begin();
        shaderProgram.setUniformf("screenSize", width, height);
        shaderProgram.end();
    }

    /**
     * Interface for a class that can render a background
     */
    public interface BackgroundRenderable {
        void setup(BackgroundRenderer renderer);
        void renderBackground(SpriteBatch batch, float deltaTime);
    }

    //The default background rendering
    public static final BackgroundRenderable DEFAULT = new BackgroundRenderable() {

        private BackgroundRenderer renderer;

        @Override
        public void setup(BackgroundRenderer renderer) {
            this.renderer = renderer;
        }

        @Override
        public void renderBackground(SpriteBatch batch, float deltaTime) {
            Vector2 size = renderer.getScreenSize();

            batch.draw(renderer.getTexture(), 0, 0, size.x, size.y);
        }
    };

    @Override
    public void dispose() {
        batch.dispose();
        shaderProgram.dispose();
    }

    // ---------------- Getter Methods -----------------------

    public ShaderProgram getShaderProgram() { return shaderProgram; }

    public float getTime() { return time; }

    public Vector2 getScreenSize() { return screenSize; }

    public Texture getTexture() { return backgroundTexture; }

}
