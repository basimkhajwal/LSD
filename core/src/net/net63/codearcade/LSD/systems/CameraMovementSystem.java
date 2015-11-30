package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import net.net63.codearcade.LSD.components.BodyComponent;
import net.net63.codearcade.LSD.components.PlayerComponent;
import net.net63.codearcade.LSD.events.EventQueue;
import net.net63.codearcade.LSD.events.GameEvent;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * System to follow the update the camera in order
 * to follow the player in the centre
 *
 * Created by Basim on 17/09/15.
 */
public class CameraMovementSystem extends IteratingSystem {

    //Camera pointer and the previous position
    private OrthographicCamera camera;
    private Vector2 oldPos;

    //Event handler
    private EventQueue eventQueue;

    //Mapper(s)
    private ComponentMapper<BodyComponent> bodyMapper;

    //Whether to reset the camera or not
    private boolean forcedUpdate = true;

    public CameraMovementSystem(OrthographicCamera camera, Signal<GameEvent> gameEventSignal) {
        super(Family.all(PlayerComponent.class).get(), Constants.SYSTEM_PRIORITIES.CAMERA_MOVEMENT);

        this.camera = camera;
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);

        eventQueue = new EventQueue();
        gameEventSignal.add(eventQueue);
    }

    @Override
    public void processEntity(Entity player, float deltaTime) {

        //Check for resize events
        for (GameEvent event : eventQueue.getEvents()) {
            if (event == GameEvent.RESIZE) forcedUpdate = true;
        }

        //Get the player position
        Vector2 pos = bodyMapper.get(player).body.getPosition();

        //Re-centre only if forced to or the player has changed position
        if (!pos.equals(oldPos) || forcedUpdate) {
            //Set camera to centre
            camera.position.set(pos.x, pos.y, camera.position.z);
            camera.update();

            //Keep current position for next time
            oldPos = pos.cpy();
            forcedUpdate = false;
        }
    }

    /**
     * Make sure that the camera is centred in the next tick
     */
    public void forceUpdate() { forcedUpdate = true; }

}
