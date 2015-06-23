package net.net63.codearcade.LSD.utils;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;

/**
 * Created by Basim on 23/06/15.
 */
public class GameWorld {

    private Engine engine;

    public GameWorld (Engine engine) {
        this.engine = engine;
    }

    public Entity createPlayer() {

        Entity player = new Entity();

        engine.addEntity(player);

        return player;
    }

}
