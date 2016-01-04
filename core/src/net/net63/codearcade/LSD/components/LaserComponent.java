package net.net63.codearcade.LSD.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Basim on 31/12/15.
 */
public class LaserComponent implements Component {

    //Between 0 and 360 clockwise with 0 pointing right
    public float angle;
    public float interval;

    //0 - up, 1 - right, 2 - down, 3 - left
    public int direction;

    public float laserTime = 0;
    public boolean laserEnabled = false;

    //public float laserUpdateTime = Constants.LASER_UPDATE_TIME;

    public Vector2 laserEndPos = new Vector2();
    public boolean updateLaser = false;
    public Body laserSensorBody;
}
