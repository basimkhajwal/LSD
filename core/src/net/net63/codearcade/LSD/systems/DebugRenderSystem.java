package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

import net.net63.codearcade.LSD.components.WorldComponent;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 26/06/15.
 */
public class DebugRenderSystem extends IteratingSystem {

    private Box2DDebugRenderer debugRenderer;

    private OrthographicCamera camera;

    private ComponentMapper<WorldComponent> worldMapper;

    @SuppressWarnings("unchecked")
	public DebugRenderSystem() {
        super(Family.all(WorldComponent.class).get(), Constants.SYSTEM_PRIORITIES.DEBUG_RENDER);

        debugRenderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false);

        worldMapper = ComponentMapper.getFor(WorldComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        debugRenderer.render(worldMapper.get(entity).world, camera.combined);
    }
}
