package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import net.net63.codearcade.LSD.components.BodyComponent;
import net.net63.codearcade.LSD.components.LaserComponent;
import net.net63.codearcade.LSD.managers.Assets;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 01/01/16.
 */
public class LaserSystem extends IteratingSystem implements Disposable {

    private TextureRegion baseTexture;

    private SpriteBatch batch;
    private OrthographicCamera gameCamera;
    private World world;

    private ComponentMapper<LaserComponent> laserMapper;
    private ComponentMapper<BodyComponent> bodyMapper;

    public LaserSystem(OrthographicCamera gameCamera, World world) {
        super(Family.all(LaserComponent.class).get(), Constants.SYSTEM_PRIORITIES.LASER);

        this.gameCamera = gameCamera;
        this.world = world;

        batch = new SpriteBatch();
        baseTexture = new TextureRegion(Assets.getAsset(Assets.Images.LASER_BASE, Texture.class));

        laserMapper = ComponentMapper.getFor(LaserComponent.class);
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
    }

    @Override
    public void update(float deltaTime) {
        batch.setProjectionMatrix(gameCamera.combined);
        batch.begin();

        super.update(deltaTime);

        batch.end();
    }

    private final RayCastCallback laserCallBack = new RayCastCallback() {
        @Override
        public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
            return 0;
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
        float angle = 180 - (laser.direction * 90);

        Vector2 pos = new Vector2(Constants.LASER_HEAD_ORIGIN_X - laserWidth / 2, Constants.LASER_HEAD_ORIGIN_Y - laserHeight / 2);
        pos.set(body.getWorldPoint(pos)).sub(Constants.LASER_BODY_ORIGIN_X, Constants.LASER_BODY_ORIGIN_Y);

        batch.draw(baseTexture, pos.x, pos.y, width / 2, height / 2, width, height, 1, 1, angle);

        laser.laserUpdateTime += deltaTime;
        laser.laserTime += deltaTime;

        if (laser.laserTime >= laser.interval) {
            laser.laserTime = 0;
            laser.laserEnabled = !laser.laserEnabled;
        }

        if (laser.laserUpdateTime >= Constants.LASER_UPDATE_TIME) {

            //TODO Calculate call back here

        }

        //TODO Draw the laser

    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
