package net.net63.codearcade.LSD.utils;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import net.net63.codearcade.LSD.components.PlayerComponent;

/**
 * Utility class to hold mappers that don't have to be re-created for every system
 *
 * Created by Basim on 23/06/15.
 */
public class ComponentMappers {

    public static ComponentMapper<PlayerComponent> playerMapper = ComponentMapper.getFor(PlayerComponent.class);
}
