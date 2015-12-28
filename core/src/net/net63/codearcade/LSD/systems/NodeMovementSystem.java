package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import net.net63.codearcade.LSD.components.BodyComponent;
import net.net63.codearcade.LSD.components.NodeMovementComponent;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 28/12/15.
 */
public class NodeMovementSystem extends IteratingSystem {

    private ComponentMapper<NodeMovementComponent> nodeMapper;
    private ComponentMapper<BodyComponent> bodyMapper;

    public NodeMovementSystem() {
        super(Family.all(BodyComponent.class, NodeMovementComponent.class).get(), Constants.SYSTEM_PRIORITIES.NODE_MOVEMENT);

        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        nodeMapper = ComponentMapper.getFor(NodeMovementComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        NodeMovementComponent node = nodeMapper.get(entity);
        Body body = bodyMapper.get(entity).body;

        
    }

}
