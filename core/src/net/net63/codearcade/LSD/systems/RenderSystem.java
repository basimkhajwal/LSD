package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Disposable;
import net.net63.codearcade.LSD.components.BodyComponent;
import net.net63.codearcade.LSD.components.RenderComponent;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Entity system to render entities with a render
 * component and a physics body component
 *
 * Created by Basim on 03/07/15.
 */
public class RenderSystem extends IteratingSystem implements Disposable {

    //Temporary storage
    private static final Vector2 tmp = new Vector2();
    private static final Vector2 tmp2 = new Vector2();

    //Rendering instances
    private OrthographicCamera camera;
    private SpriteBatch batch;

    //Component retrievers
    private ComponentMapper<RenderComponent> renderMapper;
    private ComponentMapper<BodyComponent> bodyMapper;

    public RenderSystem (OrthographicCamera camera) {
        super(Family.all(RenderComponent.class, BodyComponent.class).get(), Constants.SYSTEM_PRIORITIES.RENDER);

        this.camera = camera;
        batch = new SpriteBatch();

        renderMapper = ComponentMapper.getFor(RenderComponent.class);
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
    }

    @Override
    public void update(float deltaTime) {
        //Apply the batch then render each entity
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        super.update(deltaTime);

        batch.end();
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        //Get all components and details of the entity to render
        Body body = bodyMapper.get(entity).body;
        RenderComponent renderComponent = renderMapper.get(entity);
        Fixture fixture = body.getFixtureList().first();
        Vector2 position = body.getPosition();

        //Short circuit if rendering is set to false or invalid body
        if (!renderComponent.render || fixture == null) return;

        //Get dimensions depending on body type (tmp = pos, tmp2 = size)
        switch (fixture.getType()) {

            //Only handles rectangles
            case Polygon:
                PolygonShape polygon = (PolygonShape) fixture.getShape();

                //Get vertices and compute dimensions
                polygon.getVertex(0, tmp);
                polygon.getVertex(2, tmp2);

                tmp2.sub(tmp);
                tmp.add(position);

                break;

            //Set sizes for a circle shape
            case Circle:
                float radius = fixture.getShape().getRadius();

                tmp.set(position).sub(radius, radius);
                tmp2.set(radius * 2, radius * 2);

                break;

            //Other shapes aren't rendered
            default: return;
        }

        //If the texture is being tiled
        if (renderComponent.tileToSize) {
            //Calculate tile sizes and amounts
            float tileWidth = renderComponent.tileWidth * Constants.PIXEL_TO_METRE;
            float tileHeight = renderComponent.tileHeight * Constants.PIXEL_TO_METRE;
            int tilesAcross = (int) Math.ceil(tmp2.x / tileWidth);
            int tilesUp = (int) Math.ceil(tmp2.y / tileHeight);

            //Render the texture at each required co-ordinate
            for (int row = 0; row < tilesUp; row++) {
                for (int col = 0; col < tilesAcross; col++) {
                    batch.draw(renderComponent.texture,
                            tmp.x + col * tileWidth,
                            tmp.y + row * tileHeight,
                            tileWidth, tileHeight);
                }
            }

        } else {
            //Simply draw at the given position and size
            batch.draw(renderComponent.texture, tmp.x, tmp.y, tmp2.x / 2, tmp2.y / 2, tmp2.x, tmp2.y, 1, 1, body.getAngle() * MathUtils.radiansToDegrees);
        }

    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
