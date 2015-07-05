package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Disposable;
import net.net63.codearcade.LSD.components.WorldComponent;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 26/06/15.
 */
public class DebugRenderSystem extends IteratingSystem implements Disposable{

    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private ComponentMapper<WorldComponent> worldMapper;

    @SuppressWarnings("unchecked")
	public DebugRenderSystem(OrthographicCamera camera) {
        super(Family.all(WorldComponent.class).get(), Constants.SYSTEM_PRIORITIES.DEBUG_RENDER);

        this.camera = camera;
        debugRenderer = new Box2DDebugRenderer();

        worldMapper = ComponentMapper.getFor(WorldComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        debugRenderer.render(worldMapper.get(entity).world, camera.combined);
    }

    @Override
    public void dispose() {
        debugRenderer.dispose();
    }
}
