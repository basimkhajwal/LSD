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

        //Check how far the object has come from the previous node
        int previousNode = node.nextNode + (node.movingForward ? -1: 1);
        float distanceTravelled = body.getPosition().dst(node.nodes[previousNode]);

        //Check if it has gone further than was needed to get to the next node
        if (distanceTravelled >= node.distanceToNext) {

            //Check if the node has reached the end of it's movement line
            if (node.nextNode == (node.movingForward ? node.nodes.length - 1 : 0)) {
                //Reverse the direction
                node.movingForward = !node.movingForward;
            }

            //Make sure the object is set to the position of the node
            body.setTransform(node.nodes[node.nextNode], body.getAngle());

            //Update the next node
            node.nextNode += node.movingForward ? 1 : -1;

            //Update the distance needed to get to the next node
            node.distanceToNext = node.nodes[node.nextNode].dst(body.getPosition());
        }
    }

}
