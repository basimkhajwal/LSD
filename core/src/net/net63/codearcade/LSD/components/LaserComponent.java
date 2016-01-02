package net.net63.codearcade.LSD.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Basim on 31/12/15.
 */
public class LaserComponent implements Component {

    //Between 0 and 360 clockwise with 0 pointing right
    public float angle;
    public float interval;

    //0 - up, 1 - right, 2 - down, 3 - left
    public int direction;

    public float currentTime = 0;
    public boolean laserEnabled = false;
}
