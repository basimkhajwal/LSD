package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import net.net63.codearcade.LSD.components.BodyComponent;
import net.net63.codearcade.LSD.components.PlayerComponent;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 17/09/15.
 */
public class CameraMovementSystem extends EntitySystem {

    private OrthographicCamera camera;

    private ImmutableArray<Entity> players;
    private ComponentMapper<BodyComponent> bodyMapper;

    public CameraMovementSystem(OrthographicCamera camera) {
        super(Constants.SYSTEM_PRIORITIES.CAMERA_MOVEMENT);

        this.camera = camera;

        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

        players = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        Entity player = players.first();
        if (player == null) return;

        Vector2 pos = bodyMapper.get(player).body.getPosition();
        camera.position.set(pos.x + camera.viewportWidth / 2.0f, pos.y + camera.viewportHeight / 2.0f, camera.position.z);
    }


}
