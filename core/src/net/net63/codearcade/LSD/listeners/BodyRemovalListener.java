package net.net63.codearcade.LSD.listeners;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.physics.box2d.World;
import net.net63.codearcade.LSD.components.BodyComponent;

/**
 * Created by Basim on 11/07/15.
 */
public class BodyRemovalListener implements EntityListener{

    private World world;
    private ComponentMapper<BodyComponent> bodyMapper;

    public BodyRemovalListener(World world) {
        this.world = world;

        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
    }

    @Override
    public void entityAdded(Entity entity) { }

    @Override
    public void entityRemoved(Entity entity) {
        if (bodyMapper.has(entity)) {
            world.destroyBody(bodyMapper.get(entity).body);
        }
    }

}