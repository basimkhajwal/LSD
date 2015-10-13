package net.net63.codearcade.LSD.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Basim on 13/10/15.
 */
public class PhysicsParticleComponent implements Component {

    public Array<Body> particles = new Array<Body>();

    public float finalTime = 0f;
    public float currentTime = 0f;

}
