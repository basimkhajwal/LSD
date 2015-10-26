package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 26/10/15.
 */
public class CameraShakeSystem extends EntitySystem {

    private static final float SHAKE_AMOUNT = 0.2f;

    private OrthographicCamera camera;
    private Vector3 previousPosition = new Vector3();

    private float shakeTime = 0;
    private boolean shaking = false;
    private boolean restored = true;

    public CameraShakeSystem(OrthographicCamera camera) {
        super(Constants.SYSTEM_PRIORITIES.CAMERA_SHAKE);

        this.camera = camera;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        previousPosition.set(camera.position);

        if (shaking) {
            camera.position.x += MathUtils.random(-SHAKE_AMOUNT, SHAKE_AMOUNT);
            camera.position.y += MathUtils.random(-SHAKE_AMOUNT, SHAKE_AMOUNT);
            camera.update();

            shakeTime -= deltaTime;
            shaking = shakeTime > 0;

            restored = false;
        }
    }

    public void applyShake(float time) {
        this.shaking = true;
        this.shakeTime = time;
    }

    public void restoreCamera() {
        if (!restored) camera.position.set(previousPosition);

        restored = true;
    }

    public void stopShake() {
        this.shaking = false;
        restoreCamera();
    }

}
