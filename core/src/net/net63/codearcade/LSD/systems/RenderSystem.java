package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import net.net63.codearcade.LSD.components.RenderComponent;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 03/07/15.
 */
public class RenderSystem extends IteratingSystem {

    private OrthographicCamera camera;

    private ComponentMapper<RenderComponent> renderMapper;

    public RenderSystem (OrthographicCamera camera) {
        super(Family.all(RenderComponent.class).get(), Constants.SYSTEM_PRIORITIES.RENDER);

        this.camera = camera;

        renderMapper = ComponentMapper.getFor(RenderComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        
    }
}
