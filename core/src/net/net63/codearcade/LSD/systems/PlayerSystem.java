package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import net.net63.codearcade.LSD.components.BodyComponent;
import net.net63.codearcade.LSD.components.PlayerComponent;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 10/08/15.
 */
public class PlayerSystem extends IteratingSystem{

    private ComponentMapper<BodyComponent> bodyMapper;

    private Vector2 launchPoint;
    private boolean launch;

    public PlayerSystem () {
        super(Family.all(PlayerComponent.class).get(), Constants.SYSTEM_PRIORITIES.PLAYER);

        bodyMapper = ComponentMapper.getFor(BodyComponent.class);

        launchPoint = new Vector2();
        launch = false;
    }

    private void log(String message) {
        Gdx.app.log(Constants.LOG, "PlayerSystem: " + message);
    }

    private float calculateVerticalVelocity(float desiredHeight) {
        if (desiredHeight <= 0) {
            return 0;
        }

        float t = 1.0f / Constants.BOX2D_FPS;
        float stepGravity = Constants.WORLD_GRAVITY.y * t * t;

        float a = 0.5f / stepGravity;
        float b = 0.5f;
        float c = desiredHeight;

        float quadraticSolution1 = ( -b - (float) Math.sqrt( b * b - 4 * a * c ) ) / (2*a);
        float quadraticSolution2 = ( -b + (float) Math.sqrt(b * b - 4 * a * c) ) / (2*a);

        return Math.max(quadraticSolution1, quadraticSolution2);
    }

    private float calculateFlightTime(float verticalVelocity) {
        float stepGravity = Constants.WORLD_GRAVITY.y / (Constants.BOX2D_FPS * Constants.BOX2D_FPS);

        return -verticalVelocity / stepGravity - 1;
    }

    private Vector2 calculateLaunchImpulse(Vector2 currentPosition, Vector2 target) {
        float velocityUp = calculateVerticalVelocity(target.y - currentPosition.y);
        float velocityAcross = (target.x - currentPosition.x) / calculateFlightTime(velocityUp);

        return new Vector2(velocityAcross * Constants.BOX2D_FPS, velocityUp * Constants.BOX2D_FPS);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        Body body = bodyMapper.get(entity).body;

        if (launch) {
            log("Launching Player to: " + launchPoint.toString());
            body.setTransform(3, 3, 0);
            body.setLinearVelocity(0, 0);
            body.applyLinearImpulse(calculateLaunchImpulse(body.getPosition(), launchPoint), body.getWorldCenter(), true);
            launch = false;
        }
    }

    public void launchPlayer(float x, float y) {
        launch = true;
        launchPoint.set(x, y);
    }

}
