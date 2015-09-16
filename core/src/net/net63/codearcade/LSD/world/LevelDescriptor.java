package net.net63.codearcade.LSD.world;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Basim on 12/09/15.
 */
public class LevelDescriptor {

    public LevelDescriptor () { }

    private int sensorCount = 0;
    private int sensorsDestroyed = 0;
    private Rectangle worldBounds = new Rectangle();

    public void setSensorCount(int sensorCount) {
        this.sensorCount = sensorCount;
    }

    public int getSensorsDestroyed() {
        return sensorsDestroyed;
    }

    public void setSensorsDestroyed(int sensorsDestroyed) {
        this.sensorsDestroyed = sensorsDestroyed;
    }

    public int getSensorCount() {
        return sensorCount;
    }

    public Rectangle getWorldBounds() {
        return worldBounds;
    }

    public void setWorldBounds(Rectangle worldBounds) {
        this.worldBounds = worldBounds;
    }
}
