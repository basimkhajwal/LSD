package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Disposable;
import com.sun.media.jfxmediaimpl.MediaDisposer;
import com.sun.xml.internal.ws.client.sei.ResponseBuilder;
import net.net63.codearcade.LSD.components.BodyComponent;
import net.net63.codearcade.LSD.components.RenderComponent;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 03/07/15.
 */
public class RenderSystem extends IteratingSystem implements Disposable {

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private ComponentMapper<RenderComponent> renderMapper;
    private ComponentMapper<BodyComponent> bodyMapper;

    public RenderSystem (OrthographicCamera camera) {
        super(Family.all(RenderComponent.class, BodyComponent.class).get(), Constants.SYSTEM_PRIORITIES.RENDER);

        this.camera = camera;
        batch = new SpriteBatch();

        renderMapper = ComponentMapper.getFor(RenderComponent.class);
    }

    @Override
    public void update(float deltaTime) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        super.update(deltaTime);

        batch.end();
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {

        Body body = bodyMapper.get(entity).body;
        Fixture fixture = body.getFixtureList().first();

        //Only render rectangle shapes as of yet
        if (fixture != null && fixture.getType() == Shape.Type.Polygon ) {
            PolygonShape shape = (PolygonShape) fixture.getShape();

        }
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
