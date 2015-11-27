package net.net63.codearcade.LSD.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import net.net63.codearcade.LSD.components.PlayerComponent;
import net.net63.codearcade.LSD.utils.Constants;
import net.net63.codearcade.LSD.world.LevelDescriptor;

/**
 * Created by Basim on 25/11/15.
 */
public class TimerSystem extends EntitySystem implements Disposable {

    private Entity player;
    private LevelDescriptor levelDescriptor;

    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;

    private float currentTime = 0;
    private float currentMaxTime = Float.POSITIVE_INFINITY;

    private boolean timerOn = false;

    public TimerSystem(LevelDescriptor levelDescriptor) {
        super(Constants.SYSTEM_PRIORITIES.TIMER);

        this.levelDescriptor = levelDescriptor;

        camera = new OrthographicCamera();
        camera.setToOrtho(false);

        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

        player = engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (timerOn) currentTime += deltaTime;

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.DARK_GRAY);
        if (timerOn) {
            shapeRenderer.rect(30, 30, 600 * (currentTime / currentMaxTime), 50);
        } else {
            shapeRenderer.rect(30, 30, 600, 50);
        }

        shapeRenderer.end();
    }

    public void beginTimer() {
        timerOn = true;
        currentMaxTime = 5f; //TEMP
    }

    public void endTimer() {
        timerOn = false;
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
