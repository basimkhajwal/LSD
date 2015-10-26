package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 26/10/15.
 */
public class CameraShakeSystem extends EntitySystem {

    private OrthographicCamera camera;

    private boolean shaking = false;

    public CameraShakeSystem(OrthographicCamera camera) {
        super(Constants.SYSTEM_PRIORITIES.CAMERA_SHAKE);
    }


}
