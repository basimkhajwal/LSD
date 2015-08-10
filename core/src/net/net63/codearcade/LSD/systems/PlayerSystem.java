package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import net.net63.codearcade.LSD.components.BodyComponent;
import net.net63.codearcade.LSD.components.PlayerComponent;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 10/08/15.
 */
public class PlayerSystem extends IteratingSystem{

    private ComponentMapper<BodyComponent> bodyMapper;

    public PlayerSystem () {
        super(Family.all(PlayerComponent.class).get(), Constants.SYSTEM_PRIORITIES.PLAYER);

        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        Body body = bodyMapper.get(entity).body;


    }

}
