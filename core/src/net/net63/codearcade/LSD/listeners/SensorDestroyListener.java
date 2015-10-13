package net.net63.codearcade.LSD.listeners;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import net.net63.codearcade.LSD.components.BodyComponent;
import net.net63.codearcade.LSD.components.SensorComponent;

/**
 * Created by Basim on 13/10/15.
 */
public class SensorDestroyListener implements EntityListener {

    private ComponentMapper<SensorComponent> sensorMapper;
    private ComponentMapper<BodyComponent> bodyMapper;

    public SensorDestroyListener() {
        super();

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

            
        }
    }
}
