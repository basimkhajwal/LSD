package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * System to shake the camera when a platform is
 * destroyed
 *
 * Created by Basim on 26/10/15.
 */
public class CameraShakeSystem extends EntitySystem {

    //How much to vary the shake
    private static final float SHAKE_AMOUNT = 0.2f;

    //Camera pointer and store the pre-shake position
    private OrthographicCamera camera;
    private Vector3 previousPosition = new Vector3();

    //Time and flags
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

        //Store the previous position
        previousPosition.set(camera.position);

        //Apply shake
        if (shaking) {
            //Move camera by a random amount
            camera.position.x += MathUtils.random(-SHAKE_AMOUNT, SHAKE_AMOUNT);
            camera.position.y += MathUtils.random(-SHAKE_AMOUNT, SHAKE_AMOUNT);
            camera.update();

            //Keep shaking whilst time is non-negative
            shakeTime -= deltaTime;
            shaking = shakeTime > 0;

            restored = false;
        }
    }

    /**
     * Shake the screen
     *
     * @param time The amount of time to shake for (seconds)
     */
    public void applyShake(float time) {
        this.shaking = true;
        this.shakeTime = time;
    }

    /**
     * Reset the camera to the previous position if a shake
     * had previously been applied
     */
    public void restoreCamera() {
        if (!restored) camera.position.set(previousPosition);
        restored = true;
    }

    /**
     * Stop shaking the screen if shaking
     */
    public void stopShake() {
        this.shaking = false;
        restoreCamera();
    }

}
