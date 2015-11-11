package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import net.net63.codearcade.LSD.world.LevelDescriptor;

/**
 * Created by Basim on 11/11/15.
 */
public class ParallaxEffectSystem extends EntitySystem implements Disposable {

    private static final int NUM_LAYERS = 3;
    private static final int BLOCKS_PER_LAYER = 30;

    private static final Vector2 MIN_BLOCK_SIZE = new Vector2(1, 1);
    private static final Vector2 MAX_BLOCK_SIZE = new Vector2(7, 7);

    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;

    private Rectangle[][] layers = new Rectangle[NUM_LAYERS][BLOCKS_PER_LAYER];

    public ParallaxEffectSystem(OrthographicCamera camera, LevelDescriptor levelDescriptor) {
        super();

        this.camera = camera;
        shapeRenderer = new ShapeRenderer();

        generateLayers(levelDescriptor.getWorldBounds());
    }

    private void generateLayers(Rectangle bounds) {

        for (int n = 0; n < NUM_LAYERS; n++) {
            for (int i = 0; i  < BLOCKS_PER_LAYER) {
                layers[n][i] = new Rectangle(
                        MathUtils.random(bounds.x, bounds.x + bounds.width),
                        MathUtils.random(bounds.y, bounds.y + bounds.height),
                        MathUtils.random(MIN_BLOCK_SIZE.x, MAX_BLOCK_SIZE.x),
                        MathUtils.random(MAX_BLOCK_SIZE.y, MAX_BLOCK_SIZE.y)
                );
            }
        }

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        shapeRenderer.setProjectionMatrix(camera.combined);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
