package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import net.net63.codearcade.LSD.components.BodyComponent;
import net.net63.codearcade.LSD.components.LaserComponent;
import net.net63.codearcade.LSD.components.PlayerComponent;
import net.net63.codearcade.LSD.events.EventQueue;
import net.net63.codearcade.LSD.events.GameEvent;
import net.net63.codearcade.LSD.managers.Assets;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 01/01/16.
 */
public class LaserSystem extends IteratingSystem implements Disposable, ContactListener {

    private TextureRegion baseTexture;

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera gameCamera;
    private World world;

    private final Vector2 laserPos = new Vector2();
    private final Vector2 endPosition = new Vector2();

    private boolean laserHit = false;
    private final Vector2 laserHitPos = new Vector2();

    private ComponentMapper<LaserComponent> laserMapper;
    private ComponentMapper<BodyComponent> bodyMapper;

    private Signal<GameEvent> gameEventSignal;
    private EventQueue eventQueue;

    private boolean paused = false;

    public LaserSystem(OrthographicCamera gameCamera, World world, Signal<GameEvent> gameEventSignal) {
        super(Family.all(LaserComponent.class).get(), Constants.SYSTEM_PRIORITIES.LASER);

        this.gameCamera = gameCamera;
        this.world = world;
        this.gameEventSignal = gameEventSignal;

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        baseTexture = new TextureRegion(Assets.getAsset(Assets.Images.LASER_BASE, Texture.class));

        laserMapper = ComponentMapper.getFor(LaserComponent.class);
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);

        eventQueue = new EventQueue();
        gameEventSignal.add(eventQueue);
    }

    @Override
    public void update(float deltaTime) {

        for (GameEvent event: eventQueue.getEvents()) {
            switch (event) {

                case PAUSE_GAME:
                    paused = true;
                    break;

                case RESUME_GAME:
                    paused = false;
                    break;

            }
        }

        batch.setProjectionMatrix(gameCamera.combined);
        batch.begin();
        super.update(deltaTime);
        batch.end();
    }

    private final RayCastCallback laserCallBack = new RayCastCallback() {
        @Override
        public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {

            if ((fixture.getFilterData().categoryBits & Constants.MaskBits.LASER) == 0) {
                return -1;
            }

            laserHitPos.set(point);
            laserHit = true;

            return fraction;
        }
    };

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        LaserComponent laser = laserMapper.get(entity);
        Body body = bodyMapper.get(entity).body;

        float width = Constants.LASER_BODY_WIDTH;
        float height = Constants.LASER_BODY_HEIGHT;
        float laserWidth = Constants.LASER_HEAD_WIDTH;
        float laserHeight = Constants.LASER_HEAD_HEIGHT;
        float originX = Constants.LASER_BODY_ORIGIN_X;
        float originY = Constants.LASER_BODY_ORIGIN_Y;
        float angle = (360 + (2 - laser.direction) * 90) % 360;

        Vector2 pos = new Vector2(Constants.LASER_HEAD_ORIGIN_X - laserWidth / 2, Constants.LASER_HEAD_ORIGIN_Y - laserHeight / 2);
        pos.set(body.getWorldPoint(pos));

        //If it is up or down minus x-accordingly
        switch (laser.direction) {

            case 0:
                pos.sub(originX, height-originY);
                break;

            case 1:
                pos.sub(-originY, originX);
                break;

            case 2:
                pos.sub(originX, originY);
                break;

            case 3:
                pos.sub(originY, originX - width);
                break;

        }

        float rotX = (laser.direction % 2 == 0) ? width / 2 : 0;
        float rotY = (laser.direction % 2 == 0) ? height /2 : 0;

        batch.draw(baseTexture, pos.x, pos.y, rotX, rotY, width, height, 1, 1, angle);

        if (!paused) laser.laserTime += deltaTime;

        laserPos.set(Constants.LASER_HEAD_LASER_X - laserWidth / 2, Constants.LASER_HEAD_LASER_Y - laserHeight / 2);
        laserPos.set(body.getWorldPoint(laserPos));

        if (laser.laserTime >= laser.interval) {
            laser.laserTime = 0;

            if (laser.laserEnabled && laser.laserSensorBody != null) {
                world.destroyBody(laser.laserSensorBody);
                laser.laserSensorBody = null;
            }

            laser.laserEnabled = !laser.laserEnabled;

            if (laser.laserEnabled) laser.updateLaser = true;
        }

        if (laser.laserEnabled) {
            if (!paused) laser.laserUpdateTime += deltaTime;

            if (laser.laserUpdateTime >= Constants.LASER_UPDATE_TIME) {
                laser.laserUpdateTime = 0;
                laser.updateLaser = true;
            }
        }

        if (laser.laserEnabled && laser.updateLaser) {
            laser.updateLaser = false;

            endPosition.set(Constants.MAX_LASER_DISTANCE, 0);
            endPosition.setAngleRad(body.getAngle() - (float)(Math.PI / 2.0));
            endPosition.add(laserPos);

            laserHit = false;
            world.rayCast(laserCallBack, laserPos, endPosition);
            laserHitPos.set(laserHit ? laserHitPos : endPosition);

            if (laser.laserEndPos.dst(laserHitPos) > 0.01f) {
                laser.laserEndPos.set(laserHitPos);

                if (laser.laserSensorBody != null) world.destroyBody(laser.laserSensorBody);
                laser.laserSensorBody = createNewSensor(entity, body.getAngle());
            }

        }

        if (laser.laserEnabled) {
            batch.end();

            shapeRenderer.setProjectionMatrix(gameCamera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rectLine(laserPos.x, laserPos.y, laser.laserEndPos.x, laser.laserEndPos.y, Constants.LASER_BEAM_WIDTH);
            shapeRenderer.end();

            batch.begin();
        }

    }

    private Body createNewSensor(Entity laser, float angle) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.angle = angle;
        bodyDef.position.set(laserPos).lerp(laserHitPos, 0.5f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = new PolygonShape();
        fixtureDef.filter.categoryBits = Constants.CategoryBits.LASER;
        fixtureDef.filter.maskBits = Constants.MaskBits.LASER;
        ((PolygonShape) fixtureDef.shape).setAsBox(Constants.LASER_BEAM_WIDTH / 2, bodyDef.position.dst(laserHitPos));
        fixtureDef.isSensor = true;

        Body body = world.createBody(bodyDef);
        body.setUserData(laser);
        body.createFixture(fixtureDef);

        return body;
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
    }

    @Override
    public void beginContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        if (!(bodyA.getUserData() instanceof Entity) || !(bodyB.getUserData() instanceof Entity)) return;

        Entity entityA = (Entity) bodyA.getUserData();
        Entity entityB = (Entity) bodyB.getUserData();
        Entity laser, other;

        if (laserMapper.has(entityA)) {
            laser = entityA;
            other = entityB;
        } else if (laserMapper.has(entityB)) {
            laser = entityB;
            other = entityA;
        } else {
            return;
        }

        LaserComponent laserComponent = laserMapper.get(laser);
        laserComponent.updateLaser = true;

        if (other.getComponent(PlayerComponent.class) != null) {
            gameEventSignal.dispatch(GameEvent.LASER_COLLISION);
        }
    }

    @Override
    public void endContact(Contact contact) { }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) { }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) { }
}
