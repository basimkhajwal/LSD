package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import net.net63.codearcade.LSD.components.PlayerComponent;
import net.net63.codearcade.LSD.components.StateComponent;
import net.net63.codearcade.LSD.managers.Assets;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Render the aim points when the player is being
 * aimed at a point
 *
 * Created by Basim on 12/08/15.
 */
public class AimRenderSystem extends IteratingSystem implements Disposable{

    //Render variables
    private OrthographicCamera gameCamera;
    private SpriteBatch batch;

    //Textures
    private Texture blockedImage;
    private Texture trajectoryImage;

    //Mappers
    private ComponentMapper<PlayerComponent> playerMapper;
    private ComponentMapper<StateComponent> stateMapper;

    public AimRenderSystem(OrthographicCamera gameCamera) {
        super(Family.all(PlayerComponent.class).get(), Constants.SYSTEM_PRIORITIES.EFFECT_RENDER);

        this.gameCamera = gameCamera;
        batch = new SpriteBatch();

        trajectoryImage = Assets.getAsset(Assets.Images.TRAJECTORY, Texture.class);
        blockedImage = Assets.getAsset(Assets.Images.BLOCKED, Texture.class);

        stateMapper = ComponentMapper.getFor(StateComponent.class);
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
    }

    @Override
    public void processEntity(Entity player, float deltaTime) {

        PlayerComponent playerComponent = playerMapper.get(player);

        //Draw if the aiming state is set
        if (stateMapper.get(player).get() == PlayerComponent.STATE_AIMING) {

            batch.setProjectionMatrix(gameCamera.combined);
            batch.begin();

            if (!playerComponent.validLaunch) {
                float x = playerComponent.aimPosition.x; // - Constants.PLAYER_WIDTH / 2;
                float y = playerComponent.aimPosition.y; // - Constants.PLAYER_HEIGHT / 2;

                batch.draw(blockedImage, x, y, 0.3f, 0.3f);
            } else {

                //Draw the image at each trajectory point
                for (Vector2 point:  playerComponent.trajectoryPoints) {
                    batch.draw(trajectoryImage, point.x, point.y, 0.1f, 0.1f);
                }
            }

            batch.end();
        }

    }

    @Override
    public void dispose() {
        batch.dispose();
    }

}
