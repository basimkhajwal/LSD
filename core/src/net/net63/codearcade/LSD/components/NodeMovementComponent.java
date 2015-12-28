package net.net63.codearcade.LSD.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Basim on 28/12/15.
 */
public class NodeMovementComponent implements Component {

    public float delay;
    public float timeRemaining;
    public boolean hasStarted = false;

    public boolean movingForward = true;
    public float speed;

    public Vector2[] nodes;
    public int currentNode;
}
