package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import net.net63.codearcade.LSD.utils.Constants;
import net.net63.codearcade.LSD.world.LevelDescriptor;

/**
 * Created by Basim on 11/11/15.
 */
public class ParallaxEffectSystem extends EntitySystem implements Disposable {

    private static final int NUM_LAYERS = 3;
    private static final int BLOCKS_PER_LAYER = 30;

    private static final Vector2 MIN_BLOCK_SIZE = new Vector2(0.5f, 0.5f);
    private static final Vector2 MAX_BLOCK_SIZE = new Vector2(1, 1);

    private static final float MIN_GRAY = 150 / 255f;
    private static final float MAX_GRAY = 170 / 255f;

    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;

    private Rectangle[][] layers = new Rectangle[NUM_LAYERS][];
    private Color[] colors = new Color[NUM_LAYERS];

    public ParallaxEffectSystem(OrthographicCamera camera, LevelDescriptor levelDescriptor) {
        super(Constants.SYSTEM_PRIORITIES.PARALLAX_EFFECT);

        this.camera = camera;
        shapeRenderer = new ShapeRenderer();

        generateLayers(levelDescriptor.getWorldBounds());
    }

    private void generateLayers(Rectangle bounds) {

        System.out.println(bounds.toString());

        for (int n = 0; n < NUM_LAYERS; n++) {
            layers[n] = new Rectangle[BLOCKS_PER_LAYER];

            for (int i = 0; i < BLOCKS_PER_LAYER; i++) {
                layers[n][i] = new Rectangle(
                        MathUtils.random(bounds.x, bounds.x + bounds.width),
                        MathUtils.random(bounds.y, bounds.y + bounds.height),
                        MathUtils.random(MIN_BLOCK_SIZE.x, MAX_BLOCK_SIZE.x),
                        MathUtils.random(MAX_BLOCK_SIZE.y, MAX_BLOCK_SIZE.y)
                );
            }

            float gray = MathUtils.random(MIN_GRAY, MAX_GRAY);
            colors[n] = new Color(gray, gray, gray, 1);
        }

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (int i = 0; i < NUM_LAYERS; i++) {
            shapeRenderer.setColor(colors[i]);

            for (int j = 0; j < BLOCKS_PER_LAYER; j++) {
                Rectangle rectangle = layers[i][j];

                shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            }
        }

        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
