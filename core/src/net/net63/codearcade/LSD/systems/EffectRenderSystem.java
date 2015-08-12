package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 12/08/15.
 */
public class EffectRenderSystem extends EntitySystem implements Disposable{

    private OrthographicCamera gameCamera;
    private ShapeRenderer shapeRenderer;

    private Vector2 playerPos;
    private Vector2 touchPos;
    private boolean drawPlayer;

    public EffectRenderSystem (OrthographicCamera gameCamera) {
        super(Constants.SYSTEM_PRIORITIES.EFFECT_RENDER);

        this.gameCamera = gameCamera;
        shapeRenderer = new ShapeRenderer();

        drawPlayer = false;
    }

    @Override
    public void update(float deltaTime) {
        if (drawPlayer) {
            shapeRenderer.setProjectionMatrix(gameCamera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.line(playerPos.x, playerPos.y, touchPos.x, touchPos.y);
            shapeRenderer.end();
        }

    }

    public void setDrawPlayer(boolean drawPlayer) {
        this.drawPlayer = drawPlayer;
    }

    public void updatePlayerProjection(Vector2 playerPos, Vector2 touchPos) {
        this.playerPos = playerPos;
        this.touchPos = touchPos;
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }

}
