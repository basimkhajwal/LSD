package net.net63.codearcade.LSD.world;

/**
 * Created by Basim on 12/09/15.
 */
public class LevelDescriptor {

    public LevelDescriptor () { }

    private int sensorCount = 0;
    private int sensorsDestroyed = 0;

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

}
