package net.net63.codearcade.LSD.listeners;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import net.net63.codearcade.LSD.components.BodyComponent;
import net.net63.codearcade.LSD.components.ParticleComponent;
import net.net63.codearcade.LSD.components.SensorComponent;

/**
 * Created by Basim on 13/10/15.
 */
public class SensorDestroyListener implements EntityListener {

    private static final float PARTICLE_SIZE = 0.1f;

    private ComponentMapper<SensorComponent> sensorMapper;
    private ComponentMapper<BodyComponent> bodyMapper;

    private World world;
    private Engine engine;

    public SensorDestroyListener(Engine engine, World world) {
        super();

        this.engine = engine;
        this.world = world;

        sensorMapper = ComponentMapper.getFor(SensorComponent.class);
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
    }

    @Override
    public void entityAdded(Entity entity) { }

    @Override
    public void entityRemoved(Entity entity) {
        if (sensorMapper.has(entity)) {
            Body body = bodyMapper.get(entity).body;
            PolygonShape shape = (PolygonShape) body.getFixtureList().first().getShape();

            Vector2 dimensions = new Vector2();
            shape.getVertex(2, dimensions);

            Vector2 bottomLeft = body.getPosition().cpy().sub(dimensions);

            dimensions.scl(2);

            int particlesAcross = (int) (dimensions.x / PARTICLE_SIZE);
            int particlesDown = (int) (dimensions.y / PARTICLE_SIZE);

            ParticleComponent particleComponent = new ParticleComponent();

            for (int j = 0; j < particlesDown; j++) {
                for (int i = 0; i < particlesAcross; i++) {
                    particleComponent.particles.add(createParticle(bottomLeft.x + i * PARTICLE_SIZE, bottomLeft.y + j * PARTICLE_SIZE));
                }
            }

            Entity particleEntity = new Entity();
            particleEntity.add(particleComponent);
            engine.addEntity(particleEntity);
        }
    }

    private Body createParticle(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();

        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = false;
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(PARTICLE_SIZE / 2, PARTICLE_SIZE / 2);
        fixtureDef.shape = shape;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);

        return body;
    }
}
