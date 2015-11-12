package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
    private static final int BLOCKS_PER_LAYER = 10;

    private static final float MIN_BLOCK_SIZE = 0.1f;
    private static final float MAX_BLOCK_SIZE = 0.6f;

    private static final float MIN_GRAY = 245 / 255f;
    private static final float MAX_GRAY = 250 / 255f;

    private static final float ALPHA = 0.4f;

    private static final float MIN_MOVEMENT = 0.25f;
    private static final float MAX_MOVEMENT = 0.75f;
    private static final float STEP_MOVEMENT = (MAX_MOVEMENT - MIN_MOVEMENT) / NUM_LAYERS;

    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;

    private Rectangle[][] layers = new Rectangle[NUM_LAYERS][];
    private Color[] colors = new Color[NUM_LAYERS];

    private Vector2 previousPosition = new Vector2();
    private Vector2 tmp = new Vector2();

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
                float size = MathUtils.random(MIN_BLOCK_SIZE, MAX_BLOCK_SIZE);
                layers[n][i] = new Rectangle(
                        MathUtils.random(bounds.x, bounds.x + bounds.width),
                        MathUtils.random(bounds.y, bounds.y + bounds.height),
                        size, size
                );
            }

            float gray = MathUtils.random(MIN_GRAY, MAX_GRAY);
            colors[n] = new Color(gray, gray, gray, ALPHA);
        }

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        tmp.set(camera.position.x, camera.position.y);
        boolean change = false;

        if (!previousPosition.isZero() && !previousPosition.equals(tmp)) {
            change = true;
            tmp.sub(previousPosition);
        }

        previousPosition.set(camera.position.x, camera.position.y);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float initMovement = MIN_MOVEMENT + STEP_MOVEMENT / 2;

        for (int i = 0; i < NUM_LAYERS; i++) {
            shapeRenderer.setColor(colors[i]);
            float amount = initMovement + STEP_MOVEMENT * i;

            for (int j = 0; j < BLOCKS_PER_LAYER; j++) {
                Rectangle rectangle = layers[i][j];

                if (change) {
                    rectangle.x += tmp.x * amount;
                    rectangle.y += tmp.y * amount;
                }

                shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            }
        }

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
