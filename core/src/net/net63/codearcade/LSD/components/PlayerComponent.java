package net.net63.codearcade.LSD.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Basim on 23/06/15.
 */
public class PlayerComponent implements Component{

    public static final int STATE_STILL     = 0;
    public static final int STATE_AIMING    = 1;
    public static final int STATE_FIRING    = 2;
    public static final int STATE_JUMPING   = 3;
    public static final int STATE_FALLING   = 4;
    public static final int STATE_HITTING   = 5;

    public Vector2 aimPosition;
    public Vector2 launchImpulse;
    public Vector2[] trajectoryPoints;

    public Entity sensorEntity = null;

    public boolean isFlying = false;
    public boolean isDead = false;
}
