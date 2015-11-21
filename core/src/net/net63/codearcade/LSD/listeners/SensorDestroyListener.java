package net.net63.codearcade.LSD.listeners;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import net.net63.codearcade.LSD.components.BodyComponent;
import net.net63.codearcade.LSD.components.SensorComponent;
import net.net63.codearcade.LSD.world.WorldBuilder;

/**
 * Created by Basim on 13/10/15.
 */
public class SensorDestroyListener implements EntityListener {

    private static final float MAX_PARTICLE_SIZE = 0.09f;
    private static final float MIN_PARTICLE_SIZE = 0.05f;
    private static final int PARTICLE_NUM = 50;

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

            Vector2[] positions = new Vector2[PARTICLE_NUM];
            Color[] colors = new Color[PARTICLE_NUM];

            for (int i = 0; i < PARTICLE_NUM; i++) {
                positions[i] = new Vector2(
                        bottomLeft.x + MathUtils.random(0, dimensions.x),
                        bottomLeft.y + MathUtils.random(0, dimensions.y));

                colors[i] = Color.BLACK;
            }

            WorldBuilder.createParticleEffect(positions, colors, 3);
        }
    }
}
