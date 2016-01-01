package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;
import net.net63.codearcade.LSD.components.BodyComponent;
import net.net63.codearcade.LSD.components.LaserComponent;
import net.net63.codearcade.LSD.managers.Assets;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 01/01/16.
 */
public class LaserSystem extends IteratingSystem implements Disposable {

    private Texture head;
    private Texture base;

    private SpriteBatch batch;
    private OrthographicCamera gameCamera;

    private ComponentMapper<LaserComponent> laserMapper;
    private ComponentMapper<BodyComponent> bodyMapper;

    public LaserSystem(OrthographicCamera gameCamera) {
        super(Family.all(LaserComponent.class).get(), Constants.SYSTEM_PRIORITIES.LASER);

        this.gameCamera = gameCamera;
        batch = new SpriteBatch();

        head = Assets.getAsset(Assets.Images.LASER_HEAD, Texture.class);
        base = Assets.getAsset(Assets.Images.LASER_BASE, Texture.class);

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

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        LaserComponent laser = laserMapper.get(entity);
        Body body = bodyMapper.get(entity).body;


    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
