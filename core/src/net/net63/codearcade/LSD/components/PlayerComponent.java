package net.net63.codearcade.LSD.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Basim on 23/06/15.
 */
public class PlayerComponent extends Component{

    public static final int STATE_STILL     = 0;
    public static final int STATE_AIMING    = 1;
    public static final int STATE_FIRING    = 2;
    public static final int STATE_JUMPING   = 3;
    public static final int STATE_HITTING   = 4;

    public Vector2 aimPosition;
    public Vector2 launchImpulse;
    public Vector2[] trajectoryPoints;

    public boolean isDead = false;
}
