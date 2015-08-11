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

    private Vector2 calculateLaunchImpulse(Vector2 currentPosition, Vector2 target) {
        Vector2 impulse = new Vector2();

        // TODO - Implement a better launching heuristic
        impulse.set(target.cpy().sub(currentPosition)).scl(0.1f);

        return impulse;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        Body body = bodyMapper.get(entity).body;

        if (launch) {
            log("Launching Player to: " + launchPoint.toString());

            body.applyLinearImpulse(calculateLaunchImpulse(body.getPosition(), launchPoint), body.getWorldCenter(), true);
            launch = false;
        }
    }

    public void launchPlayer(float x, float y) {
        launch = true;
        launchPoint.set(x, y);
    }

}
