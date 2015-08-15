package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import net.net63.codearcade.LSD.components.BodyComponent;
import net.net63.codearcade.LSD.components.PlayerComponent;
import net.net63.codearcade.LSD.components.StateComponent;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 12/08/15.
 */
public class EffectRenderSystem extends EntitySystem implements Disposable{

    private OrthographicCamera gameCamera;
    private ShapeRenderer shapeRenderer;

    private ComponentMapper<PlayerComponent> playerMapper;
    private ComponentMapper<BodyComponent> bodyMapper;
    private ComponentMapper<StateComponent> stateMapper;

    private ImmutableArray<Entity> playerEntities;

    public EffectRenderSystem (OrthographicCamera gameCamera) {
        super(Constants.SYSTEM_PRIORITIES.EFFECT_RENDER);

        this.gameCamera = gameCamera;
        shapeRenderer = new ShapeRenderer();

        stateMapper = ComponentMapper.getFor(StateComponent.class);
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

        playerEntities = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        Entity player = playerEntities.first();

        if (stateMapper.get(player).get() == PlayerComponent.STATE_AIMING) {
            Vector2 playerPos = bodyMapper.get(player).body.getPosition();
            Vector2 aimPos = playerMapper.get(player).aimPosition;
            Vector2[] trajectories = playerMapper.get(player).trajectoryPoints;

            shapeRenderer.setProjectionMatrix(gameCamera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.RED);
            
            //shapeRenderer.line(playerPos.x, playerPos.y, touchPos.x, touchPos.y);
            shapeRenderer.end();
        }

    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }

}
