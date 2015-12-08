package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import net.net63.codearcade.LSD.components.BodyComponent;
import net.net63.codearcade.LSD.components.PlayerComponent;
import net.net63.codearcade.LSD.components.SensorComponent;
import net.net63.codearcade.LSD.components.StateComponent;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 16/09/15.
 */
public class PlayerAimSystem extends IteratingSystem {

    private World world;

    private ComponentMapper<StateComponent> stateMapper;
    private ComponentMapper<PlayerComponent> playerMapper;
    private ComponentMapper<BodyComponent> bodyMapper;
    private ComponentMapper<SensorComponent> sensorMapper;

    public PlayerAimSystem(World world) {
        super(Family.all(PlayerComponent.class).get(), Constants.SYSTEM_PRIORITIES.PLAYER_AIM);

        this.world = world;

        stateMapper = ComponentMapper.getFor(StateComponent.class);
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        sensorMapper = ComponentMapper.getFor(SensorComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        StateComponent state = stateMapper.get(entity);
        PlayerComponent playerComponent = playerMapper.get(entity);
        Body body = bodyMapper.get(entity).body;

        if (state.get() == PlayerComponent.STATE_AIMING) {
            playerComponent.launchImpulse = calculateLaunchImpulse(body.getPosition(), playerComponent.aimPosition);
            playerComponent.trajectoryPoints = calculateTrajectoryPoints(body.getPosition(), playerComponent.launchImpulse);


        }
    }

    private RayCastCallback aimValidifier = new RayCastCallback() {

        @Override
        public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
            return 0;
        }
    };

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

    private float calculateFallTime(float height) {
        float t = 1 / Constants.BOX2D_FPS;
        float stepGravity = Constants.WORLD_GRAVITY.y * t * t;

        float a = 1.0f;
        float b = 1.0f;
        float c = 2 * height / stepGravity;

        float quadraticSolution1 = ( -b - (float) Math.sqrt( b * b - 4 * a * c ) ) / (2*a);
        float quadraticSolution2 = ( -b + (float) Math.sqrt(b * b - 4 * a * c) ) / (2*a);

        return Math.max(quadraticSolution1, quadraticSolution2);
    }

    private Vector2 calculateLaunchImpulse(Vector2 currentPosition, Vector2 target) {
        float velocityUp = calculateVerticalVelocity(target.y - currentPosition.y);
        float velocityAcross = (velocityUp <= 0) ?
                ((target.x - currentPosition.x) / calculateFallTime(currentPosition.y - target.y)) :
                ((target.x - currentPosition.x) / calculateFlightTime(velocityUp));

        return new Vector2(velocityAcross * Constants.BOX2D_FPS, velocityUp * Constants.BOX2D_FPS);
    }

    private Vector2[] calculateTrajectoryPoints(Vector2 startPoint, Vector2 startingVelocity) {
        Vector2[] points = new Vector2[Constants.NUM_TRAJECTORY_PROJECTIONS];

        Vector2 stepVelocity = startingVelocity.cpy().scl(1 / Constants.BOX2D_FPS);
        float stepGravity = Constants.WORLD_GRAVITY.y / (Constants.BOX2D_FPS * Constants.BOX2D_FPS);

        for (int i = 0; i < points.length; i++) {
            float t = (i + 1) * Constants.TRAJECTORY_PROJECTION_STEPS;

            points[i] = startPoint.cpy();
            points[i].x += t * stepVelocity.x;
            points[i].y += t * stepVelocity.y + 0.5f * (t*t+t) * stepGravity;
        }

        return points;
    }

}
