package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Disposable;
import net.net63.codearcade.LSD.components.ParticleComponent;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 14/10/15.
 */
public class ParticleRenderSystem extends IteratingSystem implements Disposable {

    private ComponentMapper<ParticleComponent> particleMapper;

    private ShapeRenderer shapeRenderer;
    private OrthographicCamera gameCamera;

    public ParticleRenderSystem(OrthographicCamera gameCamera) {
        super(Family.all(ParticleComponent.class).get(), Constants.SYSTEM_PRIORITIES.PARTICLE_RENDER);

        this.gameCamera = gameCamera;

        shapeRenderer = new ShapeRenderer();
        particleMapper = ComponentMapper.getFor(ParticleComponent.class);
    }

    @Override
    public void update(float deltaTime) {
        shapeRenderer.setProjectionMatrix(gameCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        super.update(deltaTime);

        shapeRenderer.end();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ParticleComponent component = particleMapper.get(entity);

        Vector2 bottomLeft = new Vector2();
        Vector2 dimensions = new Vector2();

        for (Body particle: component.particles) {
            PolygonShape shape = (PolygonShape) particle.getFixtureList().first().getShape();

            shape.getVertex(2, dimensions);
            bottomLeft.set(particle.getPosition()).sub(dimensions);

            dimensions.scl(2);

            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(bottomLeft.x, bottomLeft.y, dimensions.x, dimensions.y, Color.RED, Color.RED, Color.RED, Color.RED);
        }
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
