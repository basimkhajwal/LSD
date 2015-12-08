package net.net63.codearcade.LSD.listeners;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import net.net63.codearcade.LSD.components.BodyComponent;
import net.net63.codearcade.LSD.components.PlayerComponent;
import net.net63.codearcade.LSD.components.SensorComponent;
import net.net63.codearcade.LSD.events.GameEvent;
import net.net63.codearcade.LSD.utils.Constants;
import net.net63.codearcade.LSD.world.WorldBuilder;

/**
 * Created by Basim on 13/10/15.
 */
public class ParticleEffectListener implements Listener<GameEvent> {

    private ComponentMapper<SensorComponent> sensorMapper;
    private ComponentMapper<PlayerComponent> playerMapper;
    private ComponentMapper<BodyComponent> bodyMapper;

    private Entity playerEntity;

    public ParticleEffectListener(Entity player) {
        this.playerEntity = player;

        sensorMapper = ComponentMapper.getFor(SensorComponent.class);
        bodyMapper = ComponentMapper.getFor(BodyComponent.class);
        playerMapper = ComponentMapper.getFor(PlayerComponent.class);
    }

    @Override
    public void receive(Signal<GameEvent> signal, GameEvent event) {
        switch (event) {

            case LAUNCH_PLAYER:
                Entity sensor = playerMapper.get(playerEntity).currentSensor;
                if (sensor != null) createSensorEffect(sensor);
                break;

            case PLAYER_DEATH:
                createPlayerEffect();
                break;

            default:
                break;
        }


    }

    private void createPlayerEffect() {
        Body body = bodyMapper.get(playerEntity).body;

        //How many particles to generate
        int num = Constants.PLAYER_DEATH_PARTICLES;

        //Position and radius of player
        Vector2 pos = body.getPosition();
        float radius = body.getFixtureList().first().getShape().getRadius();

        //Each particle start position and color
        Vector2[] positions = new Vector2[num];
        Color[] colors = new Color[num];

        //Randomly position within player radius
        for (int i = 0; i < num; i++) {
            positions[i] = new Vector2(pos);
            positions[i].add(MathUtils.random(-radius, radius), MathUtils.random(-radius, radius));

            colors[i] = Color.BROWN;
        }

        //Create particle effect from builder
        WorldBuilder.createParticleEffect(positions, colors, 2, 5, 5);
    }

    private void createSensorEffect(Entity entity) {
        Body body = bodyMapper.get(entity).body;
        PolygonShape shape = (PolygonShape) body.getFixtureList().first().getShape();

        Vector2 dimensions = new Vector2();
        shape.getVertex(2, dimensions);

        Vector2 bottomLeft = body.getPosition().cpy().sub(dimensions);
        dimensions.scl(2);

        int num = Constants.PLATFORM_PARTICLES;

        Vector2[] positions = new Vector2[num];
        Color[] colors = new Color[num];

        for (int i = 0; i < num; i++) {
            positions[i] = new Vector2(
                    bottomLeft.x + MathUtils.random(0, dimensions.x),
                    bottomLeft.y + MathUtils.random(0, dimensions.y));

            colors[i] = Color.BLACK;
        }

        WorldBuilder.createParticleEffect(positions, colors, 2);
    }
}
