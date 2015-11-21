package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Disposable;
import net.net63.codearcade.LSD.components.BodyComponent;
import net.net63.codearcade.LSD.components.RenderComponent;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 03/07/15.
 */
public class RenderSystem extends IteratingSystem implements Disposable {

    private Vector2 tmp;
    private Vector2 tmp2;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private ComponentMapper<RenderComponent> renderMapper;
    private ComponentMapper<BodyComponent> bodyMapper;

    public RenderSystem (OrthographicCamera camera) {
        super(Family.all(RenderComponent.class, BodyComponent.class).get(), Constants.SYSTEM_PRIORITIES.RENDER);

        this.camera = camera;
        batch = new SpriteBatch();

        tmp = new Vector2();
        tmp2 = new Vector2();

        renderMapper = ComponentMapper.getFor(RenderComponent.class);
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
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
        RenderComponent renderComponent = renderMapper.get(entity);
        Fixture fixture = body.getFixtureList().first();

        if (!renderComponent.render) return;

        if (fixture != null) {
            boolean rendering  = true;

            switch (fixture.getType()) {

                case Polygon:
                    PolygonShape polygon = (PolygonShape) fixture.getShape();

                    polygon.getVertex(0, tmp);
                    polygon.getVertex(2, tmp2);

                    tmp2.sub(tmp);
                    tmp.add(body.getPosition());

                    break;

                case Circle:
                    float radius = fixture.getShape().getRadius();

                    tmp.set(body.getPosition()).sub(radius, radius);
                    tmp2.set(radius * 2, radius * 2);

                    break;

                default:
                    rendering = false;
            }

            if (rendering) {
                if (renderComponent.tileToSize) {
                    float tileWidth = renderComponent.tileWidth * Constants.PIXEL_TO_METRE;
                    float tileHeight = renderComponent.tileHeight * Constants.PIXEL_TO_METRE;

                    int tilesAcross = (int) Math.ceil(tmp2.x / tileWidth);
                    int tilesUp = (int) Math.ceil(tmp2.y / tileHeight);

                    for (int row = 0; row < tilesUp; row++) {
                        for (int col = 0; col < tilesAcross; col++) {
                            batch.draw(renderComponent.texture,
                                    tmp.x + col * tileWidth,
                                    tmp.y + row * tileHeight,
                                    tileWidth, tileHeight);
                        }
                    }

                } else {
                    batch.draw(renderComponent.texture, tmp.x, tmp.y, tmp2.x, tmp2.y);
                }
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
