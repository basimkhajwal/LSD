package net.net63.codearcade.LSD.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Basim on 13/10/15.
 */
public class ParticleComponent implements Component {

    public int numParticles;
    public Body[] particles;
    public Color[] colors;
    
    public float finalTime = 0f;
    public float currentTime = 0f;

}
