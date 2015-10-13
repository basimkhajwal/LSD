package net.net63.codearcade.LSD.listeners;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import net.net63.codearcade.LSD.components.BodyComponent;
import net.net63.codearcade.LSD.components.SensorComponent;

/**
 * Created by Basim on 13/10/15.
 */
public class SensorDestroyListener implements EntityListener {

    private static final float PARTICLE_SIZE = 0.2f;

    private ComponentMapper<SensorComponent> sensorMapper;
    private ComponentMapper<BodyComponent> bodyMapper;

    private World world;

    public SensorDestroyListener(World world) {
        super();

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

            for (int j = 0; j < particlesDown; j++) {
                for (int i = 0; i < particlesAcross; i++) {

                }
            }
        }
    }

    private Body createParticle(float x, float y) {
        
    }
}
