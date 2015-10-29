package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import net.net63.codearcade.LSD.components.PlayerComponent;
import net.net63.codearcade.LSD.components.StateComponent;
import net.net63.codearcade.LSD.utils.Assets;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 12/08/15.
 */
public class EffectRenderSystem extends EntitySystem implements Disposable{

    private OrthographicCamera gameCamera;
    private SpriteBatch batch;
    private Texture trajectoryImage;

    private ComponentMapper<PlayerComponent> playerMapper;
    private ComponentMapper<StateComponent> stateMapper;

    private ImmutableArray<Entity> playerEntities;

    public EffectRenderSystem (OrthographicCamera gameCamera) {
        super(Constants.SYSTEM_PRIORITIES.EFFECT_RENDER);

        this.gameCamera = gameCamera;
        batch = new SpriteBatch();

        trajectoryImage = Assets.getAsset(Assets.Images.TRAJECTORY, Texture.class);

        stateMapper = ComponentMapper.getFor(StateComponent.class);
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
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
            Vector2[] trajectories = playerMapper.get(player).trajectoryPoints;

            batch.setProjectionMatrix(gameCamera.combined);
            batch.begin();

            for (Vector2 point: trajectories) {
                batch.draw(trajectoryImage, point.x, point.y, 0.1f, 0.1f);
            }

            batch.end();
        }

    }

    @Override
    public void dispose() {
        batch.dispose();
    }

}
