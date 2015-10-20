package net.net63.codearcade.LSD.listeners;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import net.net63.codearcade.LSD.components.BodyComponent;
import net.net63.codearcade.LSD.components.ParticleComponent;
import net.net63.codearcade.LSD.components.SensorComponent;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 13/10/15.
 */
public class SensorDestroyListener implements EntityListener {

    private static final float PARTICLE_SIZE = 0.08f;
    private static final int PARTICLE_NUM = 80;

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

            ParticleComponent particleComponent = new ParticleComponent();
            particleComponent.numParticles = PARTICLE_NUM;
            particleComponent.particles = new Body[PARTICLE_NUM];
            particleComponent.colors = new Color[PARTICLE_NUM];

            particleComponent.currentTime = 0;
            particleComponent.finalTime = MathUtils.random(3f, 5f);

            for (int i = 0; i < PARTICLE_NUM; i++) {
                Body particleBody = createParticle(
                        bottomLeft.x + MathUtils.random(0, dimensions.x),
                        bottomLeft.y + MathUtils.random(0, dimensions.y));

                particleBody.applyLinearImpulse(new Vector2(MathUtils.random(-20, 20), MathUtils.random(-10, 20)), particleBody.getWorldCenter(), true);
                particleBody.setAngularVelocity(MathUtils.random(-10, 10));

                particleComponent.particles[i] = particleBody;
                particleComponent.colors[i] = new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1.0f);
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
        fixtureDef.restitution = 0.6f;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = Constants.CategoryBits.PARTICLE;
        fixtureDef.filter.maskBits = Constants.MaskBits.PARTICLE;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);

        return body;
    }
}
